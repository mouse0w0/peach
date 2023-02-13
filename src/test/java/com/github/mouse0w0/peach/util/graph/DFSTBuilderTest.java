package com.github.mouse0w0.peach.util.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DFSTBuilderTest {
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
        DFSTBuilder<Integer> builder = new DFSTBuilder<>(graph);
        List<Integer> ints = new ArrayList<>(graph.getNodes());
        ints.sort(builder.comparator());
        System.out.println(ints);
        System.out.println(builder.getComponents());
    }

    @Test
    void sccs() {
        DirectedGraph<Integer> graph = new DirectedGraph<>();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 1);
        graph.addEdge(4, 6);
        graph.addEdge(5, 6);
        DFSTBuilder<Integer> builder = new DFSTBuilder<>(graph);
        List<Integer> ints = new ArrayList<>(graph.getNodes());
        ints.sort(builder.comparator());
        System.out.println(ints);
        System.out.println(builder.getComponents());
    }
}