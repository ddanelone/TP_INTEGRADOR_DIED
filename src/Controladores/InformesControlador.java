package Controladores;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.Grafo;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

public class InformesControlador implements ActionListener, MouseListener {

    private Caminos camino;
    private CaminosDao caminoDao;
    private SystemView vista;

    public InformesControlador(Caminos camino, CaminosDao caminoDao, SystemView vista) {
        this.camino = camino;
        this.caminoDao = caminoDao;
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
        //Creo una lista con todos los caminos. Ahí tengo sucursal origen, destno, y capacidad de cada uno
        List<Caminos> lista_caminos = caminoDao.listaCaminosQuery("");
        if (e.getSource() == vista.btn_informes_flujo_maximo) {
            
            JOptionPane.showMessageDialog(null, "Flujo máximo: " + calcularFlujoMaximo(lista_caminos));
        } else if (e.getSource() == vista.btn_informes_page_rank) {
            JOptionPane.showMessageDialog(null, "Algoritmo aún no terminado.");
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

    public int calcularFlujoMaximo(List<Caminos> caminos) {
    // Crear el grafo con el número de nodos adecuado
    int numeroNodos = obtenerNumeroNodos(caminos);
    Grafo grafo = new Grafo(numeroNodos);

    // Agregar las aristas con sus capacidades
    for (Caminos camino : caminos) {
        grafo.agregarArista(camino.getOrigenId(), camino.getDestinoId(), camino.getCapacidad());
    }

    // Calcular el flujo máximo
    int flujoMaximo = grafo.calcularFlujoMaximo(11, 12); // Reemplaza 0 y 1 por los identificadores adecuados de los nodos de origen y destino

    return flujoMaximo;
}

private int obtenerNumeroNodos(List<Caminos> caminos) {
    Set<Integer> nodos = new HashSet<>();
    for (Caminos camino : caminos) {
        nodos.add(camino.getOrigenId());
        nodos.add(camino.getDestinoId());
    }
    return nodos.size();
}


}
