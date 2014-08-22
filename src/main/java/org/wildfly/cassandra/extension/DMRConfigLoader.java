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
