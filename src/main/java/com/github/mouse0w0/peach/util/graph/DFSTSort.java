package com.github.mouse0w0.peach.util.graph;

import it.unimi.dsi.fastutil.objects.*;

import java.util.*;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

/**
 * DFS topological sorting implementation.
 */
public class DFSTSort<N> {
    private ToIntFunction<N> topoGetter;
    private List<N> cycleNodes;

    public DFSTSort(Graph<N> graph) {
        build(graph);
    }

    private void build(Graph<N> graph) {
        Set<N> nodes = graph.getNodes();
        int nodeCount = nodes.size();

        ObjIntConsumer<N> topoSetter;
        // If Boolean.TRUE, node in stack.
        Map<N, Boolean> visited;
        if (graph.isUseIdentityStrategy()) {
            Reference2IntMap<N> topo = new Reference2IntOpenHashMap<>(nodeCount * 2, 0.5f);
            topoGetter = topo::getInt;
            topoSetter = topo::put;
            visited = new Reference2ObjectOpenHashMap<>(nodeCount * 2, 0.5f);
        } else {
            Object2IntMap<N> topo = new Object2IntOpenHashMap<>(nodeCount * 2, 0.5f);
            topoGetter = topo::getInt;
            topoSetter = topo::put;
            visited = new Object2ObjectOpenHashMap<>(nodeCount * 2, 0.5f);
        }
        // Topological sorting in DFS is reserved.
        int topoOrder = nodeCount;
        ObjectArrayList<N> nodeStack = new ObjectArrayList<>();
        ObjectArrayList<Iterator<N>> successorsStack = new ObjectArrayList<>();
        for (N node : nodes) {
            if (visited.containsKey(node)) continue;

            nodeStack.push(node);
            successorsStack.push(graph.getSuccessors(node).iterator());
            visited.put(node, Boolean.TRUE);

            nextFrame:
            while (!nodeStack.isEmpty()) {
                Iterator<N> successors = successorsStack.top();
                while (successors.hasNext()) {
                    N successor = successors.next();
                    Boolean b = visited.get(successor);
                    if (b == null) { // If node is not visited.
                        nodeStack.push(successor);
                        successorsStack.push(graph.getSuccessors(successor).iterator());
                        visited.put(successor, Boolean.TRUE);
                        continue nextFrame;
                    } else if (b == Boolean.TRUE) { // If node in stack.
                        if (cycleNodes == null) {
                            int size = nodeStack.size();
                            int idx = nodeStack.indexOf(successor);
                            List<N> result = new ArrayList<>(size - idx);
                            for (int i = idx; i < size; i++) {
                                result.add(nodeStack.get(i));
                            }
                            cycleNodes = result;
                        }
                    }
                }
                // No more successors, pop node.
                N currNode = nodeStack.pop();
                topoSetter.accept(currNode, --topoOrder);
                visited.put(currNode, Boolean.FALSE);
                successorsStack.pop();
            }
        }
    }

    public Comparator<N> comparator() {
        return Comparator.comparingInt(topoGetter);
    }

    public boolean isAcyclic() {
        return cycleNodes == null;
    }

    public List<N> getCycleNodes() {
        return cycleNodes;
    }
}
