package com.github.mouse0w0.peach.util.graph;

import java.util.Set;

public interface Graph<N> {
    Set<N> getNodes();

    Set<N> getPredecessors(N node);

    Set<N> getSuccessors(N node);
}
