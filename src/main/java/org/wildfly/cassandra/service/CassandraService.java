package org.wildfly.cassandra.service;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.commitlog.CommitLog;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.CassandraDaemon;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.wildfly.cassandra.logging.CassandraLogger;

import java.io.File;
import java.io.IOException;

/**
 * @author Heiko Braun
 */
public class CassandraService implements Service<CassandraService> {


    private final String suffix;
    private CassandraDaemon cassandraDaemon;

    public CassandraService(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public CassandraService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public void start(StartContext context) throws StartException {

        try {
            File configFile = new File(System.getProperty("jboss.modules.dir")
                    + "/system/layers/base/org/wildfly/cassandra/main/conf/cassandra.yaml");

            if(configFile.exists()) {
                CassandraLogger.LOGGER.infof("Starting embedded cassandra service '%s'");

                System.setProperty("cassandra.config.loader", WildflyConfigLoader.class.getName());

                cleanupAndLeaveDirs();

                cassandraDaemon = new CassandraDaemon();
                cassandraDaemon.activate();
            }
            else
            {
                context.failed(new StartException("Failed to start cassandra service. Configuration file is missing:"+configFile.getAbsolutePath()));
            }
        } catch (Throwable e) {
            context.failed(new StartException(e));
        }
    }

    @Override
    public void stop(StopContext context) {
        if(cassandraDaemon!=null)
        {
            CassandraLogger.LOGGER.infof("Stopping cassandra service '%s'.", suffix);
            cassandraDaemon.deactivate();
        }
    }

    private static void cleanupAndLeaveDirs() throws IOException {
        mkdirs();
        cleanup();
        mkdirs();

        // cleanup screws with CommitLog, this brings it back to safe state
        CommitLog.instance.resetUnsafe();
    }

    private static void cleanup() throws IOException {
        // clean up commitlog
        String[] directoryNames = {DatabaseDescriptor.getCommitLogLocation(),};
        for (String dirName : directoryNames) {
            File dir = new File(dirName);
            if (!dir.exists())
                throw new RuntimeException("No such directory: " + dir.getAbsolutePath());
            FileUtils.deleteRecursive(dir);
        }

        // clean up data directory which are stored as data directory/table/data files
        for (String dirName : DatabaseDescriptor.getAllDataFileLocations()) {
            File dir = new File(dirName);
            if (!dir.exists())
                throw new RuntimeException("No such directory: " + dir.getAbsolutePath());
            FileUtils.deleteRecursive(dir);
        }
    }

    public static void mkdirs() {
        DatabaseDescriptor.createAllDirectories();
    }


}
