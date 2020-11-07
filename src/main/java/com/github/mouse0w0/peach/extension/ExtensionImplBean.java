package com.github.mouse0w0.peach.extension;

class ExtensionImplBean {

    @Attribute("implementation")
    public String implementation;
    @Attribute("order")
    public Order order = Order.DEFAULT;

    enum Order {
        FIRST,
        EARLY,
        DEFAULT,
        LATE,
        LAST
    }
}
