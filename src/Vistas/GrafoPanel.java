package Vistas;

import Modelos.Edge;
import Modelos.Graph;
import Modelos.Vertex;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GrafoPanel extends JPanel {
    private Graph graph;
    private Map<Vertex, Integer> xCoordinates;
    private Map<Vertex, Integer> yCoordinates;

    public GrafoPanel(Graph graph) {
        this.graph = graph;
        generateRandomCoordinates();
    }

    private void generateRandomCoordinates() {
        xCoordinates = new HashMap<>();
        yCoordinates = new HashMap<>();

        int gridSize = 500; // Tamaño de la cuadrícula
        int numCols = getWidth() / gridSize;
        int numRows = getHeight() / gridSize;

        for (Vertex vertex : graph.getVertex()) {
            int x, y;
            do {
                // Generar una coordenada aleatoria dentro de la celda de la cuadrícula
                x = (int) (Math.random() * gridSize) + (numCols / 2) * gridSize;
                y = (int) (Math.random() * gridSize) + (numRows / 2) * gridSize;
            } while (xCoordinates.containsValue(x) || yCoordinates.containsValue(y));

            xCoordinates.put(vertex, x);
            yCoordinates.put(vertex, y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar el grafo
        for (Vertex vertex : graph.getVertex()) {
            int x = xCoordinates.get(vertex);
            int y = yCoordinates.get(vertex);
            g.setColor(Color.BLUE);
            g.fillOval(x - 5, y - 5, 10, 10);
            g.drawString(String.valueOf(vertex.getValue()), x - 10, y - 15);
        }

        for (Edge edge : graph.getEdges()) {
            Vertex origin = edge.getOrigin();
            Vertex end = edge.getEnd();
            int x1 = xCoordinates.get(origin);
            int y1 = yCoordinates.get(origin);
            int x2 = xCoordinates.get(end);
            int y2 = yCoordinates.get(end);
            g.setColor(Color.RED);
            g.drawLine(x1, y1, x2, y2);
            g.drawString(String.valueOf(edge.getValue()), (x1 + x2) / 2, (y1 + y2) / 2);
        }
    }
}
