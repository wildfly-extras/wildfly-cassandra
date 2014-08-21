package org.wildfly.cassandra.extension;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */
public class CassandraModel {
    public static final String CLUSTER = "cluster";
    public static final String NAME = "name";
    public static final String NUM_ATTRIBUTES = "num-attributes";
    public static final String DEBUG = "debug";
    public static final String HINTED_HANDOFF_ENABLED = "hinted-handoff-enabled";
    public static final String MAX_HINT_WINDOW = "max-hint-window-in-ms";
    public static final String HINTED_THROTTLE = "hinted-handoff-throttle-in-kb";
    public static final String HINT_DELIVERY_THREADS = "max-hints-delivery-threads";
}
