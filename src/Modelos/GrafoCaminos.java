package Modelos;

import java.util.ArrayList;
import java.util.List;

import Modelos.Graph;

public class GrafoCaminos {

    private Graph grafo;

    public GrafoCaminos(List<Caminos> caminos) {
        this.grafo = new Graph();

        // Filtrar los caminos que están marcados como "operativos"
        List<Caminos> caminosOperativos = filtrarCaminosOperativos(caminos);

        // Agregar los nodos al grafo
        for (Caminos camino : caminosOperativos) {
            grafo.addVertex(new Vertex(camino.getOrigenId()));
            grafo.addVertex(new Vertex(camino.getDestinoId()));
        }

        // Conectar los caminos al grafo
        for (Caminos camino : caminosOperativos) {
            grafo.addEdge(new Vertex(camino.getOrigenId()), new Vertex(camino.getDestinoId()), camino.getCapacidad());
        }
    }

    private List<Caminos> filtrarCaminosOperativos(List<Caminos> caminos) {
        List<Caminos> caminosOperativos = new ArrayList<>();
        for (Caminos camino : caminos) {
            if (camino.isOperativo()) {
                caminosOperativos.add(camino);
            }
        }
        return caminosOperativos;
    }

    public Graph getGrafo() {
        return grafo;
    }
}
