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
 * The cassandra runtime service.
 * Delegates to an adapter (@link WildflyCassandraDaemon) that wraps the actual C* services.
 *
 * @author Heiko Braun
 */
public class CassandraService implements Service<CassandraService> {


    private static final String CASSANDRA_DATA_FILE_DIR = "cassandra/data";
    private static final String CASSANDRA_SAVED_CACHES_DIR = "cassandra/saved_caches";
    private static final String CASSANDRA_COMMIT_LOG_DIR = "cassandra/commitlog";

    private final String clusterName;
    private final Config serviceConfig;

    private WildflyCassandraDaemon cassandraDaemon;
    private final InjectedValue<PathManager> pathManager = new InjectedValue<PathManager>();

    public CassandraService(String clusterName, Config serviceConfig) {
        this.clusterName = clusterName;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public CassandraService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public void start(StartContext context) throws StartException {

        try {
            CassandraLogger.LOGGER.infof("Starting embedded cassandra service '%s'", clusterName);

            // resolve the path location
            // includes the _clusterName_ suffix to avid conflicts when different configurations are started on the same base system
            serviceConfig.data_file_directories = new String[]{resolve(pathManager.getValue(), CASSANDRA_DATA_FILE_DIR, ServerEnvironment.SERVER_DATA_DIR)+"/"+clusterName};
            serviceConfig.saved_caches_directory = resolve(pathManager.getValue(), CASSANDRA_SAVED_CACHES_DIR, ServerEnvironment.SERVER_DATA_DIR)+"/"+clusterName;
            serviceConfig.commitlog_directory = resolve(pathManager.getValue(), CASSANDRA_COMMIT_LOG_DIR, ServerEnvironment.SERVER_DATA_DIR)+"/"+clusterName;

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
            CassandraLogger.LOGGER.infof("Stopping cassandra service '%s'.", clusterName);
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

}
