package com.github.mouse0w0.peach.util.graph;

import com.google.common.collect.AbstractIterator;

import javax.annotation.CheckForNull;
import java.util.*;

public class DirectedGraph<N> implements MutableGraph<N> {
    private final Map<N, Connections<N>> nodes;
    private final boolean useIdentityStrategy;

    public DirectedGraph() {
        this(false);
    }

    public DirectedGraph(boolean useIdentityStrategy) {
        this.useIdentityStrategy = useIdentityStrategy;
        if (useIdentityStrategy) {
            nodes = new IdentityHashMap<>();
        } else {
            nodes = new HashMap<>();
        }
    }

    @Override
    public boolean isUseIdentityStrategy() {
        return useIdentityStrategy;
    }

    @Override
    public Set<N> getNodes() {
        return nodes.keySet();
    }

    @Override
    public Set<N> getPredecessors(N node) {
        return checkConnections(node).getPredecessors();
    }

    @Override
    public Set<N> getSuccessors(N node) {
        return checkConnections(node).getSuccessors();
    }

    private Connections<N> checkConnections(N node) {
        Connections<N> connections = nodes.get(node);
        if (connections == null) {
            if (node == null) {
                throw new NullPointerException("node");
            }
            throw new IllegalArgumentException("Node is not an element of this graph.");
        }
        return connections;
    }

    @Override
    public void addNode(N node) {
        if (nodes.putIfAbsent(node, new Connections<>(useIdentityStrategy)) != null) {
            throw new IllegalStateException("Node is already an element of this graph.");
        }
    }

    @Override
    public void addEdge(N nodeU, N nodeV) {
        Connections<N> connectionsU = nodes.get(nodeU);
        if (connectionsU == null) {
            nodes.put(nodeU, connectionsU = new Connections<>(useIdentityStrategy));
        }
        connectionsU.addSuccessor(nodeV);

        Connections<N> connectionsV = nodes.get(nodeV);
        if (connectionsV == null) {
            nodes.put(nodeV, connectionsV = new Connections<>(useIdentityStrategy));
        }
        connectionsV.addPredecessor(nodeU);
    }

    @Override
    public boolean removeNode(N node) {
        Connections<N> connections = nodes.remove(node);
        if (connections == null) {
            return false;
        }

        for (N successor : connections.getSuccessors()) {
            nodes.get(successor).removePredecessor(node);
        }

        for (N predecessor : connections.getPredecessors()) {
            nodes.get(predecessor).removeSuccessor(node);
        }
        return true;
    }

    @Override
    public boolean removeEdge(N nodeU, N nodeV) {
        Connections<N> connectionsU = nodes.get(nodeU);
        Connections<N> connectionsV = nodes.get(nodeV);
        if (connectionsU == null || connectionsV == null) {
            if (nodeU == null) {
                throw new NullPointerException("nodeU");
            }
            if (nodeV == null) {
                throw new NullPointerException("nodeV");
            }
            return false;
        }

        if (connectionsU.removeSuccessor(nodeV)) {
            connectionsV.removePredecessor(nodeU);
            return true;
        }
        return false;
    }

    private static final class Connections<N> {
        private static final Object PRED = new Object();
        private static final Object SUCC = new Object();
        private static final Object PRED_AND_SUCC = new Object();

        private final Map<N, Object> connections;

        private int predecessorsCount;
        private int successorsCount;

        public Connections(boolean useIdentityStrategy) {
            if (useIdentityStrategy) {
                connections = new IdentityHashMap<>();
            } else {
                connections = new HashMap<>();
            }
        }

        public Set<N> getPredecessors() {
            return new AbstractSet<>() {
                @Override
                public Iterator<N> iterator() {
                    return new AbstractIterator<>() {
                        Iterator<Map.Entry<N, Object>> it = connections.entrySet().iterator();

                        @CheckForNull
                        @Override
                        protected N computeNext() {
                            while (it.hasNext()) {
                                Map.Entry<N, Object> next = it.next();
                                if (isPredecessor(next.getValue())) {
                                    return next.getKey();
                                }
                            }
                            return endOfData();
                        }
                    };
                }

                @Override
                public int size() {
                    return predecessorsCount;
                }

                @Override
                public boolean contains(Object o) {
                    return isPredecessor(connections.get(o));
                }
            };
        }

        public Set<N> getSuccessors() {
            return new AbstractSet<>() {
                @Override
                public Iterator<N> iterator() {
                    return new AbstractIterator<>() {
                        Iterator<Map.Entry<N, Object>> it = connections.entrySet().iterator();

                        @CheckForNull
                        @Override
                        protected N computeNext() {
                            while (it.hasNext()) {
                                Map.Entry<N, Object> next = it.next();
                                if (isSuccessor(next.getValue())) {
                                    return next.getKey();
                                }
                            }
                            return endOfData();
                        }
                    };
                }

                @Override
                public int size() {
                    return successorsCount;
                }

                @Override
                public boolean contains(Object o) {
                    return isSuccessor(connections.get(o));
                }
            };
        }

        public void addPredecessor(N node) {
            Object o = connections.get(node);
            if (o == null) {
                connections.put(node, PRED);
                predecessorsCount++;
            } else if (o == SUCC) {
                connections.put(node, PRED_AND_SUCC);
                predecessorsCount++;
            }
        }

        public void addSuccessor(N node) {
            Object o = connections.get(node);
            if (o == null) {
                connections.put(node, SUCC);
                successorsCount++;
            } else if (o == PRED) {
                connections.put(node, PRED_AND_SUCC);
                successorsCount++;
            }
        }

        public boolean removePredecessor(N node) {
            Object o = connections.get(node);
            if (o == PRED) {
                connections.remove(node);
                predecessorsCount--;
                return true;
            } else if (o == PRED_AND_SUCC) {
                connections.put(node, SUCC);
                predecessorsCount--;
                return true;
            }
            return false;
        }

        public boolean removeSuccessor(N node) {
            Object o = connections.get(node);
            if (o == SUCC) {
                connections.remove(node);
                successorsCount--;
                return true;
            } else if (o == PRED_AND_SUCC) {
                connections.put(node, PRED);
                successorsCount--;
                return true;
            }
            return false;
        }

        private static boolean isPredecessor(Object value) {
            return value != SUCC && value != null;
        }

        private static boolean isSuccessor(Object value) {
            return value != PRED && value != null;
        }
    }
}
