package Vistas;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.Edge;
import Modelos.Graph;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Modelos.Vertex;
import java.util.List;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GrafoPanel extends JPanel {

    private final Graph graph;
    private Map<Vertex, Point> vertexPositions;

    //Instanciar modelo de sucursales y caminos
    Sucursales sucursal = new Sucursales();
    SucursalesDao sucursalDao = new SucursalesDao();
    Caminos camino = new Caminos();
    CaminosDao caminoDao = new CaminosDao();

    public GrafoPanel(Graph graph) {
        this.graph = graph;
        vertexPositions = new HashMap<>();
        generateCoordinates();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        //generateRandomCoordinates();
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        dibujarGrafo(g2d);
    }

    //Funciona ahora. Debería adaptarlo pero sin borrarlo.
    private void generateRandomCoordinates() {
        vertexPositions = new HashMap<>();

        int gridSize = 500; // Tamaño de la cuadrícula
        int numCols = getWidth() / gridSize;
        int numRows = getHeight() / gridSize;

        for (Vertex vertex : graph.getVertex()) {
            int x, y;
            do {
                // Generar una coordenada aleatoria dentro de la celda de la cuadrícula
                x = (int) (Math.random() * gridSize) + (numCols / 2) * gridSize;
                y = (int) (Math.random() * gridSize) + (numRows / 2) * gridSize;
                System.out.println("X= " + x + " *** Y= " + y);
            } while (vertexPositions.containsValue(new Point(x, y)));

            vertexPositions.put(vertex, new Point(x, y));
        }
    }

    private void generateCoordinates() {
        vertexPositions = new HashMap<>();

        int x = 50;
        int y = 200;
        int[] yValues = {250, 50, 400};
        int [] xValues = {150, 0, 0};
        
        int i = 0;
        for (Vertex vertex : graph.getVertex()) {
            
            // Verificar si el valor de y debe alterna 
            y = yValues[i%3];
            // Agregar la posición actual del vértice al mapa de vertexPositions
            if ((i % 3) == 0) {
                 vertexPositions.put(vertex, new Point(x+25, y));
            } else {
                vertexPositions.put(vertex, new Point(x, y));
            } 
           x +=xValues[i%3];
            
            i++;
            // Imprimir las coordenadas actuales (opcional, para verificar)
            // System.out.println("X= " + x + " *** Y= " + y);
        }
    }

    protected void dibujarGrafo(Graphics2D g2d) {

        // Dibujar los vértices
        for (Vertex vertex : graph.getVertex()) {
            Point position = vertexPositions.get(vertex);
            int x = (int) position.getX();
            int y = (int) position.getY();

            g2d.setColor(Color.BLUE);
            g2d.fillOval(x - 5, y - 5, 25, 25);
            g2d.drawString(nombreSucursal(vertex.getValue()), x - 10, y - 15);
        }

        // Dibujar las aristas con flechas
        for (Edge edge : graph.getEdges()) {
            Vertex origin = edge.getOrigin();
            Vertex end = edge.getEnd();

            Point originPosition = vertexPositions.get(origin);
            int x1 = (int) originPosition.getX();
            int y1 = (int) originPosition.getY();

            Point endPosition = vertexPositions.get(end);
            int x2 = (int) endPosition.getX();
            int y2 = (int) endPosition.getY();

            g2d.setColor(Color.RED);
            drawArrow(g2d, x1, y1, x2, y2);
            g2d.drawString(String.valueOf(edge.getValue()), (x1 + x2) / 2, (y1 + y2) / 2);
        }

    }

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 10;

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawLine(x1, y1, x2, y2);
        g2d.fillPolygon(new int[]{x2, (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6)), (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6))},
                new int[]{y2, (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6)), (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6))}, 3);
    }

    private String nombreSucursal(int id) {
        List<Sucursales> lista = sucursalDao.listaSucursalesQuery("" + id);
        return lista.isEmpty() ? "" : lista.get(0).getNombre();

    }
}
