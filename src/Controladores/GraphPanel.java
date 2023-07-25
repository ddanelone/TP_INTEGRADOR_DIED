package Controladores;

import Modelos.Edge;
import Modelos.Graph;
import Modelos.Vertex;
import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphPanel extends JPanel {
    private Graph graph;

    private Map<Vertex, Integer> xCoordinates;
    private Map<Vertex, Integer> yCoordinates;

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(600, 600));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        generateRandomCoordinates();
        addCloseButton();
    }

   private void generateRandomCoordinates() {
    xCoordinates = new HashMap<>();
    yCoordinates = new HashMap<>();

    int gridSize = 400; // Tamaño de la cuadrícula
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


    private void addCloseButton() {
    JButton closeButton = new JButton("Cerrar");
    closeButton.setFont(new Font("Tahoma", Font.BOLD, 14));
    closeButton.setPreferredSize(new Dimension(120, 30));
    closeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Cerrar la ventana al presionar el botón
            SwingUtilities.getWindowAncestor(GraphPanel.this).dispose();
        }
    });

    // Agregar el botón en la parte inferior y centrado horizontalmente del panel
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.PAGE_END; // Alinear en la parte inferior
    add(closeButton, gbc);
}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar los vértices
        for (Vertex vertex : graph.getVertex()) {
            int x = xCoordinates.get(vertex);
            int y = yCoordinates.get(vertex);
            g.setColor(Color.BLUE);
            g.fillOval(x - 7, y - 7, 14, 14); // Aumentamos el tamaño del círculo
            g.drawString(String.valueOf(vertex.getValue()), x - 10, y - 15);
        }

        // Dibujar las aristas
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
