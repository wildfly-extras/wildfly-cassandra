package org.wildfly.cassandra.extension;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */
enum Attribute {
    UNKNOWN(null),
    CLUSTER(CassandraSubsystemModel.CLUSTER);

    private final String name;

    private Attribute(final String name) {
        this.name = name;
    }

    /**
     * Get the local name of this element.
     *
     * @return the local name
     */
    public String getLocalName() {
        return name;
    }

    private static final Map<String, Attribute> attributes;

    static {
        final Map<String, Attribute> map = new HashMap<String, Attribute>();
        for (Attribute attribute : values()) {
            final String name = attribute.getLocalName();
            if (name != null) { map.put(name, attribute); }
        }
        attributes = map;
    }

    public static Attribute forName(String localName) {
        final Attribute attribute = attributes.get(localName);
        return attribute == null ? UNKNOWN : attribute;
    }
}