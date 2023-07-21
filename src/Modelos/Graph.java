package Modelos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph {

    private List<Edge> edges;
    private List<Vertex> vertices;

    public Graph() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    public Graph addVertex(Vertex vertex) {
        vertices.add(vertex);
        return this;
    }

    public Graph addEdge(Vertex origin, Vertex end, int value) {
        edges.add(new Edge(origin, end, value));
        return this;
    }

    public List<Vertex> getNeighbours(Vertex vertex) {
        List<Vertex> neighbours = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getOrigin().equals(vertex)) {
                neighbours.add(edge.getEnd());
            }
        }
        return neighbours;
    }

    private void findPaths(Vertex current, Vertex destination, List<Vertex> currentPath, Set<Vertex> visited, List<List<Vertex>> paths) {
        visited.add(current);
        currentPath.add(current);

        if (current.equals(destination)) {
            paths.add(new ArrayList<>(currentPath));
        } else {
            List<Vertex> neighbours = getNeighbours(current);
            for (Vertex neighbour : neighbours) {
                if (!visited.contains(neighbour)) {
                    findPaths(neighbour, destination, currentPath, visited, paths);
                }
            }
        }

        visited.remove(current);
        currentPath.remove(current);
    }

    public List<List<Vertex>> findAllPaths(Vertex origin, Vertex destination) {
        List<List<Vertex>> paths = new ArrayList<>();
        List<Vertex> currentPath = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();

        findPaths(origin, destination, currentPath, visited, paths);

        return paths;
    }
    
    public String graphToString() {
        List<Edge> sortedEdges = edges.stream()
                .sorted(Comparator.comparingInt(e -> e.getOrigin().getValue()))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (Edge edge : sortedEdges) {
            sb.append(edge.getOrigin().getValue())
                    .append(" --> ")
                    .append(edge.getValue())
                    .append(" --> ")
                    .append(edge.getEnd().getValue())
                    .append("\n");
        }

        return sb.toString();
    }
    
  public List<Vertex> getAccessibleVertices(int destinationId) {
    List<Vertex> accessibleVertices = new ArrayList<>();
    Set<Vertex> visited = new HashSet<>();
    Vertex destinationVertex = new Vertex(destinationId);

    for (Vertex vertex : vertices) {
        if (vertex.getValue() != destinationId && !visited.contains(vertex)) {
            dfs(vertex, destinationVertex, visited, accessibleVertices);
        }
    }

    return accessibleVertices;
}

private void dfs(Vertex current, Vertex destination, Set<Vertex> visited, List<Vertex> accessibleVertices) {
    visited.add(current);

    if (current.equals(destination)) {
        return;
    }

    List<Vertex> neighbours = getNeighbours(current);
    for (Vertex neighbour : neighbours) {
        if (!visited.contains(neighbour)) {
            dfs(neighbour, destination, visited, accessibleVertices);
        }
    }

    if (!accessibleVertices.contains(current)) {
        accessibleVertices.add(current);
    }
}
}
