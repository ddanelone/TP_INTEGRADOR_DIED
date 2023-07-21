package Controladores;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.GrafoCaminos;
import Modelos.Graph;
import Modelos.Vertex;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

public class InformesControlador implements ActionListener, MouseListener {
    private SystemView vista;
    
    //Instanciamos el modelo de Caminos;
    Caminos camino = new Caminos();
    CaminosDao caminoDao = new CaminosDao();
    
    //recuperamos una lista de caminos, y se la pasamos al instanciar GrafoCaminos
    GrafoCaminos grafoCamino = new GrafoCaminos(caminoDao.listaCaminosQuery(""));
    Graph grafo = grafoCamino.getGrafo();

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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vista.btn_informes_flujo_maximo) {
             //Recorrido en profundidad: ok   
            //grafo.bfs(grafo.getVertex(1));
            //grafo.getAccessibleVertices(3)
            //ver los vértices a los cuales puedo acceder desde un vertice determinado: ok
            //grafo.bfs(grafo.getVertex(6)).toString()
            
            
            vista.txt_areat_informes.setText(grafo.calculatePageRank(0.85, 20).toString());
            //JOptionPane.showMessageDialog(null, grafo.getAccessibleVertices(3));
        } else if (e.getSource()== vista.btn_informes_page_rank) {
            double damping = Double.parseDouble(vista.txt_informes_factorA.getText().trim());
            int iteraciones = Integer.parseInt(vista.txt_informes_cantI.getText().trim());
            vista.txt_areat_informes.setText(formatPageRank(grafo.calculatePageRank(damping, iteraciones)));
        }else if (e.getSource()== vista.btn_informes_page_limpiar) {
            vista.txt_areat_informes.setText("Seleccione un algoritmo para mostrar su resultado en pantala.\n\n Los valores indicados en 'Factor de Amortiguación' y "
                    + "'Cantidad iteraciones' serán utilizados para el cálculo del PageRank (R).");
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
    StringBuilder sb = new StringBuilder();
    sb.append("PageRank (R) : Sucursal\n"); // Encabezado
    sb.append("=========================\n\n");

    for (Vertex vertex : pageRankVertices) {
        sb.append("Sucursal ")
          .append(vertex.getValue())
          .append(": ")
          .append(vertex.getPageRank())
          .append("\n");
    }

    return sb.toString();
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
