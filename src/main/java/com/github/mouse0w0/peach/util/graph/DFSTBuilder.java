package com.github.mouse0w0.peach.util.graph;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * DFS topological sorting implementation.
 */
public final class DFSTBuilder<N> {
    private final Comparator<N> comparator;
    private final boolean acyclic;
    private final List<List<N>> components;

    @SuppressWarnings("unchecked")
    public DFSTBuilder(Graph<N> graph) {
        N[] nodes = (N[]) graph.getNodes().toArray();
        int nodeCount = nodes.length;
        Object2IntMap<N> nodeIndex = new Object2IntOpenHashMap<>(nodeCount * 2, 0.5f);
        for (int i = 0; i < nodeCount; i++) {
            nodeIndex.put(nodes[i], i);
        }

        ObjectArrayList<Frame<N>> frameStack = new ObjectArrayList<>();
        IntArrayList nodeStack = new IntArrayList();
        boolean[] stacked = new boolean[nodeCount];
        int time = 1; // 0 means not visited.
        int[] dfn = new int[nodeCount];
        int[] low = new int[nodeCount];
        int topoIndex = 0;
        int[] topo = new int[nodeCount];
        boolean acyclic = true;
        ImmutableList.Builder<List<N>> sccsBuilder = ImmutableList.builder();
        for (int i = 0; i < nodeCount; i++) {
            if (dfn[i] != 0) continue;

            // Topological sorting in DFS is reversed, get predecessors.
            frameStack.push(new Frame<>(i, graph.getPredecessors(nodes[i]).iterator()));
            nodeStack.push(i);
            stacked[i] = true;
            dfn[i] = low[i] = time++;

            nextFrame:
            while (!frameStack.isEmpty()) {
                Frame<N> frame = frameStack.top();
                int currIndex = frame.nodeIndex;
                if (frame.succIndex != -1) {
                    low[currIndex] = Math.min(low[currIndex], low[frame.succIndex]);
                }

                Iterator<N> succIterator = frame.succIterator;
                while (succIterator.hasNext()) {
                    N succNode = succIterator.next();
                    int succIndex = nodeIndex.applyAsInt(succNode);
                    if (dfn[succIndex] == 0) { // If node is not visited.
                        frame.succIndex = succIndex;
                        // Topological sorting in DFS is reversed, get predecessors.
                        frameStack.push(new Frame<>(succIndex, graph.getPredecessors(succNode).iterator()));
                        nodeStack.push(succIndex);
                        stacked[succIndex] = true;
                        dfn[succIndex] = low[succIndex] = time++;
                        continue nextFrame;
                    } else if (stacked[succIndex]) { // If node in stack.
                        low[currIndex] = Math.min(low[currIndex], low[succIndex]);
                        acyclic = false;
                    }
                }
                // No more successors.
                frameStack.pop();
                topo[currIndex] = topoIndex++;
                if (dfn[currIndex] == low[currIndex]) {
                    ImmutableList.Builder<N> sccBuilder = ImmutableList.builder();
                    int popIndex;
                    do {
                        popIndex = nodeStack.popInt();
                        stacked[popIndex] = false;
                        sccBuilder.add(nodes[popIndex]);
                    } while (popIndex != currIndex);
                    sccsBuilder.add(sccBuilder.build());
                }
            }
        }

        Object2IntMap<N> topoMap = new Object2IntOpenHashMap<>(nodeCount * 2, 0.5f);
        for (int i = 0; i < nodeCount; i++) {
            topoMap.put(nodes[i], topo[i]);
        }
        this.comparator = Comparator.comparingInt(topoMap);
        this.acyclic = acyclic;
        this.components = sccsBuilder.build();
    }

    public Comparator<N> comparator() {
        return comparator;
    }

    public boolean isAcyclic() {
        return acyclic;
    }

    /**
     * @return the list of strong connected component.
     */
    public List<List<N>> getComponents() {
        return components;
    }

    private static final class Frame<N> {
        private int nodeIndex;
        private Iterator<N> succIterator;
        private int succIndex = -1;

        public Frame(int nodeIndex, Iterator<N> succIterator) {
            this.nodeIndex = nodeIndex;
            this.succIterator = succIterator;
        }
    }
}
