package com.github.mouse0w0.peach.extension;

class ExtensionObjectDescriptor implements ExtensionDescriptor {
    private final Object value;

    public ExtensionObjectDescriptor(Object value) {
        this.value = value;
    }

    @Override
    public Object newInstance() {
        return value;
    }

    @Override
    public int compareTo(ExtensionDescriptor o) {
        if (o instanceof ExtensionObjectDescriptor) {
            ExtensionObjectDescriptor otherDesc = (ExtensionObjectDescriptor) o;
            if (value instanceof Comparable && otherDesc.value instanceof Comparable) {
                Comparable<Object> thisValue = (Comparable<Object>) value;
                Comparable<Object> otherValue = (Comparable<Object>) otherDesc.value;
                return thisValue.compareTo(otherValue);
            }
        }
        return 0;
    }
}
