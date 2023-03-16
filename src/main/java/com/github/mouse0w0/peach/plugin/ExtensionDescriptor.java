package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.peach.extension.ExtensionOrder;
import org.dom4j.Element;

public final class ExtensionDescriptor {
    private final String implementation;
    private final String id;
    private final ExtensionOrder order;
    private Element element;

    public ExtensionDescriptor(String implementation, String id, ExtensionOrder order, Element element) {
        this.implementation = implementation;
        this.id = id;
        this.order = order;
        this.element = element;
    }

    public String getImplementation() {
        return implementation;
    }

    public String getId() {
        return id;
    }

    public ExtensionOrder getOrder() {
        return order;
    }

    public Element getElement() {
        return element;
    }

    public void freeElement() {
        this.element = null;
    }
}
