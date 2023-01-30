package com.github.mouse0w0.peach.util.graph;

import org.junit.jupiter.api.Test;

class DFSTopoSortTest {

    @Test
    void sort() {
        DirectedGraph<Integer> graph = new DirectedGraph<>();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 5);
        graph.addEdge(4, 6);
        graph.addEdge(5, 6);
        DFSTopoSort<Integer> sort = new DFSTopoSort<>(graph);
    }
}