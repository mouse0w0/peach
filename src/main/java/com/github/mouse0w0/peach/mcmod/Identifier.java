package com.github.mouse0w0.peach.mcmod;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.regex.Pattern;

@JsonAdapter(Identifier.TypeAdapter.class)
public final class Identifier implements Comparable<Identifier> {
    public static final Pattern VALID_NAMESPACE = Pattern.compile("[a-z0-9._-]+");
    public static final Pattern VALID_NAME = Pattern.compile("[a-z0-9/._-]+");

    private final String namespace;
    private final String name;

    public static Identifier of(String identifier) {
        int separatorIndex = identifier.indexOf(':');
        if (separatorIndex == -1) {
            throw new IllegalArgumentException("namespace not specified, identifier: '" + identifier + "'");
        }
        return new Identifier(identifier.substring(0, separatorIndex), identifier.substring(separatorIndex + 1));
    }

    public static Identifier of(String namespace, String name) {
        return new Identifier(namespace, name);
    }

    private Identifier(String namespace, String name) {
        if (namespace == null) throw new NullPointerException("namespace");
        if (namespace.isEmpty()) throw new IllegalArgumentException("namespace is empty");
        if (name == null) throw new NullPointerException("name");
        if (name.isEmpty()) throw new IllegalArgumentException("name is empty");
        this.namespace = namespace;
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public boolean isValid() {
        return VALID_NAMESPACE.matcher(namespace).matches() && VALID_NAME.matcher(name).matches();
    }

    @Override
    public String toString() {
        return namespace + ":" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        if (!namespace.equals(that.namespace)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(Identifier o) {
        int result = namespace.compareTo(o.namespace);
        return result == 0 ? name.compareTo(o.name) : result;
    }

    public static final class TypeAdapter extends com.google.gson.TypeAdapter<Identifier> {
        @Override
        public void write(JsonWriter out, Identifier value) throws IOException {
            if (value == null) out.nullValue();
            else out.value(value.toString());
        }

        @Override
        public Identifier read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return Identifier.of(in.nextString());
            }
        }
    }
}
