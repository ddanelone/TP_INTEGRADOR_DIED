package Modelos;

import java.util.ArrayList;
import java.util.List;

public class GrafoCaminos {

    private Graph<Integer> grafo;

    public GrafoCaminos(List<Caminos> caminos) {
        this.grafo = new Graph<>();

        // Filtrar los caminos que están marcados como "operativos"
        List<Caminos> caminosOperativos = filtrarCaminosOperativos(caminos);

        // Agregar los nodos al grafo
        for (Caminos camino : caminosOperativos) {
            grafo.addNodo(camino.getOrigenId());
            grafo.addNodo(camino.getDestinoId());
        }

        // Conectar los caminos al grafo
        for (Caminos camino : caminosOperativos) {
            grafo.conectar(camino.getOrigenId(), camino.getDestinoId(), camino.getCapacidad());
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

    public Graph<Integer> getGrafo() {
        return grafo;
    }
}
