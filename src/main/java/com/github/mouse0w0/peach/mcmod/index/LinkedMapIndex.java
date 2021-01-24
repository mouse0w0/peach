package com.github.mouse0w0.peach.mcmod.index;

import org.apache.commons.collections4.map.CompositeMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LinkedMapIndex<K, V> extends Index<Map<K, V>> {

    public LinkedMapIndex(String name) {
        super(name);
    }

    @Override
    public Map<K, V> create() {
        return new LinkedHashMap<>();
    }

    @Override
    public Map<K, V> compose(Stream<Map<K, V>> stream) {
        CompositeMap<K, V> map = new CompositeMap<>();
        stream.forEach(map::addComposited);
        return map;
    }

}
