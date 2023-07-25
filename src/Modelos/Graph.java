package Modelos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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

    //SE PUEDE ELIMINAR AL FINAL
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

    public List<Integer> getNeighbourhood(int value) {
        Vertex vertex = getVertex(value);
        List<Integer> neighbours = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getOrigin().equals(vertex)) {
                neighbours.add(edge.getEnd().getValue());
            }
        }
        return neighbours;
    }

    public Vertex getVertex(int value) {
        for (Vertex vertex : vertices) {
            if (vertex.getValue() == value) {
                return vertex;
            }
        }
        return null;
    }

    private List<Vertex> getNeighbourhood(Vertex vertex) {
        List<Vertex> neighbours = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getOrigin().equals(vertex)) {
                neighbours.add(edge.getEnd());
            }
        }
        return neighbours;
    }

    public void printEdges() {
        System.out.println(this.edges.toString());
    }

    public Integer gradoEntrada(Vertex vertice) {
        Integer res = 0;
        for (Edge arista : this.edges) {
            if (arista.getEnd().equals(vertice)) {
                ++res;
            }
        }
        return res;
    }

    public Integer gradoSalida(Vertex vertice) {
        Integer res = 0;
        for (Edge arista : this.edges) {
            if (arista.getOrigin().equals(vertice)) {
                ++res;
            }
        }
        return res;
    }

    protected Edge findEdge(int v1, int v2) {
        return this.findEdge(new Vertex(v1), new Vertex(v2));
    }

    private boolean isAdjacent(Vertex v1, Vertex v2) {
        List<Vertex> ady = this.getNeighbourhood(v1);
        for (Vertex unAdy : ady) {
            if (unAdy.equals(v2)) {
                return true;
            }
        }
        return false;
    }

    protected Edge findEdge(Vertex v1, Vertex v2) {
        for (Edge unaArista : this.edges) {
            if (unaArista.getOrigin().equals(v1) && unaArista.getEnd().equals(v2)) {
                return unaArista;
            }
        }
        return null;
    }

    public List<Edge> getEdges() {
        return edges;
    }
    
    public List<Vertex> getVertex() {
        return vertices;
    }

    public int getVertexCount() {
        return vertices.size();
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

    public List<Integer> bfs(Vertex inicio) {
        List<Integer> resultado = new ArrayList<>();
        Queue<Vertex> pendientes = new LinkedList<>();
        Set<Vertex> marcados = new HashSet<>();

        marcados.add(inicio);
        pendientes.add(inicio);

        while (!pendientes.isEmpty()) {
            Vertex actual = pendientes.poll();
            List<Vertex> adyacentes = getNeighbourhood(actual);
            resultado.add(actual.getValue());

            for (Vertex v : adyacentes) {
                if (!marcados.contains(v)) {
                    pendientes.add(v);
                    marcados.add(v);
                }
            }
        }

        return resultado;
    }

    public List<Vertex> calculatePageRank(double dampingFactor, int iterations) {
        // Asignar valor inicial de 1.0 a cada vértice
        for (Vertex vertex : vertices) {
            vertex.setPageRank(1.0);
        }

        // Realizar las iteraciones para calcular el PageRank
        for (int i = 0; i < iterations; i++) {
            // Copiar los valores de PageRank actuales para cada vértice
            Map<Vertex, Double> currentPageRanks = new HashMap<>();
            for (Vertex vertex : vertices) {
                currentPageRanks.put(vertex, vertex.getPageRank());
            }

            // Calcular los nuevos PageRank en esta iteración
            for (Vertex vertex : vertices) {
                double sum = 0.0;
                List<Vertex> neighbours = getNeighbours(vertex);
                for (Vertex neighbour : neighbours) {
                    int outDegree = gradoSalida(neighbour);
                    if (outDegree > 0) {
                        sum += currentPageRanks.get(neighbour) / outDegree;
                    }
                }
                double newPageRank = (1 - dampingFactor) + dampingFactor * sum;
                vertex.setPageRank(newPageRank);
            }
        }

        // Usar un conjunto HashSet para evitar duplicados y mantener el orden según el PageRank
        Set<Vertex> uniqueVertices = new HashSet<>(vertices);

        // Convertir el conjunto en una lista y ordenarla según el PageRank descendente
        List<Vertex> sortedVertices = new ArrayList<>(uniqueVertices);
        sortedVertices.sort((v1, v2) -> Double.compare(v2.getPageRank(), v1.getPageRank()));

        return sortedVertices;
    }

    //BORRAR AL FINAL
    public String representarCaminos(List<List<Vertex>> caminos) {
        StringBuilder sb = new StringBuilder();

        for (List<Vertex> camino : caminos) {
            for (Vertex vertex : camino) {
                sb.append(vertex.getValue()).append(" -> ");
            }
            sb.setLength(sb.length() - 4); // Eliminar la última flecha "->"
            sb.append("\n");
        }

        return sb.toString();
    }

    // Método para obtener una lista de listas de enteros a partir de los caminos
    public List<List<Integer>> obtenerCaminosComoListaDeEnteros(List<List<Vertex>> caminos) {
        List<List<Integer>> caminosEnteros = new ArrayList<>();

        for (List<Vertex> camino : caminos) {
            List<Integer> caminoEntero = new ArrayList<>();
            for (Vertex vertex : camino) {
                caminoEntero.add(vertex.getValue());
            }
            caminosEnteros.add(caminoEntero);
        }

        return caminosEnteros;
    }

}
