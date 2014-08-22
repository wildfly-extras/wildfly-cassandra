package org.wildfly.cassandra.extension;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */

import org.apache.cassandra.config.Config;
import org.apache.cassandra.config.SeedProviderDef;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.controller.services.path.PathManagerService;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

/**
 * Handler responsible for adding the cluster resource to the model
 *
 * @author Heiko Braun
 */
class ClusterAdd extends AbstractAddStepHandler {

    public final static ClusterAdd INSTANCE = new ClusterAdd();
    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("cassandra");

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        for (AttributeDefinition def : ClusterDefinition.ATTRIBUTES) {
            def.validateAndSet(operation, model);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> controllers) throws OperationFailedException {
        final PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));
        ModelNode fullTree = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));
        installRuntimeServices(context, address, fullTree, verificationHandler, controllers);
    }

    static void installRuntimeServices(OperationContext context, PathAddress address, ModelNode fullModel, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> controllers) throws OperationFailedException {
        String suffix = address.getLastElement().getValue();

        final Config serviceConfig = createServiceConfig(context, address, fullModel);

        // Create path services
        //String bindingsPath = PATHS.get(BINDINGS_DIRECTORY).resolveModelAttribute(context, model.get(PATH, BINDINGS_DIRECTORY)).asString();

        CassandraService service = new CassandraService(suffix, serviceConfig);
        ServiceController<CassandraService> controller = context.getServiceTarget()
                .addService(SERVICE_NAME, service)
                .addListener(verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .addDependency(PathManagerService.SERVICE_NAME, PathManager.class, service.getPathManagerInjector())
                .install();
        controllers.add(controller);

    }

    private static Config createServiceConfig(OperationContext context, PathAddress address, ModelNode fullModel) throws OperationFailedException {
        // create the actual cassandra config singleton
        final Config cassandraConfig = new Config();
        cassandraConfig.cluster_name = address.getLastElement().getValue();
        cassandraConfig.num_tokens= ClusterDefinition.NUM_TOKENS.resolveModelAttribute(context, fullModel).asInt();
        cassandraConfig.hinted_handoff_enabled = ClusterDefinition.HINTED_HANDOFF_ENABLED.resolveModelAttribute(context, fullModel).asString();

        cassandraConfig.authenticator = ClusterDefinition.AUTHENTICATOR.resolveModelAttribute(context, fullModel).asString();
        cassandraConfig.authorizer= ClusterDefinition.AUTHORIZER.resolveModelAttribute(context, fullModel).asString();
        cassandraConfig.partitioner= ClusterDefinition.PARTIONER.resolveModelAttribute(context, fullModel).asString();

        // The cassandra config is a real brainfuck
        LinkedHashMap providerConfig = new LinkedHashMap();
        providerConfig.put("class_name", ClusterDefinition.SEED_PROVIDER.resolveModelAttribute(context, fullModel).asString());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("seeds", ClusterDefinition.SEEDS.resolveModelAttribute(context, fullModel).asString());
        ArrayList wrapper = new ArrayList();
        wrapper.add(params);
        providerConfig.put("parameters", wrapper);

        SeedProviderDef providerDef = new SeedProviderDef(providerConfig);
        cassandraConfig.seed_provider = providerDef;
        //cassandraConfig.seed_provider.class_name = ClusterDefinition.SEED_PROVIDER.resolveModelAttribute(context, fullModel).asString();
        //cassandraConfig.seed_provider.parameters = params;


        cassandraConfig.listen_address = ClusterDefinition.LISTEN_ADDRESS.resolveModelAttribute(context, fullModel).asString();
        cassandraConfig.broadcast_address = ClusterDefinition.BROADCAST_ADDRESS.resolveModelAttribute(context, fullModel).asString();

        cassandraConfig.start_native_transport= ClusterDefinition.START_NATIVE_TRANSPORT.resolveModelAttribute(context, fullModel).asBoolean();
        cassandraConfig.start_rpc = ClusterDefinition.START_RPC.resolveModelAttribute(context, fullModel).asBoolean();

        cassandraConfig.native_transport_port= ClusterDefinition.NATIVE_TRANSPORT_PORT.resolveModelAttribute(context, fullModel).asInt();
        cassandraConfig.rpc_port= ClusterDefinition.RPC_PORT.resolveModelAttribute(context, fullModel).asInt();

        cassandraConfig.internode_authenticator= ClusterDefinition.INTERNODE_AUTHENTICATOR.resolveModelAttribute(context, fullModel).asString();

        // NOTE: The directories are resolved as part of the service boostrap due to a service dependency on PathManager

        /*cassandraConfig.data_file_directories= new String[]{ClusterDefinition.DATA_FILE_DIR.resolveModelAttribute(context, fullModel).asString()};
        cassandraConfig.saved_caches_directory= ClusterDefinition.SAVED_CACHES_DIR.resolveModelAttribute(context, fullModel).asString();
        cassandraConfig.commitlog_directory= ClusterDefinition.COMMIT_LOG_DIR.resolveModelAttribute(context, fullModel).asString();*/

        cassandraConfig.commitlog_sync= Config.CommitLogSync.valueOf(ClusterDefinition.COMMIT_LOG_SYNC.resolveModelAttribute(context, fullModel).asString());
        cassandraConfig.commitlog_sync_period_in_ms = ClusterDefinition.COMMIT_LOG_SYNC_PERIOD.resolveModelAttribute(context, fullModel).asInt();

        cassandraConfig.endpoint_snitch= ClusterDefinition.ENDPOINT_SNITCH.resolveModelAttribute(context, fullModel).asString();
        cassandraConfig.request_scheduler= ClusterDefinition.REQUEST_SCHEDULER.resolveModelAttribute(context, fullModel).asString();
        // TODO: encryption options
        //cassandraConfig.server_encryption_options =
        //cassandraConfig.client_encryption_options =

        return cassandraConfig;

    }

}
