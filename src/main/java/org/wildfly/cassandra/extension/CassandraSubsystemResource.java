package org.wildfly.cassandra.extension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */
public class CassandraSubsystemResource extends PersistentResourceDefinition {
    public static final CassandraSubsystemResource INSTANCE = new CassandraSubsystemResource();

       private CassandraSubsystemResource() {
           super(CassandraExtension.SUBSYSTEM_PATH,
                   CassandraExtension.getResourceDescriptionResolver("default"),
                   CassandraSubsystemAdd.INSTANCE,
                   ReloadRequiredRemoveStepHandler.INSTANCE);
       }

       @Override
       public Collection<AttributeDefinition> getAttributes() {
           return Collections.emptySet();
       }

       @Override
       protected List<? extends PersistentResourceDefinition> getChildren() {
           return Arrays.asList(CassandraSubsystemDefinition.INSTANCE);
       }
}
