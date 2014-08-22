/*
 * JBoss, Home of Professional Open Source
 *  Copyright ${year}, Red Hat, Inc., and individual contributors
 *  by the @authors tag. See the copyright.txt in the distribution for a
 *  full listing of individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wildfly.cassandra.extension;

import com.addthis.metrics.reporter.config.ReporterConfig;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.cassandra.concurrent.JMXEnabledThreadPoolExecutor;
import org.apache.cassandra.concurrent.Stage;
import org.apache.cassandra.concurrent.StageManager;
import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.config.Schema;
import org.apache.cassandra.db.ColumnFamilyStore;
import org.apache.cassandra.db.Keyspace;
import org.apache.cassandra.db.MeteredFlusher;
import org.apache.cassandra.db.SystemKeyspace;
import org.apache.cassandra.db.commitlog.CommitLog;
import org.apache.cassandra.db.compaction.CompactionManager;
import org.apache.cassandra.db.compaction.LeveledManifest;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.io.FSError;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.metrics.StorageMetrics;
import org.apache.cassandra.service.CacheService;
import org.apache.cassandra.service.Daemon;
import org.apache.cassandra.service.GCInspector;
import org.apache.cassandra.service.NativeAccessMBean;
import org.apache.cassandra.service.Server;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.thrift.ThriftServer;
import org.apache.cassandra.tracing.Tracing;
import org.apache.cassandra.utils.CLibrary;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.Mx4jTool;
import org.apache.cassandra.utils.Pair;
import org.wildfly.cassandra.logging.CassandraLogger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The <code>CassandraDaemon</code> is an abstraction for a Cassandra daemon
 * service, which defines not only a way to activate and deactivate it, but also
 * hooks into its lifecycle methods (see {@link #setup()}, {@link #start()},
 * {@link #stop()} and {@link #setup()}).
 */
public class WildflyCassandraDaemon implements Daemon
{
    public static final String MBEAN_NAME = "org.apache.cassandra.db:type=NativeAccess";

    // Have a dedicated thread to call exit to avoid deadlock in the case where the thread that wants to invoke exit
    // belongs to an executor that our shutdown hook wants to wait to exit gracefully. See CASSANDRA-5273.
    private static final Thread exitThread = new Thread(new Runnable()
    {
        public void run()
        {
            //System.exit(100);
            // TODO: is this still needed when running embedded?
        }
    }, "Exit invoker");

        
    private static final WildflyCassandraDaemon instance = new WildflyCassandraDaemon();

    public Server thriftServer;
    public Server nativeServer;

    /**
     * This is a hook for concrete daemons to initialize themselves suitably.
     *
     * Subclasses should override this to finish the job (listening on ports, etc.)
     *
     * @throws IOException
     */
    protected void setup()
    {
        // log warnings for different kinds of sub-optimal JVMs.  tldr use 64-bit Oracle >= 1.6u32
        if (!DatabaseDescriptor.hasLargeAddressSpace())
            CassandraLogger.LOGGER.infof("32bit JVM detected.  It is recommended to run Cassandra on a 64bit JVM for better performance.");
        String javaVersion = System.getProperty("java.version");
        String javaVmName = System.getProperty("java.vm.name");
        CassandraLogger.LOGGER.infof("JVM vendor/version: {}/{}", javaVmName, javaVersion);
        if (javaVmName.contains("OpenJDK"))
        {
            // There is essentially no QA done on OpenJDK builds, and
            // clusters running OpenJDK have seen many heap and load issues.
            CassandraLogger.LOGGER.warn("OpenJDK is not recommended. Please upgrade to the newest Oracle Java release");
        }
        else if (!javaVmName.contains("HotSpot"))
        {
            CassandraLogger.LOGGER.warn("Non-Oracle JVM detected.  Some features, such as immediate unmap of compacted SSTables, may not work as intended");
        }
     /*   else
        {
            String[] java_version = javaVersion.split("_");
            String java_major = java_version[0];
            int java_minor;
            try
            {
                java_minor = (java_version.length > 1) ? Integer.parseInt(java_version[1]) : 0;
            }
            catch (NumberFormatException e)
            {
                // have only seen this with java7 so far but no doubt there are other ways to break this
                CassandraLogger.LOGGER.infof("Unable to parse java version {}", Arrays.toString(java_version));
                java_minor = 32;
            }
        }
     */
        CassandraLogger.LOGGER.infof("Heap size: {}/{}", Runtime.getRuntime().totalMemory(), Runtime.getRuntime().maxMemory());
        for(MemoryPoolMXBean pool: ManagementFactory.getMemoryPoolMXBeans())
            CassandraLogger.LOGGER.infof("{} {}: {}", pool.getName(), pool.getType(), pool.getPeakUsage());
        CassandraLogger.LOGGER.infof("Classpath: {}", System.getProperty("java.class.path"));
        CLibrary.tryMlockall();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            public void uncaughtException(Thread t, Throwable e)
            {
                StorageMetrics.exceptions.inc();
                CassandraLogger.LOGGER.error("Exception in thread " + t, e);
                Tracing.trace("Exception in thread " + t, e);
                for (Throwable e2 = e; e2 != null; e2 = e2.getCause())
                {
                    // some code, like FileChannel.map, will wrap an OutOfMemoryError in another exception
                    if (e2 instanceof OutOfMemoryError)
                        exitThread.start();

                    if (e2 instanceof FSError)
                    {
                        if (e2 != e) // make sure FSError gets logged exactly once.
                            CassandraLogger.LOGGER.error("Exception in thread " + t, e2);
                        FileUtils.handleFSError((FSError) e2);
                    }
                }
            }
        });

        // check all directories(data, commitlog, saved cache) for existence and permission
        Iterable<String> dirs = Iterables.concat(Arrays.asList(DatabaseDescriptor.getAllDataFileLocations()),
                Arrays.asList(DatabaseDescriptor.getCommitLogLocation(),
                        DatabaseDescriptor.getSavedCachesLocation()));
        for (String dataDir : dirs)
        {
            CassandraLogger.LOGGER.debugf("Checking directory {}", dataDir);
            File dir = new File(dataDir);
            if (dir.exists())
                assert dir.isDirectory() && dir.canRead() && dir.canWrite() && dir.canExecute()
                        : String.format("Directory %s is not accessible.", dataDir);
        }

        if (CacheService.instance == null) // should never happen
            throw new RuntimeException("Failed to initialize Cache Service.");

        // check the system keyspace to keep user from shooting self in foot by changing partitioner, cluster name, etc.
        // we do a one-off scrub of the system keyspace first; we can't load the list of the rest of the keyspaces,
        // until system keyspace is opened.
        for (CFMetaData cfm : Schema.instance.getKeyspaceMetaData(Keyspace.SYSTEM_KS).values())
            ColumnFamilyStore.scrubDataDirectories(Keyspace.SYSTEM_KS, cfm.cfName);
        try
        {
            SystemKeyspace.checkHealth();
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException("Fatal exception during initialization", e);
        }

        // load keyspace descriptions.
        DatabaseDescriptor.loadSchemas();

        try
        {
            LeveledManifest.maybeMigrateManifests();
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not migrate old leveled manifest. Move away the .json file in the data directory", e);
        }

        // clean up compaction leftovers
        Map<Pair<String, String>, Map<Integer, UUID>> unfinishedCompactions = SystemKeyspace.getUnfinishedCompactions();
        for (Pair<String, String> kscf : unfinishedCompactions.keySet())
            ColumnFamilyStore.removeUnfinishedCompactionLeftovers(kscf.left, kscf.right, unfinishedCompactions.get(kscf));
        SystemKeyspace.discardCompactionsInProgress();

        // clean up debris in the rest of the keyspaces
        for (String keyspaceName : Schema.instance.getKeyspaces())
        {
            // Skip system as we've already cleaned it
            if (keyspaceName.equals(Keyspace.SYSTEM_KS))
                continue;

            for (CFMetaData cfm : Schema.instance.getKeyspaceMetaData(keyspaceName).values())
                ColumnFamilyStore.scrubDataDirectories(keyspaceName, cfm.cfName);
        }

        // initialize keyspaces
        for (String keyspaceName : Schema.instance.getKeyspaces())
        {
            if (CassandraLogger.LOGGER.isDebugEnabled())
                CassandraLogger.LOGGER.debug("opening keyspace " + keyspaceName);
            // disable auto compaction until commit log replay ends
            for (ColumnFamilyStore cfs : Keyspace.open(keyspaceName).getColumnFamilyStores())
            {
                for (ColumnFamilyStore store : cfs.concatWithIndexes())
                {
                    store.disableAutoCompaction();
                }
            }
        }

        if (CacheService.instance.keyCache.size() > 0)
            CassandraLogger.LOGGER.infof("completed pre-loading ({} keys) key cache.", CacheService.instance.keyCache.size());

        if (CacheService.instance.rowCache.size() > 0)
            CassandraLogger.LOGGER.infof("completed pre-loading ({} keys) row cache.", CacheService.instance.rowCache.size());

        try
        {
            GCInspector.instance.start();
        }
        catch (Throwable t)
        {
            CassandraLogger.LOGGER.warn("Unable to start GCInspector (currently only supported on the Sun JVM)");
        }

        // MeteredFlusher can block if flush queue fills up, so don't put on scheduledTasks
        // Start it before commit log, so memtables can flush during commit log replay
        StorageService.optionalTasks.scheduleWithFixedDelay(new MeteredFlusher(), 1000, 1000, TimeUnit.MILLISECONDS);

        // replay the log if necessary
        try
        {
            CommitLog.instance.recover();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        // enable auto compaction
        for (Keyspace keyspace : Keyspace.all())
        {
            for (ColumnFamilyStore cfs : keyspace.getColumnFamilyStores())
            {
                for (final ColumnFamilyStore store : cfs.concatWithIndexes())
                {
                    store.enableAutoCompaction();
                }
            }
        }
        // start compactions in five minutes (if no flushes have occurred by then to do so)
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                for (Keyspace keyspaceName : Keyspace.all())
                {
                    for (ColumnFamilyStore cf : keyspaceName.getColumnFamilyStores())
                    {
                        for (ColumnFamilyStore store : cf.concatWithIndexes())
                            CompactionManager.instance.submitBackground(store);
                    }
                }
            }
        };
        StorageService.optionalTasks.schedule(runnable, 5 * 60, TimeUnit.SECONDS);

        SystemKeyspace.finishStartup();

        // start server internals
        StorageService.instance.registerDaemon(this);
        try
        {
            StorageService.instance.initServer();
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException("Fatal configuration error", e);
        }

        Mx4jTool.maybeLoad();

        // Metrics
        String metricsReporterConfigFile = System.getProperty("cassandra.metricsReporterConfigFile");
        if (metricsReporterConfigFile != null)
        {
            CassandraLogger.LOGGER.infof("Trying to load metrics-reporter-config from file: {}", metricsReporterConfigFile);
            try
            {
                String reportFileLocation = WildflyCassandraDaemon.class.getClassLoader().getResource(metricsReporterConfigFile).getFile();
                ReporterConfig.loadFromFile(reportFileLocation).enableAll();
            }
            catch (Exception e)
            {
                CassandraLogger.LOGGER.warn("Failed to load metrics-reporter-config, metric sinks will not be activated", e);
            }
        }

        if (!FBUtilities.getBroadcastAddress().equals(InetAddress.getLoopbackAddress()))
            waitForGossipToSettle();

        // Thift
        InetAddress rpcAddr = DatabaseDescriptor.getRpcAddress();
        int rpcPort = DatabaseDescriptor.getRpcPort();
        thriftServer = new ThriftServer(rpcAddr, rpcPort);
        StorageService.instance.registerThriftServer(thriftServer);

        // Native transport
        InetAddress nativeAddr = DatabaseDescriptor.getNativeTransportAddress();
        int nativePort = DatabaseDescriptor.getNativeTransportPort();
        nativeServer = new org.apache.cassandra.transport.Server(nativeAddr, nativePort);
        StorageService.instance.registerNativeServer(nativeServer);
    }

    /**
     * Initialize the Cassandra Daemon based on the given <a
     * href="http://commons.apache.org/daemon/jsvc.html">Commons
     * Daemon</a>-specific arguments. To clarify, this is a hook for JSVC.
     *
     * @param arguments
     *            the arguments passed in from JSVC
     * @throws IOException
     */
    public void init(String[] arguments) throws IOException
    {
        setup();
    }

    /**
     * Start the Cassandra Daemon, assuming that it has already been
     * initialized via {@link #init(String[])}
     *
     * Hook for JSVC
     */
    public void start()
    {
        String nativeFlag = System.getProperty("cassandra.start_native_transport");
        if ((nativeFlag != null && Boolean.parseBoolean(nativeFlag)) || (nativeFlag == null && DatabaseDescriptor.startNativeTransport()))
            nativeServer.start();
        else
            CassandraLogger.LOGGER.infof("Not starting native transport as requested. Use JMX (StorageService->startNativeTransport()) or nodetool (enablebinary) to start it");

        String rpcFlag = System.getProperty("cassandra.start_rpc");
        if ((rpcFlag != null && Boolean.parseBoolean(rpcFlag)) || (rpcFlag == null && DatabaseDescriptor.startRpc()))
            thriftServer.start();
        else
            CassandraLogger.LOGGER.infof("Not starting RPC server as requested. Use JMX (StorageService->startRPCServer()) or nodetool (enablethrift) to start it");
    }

    /**
     * Stop the daemon, ideally in an idempotent manner.
     *
     * Hook for JSVC
     */
    public void stop()
    {
        // this doesn't entirely shut down Cassandra, just the RPC server.
        // jsvc takes care of taking the rest down
        CassandraLogger.LOGGER.infof("Cassandra shutting down...");
        thriftServer.stop();
        nativeServer.stop();
    }


    /**
     * Clean up all resources obtained during the lifetime of the daemon. This
     * is a hook for JSVC.
     */
    public void destroy()
    {}

    /**
     * A convenience method to initialize and start the daemon in one shot.
     */
    public void activate()
    {
        String pidFile = System.getProperty("cassandra-pidfile");

        try
        {
            try
            {
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                mbs.registerMBean(new StandardMBean(new NativeAccess(), NativeAccessMBean.class), new ObjectName(MBEAN_NAME));
            }
            catch (Exception e)
            {
                CassandraLogger.LOGGER.error("error registering MBean " + MBEAN_NAME, e);
                //Allow the server to start even if the bean can't be registered
            }

            setup();

            if (pidFile != null)
            {
                new File(pidFile).deleteOnExit();
            }

            /*if (System.getProperty("cassandra-foreground") == null)
            {
                System.out.close();
                System.err.close();
            }*/

            start();
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Exception encountered during startup", e);
        }
    }

    /**
     * A convenience method to stop and destroy the daemon in one shot.
     */
    public void deactivate()
    {
        stop();
        destroy();
    }

    private void waitForGossipToSettle()
    {
        int forceAfter = Integer.getInteger("cassandra.skip_wait_for_gossip_to_settle", -1);
        if (forceAfter == 0)
        {
            return;
        }
        final int GOSSIP_SETTLE_MIN_WAIT_MS = 5000;
        final int GOSSIP_SETTLE_POLL_INTERVAL_MS = 1000;
        final int GOSSIP_SETTLE_POLL_SUCCESSES_REQUIRED = 3;

        CassandraLogger.LOGGER.infof("Waiting for gossip to settle before accepting client requests...");
        Uninterruptibles.sleepUninterruptibly(GOSSIP_SETTLE_MIN_WAIT_MS, TimeUnit.MILLISECONDS);
        int totalPolls = 0;
        int numOkay = 0;
        JMXEnabledThreadPoolExecutor gossipStage = (JMXEnabledThreadPoolExecutor)StageManager.getStage(Stage.GOSSIP);
        while (numOkay < GOSSIP_SETTLE_POLL_SUCCESSES_REQUIRED)
        {
            Uninterruptibles.sleepUninterruptibly(GOSSIP_SETTLE_POLL_INTERVAL_MS, TimeUnit.MILLISECONDS);
            long completed = gossipStage.getCompletedTasks();
            long active = gossipStage.getActiveCount();
            long pending = gossipStage.getPendingTasks();
            totalPolls++;
            if (active == 0 && pending == 0)
            {
                CassandraLogger.LOGGER.debugf("Gossip looks settled. CompletedTasks: {}", completed);
                numOkay++;
            }
            else
            {
                CassandraLogger.LOGGER.infof("Gossip not settled after {} polls. Gossip Stage active/pending/completed: {}/{}/{}", totalPolls, active, pending, completed);
                numOkay = 0;
            }
            if (forceAfter > 0 && totalPolls > forceAfter)
            {
                CassandraLogger.LOGGER.warnf("Gossip not settled but startup forced by cassandra.skip_wait_for_gossip_to_settle. Gossip Stage total/active/pending/completed: {}/{}/{}/{}",
                        totalPolls, active, pending, completed);
                break;
            }
        }
        if (totalPolls > GOSSIP_SETTLE_POLL_SUCCESSES_REQUIRED)
            CassandraLogger.LOGGER.infof("Gossip settled after {} extra polls; proceeding", totalPolls - GOSSIP_SETTLE_POLL_SUCCESSES_REQUIRED);
        else
            CassandraLogger.LOGGER.infof("No gossip backlog; proceeding");
    }

    public static void stop(String[] args)
    {
        instance.deactivate();
    }

    public static void main(String[] args)
    {
        instance.activate();
    }

    static class NativeAccess implements NativeAccessMBean
    {
        public boolean isAvailable()
        {
            return CLibrary.jnaAvailable();
        }

        public boolean isMemoryLockable()
        {
            return CLibrary.jnaMemoryLockable();
        }
    }
}
