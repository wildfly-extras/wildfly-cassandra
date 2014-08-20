package org.wildfly.cassandra.extension;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PersistentResourceXMLDescription;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.List;

import static org.jboss.as.controller.PersistentResourceXMLDescription.builder;


/**
 * The subsystem parser, which uses stax to read and write to and from xml
 */
public class CassandraSubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

    private static final PersistentResourceXMLDescription xmlDescription;

    static {
        xmlDescription = builder(CassandraSubsystemResource.INSTANCE)
                .addChild(
                        builder(ClusterDefinition.INSTANCE)
                                .addAttributes(ClusterDefinition.INSTANCE.getAttributes())
                )               .setXmlElementName(CassandraSubsystemModel.CLUSTER)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {
        ModelNode model = new ModelNode();
        model.get(CassandraSubsystemResource.INSTANCE.getPathElement().getKeyValuePair()).set(context.getModelNode());//this is bit of workaround for SPRD to work properly
        xmlDescription.persist(writer, model, Namespace.CURRENT.getUriString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
        xmlDescription.parse(reader, PathAddress.EMPTY_ADDRESS, list);
    }

}

