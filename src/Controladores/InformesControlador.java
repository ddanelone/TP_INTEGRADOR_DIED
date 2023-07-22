package Controladores;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.GrafoCaminos;
import Modelos.Graph;
import Modelos.MaxEnvioCalculator;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Modelos.Vertex;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class InformesControlador implements ActionListener, MouseListener {

    private SystemView vista;
    private Graph grafo;
    private MaxEnvioCalculator calculator;

    //Instanciamos el modelo de Caminos;
    Caminos camino = new Caminos();
    CaminosDao caminoDao = new CaminosDao();

    //Instanciamos el modelo de Sucursales
    Sucursales sucursal = new Sucursales();
    SucursalesDao sucursalDao = new SucursalesDao();
    
    private List<Caminos> caminos = new ArrayList<>();
    private List<Sucursales> sucursales = new ArrayList<>();
       
    public InformesControlador(SystemView vista) {
        this.vista = vista;
        //Botón de flujo máximo
        this.vista.btn_informes_flujo_maximo.addActionListener(this);
        //Ponemos el botón en escucha el botón de Page Rank
        this.vista.btn_informes_page_rank.addActionListener(this);
        //Ponemos en escucha el Label
        this.vista.jLabelInformes.addMouseListener(this);
        //Botón cancelar
        this.vista.btn_informes_page_limpiar.addActionListener(this);
        this.actualizarGrafo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btn_informes_flujo_maximo) {
            actualizarGrafo();
            // Calcular el máximo envío en kilos desde la sucursal puerto al centro
            int maxEnvio = calculator.getMaxFlow();
            vista.txt_areat_informes.setText("Flujo Máximo posible entre el Puerto y la Sucursal Centro: " + maxEnvio + " Kg");
        } else if (e.getSource() == vista.btn_informes_page_rank) {
            actualizarGrafo();
            double damping = Double.parseDouble(vista.txt_informes_factorA.getText().trim());
            int iteraciones = Integer.parseInt(vista.txt_informes_cantI.getText().trim());
            vista.txt_areat_informes.setText(formatPageRank(grafo.calculatePageRank(damping, iteraciones)));
        } else if (e.getSource() == vista.btn_informes_page_limpiar) {
            vista.txt_areat_informes.setText("Seleccione un algoritmo para mostrar su resultado en pantala.\n\n Los valores indicados en 'Factor de Amortiguación' y "
                    + "'Cantidad iteraciones' serán utilizados para el cálculo del PageRank (R). \n\n"
                    + "Puede modificar el estado Operativo/No Operativo de Sucursales y el estado Habilitado/No Habilitado de Caminos en el\n\n" 
                    + "menú de Administración Rápido o en el ABM de cada entidad.");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.jLabelInformes) {
            vista.jTabbedPane1.setSelectedIndex(6);
        }
    }

    //Método para formatear la salida del PageRank y asignarlo a la pantalla.
    public String formatPageRank(List<Vertex> pageRankVertices) {
        int i = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("PageRank (R)  :  Sucursal\n"); // Encabezado
        sb.append("=========================\n\n");
        for (Vertex vertex : pageRankVertices) {
            String nombreSuc = null;
            for (Sucursales unaSucursal : sucursales) {
                if (unaSucursal.getId() == vertex.getValue()) {
                    nombreSuc = unaSucursal.getNombre();
                }
            }
            sb.append("#" + i+" - ")
                    .append(nombreSuc)
                    .append(": ")
                    .append(vertex.getPageRank())
                    .append("\n");
            i++;
        }

        return sb.toString();
    }
    
    // Método para actualizar el grafo con la información actualizada de caminos y sucursales
    private void actualizarGrafo() {
        List<Caminos> caminos = caminoDao.listaCaminosQuery("");
        List<Sucursales> sucursales = sucursalDao.listaSucursalesQuery("");

        GrafoCaminos grafoCamino = new GrafoCaminos(caminos, sucursales);
        this.grafo = grafoCamino.getGrafo();

        // Crear una nueva instancia de MaxEnvioCalculator con el grafo actualizado
        this.calculator = new MaxEnvioCalculator(grafo, 1, 11);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
