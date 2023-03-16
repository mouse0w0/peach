package com.github.mouse0w0.peach.extension;

@FunctionalInterface
public interface Converter<T> {
    T convert(String value);
}
