package org.wildfly.cassandra.service;

import org.apache.cassandra.config.Config;
import org.apache.cassandra.config.ConfigurationLoader;
import org.apache.cassandra.config.YamlConfigurationLoader;
import org.apache.cassandra.exceptions.ConfigurationException;

import java.io.File;

/**
 * @author Heiko Braun
 * @since 16/08/14
 */
public class WildflyConfigLoader implements ConfigurationLoader {

    @Override
    public Config loadConfig() throws ConfigurationException {

        // TODO: Delegate to subsystem configuration once it's implemented
        // for now relay on an external yaml configuration file

        File configFile = new File(System.getProperty("jboss.modules.dir")
                            + "/system/layers/base/org/wildfly/cassandra/main/conf/cassandra.yaml");

        System.setProperty("cassandra.config", "file:" + configFile.getAbsolutePath());
        System.setProperty("cassandra-foreground", "true");
        YamlConfigurationLoader delegate = new YamlConfigurationLoader();
        return delegate.loadConfig();
    }
}
