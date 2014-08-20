package org.wildfly.cassandra.extension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.DefaultAttributeMarshaller;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author Heiko Braun
 * @since 18/08/14
 */
public class Attributes {

    static final SimpleAttributeDefinition num_attributes = SimpleAttributeDefinitionBuilder.create("num-attributes", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("num-attributes");

                    final String value = resourceModel.get("num-attributes").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setDefaultValue(new ModelNode(256))
            .setRestartJVM()
            .build();


    static final SimpleAttributeDefinition hinted_handoff = SimpleAttributeDefinitionBuilder.create("hinted-handoff-enabled", ModelType.BOOLEAN, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("hinted-handoff-enabled");

                    final String value = resourceModel.get("hinted-handoff-enabled").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setDefaultValue(new ModelNode(true))
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition max_hint_window_in_ms = SimpleAttributeDefinitionBuilder.create("max-hint-window-in-ms", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("max-hint-window-in-ms");

                    final String value = resourceModel.get("max-hint-window-in-ms").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setDefaultValue(new ModelNode(10800000))
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition hinted_handoff_throttle_in_kb = SimpleAttributeDefinitionBuilder.create("hinted-handoff-throttle-in-kb", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("hinted-handoff-throttle-in-kb");

                    final String value = resourceModel.get("hinted-handoff-throttle-in-kb").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setDefaultValue(new ModelNode(1024))
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition max_hints_delivery_threads = SimpleAttributeDefinitionBuilder.create("max-hints-delivery-threads", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("max-hints-delivery-threads");

                    final String value = resourceModel.get("max-hints-delivery-threads").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setDefaultValue(new ModelNode(2))
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition batchlog_replay_throttle_in_kb = SimpleAttributeDefinitionBuilder.create("batchlog-replay-throttle-in-kb", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("batchlog-replay-throttle-in-kb");

                    final String value = resourceModel.get("batchlog-replay-throttle-in-kb").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setDefaultValue(new ModelNode(1024))
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition authenticator = SimpleAttributeDefinitionBuilder.create("authenticator", ModelType.STRING, false)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("authenticator");

                    final String value = resourceModel.get("authenticator").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition authorizer = SimpleAttributeDefinitionBuilder.create("authorizer", ModelType.STRING, false)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("authorizer");

                    final String value = resourceModel.get("authorizer").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition permissions_validity_in_ms = SimpleAttributeDefinitionBuilder.create("permissions-validity-in-ms", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("permissions-validity-in-ms");

                    final String value = resourceModel.get("permissions-validity-in-ms").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setDefaultValue(new ModelNode(2000))
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition partitioner = SimpleAttributeDefinitionBuilder.create("partitioner", ModelType.STRING, false)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("partitioner");

                    final String value = resourceModel.get("partitioner").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition data_file_directories = SimpleAttributeDefinitionBuilder.create("data-file-directories", ModelType.STRING, false)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("data-file-directories");

                    final String value = resourceModel.get("data-file-directories").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition commitlog_directory = SimpleAttributeDefinitionBuilder.create("commitlog-directory", ModelType.STRING, false)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("commitlog-directory");

                    final String value = resourceModel.get("commitlog-directory").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition commit_failure_policy = SimpleAttributeDefinitionBuilder.create("commit-failure-policy", ModelType.STRING, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("commit-failure-policy");

                    final String value = resourceModel.get("commit-failure-policy").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .setDefaultValue(new ModelNode("stop"))
            .build();

    static final SimpleAttributeDefinition key_cache_size_in_mb = SimpleAttributeDefinitionBuilder.create("key-cache-size-in-mb", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("key-cache-size-in-mb");

                    final String value = resourceModel.get("key-cache-size-in-mb").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .setDefaultValue(new ModelNode(100))
            .build();

    static final SimpleAttributeDefinition key_cache_save_period = SimpleAttributeDefinitionBuilder.create("key-cache-save-period", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("key-cache-save-period");

                    final String value = resourceModel.get("key-cache-save-period").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .setDefaultValue(new ModelNode(14400))
            .build();

    static final SimpleAttributeDefinition row_cache_size_in_mb = SimpleAttributeDefinitionBuilder.create("row-cache-size-in-mb", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("row-cache-size-in-mb");

                    final String value = resourceModel.get("row-cache-size-in-mb").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .setDefaultValue(new ModelNode(0))
            .build();

    static final SimpleAttributeDefinition row_cache_save_period = SimpleAttributeDefinitionBuilder.create("row-cache-save-period", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("row-cache-save-period");

                    final String value = resourceModel.get("row-cache-save-period").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .setDefaultValue(new ModelNode(0))
            .build();

    static final SimpleAttributeDefinition saved_caches_directory = SimpleAttributeDefinitionBuilder.create("saved-caches-directory", ModelType.STRING, false)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("saved-caches-directory");

                    final String value = resourceModel.get("saved-caches-directory").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .build();

    static final SimpleAttributeDefinition commitlog_sync = SimpleAttributeDefinitionBuilder.create("commitlog-sync", ModelType.STRING, true)
               .setAllowExpression(true)
               .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                   @Override
                   public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                       marshallAsElement(attribute, resourceModel, true, writer);
                   }

                   @Override
                   public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                       writer.writeStartElement("commitlog-sync");

                       final String value = resourceModel.get("commitlog-sync").asString();
                       writer.writeCharacters(value);

                       writer.writeEndElement();
                   }
               })
               .setRestartJVM()
            .setDefaultValue(new ModelNode("periodic"))
               .build();

    static final SimpleAttributeDefinition commitlog_sync_period_in_ms = SimpleAttributeDefinitionBuilder.create("commitlog-sync-period-in-ms", ModelType.LONG, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {

                    writer.writeStartElement("commitlog-sync-period-in-ms");

                    final String value = resourceModel.get("commitlog-sync-period-in-ms").asString();
                    writer.writeCharacters(value);

                    writer.writeEndElement();
                }
            })
            .setRestartJVM()
            .setDefaultValue(new ModelNode(10000))
            .build();
}
