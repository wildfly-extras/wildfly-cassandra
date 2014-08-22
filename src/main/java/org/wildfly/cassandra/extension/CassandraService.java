package org.wildfly.cassandra.extension;

import org.apache.cassandra.config.Config;
import org.jboss.as.controller.services.path.AbsolutePathService;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.cassandra.logging.CassandraLogger;

/**
 * The actual cassandra runtime service
 * @author Heiko Braun
 */
public class CassandraService implements Service<CassandraService> {


    private static final String CASSANDRA_DATA_FILE_DIR = "cassandra/data";
    private static final String CASSANDRA_SAVED_CACHES_DIR = "cassandra/saved_caches";
    private static final String CASSANDRA_COMMIT_LOG_DIR = "cassandra/commitlog";
    private final String suffix;
    private final Config serviceConfig;

    private WildflyCassandraDaemon cassandraDaemon;
    private final InjectedValue<PathManager> pathManager = new InjectedValue<PathManager>();

    public CassandraService(String suffix, Config serviceConfig) {
        this.suffix = suffix;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public CassandraService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public void start(StartContext context) throws StartException {

        try {
            CassandraLogger.LOGGER.infof("Starting embedded cassandra service '%s'");

            // resolve the path location
            serviceConfig.data_file_directories = new String[]{resolve(pathManager.getValue(), CASSANDRA_DATA_FILE_DIR, ServerEnvironment.SERVER_DATA_DIR)};
            serviceConfig.saved_caches_directory = resolve(pathManager.getValue(), CASSANDRA_SAVED_CACHES_DIR, ServerEnvironment.SERVER_DATA_DIR);
            serviceConfig.commitlog_directory = resolve(pathManager.getValue(), CASSANDRA_COMMIT_LOG_DIR, ServerEnvironment.SERVER_DATA_DIR);

            // static injection needed due to the way C* initialises it's ConfigLoader
            DMRConfigLoader.CASSANDRA_CONFIG = serviceConfig;
            System.setProperty("cassandra.config.loader", DMRConfigLoader.class.getName());

            cassandraDaemon = new WildflyCassandraDaemon();
            cassandraDaemon.activate();

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

    public Injector<PathManager> getPathManagerInjector(){
        return pathManager;
    }


    private String resolve(PathManager pathManager, String path, String relativeToPath) {
        // discard the relativeToPath if the path is absolute and must not be resolved according
        // to the default relativeToPath value
        String relativeTo = AbsolutePathService.isAbsoluteUnixOrWindowsPath(path) ? null : relativeToPath;
        return pathManager.resolveRelativePathEntry(path, relativeTo);
    }



   /* static class PathConfig {
        private final String dataFileDir;
        private final String savedCachesDir;
        private final String commitlogDir;

        private final String dataFileRel;
        private final String saveCacheRel;
        private final String cmmitlogRel;

        public PathConfig(String dataFileDir, String dataFileRel,
                          String savedCachesDir, String saveCacheRel,
                          String commitlogDir, String commitlogRel) {
            this.dataFileDir = dataFileDir;
            this.savedCachesDir = savedCachesDir;
            this.commitlogDir = commitlogDir;

            this.dataFileRel = dataFileRel;
            this.saveCacheRel = saveCacheRel;
            this.cmmitlogRel = commitlogRel;
        }

        String resolveDataFilePath(PathManager pathManager) {
            return resolve(pathManager, dataFileDir, dataFileRel);
        }

        String resolveSavedCachesPath(PathManager pathManager) {
            return resolve(pathManager, savedCachesDir, saveCacheRel);
        }

        String resolveCommitlogPath(PathManager pathManager) {
            return resolve(pathManager, commitlogDir, commitlogDir);
        }

    }*/
}
