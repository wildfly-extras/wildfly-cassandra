package org.wildfly.cassandra.extension;

import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.wildfly.cassandra.logging.CassandraLogger;

/**
 * @author Heiko Braun
 */
public class CassandraService implements Service<CassandraService> {


    private final String suffix;

    public CassandraService(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public CassandraService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public void start(StartContext context) throws StartException {
        //CassandraLogger.LOGGER.infof("Starting embedded cassandra service '%s'.", suffix);
        System.out.println("Starting embedded cassandra service");
    }

    @Override
    public void stop(StopContext context) {
        //CassandraLogger.LOGGER.infof("Stopping cassandra service '%s'.", suffix);
        System.out.println("Stopping embedded cassandra service");
    }

    public static ServiceName createServiceName(String suffix) {
        return ServiceName.JBOSS.append("cassandra", suffix);
    }

}
