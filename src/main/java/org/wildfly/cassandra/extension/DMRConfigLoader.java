package org.wildfly.cassandra.extension;

import org.apache.cassandra.config.Config;
import org.apache.cassandra.config.ConfigurationLoader;
import org.apache.cassandra.exceptions.ConfigurationException;

/**
 * A delegate mechanism to load the cassandra configuration from the wildfly management model.
 *
 * @author Heiko Braun
 * @since 16/08/14
 */
public class DMRConfigLoader implements ConfigurationLoader {

    static Config CASSANDRA_CONFIG = null;

    @Override
    public Config loadConfig() throws ConfigurationException {
        if(null==CASSANDRA_CONFIG)
            throw new IllegalStateException("Cassandra config not initialized");

        return CASSANDRA_CONFIG;
    }

    /*private Config loadFromConfigFile() throws ConfigurationException {
        File configFile = new File(System.getProperty("jboss.modules.dir")
                            + "/system/layers/base/org/wildfly/cassandra/main/conf/cassandra.yaml");

        System.setProperty("cassandra.config", "file:" + configFile.getAbsolutePath());
        System.setProperty("cassandra-foreground", "true");
        YamlConfigurationLoader delegate = new YamlConfigurationLoader();
        return delegate.loadConfig();
    }*/
}
