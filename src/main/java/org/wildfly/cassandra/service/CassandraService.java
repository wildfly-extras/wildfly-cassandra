package org.wildfly.cassandra.service;

import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.wildfly.cassandra.extension.DMRConfigLoader;
import org.wildfly.cassandra.logging.CassandraLogger;

/**
 * The actual cassandra runtime service
 * @author Heiko Braun
 */
public class CassandraService implements Service<CassandraService> {


    private final String suffix;
    private WildflyCassandraDaemon cassandraDaemon;

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
            CassandraLogger.LOGGER.infof("Starting embedded cassandra service '%s'");

            System.setProperty("cassandra.config.loader", DMRConfigLoader.class.getName());

            //cleanupAndLeaveDirs();

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
}
