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

import org.jboss.as.controller.AttributeDefinition;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The cluster resource definition. Carries the main cassanda configuration attributes.
 *
 * @author Heiko Braun
 * @since 20/08/14
 */
public class ClusterDefinition extends PersistentResourceDefinition {

    private final List<AccessConstraintDefinition> accessConstraints;

    private ClusterDefinition() {
        super(CassandraExtension.CLUSTER_PATH,
                CassandraExtension.getResourceDescriptionResolver(CassandraModel.CLUSTER),
                ClusterAdd.INSTANCE,
                new ServiceRemoveStepHandler(ClusterAdd.SERVICE_NAME, ClusterAdd.INSTANCE));

        ApplicationTypeConfig atc = new ApplicationTypeConfig(CassandraExtension.SUBSYSTEM_NAME, CassandraModel.CLUSTER);
        accessConstraints = new ApplicationTypeAccessConstraintDefinition(atc).wrapAsList();
    }

    // -----------

    static final SimpleAttributeDefinition DEBUG =
            new SimpleAttributeDefinitionBuilder(CassandraModel.DEBUG, ModelType.BOOLEAN, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(false))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition NUM_TOKENS =
            new SimpleAttributeDefinitionBuilder(CassandraModel.NUM_TOKENS, ModelType.INT, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(256))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition HINTED_HANDOFF_ENABLED =
            new SimpleAttributeDefinitionBuilder(CassandraModel.HINTED_HANDOFF_ENABLED, ModelType.BOOLEAN, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(true))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition AUTHENTICATOR =
            new SimpleAttributeDefinitionBuilder(CassandraModel.AUTHENTICATOR, ModelType.STRING, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode("AllowAllAuthenticator"))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition AUTHORIZER =
            new SimpleAttributeDefinitionBuilder(CassandraModel.AUTHORIZER, ModelType.STRING, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode("AllowAllAuthorizer"))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition PARTIONER =
            new SimpleAttributeDefinitionBuilder(CassandraModel.PARTITIONER, ModelType.STRING, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode("org.apache.cassandra.dht.Murmur3Partitioner"))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition SEED_PROVIDER =
            new SimpleAttributeDefinitionBuilder(CassandraModel.SEED_PROVIDER, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();

    // TODO: this should become a list of seeds
    static final SimpleAttributeDefinition SEEDS =
            new SimpleAttributeDefinitionBuilder(CassandraModel.SEEDS, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();


    static final SimpleAttributeDefinition LISTEN_ADDRESS =
            new SimpleAttributeDefinitionBuilder(CassandraModel.LISTEN_ADDRESS, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition BROADCAST_ADDRESS=
            new SimpleAttributeDefinitionBuilder(CassandraModel.BROADCAST_ADDRESS, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition START_NATIVE_TRANSPORT =
            new SimpleAttributeDefinitionBuilder(CassandraModel.START_NATIVE_TRANSPORT, ModelType.BOOLEAN, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition START_RPC =
            new SimpleAttributeDefinitionBuilder(CassandraModel.START_RPC, ModelType.BOOLEAN, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition NATIVE_TRANSPORT_PORT =
            new SimpleAttributeDefinitionBuilder(CassandraModel.NATIVE_TRANSPORT_PORT, ModelType.INT, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(9042))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition RPC_PORT =
            new SimpleAttributeDefinitionBuilder(CassandraModel.RPC_PORT, ModelType.INT, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(9160))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition INTERNODE_AUTHENTICATOR =
            new SimpleAttributeDefinitionBuilder(CassandraModel.INTERNODE_AUTHENTICATOR, ModelType.STRING, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode("org.apache.cassandra.auth.AllowAllInternodeAuthenticator"))
                    .setRestartAllServices()
                    .build();

   /* static final SimpleAttributeDefinition DATA_FILE_DIR =
            new SimpleAttributeDefinitionBuilder(CassandraModel.DATA_FILE_DIR, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition SAVED_CACHES_DIR =
            new SimpleAttributeDefinitionBuilder(CassandraModel.SAVED_CACHES_DIR, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition COMMIT_LOG_DIR =
            new SimpleAttributeDefinitionBuilder(CassandraModel.COMMIT_LOG_DIR, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setRestartAllServices()
                    .build();
*/
    static final SimpleAttributeDefinition COMMIT_LOG_SYNC =
            new SimpleAttributeDefinitionBuilder(CassandraModel.COMMIT_LOG_SYNC, ModelType.STRING, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode("periodic"))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition COMMIT_LOG_SYNC_PERIOD =
            new SimpleAttributeDefinitionBuilder(CassandraModel.COMMIT_LOG_SYNC_PERIOD, ModelType.INT, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(10000))
                    .setRestartAllServices()
                    .build();


    static final SimpleAttributeDefinition ENDPOINT_SNITCH =
            new SimpleAttributeDefinitionBuilder(CassandraModel.ENDPOINT_SNITCH, ModelType.STRING, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode("SimpleSnitch"))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition REQUEST_SCHEDULER =
            new SimpleAttributeDefinitionBuilder(CassandraModel.REQUEST_SCHEDULER, ModelType.STRING, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode("org.apache.cassandra.scheduler.NoScheduler"))
                    .setRestartAllServices()
                    .build();


    static final SimpleAttributeDefinition SERVER_ENCRYPTION =
            new SimpleAttributeDefinitionBuilder(CassandraModel.SERVER_ENCRYPTION, ModelType.BOOLEAN, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(false))
                    .setRestartAllServices()
                    .build();

    static final SimpleAttributeDefinition CLIENT_ENCRYPTION =
            new SimpleAttributeDefinitionBuilder(CassandraModel.CLIENT_ENCRYPTION, ModelType.BOOLEAN, true)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(false))
                    .setRestartAllServices()
                    .build();


    // -----------
    static final AttributeDefinition[] ATTRIBUTES = {
            DEBUG, NUM_TOKENS,
            HINTED_HANDOFF_ENABLED,
            AUTHENTICATOR, AUTHORIZER,
            PARTIONER,
            SEED_PROVIDER, SEEDS,
            LISTEN_ADDRESS, BROADCAST_ADDRESS,
            START_NATIVE_TRANSPORT, NATIVE_TRANSPORT_PORT,
            START_RPC, RPC_PORT,
            INTERNODE_AUTHENTICATOR,
            COMMIT_LOG_SYNC, COMMIT_LOG_SYNC_PERIOD,
            ENDPOINT_SNITCH,
            REQUEST_SCHEDULER,
            CLIENT_ENCRYPTION, SERVER_ENCRYPTION

    };

    private static final List CHILDREN = Collections.EMPTY_LIST;

    static final ClusterDefinition INSTANCE = new ClusterDefinition();

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