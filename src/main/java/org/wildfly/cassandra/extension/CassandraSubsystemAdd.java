package org.wildfly.cassandra.extension;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

import java.util.List;

class CassandraSubsystemAdd extends AbstractBoottimeAddStepHandler {

    //public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("cassandra");
    static final CassandraSubsystemAdd INSTANCE = new CassandraSubsystemAdd();

    private CassandraSubsystemAdd() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        model.setEmptyObject();
    }

    @Override
    protected void performBoottime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

       // register deployers if needed

    }
}
