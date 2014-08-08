package org.wildfly.cassandra.extension;

import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

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
        System.out.println("Starting cassandra service");
    }

    @Override
    public void stop(StopContext context) {
        System.out.println("Stopping cassandra service");
    }

    public static ServiceName createServiceName(String suffix) {
        return ServiceName.JBOSS.append("cassandra", suffix);
    }

}
