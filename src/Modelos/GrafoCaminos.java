package Modelos;

import java.util.ArrayList;
import java.util.List;

public class GrafoCaminos {
    private Graph grafo;

    public GrafoCaminos(List<Caminos> caminos, List<Sucursales> sucursales) {
        this.grafo = new Graph();

        // Filtrar los caminos que están marcados como "operativos"
        List<Caminos> caminosOperativos = filtrarCaminosOperativos(caminos);

        // Obtener la lista de IDs de sucursales que están operativas
        List<Integer> sucursalesOperativasIds = obtenerSucursalesOperativasIds(sucursales);

        // Agregar los nodos al grafo
        for (Caminos camino : caminosOperativos) {
            if (sucursalesOperativasIds.contains(camino.getOrigenId()) && sucursalesOperativasIds.contains(camino.getDestinoId())) {
                grafo.addVertex(new Vertex(camino.getOrigenId()));
                grafo.addVertex(new Vertex(camino.getDestinoId()));
            }
        }

        // Conectar los caminos operativos al grafo
        for (Caminos camino : caminosOperativos) {
            if (sucursalesOperativasIds.contains(camino.getOrigenId()) && sucursalesOperativasIds.contains(camino.getDestinoId())) {
                grafo.addEdge(new Vertex(camino.getOrigenId()), new Vertex(camino.getDestinoId()), camino.getCapacidad());
            }
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

    private List<Integer> obtenerSucursalesOperativasIds(List<Sucursales> sucursales) {
        List<Integer> sucursalesOperativasIds = new ArrayList<>();
        for (Sucursales sucursal : sucursales) {
            if (sucursal.isOperativa()) {
                sucursalesOperativasIds.add(sucursal.getId());
            }
        }
        return sucursalesOperativasIds;
    }

    public Graph getGrafo() {
        return grafo;
    }
}
