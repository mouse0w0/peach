package com.github.mouse0w0.peach.mcmod.index;

import org.apache.commons.collections4.set.CompositeSet;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class HashSetIndex<E> extends Index<Set<E>> {
    public HashSetIndex(String name) {
        super(name);
    }

    @Override
    public Set<E> create() {
        return new HashSet<>();
    }

    @Override
    public Set<E> compose(Stream<Set<E>> stream) {
        CompositeSet<E> set = new CompositeSet<>();
        stream.forEach(set::addComposited);
        return set;
    }
}
