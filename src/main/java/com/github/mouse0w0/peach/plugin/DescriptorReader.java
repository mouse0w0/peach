package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.peach.extension.ExtensionOrder;
import com.github.mouse0w0.peach.service.ServiceDescriptor;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.version.VersionRange;
import org.dom4j.CDATA;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.xml.stream.XMLStreamConstants.*;

final class DescriptorReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(DescriptorReader.class);

    public static PluginDescriptor read(String systemId, InputStream inputStream) throws IOException, XMLStreamException {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            return read(XMLInputFactory.newDefaultFactory().createXMLStreamReader(systemId, bis));
        }
    }

    public static PluginDescriptor read(XMLStreamReader reader) throws XMLStreamException {
        if (reader.getEventType() != START_DOCUMENT) {
            throw new XMLStreamException("Expected START_DOCUMENT event, found: " + reader.getEventType(), reader.getLocation());
        }
        PluginDescriptor descriptor = new PluginDescriptor();
        while (true) {
            if (reader.next() == START_ELEMENT) break;
        }
        readPlugin(reader, descriptor);
        if (reader.next() != END_DOCUMENT) {
            throw new XMLStreamException("Expected END_DOCUMENT event, found: " + reader.getEventType(), reader.getLocation());
        }
        return descriptor;
    }

    private static void readPlugin(XMLStreamReader reader, PluginDescriptor descriptor) throws XMLStreamException {
        while (true) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "id" -> descriptor.id = reader.getElementText();
                    case "name" -> descriptor.name = reader.getElementText();
                    case "version" -> descriptor.version = reader.getElementText();
                    case "logo" -> descriptor.logo = reader.getElementText();
                    case "license" -> descriptor.license = reader.getElementText();
                    case "description" -> descriptor.description = reader.getElementText();
                    case "category" -> descriptor.category = reader.getElementText();
                    case "author" -> descriptor.author = reader.getElementText();
                    case "authorUrl" -> descriptor.authorUrl = reader.getElementText();
                    case "authorEmail" -> descriptor.authorEmail = reader.getElementText();
                    case "homepage" -> descriptor.homepage = reader.getElementText();
                    case "repository" -> descriptor.repository = reader.getElementText();
                    case "issueTrackingUrl" -> descriptor.issueTrackingUrl = reader.getElementText();
                    case "issueTrackingEmail" -> descriptor.issueTrackingEmail = reader.getElementText();
                    case "updateUrl" -> descriptor.updateUrl = reader.getElementText();
                    case "dependencies" -> readDependencies(reader, descriptor);
                    case "actions" -> readActions(reader, descriptor);
                    case "extensionPoints" -> readExtensionPoints(reader, descriptor);
                    case "extensions" -> readExtensions(reader, descriptor);
                    default -> LOGGER.error("Unknown element `{}`\n{}", reader.getLocalName(), reader.getLocation());
                }
                skipElement(reader);
            } else if (event == END_ELEMENT) {
                return;
            }
        }
    }

    private static void readDependencies(XMLStreamReader reader, PluginDescriptor descriptor) throws XMLStreamException {
        List<PluginDependency> dependencies = descriptor.dependencies;
        if (dependencies == null) {
            dependencies = new ArrayList<>();
            descriptor.dependencies = dependencies;
        }

        while (true) {
            int type = reader.next();
            if (type == START_ELEMENT) {
                if ("dependency".equals(reader.getLocalName())) {
                    String[] strings = StringUtils.split(reader.getElementText(), ':');
                    switch (strings.length) {
                        case 1 ->
                                dependencies.add(new PluginDependency(strings[0], PluginDependency.ACCEPT_ALL_VERSION, false));
                        case 2 ->
                                dependencies.add(new PluginDependency(strings[0], VersionRange.createFromVersionSpec(strings[1]), false));
                        case 3 ->
                                dependencies.add(new PluginDependency(strings[0], VersionRange.createFromVersionSpec(strings[1]), "optional".equals(strings[2])));
                        default -> LOGGER.error("Invalid dependency at\n{}", reader.getLocation());
                    }
                } else {
                    LOGGER.error("Unknown element `{}`\n{}", reader.getLocalName(), reader.getLocation());
                }
                skipElement(reader);
            } else if (type == END_ELEMENT) {
                return;
            }
        }
    }

    private static void readActions(XMLStreamReader reader, PluginDescriptor descriptor) throws XMLStreamException {
        List<ActionDescriptor> actions = descriptor.actions;
        if (actions == null) {
            actions = new ArrayList<>();
            descriptor.actions = actions;
        }

        while (true) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                actions.add(new ActionDescriptor(readElement(reader)));
            } else if (event == END_ELEMENT) {
                return;
            }
        }
    }

    private static void readExtensionPoints(XMLStreamReader reader, PluginDescriptor descriptor) throws XMLStreamException {
        List<ExtensionPointDescriptor> extensionPoints = descriptor.extensionPoints;
        if (extensionPoints == null) {
            extensionPoints = new ArrayList<>();
            descriptor.extensionPoints = extensionPoints;
        }

        while (true) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                if ("extensionPoint".equals(reader.getLocalName())) {
                    String name = null;
                    String beanClass = null;
                    String interfaceClass = null;
                    for (int i = 0, count = reader.getAttributeCount(); i < count; i++) {
                        switch (reader.getAttributeLocalName(i)) {
                            case "name" -> name = reader.getAttributeValue(i);
                            case "beanClass" -> beanClass = reader.getAttributeValue(i);
                            case "interface" -> interfaceClass = reader.getAttributeValue(i);
                        }
                    }
                    if (name == null) {
                        throw new RuntimeException("`name` attribute not specified for extension point at \n" + reader.getLocation());
                    }
                    if (beanClass == null && interfaceClass == null) {
                        throw new RuntimeException("Neither `beanClass` nor `interface` attribute is specified for extension point at \n" + reader.getLocation());
                    }
                    if (beanClass != null && interfaceClass != null) {
                        throw new RuntimeException("Both `beanClass` and `interface` attribute are specified for extension point at \n" + reader.getLocation());
                    }
                    boolean bean = beanClass != null;
                    extensionPoints.add(new ExtensionPointDescriptor(name, bean ? beanClass : interfaceClass, bean));
                } else {
                    LOGGER.error("Unknown element `{}`\n{}", reader.getLocalName(), reader.getLocation());
                }
                skipElement(reader);
            } else if (event == END_ELEMENT) {
                return;
            }
        }
    }

    private static void readExtensions(XMLStreamReader reader, PluginDescriptor descriptor) throws XMLStreamException {
        Map<String, List<ExtensionDescriptor>> extensions = descriptor.extensions;
        if (extensions == null) {
            extensions = new HashMap<>();
            descriptor.extensions = extensions;
        }

        String namespace = findAttributeValue(reader, "namespace");

        while (true) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                String fullName = createFullName(namespace, reader.getLocalName());

                switch (fullName) {
                    case "peach.applicationService" -> descriptor.addApplicationService(readService(reader));
                    case "peach.projectService" -> descriptor.addProjectService(readService(reader));
                    case "peach.applicationListener" -> descriptor.addApplicationListener(readListener(reader));
                    case "peach.projectListener" -> descriptor.addProjectListeners(readListener(reader));
                    default -> {
                        String implementation = null;
                        String id = null;
                        ExtensionOrder order = null;
                        for (int i = 0, count = reader.getAttributeCount(); i < count; i++) {
                            switch (reader.getAttributeLocalName(i)) {
                                case "implementation" -> implementation = reader.getAttributeValue(i);
                                case "id" -> id = reader.getAttributeValue(i);
                                case "order" -> order = ExtensionOrder.of(reader.getAttributeValue(i));
                            }
                        }
                        extensions.computeIfAbsent(fullName, k -> new ArrayList<>()).add(new ExtensionDescriptor(implementation, id, order != null ? order : ExtensionOrder.DEFAULT, readElement(reader)));
                    }
                }
                skipElement(reader);
            } else if (event == END_ELEMENT) {
                return;
            }
        }
    }

    private static ServiceDescriptor readService(XMLStreamReader reader) {
        String interfaceClassName = null;
        String implementationClassName = null;
        boolean override = false;
        ServiceDescriptor.PreloadMode preloadMode = ServiceDescriptor.PreloadMode.FALSE;
        for (int i = 0, count = reader.getAttributeCount(); i < count; i++) {
            switch (reader.getAttributeLocalName(i)) {
                case "interface" -> interfaceClassName = reader.getAttributeValue(i);
                case "implementation" -> implementationClassName = reader.getAttributeValue(i);
                case "override" -> override = Boolean.parseBoolean(reader.getAttributeValue(i));
                case "preload" -> {
                    switch (reader.getAttributeValue(i)) {
                        case "true" -> preloadMode = ServiceDescriptor.PreloadMode.TRUE;
                        case "await" -> preloadMode = ServiceDescriptor.PreloadMode.AWAIT;
                        case "false" -> preloadMode = ServiceDescriptor.PreloadMode.FALSE;
                        default ->
                                LOGGER.error("Unknown preload `{}`\n{}", reader.getAttributeValue(i), reader.getLocation());
                    }
                }
            }
        }
        return new ServiceDescriptor(interfaceClassName, implementationClassName, override, preloadMode);
    }

    private static ListenerDescriptor readListener(XMLStreamReader reader) {
        String topic = null;
        String listenerClassName = null;
        for (int i = 0, count = reader.getAttributeCount(); i < count; i++) {
            switch (reader.getAttributeLocalName(i)) {
                case "topic" -> topic = reader.getAttributeValue(i);
                case "listener" -> listenerClassName = reader.getAttributeValue(i);
            }
        }
        return new ListenerDescriptor(topic, listenerClassName);
    }

    private static String findAttributeValue(XMLStreamReader reader, String attributeName) {
        for (int i = 0, count = reader.getAttributeCount(); i < count; i++) {
            if (attributeName.equals(reader.getAttributeLocalName(i))) {
                return reader.getAttributeValue(i);
            }
        }
        return null;
    }

    private static void skipElement(XMLStreamReader reader) throws XMLStreamException {
        if (reader.getEventType() == END_ELEMENT) {
            return;
        }
        int depth = 0;
        while (true) {
            switch (reader.next()) {
                case START_ELEMENT -> depth++;
                case END_ELEMENT -> {
                    if (depth == 0) return;
                    depth--;
                }
            }
        }
    }

    private static final DocumentFactory FACTORY = DocumentFactory.getInstance();

    private static Node readNode(XMLStreamReader reader) throws XMLStreamException {
        return switch (reader.getEventType()) {
            case START_ELEMENT -> readElement(reader);
            case CHARACTERS, SPACE -> readCharacters(reader);
            case ENTITY_REFERENCE -> readEntityReference(reader);
            case CDATA -> readCDATA(reader);
            default -> throw new XMLStreamException("Unexpected event: " + reader.getEventType(), reader.getLocation());
        };
    }

    private static Element readElement(XMLStreamReader reader) throws XMLStreamException {
        javax.xml.namespace.QName startName = reader.getName();
        Element element = FACTORY.createElement(createQName(startName));

        for (int i = 0, count = reader.getAttributeCount(); i < count; i++) {
            element.addAttribute(createQName(reader.getAttributeName(i)), reader.getAttributeValue(i));
        }

        while (true) {
            if (!reader.hasNext()) {
                throw new XMLStreamException("Unexpected end of stream while reading element content", reader.getLocation());
            }

            int event = reader.next();
            if (event == END_ELEMENT) {
                javax.xml.namespace.QName endName = reader.getName();

                if (!startName.equals(endName)) {
                    throw new XMLStreamException("Expected " + startName + " end-tag, but found " + endName, reader.getLocation());
                }

                break;
            }

            Node node = readNode(reader);
            element.add(node);
        }

        return element;
    }

    private static Text readCharacters(XMLStreamReader reader) {
        return FACTORY.createText(reader.getText());
    }

    private static Entity readEntityReference(XMLStreamReader reader) {
        return FACTORY.createEntity(reader.getLocalName(), reader.getText());
    }

    private static CDATA readCDATA(XMLStreamReader reader) {
        return FACTORY.createCDATA(reader.getText());
    }

    private static QName createQName(javax.xml.namespace.QName qName) {
        return FACTORY.createQName(qName.getLocalPart(), qName.getPrefix(), qName.getNamespaceURI());
    }

    private static String createFullName(String namespace, String name) {
        return namespace == null || namespace.isEmpty() ? name : namespace + "." + name;
    }

    private DescriptorReader() {
    }
}
