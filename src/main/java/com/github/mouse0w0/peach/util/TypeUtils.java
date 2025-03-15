package com.github.mouse0w0.peach.util;

import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApiStatus.Internal
public class TypeUtils extends org.apache.commons.lang3.reflect.TypeUtils {
    public static ParameterizedType list(Type typeArgument) {
        return parameterize(List.class, typeArgument);
    }

    public static ParameterizedType set(Type typeArgument) {
        return parameterize(Set.class, typeArgument);
    }

    public static ParameterizedType map(Type typeArgument0, Type typeArgument1) {
        return parameterize(Map.class, typeArgument0, typeArgument1);
    }
}
