package org.wildfly.cassandra.extension;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.wildfly.cassandra.service.CassandraService;

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

        CassandraService service = new CassandraService(suffix);
        ServiceController<CassandraService> controller = context.getServiceTarget()
                .addService(SERVICE_NAME, service)
                .addListener(verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
        controllers.add(controller);

    }
}
