package org.wildfly.cassandra.extension;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.service.CassandraDaemon;
import org.apache.thrift.transport.TTransportException;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.wildfly.cassandra.logging.CassandraLogger;
import org.wildfly.cassandra.server.EmbeddedServerHelper;

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
        CassandraLogger.LOGGER.infof("Starting embedded cassandra service '%s'.", suffix);
        try {
            cassandraDaemon = new EmbeddedServerHelper().startEmbeddedCassandra(
                    new File(System.getProperty("user.home") + "/cassandra.yaml"),
                    System.getProperty("java.io.tmpdir"),
                    3000
            );
        } catch (Throwable e) {
            context.failed(new StartException(e));
        }
    }

    @Override
    public void stop(StopContext context) {
        CassandraLogger.LOGGER.infof("Stopping cassandra service '%s'.", suffix);
        if(cassandraDaemon!=null)
        {
            cassandraDaemon.deactivate();
        }
    }

    public static ServiceName createServiceName(String suffix) {
        return ServiceName.JBOSS.append("cassandra", suffix);
    }

}
