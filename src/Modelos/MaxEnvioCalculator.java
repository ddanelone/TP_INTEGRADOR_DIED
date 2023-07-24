package Modelos;

import java.util.*;

public class MaxEnvioCalculator {

    private Graph graph;
    private int source; // Nodo fuente
    private int sink; // Nodo sumidero;

    public MaxEnvioCalculator(Graph graph, int source, int sink) {
        this.graph = graph;
        this.source = source;
        this.sink = sink;
    }

    public int getMaxFlow() {
        int maxFlow = 0;
        List<List<Integer>> residualGraph = new ArrayList<>();

        // Inicializar la matriz de capacidad residual con las capacidades de las aristas
        for (int i = 0; i < graph.getVertexCount(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < graph.getVertexCount(); j++) {
                row.add(0);
            }
            residualGraph.add(row);
        }
        for (Edge edge : graph.getEdges()) {
            residualGraph.get(edge.getOrigin().getValue() - 1).set(edge.getEnd().getValue() - 1, edge.getValue());
        }
        
        // Realizar BFS para encontrar caminos aumentantes y actualizar el flujo máximo
        int[] parent = new int[graph.getVertexCount()];
        while (true) {
            int minCapacity = findAugmentingPath(residualGraph, parent);
            if (minCapacity == 0) {
                break; // No hay más caminos aumentantes
            }
       
            // Actualizar la capacidad residual del camino y el flujo máximo
            int v = sink - 1;
            while (v != source - 1) {
                int u = parent[v];
                residualGraph.get(u).set(v, residualGraph.get(u).get(v) - minCapacity);
                residualGraph.get(v).set(u, residualGraph.get(v).get(u) + minCapacity);
                v = u;
            }

            maxFlow += minCapacity;
        }
        return maxFlow;
    }

    private int findAugmentingPath(List<List<Integer>> residualGraph, int[] parent) {
        Arrays.fill(parent, -1);
        parent[source - 1] = -2;
        int[] capacity = new int[graph.getVertexCount()];
        capacity[source - 1] = Integer.MAX_VALUE;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 1; v <= graph.getVertexCount(); v++) {
                int capacityUV = residualGraph.get(u - 1).get(v - 1);
                if (capacityUV > 0 && parent[v - 1] == -1) {
                    parent[v - 1] = u - 1;
                    capacity[v - 1] = Math.min(capacity[u - 1], capacityUV);
                    if (v != sink) {
                        queue.add(v);
                    } else {
                        return capacity[sink - 1];
                    }
                }
            }
        }

        return 0;
    }
}
