package com.romiiis.complexTests;

import java.util.*;


/**
 * A class that generates random cyclic graphs for testing purposes.
 * The class provides methods for generating a random cyclic graph and copying a graph with cyclic references.
 * The class is used in the ComplexTestsData class.
 */
public class TestGraphGenerator {

    /**
     * Generates a random graph with a specified number of nodes and random cyclic references.
     * Each node can have 1 to 3 neighbors.
     * The graph is represented by the root node.
     * The graph is not guaranteed to be connected.
     *
     * @param nodeCount the number of nodes in the graph
     * @return the root node of the generated graph
     */
    public static GraphNode generateCyclicGraph(int nodeCount) {

        // Node creation
        List<GraphNode> nodes = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new GraphNode("Node" + i));
        }

        // Random neighbor assignment
        Random rand = new Random();
        for (int i = 0; i < nodeCount; i++) {

            // Random number of neighbors (1-3)
            int numNeighbors = rand.nextInt(3) + 1;
            for (int j = 0; j < numNeighbors; j++) {
                int neighborIndex = rand.nextInt(nodeCount);
                GraphNode neighbor = nodes.get(neighborIndex);

                // Avoid self-loops
                if (neighbor != nodes.get(i)) {
                    nodes.get(i).addNeighbor(neighbor);
                }
            }
        }

        // Random cyclic reference
        int cycleStartIndex = rand.nextInt(nodeCount);
        int cycleEndIndex = rand.nextInt(nodeCount);

        // Avoid self-loop
        while (cycleStartIndex == cycleEndIndex) {
            cycleEndIndex = rand.nextInt(nodeCount);
        }

        GraphNode cycleStartNode = nodes.get(cycleStartIndex);
        GraphNode cycleEndNode = nodes.get(cycleEndIndex);

        // Add cyclic reference
        cycleStartNode.addNeighbor(cycleEndNode);


        return nodes.get(0);
    }

    /**
     * Copies a graph with cyclic references using a recursive approach.
     * The method creates a new graph with the same structure as the original graph.
     * The method uses a map to keep track of visited nodes and their copies.
     * The method returns the root node of the copied graph.
     *
     * @param root the root node of the original graph
     * @return the root node of the copied graph
     */
    public static GraphNode copyGraph(GraphNode root) {
        Map<GraphNode, GraphNode> visited = new HashMap<>();
        return copyGraphHelper(root, visited);
    }

    /**
     * Helper method for copying a graph with cyclic references using a recursive approach.
     * The method creates a new graph with the same structure as the original graph.
     * The method uses a map to keep track of visited nodes and their copies.
     * The method returns the root node of the copied graph.
     *
     * @param node    the current node to copy
     * @param visited a map of visited nodes and their copies
     * @return the copied node
     */
    private static GraphNode copyGraphHelper(GraphNode node, Map<GraphNode, GraphNode> visited) {

        if (node == null) {
            return null;
        }

        // Check if the node was already copied to prevent circular references
        if (visited.containsKey(node)) {
            return visited.get(node);
        }

        // Create a copy of the current node
        GraphNode copy = new GraphNode(node.value);
        visited.put(node, copy);  // Uchování kopie uzlu

        // Recursively copy neighbors
        for (GraphNode neighbor : node.neighbors) {
            copy.addNeighbor(copyGraphHelper(neighbor, visited));
        }

        return copy;
    }

    /**
     * Copies a graph with cyclic references using an iterative approach.
     * The method creates a new graph with the same structure as the original graph.
     * The method uses a stack to perform a depth-first traversal of the original graph.
     * The method uses a map to keep track of visited nodes and their copies.
     * The method returns the root node of the copied graph.
     *
     * @param root the root node of the original graph
     * @return the root node of the copied graph
     */
    public static GraphNode copyGraphIterative(GraphNode root) {
        if (root == null) {
            return null;
        }

        // Map to store visited nodes and their copies
        Map<GraphNode, GraphNode> visited = new HashMap<>();
        Stack<GraphNode> stack = new Stack<>();
        stack.push(root);

        // Create a copy of the root node
        visited.put(root, new GraphNode(root.value));

        while (!stack.isEmpty()) {
            GraphNode node = stack.pop();

            // Iterate over the neighbors of the current node
            for (GraphNode neighbor : node.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    visited.put(neighbor, new GraphNode(neighbor.value));
                    stack.push(neighbor);
                }
                visited.get(node).addNeighbor(visited.get(neighbor));
            }
        }

        return visited.get(root);
    }


}

