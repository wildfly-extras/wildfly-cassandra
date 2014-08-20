package org.wildfly.cassandra.extension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.RestartParentWriteAttributeHandler;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceName;

import java.util.Collection;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */
public class CassandraWriteAttributeHandler extends RestartParentWriteAttributeHandler {

    CassandraWriteAttributeHandler(AttributeDefinition... attributeDefinitions) {
        super(CassandraSubsystemModel.CLUSTER_NAME, attributeDefinitions);
    }

    CassandraWriteAttributeHandler(Collection<AttributeDefinition> attributeDefinitions) {
        super(CassandraSubsystemModel.CLUSTER_NAME, attributeDefinitions);
    }

    @Override
    protected void recreateParentService(OperationContext context, PathAddress parentAddress, ModelNode parentModel, ServiceVerificationHandler verificationHandler) throws OperationFailedException {
        //CassandraSubsystemAdd.installRuntimeServices(context, parentAddress, parentModel, verificationHandler, new ArrayList<ServiceController<?>>());
    }

    @Override
    protected ServiceName getParentServiceName(PathAddress parentAddress) {
        return CassandraSubsystemAdd.SERVICE_NAME.append(parentAddress.getLastElement().getValue());
    }

    @Override
    protected void removeServices(OperationContext context, ServiceName parentService, ModelNode parentModel) throws OperationFailedException {
        super.removeServices(context, parentService, parentModel);
    }
}
