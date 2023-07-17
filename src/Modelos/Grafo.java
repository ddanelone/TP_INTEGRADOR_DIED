package Modelos;

import java.util.*;

public class Grafo {
    private int numeroNodos;
    private ArrayList<ArrayList<Arista>> listaAdyacencia;

    public Grafo(int numeroNodos) {
        this.numeroNodos = numeroNodos;
        this.listaAdyacencia = new ArrayList<ArrayList<Arista>>();
        for (int i = 0; i < numeroNodos; i++) {
            this.listaAdyacencia.add(new ArrayList<Arista>());
        }
    }

    public void agregarArista(int origen, int destino, int capacidad) {
        Arista arista = new Arista(destino, capacidad);
        this.listaAdyacencia.get(origen).add(arista);
    }

    public int calcularFlujoMaximo(int fuente, int sumidero) {
        int[] flujo = new int[numeroNodos];
        int flujoMaximo = 0;

        while (existeCaminoAumentante(fuente, sumidero, flujo)) {
            int flujoAumentante = Integer.MAX_VALUE;
            int nodoActual = sumidero;

            while (nodoActual != fuente) {
                int nodoAnterior = flujo[nodoActual];
                int capacidadArista = 0;

                for (Arista arista : listaAdyacencia.get(nodoAnterior)) {
                    if (arista.destino == nodoActual) {
                        capacidadArista = arista.capacidad;
                        break;
                    }
                }

                flujoAumentante = Math.min(flujoAumentante, capacidadArista);
                nodoActual = nodoAnterior;
            }

            flujoMaximo += flujoAumentante;

            nodoActual = sumidero;
            while (nodoActual != fuente) {
                int nodoAnterior = flujo[nodoActual];
                for (Arista arista : listaAdyacencia.get(nodoAnterior)) {
                    if (arista.destino == nodoActual) {
                        arista.capacidad -= flujoAumentante;
                        break;
                    }
                }

                boolean aristaEncontrada = false;
                for (Arista arista : listaAdyacencia.get(nodoActual)) {
                    if (arista.destino == nodoAnterior) {
                        aristaEncontrada = true;
                        arista.capacidad += flujoAumentante;
                        break;
                    }
                }

                if (!aristaEncontrada) {
                    Arista aristaInversa = new Arista(nodoAnterior, flujoAumentante);
                    listaAdyacencia.get(nodoActual).add(aristaInversa);
                }

                nodoActual = nodoAnterior;
            }
        }

        return flujoMaximo;
    }

    private boolean existeCaminoAumentante(int fuente, int sumidero, int[] flujo) {
        boolean[] visitado = new boolean[numeroNodos];
        Arrays.fill(visitado, false);

        Queue<Integer> cola = new LinkedList<>();
        cola.add(fuente);
        visitado[fuente] = true;

        while (!cola.isEmpty()) {
            int nodoActual = cola.poll();

            for (Arista arista : listaAdyacencia.get(nodoActual)) {
                if (!visitado[arista.destino] && arista.capacidad > 0) {
                    cola.add(arista.destino);
                    visitado[arista.destino] = true;
                    flujo[arista.destino] = nodoActual;

                    if (arista.destino == sumidero) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private class Arista {
        public int destino;
        public int capacidad;

        public Arista(int destino, int capacidad) {
            this.destino = destino;
            this.capacidad = capacidad;
        }
    }
}
