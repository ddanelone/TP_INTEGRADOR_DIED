package Vistas;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.Edge;
import Modelos.GrafoCaminos;
import Modelos.Graph;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Modelos.Vertex;
import javax.swing.WindowConstants;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VerGrafoDinamico extends javax.swing.JFrame {

    // Variable booleana para indicar si la ventana del grafo está abierta
    private static boolean ventanaAbierta = false;

    //Instanciar modelo de sucursales y caminos
    Sucursales sucursal = new Sucursales();
    SucursalesDao sucursalDao = new SucursalesDao();
    Caminos camino = new Caminos();
    CaminosDao caminoDao = new CaminosDao();

    public VerGrafoDinamico() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Grafo de Sucursales y rutas creado dinámicamente");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Crear el grafo y el GrafoPanel
        //Graph graph = createGraph();
        Graph graph = createGraphWithPageRank();
        GrafoPanel grafoPanel = new GrafoPanel(graph);

        // Establecer el tamaño del panel y agregar el GrafoPanel al centro del mismo
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(grafoPanel, BorderLayout.CENTER);

        // Crear el panel para el botón "Cerrar" y establecer el diseño con FlowLayout centrado
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Configurar el botón "Cerrar" y agregarlo al panel del botón
        btn_grafo_vista.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        btn_grafo_vista.setPreferredSize(new Dimension(120, 30));
        btn_grafo_vista.addActionListener(e -> dispose());

        buttonPanel.add(btn_grafo_vista);

        // Agregar el panel del botón en la parte inferior del JPanel
        jPanel1.add(buttonPanel, BorderLayout.PAGE_END);

        // Establecer el tamaño de la ventana y centrarla horizontal y verticalmente
        setSize(1200, 600);
        SwingUtilities.invokeLater(() -> setLocationRelativeTo(null));
    }

    // Método para centrar el JFrame en la pantalla
    private void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();
        window.setLocation((screenSize.width - windowWidth) / 2, (screenSize.height - windowHeight) / 2);
    }

    // Método para crear el grafo, reemplaza este método con el código para crear el grafo
    private Graph createGraph() {
        GrafoCaminos grafoCamino = new GrafoCaminos(caminoDao.listaCaminosQuery(""), sucursalDao.listaSucursalesQuery(""));
        Graph graph = grafoCamino.getGrafo();

        // Paso 1: Crear un conjunto para almacenar los vértices únicos
        Set<Vertex> uniqueVertices = new HashSet<>();

        // Paso 2: Recorrer todas las aristas y agregar los vértices al conjunto
        for (Edge edge : graph.getEdges()) {
            uniqueVertices.add(edge.getOrigin());
            uniqueVertices.add(edge.getEnd());
        }

        // Paso 3: Crear un nuevo grafo y agregar los vértices únicos
        Graph filteredGraph = new Graph();
        for (Vertex vertex : uniqueVertices) {
            filteredGraph.addVertex(vertex);
        }
        
        // Paso 4: Recorrer nuevamente todas las aristas y agregar solo aquellas que conecten vértices presentes en el nuevo grafo
        for (Edge edge : graph.getEdges()) {
            Vertex origin = edge.getOrigin();
            Vertex end = edge.getEnd();

            if (filteredGraph.getVertex().contains(origin) && filteredGraph.getVertex().contains(end)) {
                filteredGraph.addEdge(origin, end, edge.getValue());
            }
        }
        //Y si lo ordenamos por page rank????
        return filteredGraph;
    }
    
      private Graph createGraphWithPageRank() {
        GrafoCaminos grafoCamino = new GrafoCaminos(caminoDao.listaCaminosQuery(""), sucursalDao.listaSucursalesQuery(""));
        Graph graph = grafoCamino.getGrafo();

        // Paso 1: Crear un conjunto para almacenar los vértices únicos
        Set<Vertex> uniqueVertices = new HashSet<>();

        // Paso 2: Recorrer todas las aristas y agregar los vértices al conjunto
        for (Edge edge : graph.getEdges()) {
            uniqueVertices.add(edge.getOrigin());
            uniqueVertices.add(edge.getEnd());
        }

        // Paso 3: Crear un nuevo grafo y agregar los vértices únicos
        Graph filteredGraph = new Graph();
        for (Vertex vertex : uniqueVertices) {
            filteredGraph.addVertex(vertex);
        }
        
        // Paso 4: Recorrer nuevamente todas las aristas y agregar solo aquellas que conecten vértices presentes en el nuevo grafo
        for (Edge edge : graph.getEdges()) {
            Vertex origin = edge.getOrigin();
            Vertex end = edge.getEnd();

            if (filteredGraph.getVertex().contains(origin) && filteredGraph.getVertex().contains(end)) {
                filteredGraph.addEdge(origin, end, edge.getValue());
            }
        }

        // Obtener la lista de vértices ordenados por PageRank
        List<Vertex> sortedVertices = calculatePageRank(0.85, 100, filteredGraph);

        // Actualizar el grafo con la nueva lista ordenada de vértices
        filteredGraph.updateVertices(sortedVertices);

        return filteredGraph;
    }

    private List<Vertex> calculatePageRank(double dampingFactor, int iterations, Graph graph) {
        // Asignar valor inicial de 1.0 a cada vértice
        for (Vertex vertex : graph.getVertex()) {
            vertex.setPageRank(1.0);
        }

        // Realizar las iteraciones para calcular el PageRank
        for (int i = 0; i < iterations; i++) {
            // Copiar los valores de PageRank actuales para cada vértice
            Map<Vertex, Double> currentPageRanks = new HashMap<>();
            for (Vertex vertex : graph.getVertex()) {
                currentPageRanks.put(vertex, vertex.getPageRank());
            }

            // Calcular los nuevos PageRank en esta iteración
            for (Vertex vertex : graph.getVertex()) {
                double sum = 0.0;
                List<Vertex> neighbours = graph.getNeighbours(vertex);
                for (Vertex neighbour : neighbours) {
                    int outDegree = graph.gradoSalida(neighbour);
                    if (outDegree > 0) {
                        sum += currentPageRanks.get(neighbour) / outDegree;
                    }
                }
                double newPageRank = (1 - dampingFactor) + dampingFactor * sum;
                vertex.setPageRank(newPageRank);
            }
        }

        // Usar un conjunto HashSet para evitar duplicados y mantener el orden según el PageRank
        Set<Vertex> uniqueVertices = new HashSet<>(graph.getVertex());

        // Convertir el conjunto en una lista y ordenarla según el PageRank descendente
        List<Vertex> sortedVertices = new ArrayList<>(uniqueVertices);
        sortedVertices.sort((v1, v2) -> Double.compare(v2.getPageRank(), v1.getPageRank()));

        return sortedVertices;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btn_grafo_vista = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_grafo_vista.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_grafo_vista.setText("Cerrar");
        btn_grafo_vista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_grafo_vistaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(22, Short.MAX_VALUE)
                                .addComponent(btn_grafo_vista, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(204, 204, 204))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btn_grafo_vista, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    private void btn_grafo_vistaActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == btn_grafo_vista) {
            dispose();
        }
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btn_grafo_vista;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration                   
}
