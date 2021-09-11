package com.github.mouse0w0.peach.mcmod;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.regex.Pattern;

@JsonAdapter(ResourceLocation.TypeAdapter.class)
public final class ResourceLocation implements Comparable<ResourceLocation> {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("[a-z][a-z0-9._-]*");
    private static final Pattern PATH_PATTERN = Pattern.compile("[a-z][a-z0-9/._-]*");

    public static final Pattern PATTERN = Pattern.compile(NAMESPACE_PATTERN + ":" + PATH_PATTERN);

    private final String namespace;
    private final String path;

    public ResourceLocation(String location) {
        int separatorIndex = location.indexOf(':');
        if (separatorIndex == -1) {
            throw new IllegalArgumentException("namespace not specified, location: '" + location + "'");
        }
        namespace = location.substring(0, separatorIndex);
        path = location.substring(separatorIndex + 1);
    }

    public ResourceLocation(String namespace, String path) {
        checkNamespace(namespace);
        checkPath(path);
        this.namespace = namespace;
        this.path = path;
    }

    private void checkNamespace(String namespace) {
        if (namespace == null) throw new NullPointerException("namespace");
        if (namespace.isEmpty()) throw new IllegalArgumentException("namespace is empty");
        if (!NAMESPACE_PATTERN.matcher(namespace).matches()) {
            throw new IllegalArgumentException("namespace is invalid, " +
                    "namespace: '" + namespace + "', pattern: '" + NAMESPACE_PATTERN + "'");
        }
    }

    private void checkPath(String path) {
        if (path == null) throw new NullPointerException("path");
        if (path.isEmpty()) throw new IllegalArgumentException("path is empty");
        if (!PATH_PATTERN.matcher(path).matches()) {
            throw new IllegalArgumentException("path is invalid, " +
                    "path: '" + path + "', pattern: '" + PATH_PATTERN + "'");
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceLocation that = (ResourceLocation) o;

        if (!namespace.equals(that.namespace)) return false;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public int compareTo(ResourceLocation o) {
        int result = namespace.compareTo(o.namespace);
        return result == 0 ? path.compareTo(o.path) : result;
    }

    public static final class TypeAdapter extends com.google.gson.TypeAdapter<ResourceLocation> {
        @Override
        public void write(JsonWriter out, ResourceLocation value) throws IOException {
            if (value == null) out.nullValue();
            else out.value(value.toString());
        }

        @Override
        public ResourceLocation read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return new ResourceLocation(in.nextString());
            }
        }
    }
}
