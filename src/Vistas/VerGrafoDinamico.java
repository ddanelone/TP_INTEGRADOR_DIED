package Vistas;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.GrafoCaminos;
import Modelos.Graph;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import javax.swing.WindowConstants;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VerGrafoDinamico extends javax.swing.JFrame {
    
     // Variable booleana para indicar si la ventana del grafo está abierta
    private static boolean ventanaAbierta = false;

    public VerGrafoDinamico() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Grafo de Sucursales y rutas creado dinámicamente");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Crear el grafo y el GrafoPanel
        Graph graph = createGraph(); // Reemplaza createGraph() con el código para crear el grafo
        GrafoPanel grafoPanel = new GrafoPanel(graph);

        // Establecer el tamaño del panel y agregar el GrafoPanel al centro del mismo
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(grafoPanel, BorderLayout.CENTER);

        // Crear el panel para el botón "Cerrar" y establecer el diseño con FlowLayout centrado
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Configurar el botón "Cerrar" y agregarlo al panel del botón
        btn_grafo_vista.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        btn_grafo_vista.setPreferredSize(new Dimension(120, 30));
        btn_grafo_vista.addActionListener(e -> dispose());
        
        buttonPanel.add(btn_grafo_vista);

        // Agregar el panel del botón en la parte inferior del JPanel
        jPanel1.add(buttonPanel, BorderLayout.PAGE_END);

        // Establecer el tamaño de la ventana y centrarla horizontal y verticalmente
        setSize(700, 600);
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
        Sucursales sucursal = new Sucursales();
        SucursalesDao sucursalDao = new SucursalesDao();
        //Instanciar el modelo ProductoCantidad
        //Instanciamos el modelo de Caminos;
        Caminos camino = new Caminos();
        CaminosDao caminoDao = new CaminosDao();
        GrafoCaminos grafoCamino = new GrafoCaminos(caminoDao.listaCaminosQuery(""), sucursalDao.listaSucursalesQuery(""));
        Graph graph = grafoCamino.getGrafo();
        return graph;
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
