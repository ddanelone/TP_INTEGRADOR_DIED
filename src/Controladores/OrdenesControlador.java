/*
La lógica de esta clase es compleja. Trataremos de implementarla de la siguiente manera:
En un primer momento, el comboBox de Sucursales de Origen posibles estará deshabilitado. El usuario seleccionará el destino de entrega, 
la fecha de la orden quizás la ponemos con un setTimeStamp(), pero lo resolveremos luego; y el sistema le permitirá elegir productos
en el comboBox correspondiente y agregarlos a la lista; un método irá calculando el peso total; también habrá que calcular el tiempo máximo desde
el momento del envío y ver si las sucursales por las cuales el pedido debe transitar, están abiertas dentro del tiempo útil (un quilombo, básicamente);
fecho, otro método filtrará las sucursales operativas y cuyos caminos están operativos y permiten el flujo de kilogramos; Se habilitará así
la posiblidad de elegir en el comboBox correspondiente la sucursalde origen posible (previo verificar que tengan stock de todos los productos en cantidad
suficiente.
IDEA INICIAL: 
PASO 1: traer el modelo de grafos, vertices y aristas de Martín, y ver la forma de crear un grafo con esa lógica, en base a la información que tenemos de 
sucursales y caminos.
PASO 2: filtrar las sucursales con Stock;
PASO 3: eliminar esos vértices del grafo;
PASO 4: hacer un recorrido en profundidad para encontrar todos los caminos posibles.
PASO 5: presentarle al usuario las opciones posibles, y que ese seleccione una ruta (hay que mostrar el tiempo total de demora).
PASO 6: asignarsela a la tabla ordenes_provision y cambiar el estado a EN PROCESO.
PASO 7: no morir en el intento.
*/
package Controladores;

import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class OrdenesControlador implements ActionListener, MouseListener, KeyListener{
    private SystemView vista;
    

    public OrdenesControlador(SystemView vista) {
        this.vista = vista;
        
        //Ponemos en escucha el Label
        this.vista.jLabelOrdenes.addMouseListener(this);
    
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.jLabelOrdenes) {
            vista.jTabbedPane1.setSelectedIndex(4);
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
