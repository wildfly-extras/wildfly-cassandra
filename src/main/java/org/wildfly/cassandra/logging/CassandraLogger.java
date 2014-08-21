package org.wildfly.cassandra.logging;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.MessageLogger;

/**
 * Log messages for WildFly cassandra module
 * @author Heiko Braun
 */
@MessageLogger(projectCode = "<<none>>")
public interface CassandraLogger extends BasicLogger {
    /**
     * A logger with the category {@code org.wildfly.cassandra}.
     */
    CassandraLogger LOGGER = Logger.getMessageLogger(CassandraLogger.class, "org.wildfly.cassandra");

}
