package org.wildfly.cassandra.extension;

import org.apache.cassandra.service.CassandraDaemon;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.wildfly.cassandra.logging.CassandraLogger;
import org.wildfly.cassandra.server.EmbeddedServerHelper;

import java.io.File;

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
                    + "/system/layers/base/org/wildfly/cassandra/main/cassandra.yaml");

            // standalone or domain temp dir
            String tempDir = System.getProperty("jboss.domain.temp.dir")!=null ?
                    System.getProperty("jboss.domain.temp.dir") : System.getProperty("jboss.server.temp.dir");

            // TODO: integrate cassandra configuration or at least the config file location
            if(configFile.exists()) {
                CassandraLogger.LOGGER.infof("Starting embedded cassandra service '%s' with config: %s", suffix, configFile.getAbsolutePath());

                cassandraDaemon = new EmbeddedServerHelper().startEmbeddedCassandra(
                        configFile, tempDir, 3000
                );
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

    public static ServiceName createServiceName(String suffix) {
        return ServiceName.JBOSS.append("cassandra", suffix);
    }

}
