package com.github.mouse0w0.peach.extension;

import java.lang.reflect.Constructor;

class ExtensionClassDescriptor implements ExtensionDescriptor {

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

    @Override
    @SuppressWarnings("unchecked")
    public Object newInstance() {
        try {
            Class<?> clazz = Class.forName(implementation);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ExtensionException("Failed to new instance of " + implementation, e);
        }
    }

    @Override
    public int compareTo(ExtensionDescriptor o) {
        return o instanceof ExtensionClassDescriptor ? order.compareTo(((ExtensionClassDescriptor) o).order) : 0;
    }
}
