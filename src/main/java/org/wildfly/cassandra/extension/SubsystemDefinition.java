package org.wildfly.cassandra.extension;

import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;

/**
 * @author <a href="mailto:tcerar@redhat.com">Tomaz Cerar</a>
 */
public class SubsystemDefinition extends SimpleResourceDefinition {
    public static final SubsystemDefinition INSTANCE = new SubsystemDefinition();

    private SubsystemDefinition() {
        super(SubsystemExtension.SUBSYSTEM_PATH,
                SubsystemExtension.getResourceDescriptionResolver(null),
                //We always need to add an 'add' operation
                SubsystemAdd.INSTANCE,
                //Every resource that is added, normally needs a remove operation
                SubsystemRemove.INSTANCE);
    }

    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        //you can register aditional operations here
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {

       /* resourceRegistration.registerReadWriteAttribute(Attributes.authenticator, null, new ReloadRequiredWriteAttributeHandler(Attributes.authenticator));
        resourceRegistration.registerReadWriteAttribute(Attributes.authorizer, null, new ReloadRequiredWriteAttributeHandler(Attributes.authorizer));
        resourceRegistration.registerReadWriteAttribute(Attributes.batchlog_replay_throttle_in_kb, null, new ReloadRequiredWriteAttributeHandler(Attributes.batchlog_replay_throttle_in_kb));*/
        resourceRegistration.registerReadWriteAttribute(Attributes.cluster_name, null, new ReloadRequiredWriteAttributeHandler(Attributes.cluster_name));
        /*resourceRegistration.registerReadWriteAttribute(Attributes.commit_failure_policy, null, new ReloadRequiredWriteAttributeHandler(Attributes.commit_failure_policy));
        resourceRegistration.registerReadWriteAttribute(Attributes.commitlog_directory, null, new ReloadRequiredWriteAttributeHandler(Attributes.commitlog_directory));
        resourceRegistration.registerReadWriteAttribute(Attributes.commitlog_sync, null, new ReloadRequiredWriteAttributeHandler(Attributes.commitlog_sync));
        resourceRegistration.registerReadWriteAttribute(Attributes.commitlog_sync_period_in_ms, null, new ReloadRequiredWriteAttributeHandler(Attributes.commitlog_sync_period_in_ms));
        resourceRegistration.registerReadWriteAttribute(Attributes.data_file_directories, null, new ReloadRequiredWriteAttributeHandler(Attributes.data_file_directories));
        resourceRegistration.registerReadWriteAttribute(Attributes.hinted_handoff, null, new ReloadRequiredWriteAttributeHandler(Attributes.hinted_handoff));
        resourceRegistration.registerReadWriteAttribute(Attributes.hinted_handoff_throttle_in_kb, null, new ReloadRequiredWriteAttributeHandler(Attributes.hinted_handoff_throttle_in_kb));
        resourceRegistration.registerReadWriteAttribute(Attributes.key_cache_save_period, null, new ReloadRequiredWriteAttributeHandler(Attributes.key_cache_save_period));
        resourceRegistration.registerReadWriteAttribute(Attributes.key_cache_size_in_mb, null, new ReloadRequiredWriteAttributeHandler(Attributes.key_cache_size_in_mb));
        resourceRegistration.registerReadWriteAttribute(Attributes.max_hint_window_in_ms, null, new ReloadRequiredWriteAttributeHandler(Attributes.max_hint_window_in_ms));
        resourceRegistration.registerReadWriteAttribute(Attributes.max_hints_delivery_threads, null, new ReloadRequiredWriteAttributeHandler(Attributes.max_hints_delivery_threads));
        resourceRegistration.registerReadWriteAttribute(Attributes.num_attributes, null, new ReloadRequiredWriteAttributeHandler(Attributes.num_attributes));
        resourceRegistration.registerReadWriteAttribute(Attributes.partitioner, null, new ReloadRequiredWriteAttributeHandler(Attributes.partitioner));
        resourceRegistration.registerReadWriteAttribute(Attributes.permissions_validity_in_ms, null, new ReloadRequiredWriteAttributeHandler(Attributes.permissions_validity_in_ms));
        resourceRegistration.registerReadWriteAttribute(Attributes.row_cache_save_period, null, new ReloadRequiredWriteAttributeHandler(Attributes.row_cache_save_period));
        resourceRegistration.registerReadWriteAttribute(Attributes.row_cache_size_in_mb, null, new ReloadRequiredWriteAttributeHandler(Attributes.row_cache_size_in_mb));
        resourceRegistration.registerReadWriteAttribute(Attributes.saved_caches_directory, null, new ReloadRequiredWriteAttributeHandler(Attributes.saved_caches_directory));*/


    }
}
