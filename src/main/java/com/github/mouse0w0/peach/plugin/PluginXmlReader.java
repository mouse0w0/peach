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

final class PluginXmlReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginXmlReader.class);

    public static PluginXml read(String systemId, InputStream inputStream) throws IOException, XMLStreamException {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            return read(XMLInputFactory.newDefaultFactory().createXMLStreamReader(systemId, bis));
        }
    }

    public static PluginXml read(XMLStreamReader reader) throws XMLStreamException {
        if (reader.getEventType() != START_DOCUMENT) {
            throw new XMLStreamException("Expected START_DOCUMENT event, found: " + reader.getEventType(), reader.getLocation());
        }
        PluginXml xml = new PluginXml();
        while (true) {
            if (reader.next() == START_ELEMENT) break;
        }
        readPlugin(reader, xml);
        if (reader.next() != END_DOCUMENT) {
            throw new XMLStreamException("Expected END_DOCUMENT event, found: " + reader.getEventType(), reader.getLocation());
        }
        return xml;
    }

    private static void readPlugin(XMLStreamReader reader, PluginXml xml) throws XMLStreamException {
        while (true) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "id" -> xml.id = reader.getElementText();
                    case "name" -> xml.name = reader.getElementText();
                    case "version" -> xml.version = reader.getElementText();
                    case "logo" -> xml.logo = reader.getElementText();
                    case "license" -> xml.license = reader.getElementText();
                    case "description" -> xml.description = reader.getElementText();
                    case "category" -> xml.category = reader.getElementText();
                    case "author" -> xml.author = reader.getElementText();
                    case "authorUrl" -> xml.authorUrl = reader.getElementText();
                    case "authorEmail" -> xml.authorEmail = reader.getElementText();
                    case "homepage" -> xml.homepage = reader.getElementText();
                    case "repository" -> xml.repository = reader.getElementText();
                    case "issueTrackingUrl" -> xml.issueTrackingUrl = reader.getElementText();
                    case "issueTrackingEmail" -> xml.issueTrackingEmail = reader.getElementText();
                    case "updateUrl" -> xml.updateUrl = reader.getElementText();
                    case "dependencies" -> readDependencies(reader, xml);
                    case "actions" -> readActions(reader, xml);
                    case "extensionPoints" -> readExtensionPoints(reader, xml);
                    case "extensions" -> readExtensions(reader, xml);
                    default -> LOGGER.error("Unknown element `{}`\n{}", reader.getLocalName(), reader.getLocation());
                }
                skipElement(reader);
            } else if (event == END_ELEMENT) {
                return;
            }
        }
    }

    private static void readDependencies(XMLStreamReader reader, PluginXml xml) throws XMLStreamException {
        List<PluginDependency> dependencies = xml.dependencies;
        if (dependencies == null) {
            dependencies = new ArrayList<>();
            xml.dependencies = dependencies;
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

    private static void readActions(XMLStreamReader reader, PluginXml xml) throws XMLStreamException {
        List<ActionDescriptor> actions = xml.actions;
        if (actions == null) {
            actions = new ArrayList<>();
            xml.actions = actions;
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

    private static void readExtensionPoints(XMLStreamReader reader, PluginXml xml) throws XMLStreamException {
        List<ExtensionPointDescriptor> extensionPoints = xml.extensionPoints;
        if (extensionPoints == null) {
            extensionPoints = new ArrayList<>();
            xml.extensionPoints = extensionPoints;
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

    private static void readExtensions(XMLStreamReader reader, PluginXml xml) throws XMLStreamException {
        Map<String, List<ExtensionDescriptor>> extensions = xml.extensions;
        if (extensions == null) {
            extensions = new HashMap<>();
            xml.extensions = extensions;
        }

        String namespace = findAttributeValue(reader, "namespace");

        while (true) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                String fullName = createFullName(namespace, reader.getLocalName());

                switch (fullName) {
                    case "peach.applicationService" -> xml.addApplicationService(readService(reader));
                    case "peach.projectService" -> xml.addProjectService(readService(reader));
                    case "peach.applicationListener" -> xml.addApplicationListener(readListener(reader));
                    case "peach.projectListener" -> xml.addProjectListeners(readListener(reader));
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

    private PluginXmlReader() {
    }
}
