package org.wildfly.cassandra.extension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.DefaultAttributeMarshaller;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.ServiceRemoveStepHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.access.constraint.ApplicationTypeConfig;
import org.jboss.as.controller.access.management.AccessConstraintDefinition;
import org.jboss.as.controller.access.management.ApplicationTypeAccessConstraintDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */
public class ClusterDefinition extends PersistentResourceDefinition {

    private final List<AccessConstraintDefinition> accessConstraints;

    private ClusterDefinition(AttributeDefinition[] attributes) {
        super(CassandraExtension.CLUSTER_PATH,
                CassandraExtension.getResourceDescriptionResolver(CassandraSubsystemModel.CLUSTER),
                new ClusterAdd(attributes),
                new ServiceRemoveStepHandler(ClusterAdd.SERVICE_NAME, new ClusterAdd(attributes)));

        ApplicationTypeConfig atc = new ApplicationTypeConfig(CassandraExtension.SUBSYSTEM_NAME, CassandraSubsystemModel.CLUSTER);
        accessConstraints = new ApplicationTypeAccessConstraintDefinition(atc).wrapAsList();
    }

    // -----------

    protected static final SimpleAttributeDefinition DEBUG =
            new SimpleAttributeDefinitionBuilder(CassandraSubsystemModel.DEBUG, ModelType.BOOLEAN, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(false))
                    .setRestartAllServices()
                    .build();

    protected static final SimpleAttributeDefinition NUM_ATTRIBUTES =
            new SimpleAttributeDefinitionBuilder(CassandraSubsystemModel.NUM_ATTRIBUTES, ModelType.INT, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(256))
                    .setRestartAllServices()
                    .build();

    protected static final SimpleAttributeDefinition HINTED_HANDOFF_ENABLED =
            new SimpleAttributeDefinitionBuilder(CassandraSubsystemModel.HINTED_HANDOFF_ENABLED, ModelType.BOOLEAN, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(true))
                    .setRestartAllServices()
                    .build();

    protected static final SimpleAttributeDefinition MAX_HINT_WINDOW =
            new SimpleAttributeDefinitionBuilder(CassandraSubsystemModel.MAX_HINT_WINDOW, ModelType.LONG, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(10800000))
                    .setRestartAllServices()
                    .build();

    protected static final SimpleAttributeDefinition HINTED_THROTTLE =
            new SimpleAttributeDefinitionBuilder(CassandraSubsystemModel.HINTED_THROTTLE, ModelType.LONG, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(1024))
                    .setRestartAllServices()
                    .build();


    protected static final SimpleAttributeDefinition HINT_DELIVERY_THREADS =
                new SimpleAttributeDefinitionBuilder(CassandraSubsystemModel.HINT_DELIVERY_THREADS, ModelType.INT, true)
                        .setAllowExpression(true)
                        .setDefaultValue(new ModelNode(2))
                        .setRestartAllServices()
                        .build();

    // -----------
    private static final AttributeDefinition[] ATTRIBUTES = {
            DEBUG, NUM_ATTRIBUTES,
            HINTED_HANDOFF_ENABLED, MAX_HINT_WINDOW, HINT_DELIVERY_THREADS, HINTED_THROTTLE
    };

    private static final List CHILDREN = Collections.EMPTY_LIST;

    static final ClusterDefinition INSTANCE = new ClusterDefinition(ATTRIBUTES);

    @Override
    public void registerAttributes(final ManagementResourceRegistration rootResourceRegistration) {
        ClusterWriteAttributeHandler handler = new ClusterWriteAttributeHandler(ATTRIBUTES);
        for (AttributeDefinition attr : ATTRIBUTES) {
            rootResourceRegistration.registerReadWriteAttribute(attr, null, handler);
        }
    }

    @Override
    public Collection<AttributeDefinition> getAttributes() {
        return Arrays.asList(ATTRIBUTES);
    }

    @Override
    protected List<? extends PersistentResourceDefinition> getChildren() {
        return CHILDREN;
    }

    @Override
    public List<AccessConstraintDefinition> getAccessConstraints() {
        return accessConstraints;
    }


}