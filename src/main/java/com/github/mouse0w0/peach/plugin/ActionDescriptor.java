package com.github.mouse0w0.peach.plugin;

import org.dom4j.Element;

public final class ActionDescriptor {
    private Element element;

    public ActionDescriptor(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public void freeElement() {
        this.element = null;
    }
}
