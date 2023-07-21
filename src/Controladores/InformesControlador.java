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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vista.btn_informes_flujo_maximo) {
            JOptionPane.showMessageDialog(null, grafo.getAccessibleVertices(3));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.jLabelInformes) {
            vista.jTabbedPane1.setSelectedIndex(6);
        }
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
