package com.github.mouse0w0.peach.extension;

import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.peach.util.graph.DFSTBuilder;
import com.github.mouse0w0.peach.util.graph.DirectedGraph;
import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ExtensionOrder {
    public static final char SEPARATOR = ',';
    public static final String FIRST_STR = "first";
    public static final String LAST_STR = "last";
    public static final String BEFORE_STR = "before ";
    public static final String AFTER_STR = "after ";

    public static final ExtensionOrder DEFAULT = new ExtensionOrder();
    public static final ExtensionOrder FIRST = new ExtensionOrder(FIRST_STR);
    public static final ExtensionOrder LAST = new ExtensionOrder(LAST_STR);

    private final String str;
    private final boolean first;
    private final boolean last;
    private final Set<String> before;
    private final Set<String> after;

    public static ExtensionOrder of(String str) {
        if (str == null || str.isEmpty()) {
            return DEFAULT;
        } else if (FIRST_STR.equalsIgnoreCase(str)) {
            return FIRST;
        } else if (LAST_STR.equalsIgnoreCase(str)) {
            return LAST;
        } else {
            return new ExtensionOrder(str);
        }
    }

    private ExtensionOrder() {
        this.str = "default";
        this.first = false;
        this.last = false;
        this.before = Collections.emptySet();
        this.after = Collections.emptySet();
    }

    private ExtensionOrder(String str) {
        this.str = str;
        boolean first = false;
        boolean last = false;
        ImmutableSet.Builder<String> before = null;
        ImmutableSet.Builder<String> after = null;
        for (String s : StringUtils.split(str, SEPARATOR)) {
            String trim = s.trim();
            if (FIRST_STR.equalsIgnoreCase(trim)) {
                first = true;
            } else if (LAST_STR.equalsIgnoreCase(trim)) {
                last = true;
            } else if (StringUtils.startsWithIgnoreCase(trim, BEFORE_STR)) {
                if (before == null) {
                    before = ImmutableSet.builder();
                }
                before.add(trim.substring(BEFORE_STR.length()));
            } else if (StringUtils.startsWithIgnoreCase(trim, AFTER_STR)) {
                if (after == null) {
                    after = ImmutableSet.builder();
                }
                after.add(trim.substring(AFTER_STR.length()));
            }
        }
        this.first = first;
        this.last = last;
        this.before = before != null ? before.build() : Collections.emptySet();
        this.after = after != null ? after.build() : Collections.emptySet();
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public Set<String> getBefore() {
        return before;
    }

    public Set<String> getAfter() {
        return after;
    }

    @Override
    public String toString() {
        return str;
    }

    public static <T> void sort(List<ExtensionWrapper<T>> wrappers, Map<String, ExtensionWrapper<T>> idMap) {
        String firstFence = "first-fence";
        String lastFence = "last-fence";
        DirectedGraph<Object> graph = new DirectedGraph<>();
        for (ExtensionWrapper<?> wrapper : wrappers) {
            ExtensionOrder order = wrapper.getOrder();
            if (order.isFirst()) {
                graph.addEdge(wrapper, firstFence);
            } else if (order.isLast()) {
                graph.addEdge(lastFence, wrapper);
            } else {
                graph.addEdge(firstFence, wrapper);
                graph.addEdge(wrapper, lastFence);
            }
            for (String beforeId : order.getBefore()) {
                ExtensionWrapper<?> before = idMap.get(beforeId);
                if (before != null) {
                    graph.addEdge(wrapper, before);
                }
            }
            for (String afterId : order.getAfter()) {
                ExtensionWrapper<?> after = idMap.get(afterId);
                if (after != null) {
                    graph.addEdge(after, wrapper);
                }
            }
        }

        DFSTBuilder<Object> builder = new DFSTBuilder<>(graph);
        if (builder.isAcyclic()) {
            wrappers.sort(builder.comparator());
        } else {
            StringBuilder stringBuilder = new StringBuilder("Detected cycle: \n");
            for (List<Object> scc : builder.getComponents()) {
                if (scc.size() > 1) {
                    stringBuilder.append(scc).append('\n');
                }
            }
            throw new RuntimeException(stringBuilder.toString());
        }
    }
}
