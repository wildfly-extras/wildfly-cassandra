package org.wildfly.cassandra.extension;

import java.util.List;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.msc.service.ServiceName;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.wildfly.cassandra.service.CassandraService;

/**
 * Handler responsible for adding the subsystem resource to the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class CassandraSubsystemAdd extends AbstractBoottimeAddStepHandler {

    static final CassandraSubsystemAdd INSTANCE = new CassandraSubsystemAdd();
    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("cassandra");

    private CassandraSubsystemAdd() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performBoottime(
            OperationContext context,
            ModelNode operation, ModelNode model,
            ServiceVerificationHandler verificationHandler,
            List<ServiceController<?>> newControllers)
            throws OperationFailedException {

        String suffix = PathAddress.pathAddress(operation.get(ModelDescriptionConstants.ADDRESS)).getLastElement().getValue();

        CassandraService service = new CassandraService(suffix);
        ServiceName name = CassandraSubsystemAdd.SERVICE_NAME;
        ServiceController<CassandraService> controller = context.getServiceTarget()
                .addService(name, service)
                .addListener(verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
        newControllers.add(controller);

    }
}
