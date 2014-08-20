package org.wildfly.cassandra.extension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.ServiceRemoveStepHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.access.constraint.ApplicationTypeConfig;
import org.jboss.as.controller.access.management.AccessConstraintDefinition;
import org.jboss.as.controller.access.management.ApplicationTypeAccessConstraintDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CassandraSubsystemDefinition extends PersistentResourceDefinition {

    static final CassandraSubsystemDefinition INSTANCE = new CassandraSubsystemDefinition();

       private final List<AccessConstraintDefinition> accessConstraints;

       private CassandraSubsystemDefinition() {
           super(CassandraExtension.SUBSYSTEM_PATH,
                   CassandraExtension.getResourceDescriptionResolver(null),
                   CassandraSubsystemAdd.INSTANCE,
                   new ServiceRemoveStepHandler(CassandraSubsystemAdd.SERVICE_NAME, CassandraSubsystemAdd.INSTANCE));
           ApplicationTypeConfig atc = new ApplicationTypeConfig(CassandraExtension.SUBSYSTEM_NAME, "default");// TODO
           accessConstraints = new ApplicationTypeAccessConstraintDefinition(atc).wrapAsList();
       }

       protected static final SimpleAttributeDefinition CLUSTER_NAME =
               new SimpleAttributeDefinitionBuilder(CassandraSubsystemModel.CLUSTER_NAME, ModelType.STRING, false)
                       .setAllowExpression(true)
                       .setRestartAllServices()
                       .build();

       private static final AttributeDefinition[] ATTRIBUTES = {CLUSTER_NAME};

       @Override
       public void registerAttributes(final ManagementResourceRegistration rootResourceRegistration) {
           CassandraWriteAttributeHandler handler = new CassandraWriteAttributeHandler(ATTRIBUTES);
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
           return Collections.EMPTY_LIST;
       }

       @Override
       public List<AccessConstraintDefinition> getAccessConstraints() {
           return accessConstraints;
       }
}
