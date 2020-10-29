package com.github.mouse0w0.peach.component;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPoint;

public final class ServiceDescriptor {
    public static final ExtensionPoint<ServiceDescriptor> APPLICATION_SERVICE = ExtensionPoint.of("applicationService");
    public static final ExtensionPoint<ServiceDescriptor> PROJECT_SERVICE = ExtensionPoint.of("projectService");

    @Attribute("interface")
    public String interfaceName;
    @Attribute("implementation")
    public String implementationName;
    @Attribute("lazy")
    public boolean lazy;
}
