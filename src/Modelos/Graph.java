package Modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Graph<T extends Comparable<T>> {

    private List<Edge<T>> edges;
    private List<Vertex<T>> vertexs;

    public Graph() {
        this.edges = new ArrayList<Edge<T>>();
        this.vertexs = new ArrayList<Vertex<T>>();
    }

    public Graph<T> addNodo(T nodo) {
        this.addNodo(new Vertex<T>(nodo));
        return this;
    }

    private void addNodo(Vertex<T> nodo) {
        this.vertexs.add(nodo);
    }

    public Graph<T> conectar(T n1, T n2) {
        this.conectar(getNodo(n1), getNodo(n2), 1.0);
        return this;
    }

    public Graph<T> conectar(T n1, T n2, Number valor) {
        this.conectar(getNodo(n1), getNodo(n2), valor);
        return this;
    }

    private void conectar(Vertex<T> nodo1, Vertex<T> nodo2, Number valor) {
        this.edges.add(new Edge<T>(nodo1, nodo2, valor));
    }

    public Vertex<T> getNodo(T valor) {
        for (Vertex<T> vertex : this.vertexs) {
            if (vertex.getValue().equals(valor)) {
                return vertex;
            }
        }
        return null; // Retorna null si no se encuentra el vértice con el valor buscado
    }

    public List<T> getNeighbourhood(T valor) {
        Vertex<T> unNodo = this.getNodo(valor);
        List<T> salida = new ArrayList<T>();
        for (Edge<T> enlace : this.edges) {
            if (enlace.getOrigin().equals(unNodo)) {
                salida.add(enlace.getEnd().getValue());
            }
        }
        return salida;
    }

    private List<Vertex<T>> getNeighbourhood(Vertex<T> unNodo) {
        List<Vertex<T>> salida = new ArrayList<Vertex<T>>();
        for (Edge<T> enlace : this.edges) {
            if (enlace.getOrigin().equals(unNodo)) {
                salida.add(enlace.getEnd());
            }
        }
        return salida;
    }

    public void printEdges() {
        System.out.println(this.edges.toString());
    }

    public Integer gradoEntrada(Vertex<T> vertice) {
        Integer res = 0;
        for (Edge<T> arista : this.edges) {
            if (arista.getEnd().equals(vertice)) {
                ++res;
            }
        }
        return res;
    }

    public Integer gradoSalida(Vertex<T> vertice) {
        Integer res = 0;
        for (Edge<T> arista : this.edges) {
            if (arista.getOrigin().equals(vertice)) {
                ++res;
            }
        }
        return res;
    }

    protected Edge<T> findEdge(T v1, T v2) {
        return this.findEdge(new Vertex<T>(v1), new Vertex<T>(v2));
    }

    private boolean isAdjacent(Vertex<T> v1, Vertex<T> v2) {
        List<Vertex<T>> ady = this.getNeighbourhood(v1);
        for (Vertex<T> unAdy : ady) {
            if (unAdy.equals(v2)) {
                return true;
            }
        }
        return false;
    }

    protected Edge<T> findEdge(Vertex<T> v1, Vertex<T> v2) {
        for (Edge<T> unaArista : this.edges) {

            if (unaArista.getOrigin().equals(v1) && unaArista.getEnd().equals(v2)) {
                return unaArista;
            }
        }
        return null;
    }

    /*
    public List<T> bfs(Vertex<T> inicio) {
        List<T> resultado = new ArrayList<T>();
        //estructuras auxiliares
        Queue<Vertex<T>> pendientes = new LinkedList<Vertex<T>>();
        HashSet<Vertex<T>> marcados = new HashSet<Vertex<T>>();
        marcados.add(inicio);
        pendientes.add(inicio);

        while (!pendientes.isEmpty()) {
            Vertex<T> actual = pendientes.poll();
            List<Vertex<T>> adyacentes = this.getNeighbourhood(actual);
            resultado.add(actual.getValue());
            for (Vertex<T> v : adyacentes) {
                if (!marcados.contains(v)) {
                    pendientes.add(v);
                    marcados.add(v);
                }
            }
        }
        //System.out.println(resultado);
        return resultado;
    }

    public List<T> dfs(Vertex<T> inicio) {
        List<T> resultado = new ArrayList<T>();
        Stack<Vertex<T>> pendientes = new Stack<Vertex<T>>();
        HashSet<Vertex<T>> marcados = new HashSet<Vertex<T>>();
        marcados.add(inicio);
        pendientes.push(inicio);

        while (!pendientes.isEmpty()) {
            Vertex<T> actual = pendientes.pop();
            List<Vertex<T>> adyacentes = this.getNeighbourhood(actual);
            resultado.add(actual.getValue());
            for (Vertex<T> v : adyacentes) {
                if (!marcados.contains(v)) {
                    pendientes.push(v);
                    marcados.add(v);
                }
            }
        }
        //System.out.println(resultado);
        return resultado;
    }

    public List<T> topological() {
        List<T> resultado = new ArrayList<T>();
        Map<Vertex<T>, Integer> gradosVertex = new HashMap<Vertex<T>, Integer>();
        for (Vertex<T> vert : this.vertexs) {
            gradosVertex.put(vert, this.gradoEntrada(vert));
        }
        while (!gradosVertex.isEmpty()) {

            List<Vertex<T>> nodosSinEntradas = gradosVertex.entrySet()
                    .stream()
                    .filter(x -> x.getValue() == 0)
                    .map(p -> p.getKey())
                    .collect(Collectors.toList());

            if (nodosSinEntradas.isEmpty()) {
                System.out.println("TIENE CICLOS");
            }

            for (Vertex<T> v : nodosSinEntradas) {
                resultado.add(v.getValue());
                gradosVertex.remove(v);
                for (Vertex<T> ady : this.getNeighbourhood(v)) {
                    gradosVertex.put(ady, gradosVertex.get(ady) - 1);
                }
            }
        }

        System.out.println(resultado);
        return resultado;
    }

    private void findPathAux(Vertex<T> v1, Vertex<T> v2, List<Vertex<T>> marcados, List<List<Vertex<T>>> todos) {
        List<Vertex<T>> adyacentes = this.getNeighbourhood(v1);
        // Vector copiaMarcados;
        List<Vertex<T>> copiaMarcados = null;
        ;

        for (Vertex<T> ady : adyacentes) {
            System.out.println(">> " + ady);
            copiaMarcados = marcados.stream().collect(Collectors.toList());
            if (ady.equals(v2)) {
                copiaMarcados.add(v2);
                todos.add(new ArrayList<Vertex<T>>(copiaMarcados));
                System.out.println("Path found " + todos.toString());
            } else {
                if (!copiaMarcados.contains(ady)) {
                    copiaMarcados.add(ady);
                    this.findPathAux(ady, v2, copiaMarcados, todos);
                }
            }
        }

    }

    public List<List<Vertex<T>>> paths(T v1, T v2) {
        return this.paths(new Vertex(v1), new Vertex(v2));
    }

    public List<List<Vertex<T>>> paths(Vertex<T> v1, Vertex<T> v2) {
        List<List<Vertex<T>>> salida = new ArrayList<List<Vertex<T>>>();
        List<Vertex<T>> marcados = new ArrayList<Vertex<T>>();
        marcados.add(v1);
        findPathAux(v1, v2, marcados, salida);
        return salida;
    }

    public Map<T, Integer> minimumPathDijkstra(T valorOrigen) {
        Vertex<T> vOrigen = new Vertex<T>(valorOrigen);
        Map<Vertex<T>, Integer> caminosResultado = this.minimumPathDijkstra(vOrigen);
        Map<T, Integer> resultado = new LinkedHashMap<T, Integer>();
        for (Entry<Vertex<T>, Integer> unNodo : caminosResultado.entrySet()) {
            resultado.put(unNodo.getKey().getValue(), unNodo.getValue());
        }
        return resultado;
    }

    private Map<Vertex<T>, Integer> minimumPathDijkstra(Vertex<T> origen) {

        // inicializo todas las distancias a INFINITO
        Map<Vertex<T>, Integer> distancias = new HashMap<Vertex<T>, Integer>();
        for (Vertex<T> unVertex : this.vertexs) {
            distancias.put(unVertex, Integer.MAX_VALUE);
        }
        distancias.put(origen, 0);

        // guardo visitados y pendientes de visitar
        Set<Vertex<T>> visitados = new HashSet<Vertex<T>>();
        TreeMap<Integer, Vertex<T>> aVisitar = new TreeMap<Integer, Vertex<T>>();

        aVisitar.put(0, origen);

        while (!aVisitar.isEmpty()) {
            Entry<Integer, Vertex<T>> nodo = aVisitar.pollFirstEntry();
            visitados.add(nodo.getValue());

            int nuevaDistancia = Integer.MIN_VALUE;
            List<Vertex<T>> adyacentes = this.getNeighbourhood(nodo.getValue());

            for (Vertex<T> unAdy : adyacentes) {
                if (!visitados.contains(unAdy)) {
                    Edge<T> enlace = this.findEdge(nodo.getValue(), unAdy);
                    if (enlace != null) {
                        nuevaDistancia = enlace.getValue().intValue();
                    }
                    int distanciaHastaAdy = distancias.get(nodo.getValue()).intValue();
                    int distanciaAnterior = distancias.get(unAdy).intValue();
                    if (distanciaHastaAdy + nuevaDistancia < distanciaAnterior) {
                        distancias.put(unAdy, distanciaHastaAdy + nuevaDistancia);
                        aVisitar.put(distanciaHastaAdy + nuevaDistancia, unAdy);
                    }
                }
            }
        }
        System.out.println("DISTANCIAS DESDE " + origen);
        System.out.println("Resultado: " + distancias);
        return distancias;
    }

    public void floydWarshall() {
        int cantVertexs = this.vertexs.size();
        int[][] matrizDistancias = new int[cantVertexs][cantVertexs];

        for (int i = 0; i < cantVertexs; i++) {
            for (int j = 0; j < cantVertexs; j++) {
                if (i == j) {
                    matrizDistancias[i][j] = 0;
                } else {
                    Edge<T> arista = this.findEdge(this.vertexs.get(i), this.vertexs.get(j));
                    if (arista != null) {
                        matrizDistancias[i][j] = arista.getValue().intValue();
                    } else {
                        matrizDistancias[i][j] = 9999;
                    }
                }
            }
        }
        printMatrix(matrizDistancias);

        for (int k = 0; k < cantVertexs; k++) {
            // Pick all vertices as source one by one 
            for (int i = 0; i < cantVertexs; i++) {
                // Pick all vertices as destination for the 
                // above picked source 
                for (int j = 0; j < cantVertexs; j++) {
                    // If vertex k is on the shortest path from 
                    // i to j, then update the value of dist[i][j] 
                    if (matrizDistancias[i][k] + matrizDistancias[k][j] < matrizDistancias[i][j]) {
                        matrizDistancias[i][j] = matrizDistancias[i][k] + matrizDistancias[k][j];
                    }
                }
            }
            System.out.println("MATRIZ " + k);
            printMatrix(matrizDistancias);
        }

    }

    public void printMatrix(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            System.out.print("[ ");
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(i + ":" + j + " = " + m[i][j] + ",   ");
            }
            System.out.println(" ]");
        }

    }

    public Boolean pathExists(Vertex<T> v1, Vertex<T> v2, Integer n) {

        return false;
    }

    public Boolean pathExistsRecursive(Vertex<T> v1, Vertex<T> v2, Integer n) {
        HashSet<Vertex<T>> visitados = new HashSet<Vertex<T>>();
        visitados.add(v1);
        return pathExistsRecursive(v1, v2, n, visitados);
    }

    private Boolean pathExistsRecursive(Vertex<T> v1, Vertex<T> v2, Integer n, HashSet<Vertex<T>> visitados) {
        return false;
    }
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex<T> vertice : vertexs) {
            List<Edge<T>> adyacentes = findEdgesFromVertex(vertice);
            for (Edge<T> arista : adyacentes) {
                sb.append("Nodo ").append(vertice.getValue()).append(" -> ").append(arista.getValue()).append(" -> Nodo ").append(arista.getEnd().getValue()).append("\n");
            }
        }
        return sb.toString();
    }

    private List<Edge<T>> findEdgesFromVertex(Vertex<T> vertice) {
        List<Edge<T>> adyacentes = new ArrayList<>();
        for (Edge<T> arista : edges) {
            if (arista.getOrigin().equals(vertice)) {
                adyacentes.add(arista);
            }
        }
        return adyacentes;
    }

    //Método que necesitamos para la clase Ordenes. Recibirá el Vértice de Destino, y me devolverá todos los caminos posibles.     
    public List<List<T>> encontrarCaminos(int idDestino) {
        List<List<T>> caminos = new ArrayList<>();
        Vertex<T> destino = null;

        for (Vertex<T> nodo : vertexs) {
            if (nodo.getValue().equals(idDestino)) {
                destino = nodo;
                break;
            }
        }

        if (destino != null) {
            for (Vertex<T> nodo : vertexs) {
                List<T> caminoActual = new ArrayList<>();
                Set<Vertex<T>> visitados = new HashSet<>();
                encontrarCaminosAux(nodo, destino, caminoActual, visitados, caminos);
            }
        } else {
            System.out.println("El vértice destino no existe en el grafo.");
        }

        return caminos;
    }

    private void encontrarCaminosAux(Vertex<T> nodoActual, Vertex<T> destino, List<T> caminoActual,
            Set<Vertex<T>> visitados, List<List<T>> caminos) {
        if (nodoActual.equals(destino)) {
            caminoActual.add(nodoActual.getValue());
            caminos.add(new ArrayList<>(caminoActual));
            caminoActual.remove(nodoActual.getValue());
            return;
        }

        visitados.add(nodoActual);
        caminoActual.add(nodoActual.getValue());

        List<Vertex<T>> vecinos = getNeighbourhood(nodoActual);
        for (Vertex<T> vecino : vecinos) {
            if (!visitados.contains(vecino)) {
                encontrarCaminosAux(vecino, destino, caminoActual, visitados, caminos);
            }
        }

        visitados.remove(nodoActual);
        caminoActual.remove(nodoActual.getValue());
    }

}
