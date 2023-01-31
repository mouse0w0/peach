package com.github.mouse0w0.peach.util.graph;

public interface MutableGraph<N> extends Graph<N> {
    void addNode(N node);

    void addEdge(N nodeU, N nodeV);

    boolean removeNode(N node);

    boolean removeEdge(N nodeU, N nodeV);
}
