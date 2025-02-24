package com.romiis.complexTests;

import java.util.*;

public class RandomCyclicGraphGenerator {

    // Generuje náhodný cyklický graf o zadaném počtu uzlů
    public static GraphNode generateCyclicGraph(int nodeCount) {
        // Vytvoření uzlů
        List<GraphNode> nodes = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new GraphNode("Node" + i));
        }

        // Propojení uzlů a přidání náhodných sousedů
        Random rand = new Random();
        for (int i = 0; i < nodeCount; i++) {
            int numNeighbors = rand.nextInt(3) + 1;  // Každý uzel může mít 1 až 3 sousedy
            for (int j = 0; j < numNeighbors; j++) {
                int neighborIndex = rand.nextInt(nodeCount);
                GraphNode neighbor = nodes.get(neighborIndex);

                // Pokud není soused stejný uzel (nechceme samocyklus)
                if (neighbor != nodes.get(i)) {
                    nodes.get(i).addNeighbor(neighbor);
                }
            }
        }

        // Náhodně vybereme cyklus, tím, že jeden uzel připojíme zpět na jiný uzel, aby vznikl cyklus
        int cycleStartIndex = rand.nextInt(nodeCount);
        int cycleEndIndex = rand.nextInt(nodeCount);

        // Zajistíme, že cyklus nebude samocyklus
        while (cycleStartIndex == cycleEndIndex) {
            cycleEndIndex = rand.nextInt(nodeCount);
        }

        GraphNode cycleStartNode = nodes.get(cycleStartIndex);
        GraphNode cycleEndNode = nodes.get(cycleEndIndex);

        // Přidání cyklického odkazu
        cycleStartNode.addNeighbor(cycleEndNode);

        return nodes.get(0); // Vracíme kořen grafu (první uzel)
    }

    // Pomocná metoda pro tisk grafu
    public static void printGraph(GraphNode root) {
        Set<GraphNode> visited = new HashSet<>();
        Queue<GraphNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();
            if (!visited.contains(current)) {
                visited.add(current);
                System.out.println(current.value + " -> " + getNeighborsString(current));
                for (GraphNode neighbor : current.neighbors) {
                    queue.add(neighbor);
                }
            }
        }
    }

    // Metoda pro kopírování grafu s cyklickými odkazy
    public static GraphNode copyGraph(GraphNode root) {
        // Mapování původního uzlu na jeho kopii
        Map<GraphNode, GraphNode> visited = new HashMap<>();
        return copyGraphHelper(root, visited);
    }

    // Pomocná rekurzivní metoda pro kopírování grafu
    private static GraphNode copyGraphHelper(GraphNode node, Map<GraphNode, GraphNode> visited) {
        if (node == null) {
            return null;
        }

        // Pokud už uzel byl navštíven (byla pro něj vytvořena kopie), vrátíme jeho kopii
        if (visited.containsKey(node)) {
            return visited.get(node);
        }

        // Vytvoření nové kopie uzlu
        GraphNode copy = new GraphNode(node.value);
        visited.put(node, copy);  // Uchování kopie uzlu

        // Rekurzivně kopírujeme všechny sousedy
        for (GraphNode neighbor : node.neighbors) {
            copy.addNeighbor(copyGraphHelper(neighbor, visited));
        }

        return copy;
    }

    // Iterativní metoda pro kopírování grafu s cyklickými odkazy
    public static GraphNode copyGraphIterative(GraphNode root) {
        if (root == null) {
            return null;
        }

        // Mapování původního uzlu na jeho kopii
        Map<GraphNode, GraphNode> visited = new HashMap<>();
        Stack<GraphNode> stack = new Stack<>();
        stack.push(root);

        // Vytvoření kopie kořenového uzlu
        visited.put(root, new GraphNode(root.value));

        while (!stack.isEmpty()) {
            GraphNode node = stack.pop();

            // Procházení všech sousedů aktuálního uzlu
            for (GraphNode neighbor : node.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    // Pokud soused ještě nebyl navštíven, vytvoříme jeho kopii
                    visited.put(neighbor, new GraphNode(neighbor.value));
                    // Přidáme souseda na zásobník pro pozdější zpracování
                    stack.push(neighbor);
                }
                // Přidáme kopii souseda k aktuálnímu uzlu
                visited.get(node).addNeighbor(visited.get(neighbor));
            }
        }

        // Vrátíme kopii kořenového uzlu
        return visited.get(root);
    }

    private static String getNeighborsString(GraphNode node) {
        StringBuilder sb = new StringBuilder();
        for (GraphNode neighbor : node.neighbors) {
            sb.append(neighbor.value).append(" ");
        }
        return sb.toString();
    }
}

