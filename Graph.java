import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph {

    private Map<GraphNode, List<GraphEdge>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public GraphNode addNode(String data) {
        GraphNode node = new GraphNode(data);
        adjacencyList.put(node, new ArrayList<>());
        return node;
    }

    public void removeNode(GraphNode node) {
        adjacencyList.values().forEach(edges -> edges.removeIf(edge -> edge.getDestination().equals(node)));
        adjacencyList.remove(node);
    }

    public void addEdge(GraphNode n1, GraphNode n2, int weight) {
        GraphEdge edge = new GraphEdge(n1, n2, weight);
        adjacencyList.get(n1).add(edge);
        adjacencyList.get(n2).add(edge);
    }

    public void removeEdge(GraphNode n1, GraphNode n2) {
        adjacencyList.get(n1).removeIf(edge -> edge.getDestination().equals(n2));
        adjacencyList.get(n2).removeIf(edge -> edge.getDestination().equals(n1));
    }

    private static class GraphNode {

        private String data;

        public GraphNode(String data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof GraphNode) {
                return data.equals(((GraphNode) obj).data);
            }
            return false;
        }

    }

    private static class GraphEdge {

        private GraphNode source;
        private GraphNode destination;
        private int weight;

        public GraphEdge(GraphNode source, GraphNode destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public GraphNode getSource() {
            return source;
        }

        public GraphNode getDestination() {
            return destination;
        }

        public int getWeight() {
            return weight;
        }
    }
    public static Graph importFromFile(String fileName) {
        Graph graph = new Graph();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            Pattern edgePattern = Pattern.compile("(\\w+)\\s*--\\s*(\\w+)(\\s*\\[\\s*weight\\s*=\\s*(\\d+)\\s*\\])?\\s*;");
            boolean undirected = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("strict") && line.endsWith("{")) {
                    undirected = true;
                } else if (line.equals("}")) {
                    break;
                } else if (undirected) {
                    Matcher matcher = edgePattern.matcher(line);
                    if (matcher.matches()) {
                        String node1Name = matcher.group(1);
                        String node2Name = matcher.group(2);
                        int weight = 1;
                        if (matcher.group(4) != null) {
                            weight = Integer.parseInt(matcher.group(4));
                        }
                        GraphNode node1 = graph.addNode(node1Name);
                        GraphNode node2 = graph.addNode(node2Name);
                        graph.addEdge(node1, node2, weight);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return graph;
    }
}
