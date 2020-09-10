package com.github.mouse0w0.peach.plugin;

import org.dom4j.Element;

public final class ServiceDescriptor {

    public String interfaceName;
    public String implementationName;

    public boolean lazy;

    public static ServiceDescriptor readFromXml(Element element) {
        ServiceDescriptor service = new ServiceDescriptor();
        service.implementationName = element.attributeValue("implementation");
        service.interfaceName = element.attributeValue("interface");
        service.lazy = "true".equals(element.attributeValue("lazy"));
        return service;
    }
}
