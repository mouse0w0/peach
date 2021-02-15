package com.github.mouse0w0.peach.mcmod;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.regex.Pattern;

@JsonAdapter(Identifier.TypeAdapter.class)
public final class Identifier implements Comparable<Identifier> {
    public static final Pattern PATTERN = Pattern.compile("[a-z][a-z0-9_]*:[a-z][a-z0-9_]*");
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-z][a-z0-9_]*");

    private final String namespace;
    private final String name;

    public Identifier(String identifier) {
        int separatorIndex = identifier.indexOf(':');
        if (separatorIndex == -1) {
            throw new InvalidIdentifierException("Namespace not specified, identifier: '" + identifier + "'");
        }
        namespace = identifier.substring(0, separatorIndex);
        name = identifier.substring(separatorIndex + 1);
    }

    public Identifier(String namespace, String name) {
        checkNamespace(namespace);
        checkName(name);
        this.namespace = namespace;
        this.name = name;
    }

    private void checkNamespace(String namespace) {
        if (namespace == null) throw new NullPointerException("namespace");
        if (namespace.isEmpty()) throw new InvalidIdentifierException("The namespace is empty");
        if (!NAME_PATTERN.matcher(namespace).matches()) {
            throw new InvalidIdentifierException("The namespace is invalid, " +
                    "namespace: '" + namespace + "', pattern: '" + NAME_PATTERN + "'");
        }
    }

    private void checkName(String name) {
        if (name == null) throw new NullPointerException("name");
        if (name.isEmpty()) throw new InvalidIdentifierException("The name is empty");
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidIdentifierException("The namespace is invalid, " +
                    "name: '" + name + "', pattern: '" + NAME_PATTERN + "'");
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
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
                return new Identifier(in.nextString());
            }
        }
    }
}
