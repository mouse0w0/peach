package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.peach.extension.Attribute;

public final class ServiceDescriptor {
    @Attribute("interface")
    public String interfaceName;
    @Attribute("implementation")
    public String implementationName;
    @Attribute("lazy")
    public boolean lazy;
}
