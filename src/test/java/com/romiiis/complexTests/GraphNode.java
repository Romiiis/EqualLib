package com.romiiis.complexTests;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {
    String value;
    List<GraphNode> neighbors;

    public GraphNode(String value) {
        this.value = value;
        this.neighbors = new ArrayList<>();
    }

    /**
     * Adds a neighbor to the node.
     *
     * @param neighbor the neighbor to add
     */
    public void addNeighbor(GraphNode neighbor) {
        neighbors.add(neighbor);
    }
}
