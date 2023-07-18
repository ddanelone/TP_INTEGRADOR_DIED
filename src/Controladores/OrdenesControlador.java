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
sucursales y caminos. X
PASO 2: filtrar las sucursales con Stock;
PASO 3: eliminar esos vértices del grafo;
PASO 4: hacer un recorrido en profundidad para encontrar todos los caminos posibles.
PASO 5: presentarle al usuario las opciones posibles, y que ese seleccione una ruta (hay que mostrar el tiempo total de demora).
PASO 6: asignarsela a la tabla ordenes_provision y cambiar el estado a EN PROCESO.
PASO 7: no morir en el intento.
*/
package Controladores;

import Modelos.DynamicComboBox;
import Modelos.Electrodomesticos;
import Modelos.ElectrodomesticosDao;
import Modelos.Ordenes;
import Modelos.OrdenesDao;
import Modelos.ProductoCantidad;
import Modelos.ProductoCantidadDao;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class OrdenesControlador implements ActionListener, MouseListener, KeyListener{
    private SystemView vista;
    private Ordenes orden;
    private OrdenesDao ordenDao;
    
    DefaultTableModel modeloProductos = new DefaultTableModel();
    DefaultTableModel modeloOrdenes = new DefaultTableModel();
    DefaultTableModel temporal = new DefaultTableModel();    
    private int id_orden = 0;
    
    
    //Instanciar el modelo de electrodomésticos
    Electrodomesticos electro = new Electrodomesticos();
    ElectrodomesticosDao electroDao = new ElectrodomesticosDao();
    //Instanciar el modelo de Sucursales
    Sucursales sucursal = new Sucursales();
    SucursalesDao sucursalDao = new SucursalesDao();
    //Instanciar el modelo ProductoCantidad
    ProductoCantidad produCant = new ProductoCantidad();
    ProductoCantidadDao produCantDao = new ProductoCantidadDao();
    

    public OrdenesControlador(Ordenes orden, OrdenesDao ordenDao, SystemView vista) {
        this.orden = orden;
        this.ordenDao = ordenDao;
        this.vista = vista;
        this.getElectroNombre();
        this.getSucursalDestinoNombre();
        this.listarTodasLasOrdenes();
        
        //La tabla de órdenes tiene el id autoincremental, así que necesito recuperar todas las órdenes para asignar
        //correctamente el nuevo id de orden a la lógica ---> QUÉ PERNO!!!!
        
        
        
        //Ponemos en escucha el Label
        this.vista.jLabelOrdenes.addMouseListener(this);
        //Escuha en el boton de Agregar electro a la orden
        this.vista.btn_ordenes_producto_agregar.addActionListener(this);
        //Botón de confirmar Orden
        this.vista.btn_ordenes_confirmar.addActionListener(this);
    
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == vista.btn_ordenes_producto_agregar) {
            //Verificamos que tengamos ya completados la sucursal de destino, fecha y tiempo máximo.
            if(vista.txt_ordenes_fecha.getText().equals("") || vista.txt_ordenes_tiempo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "No puede agregar productos a la orden antes de especificar datos de sucursal, fecha y tiempo.");
            } else {
                DynamicComboBox electro_cmb = (DynamicComboBox) vista.cmb_ordenes_producto.getSelectedItem();
                int electro_id = electro_cmb.getId();
                //vista.txt_ordenes_cantidad_producto.requestFocus();
                
                
                listarTodosLosElectros(id_orden);
            }
        }
    }
    
    //Listar todos los electros agregados OJO! ES LA TABLA DE PRODUCTOS QUE ESTÁ ASOCIADA A LA ORDEN, BOLUDIN!!!
    public void listarTodosLosElectros(int id_suc) {
        //Recupero todos los registros de la tabla y los filtro para dejar lo que correspondan a la orden actual
        List<ProductoCantidad> lista = produCantDao.listaProductoCantidadQuery("");
        lista.stream().filter(p-> p.getId() == id_suc).collect(Collectors.toList());
        
        modeloProductos = (DefaultTableModel) vista.tabla_ordenes_productos.getModel();
        Object[] fila = new Object[3];
        for (int i = 0; i < lista.size(); i++) {
            fila[0] = lista.get(i).getProductoId();
            //Tengo que consultar la tabla de productos, para ver la descripcion del que corresponde al Id. Lo tengo en el comboBox...
            int origenId = lista.get(i).getProductoId();
            String nombre = obtenerNombreProductoId(origenId);
            fila[1] = nombre;
            fila[2] = lista.get(i).getCantidad();
            modeloProductos.addRow(fila);
        }

    }
    
    //Método pararecuperar y mostrar en pantalla todas las ordenes cargadas. Lo necesito en el inicio, para tener el número del siguiente id a asignar en mi lógica
    //si es que el usuario va a cargar una orden nueva (necesito el id para la tabla de productos que voy a cargar en cada orden.
    public void listarTodasLasOrdenes() {
        List<Ordenes> lista = ordenDao.listaOrdenesQuery(vista.ordenes_search.getText());
        modeloOrdenes = (DefaultTableModel) vista.tabla_ordenes.getModel();
        Object[] fila = new Object[+5];
        for (int i = 0; i < lista.size(); i++) {
            fila[0] = lista.get(i).getSucursalOrigenId();
            fila[1] = lista.get(i).getSucursalDestinoId();
            // en la clase ProductoCantidadDao -->Hacer un metodo que recorra los productos asociados, busque sus pesos, y los sume. El método lo llamará la clase OrdenesDao. 
            fila[2] = lista.get(i).getClass(); //Necesito los kg, putah bidah 
            fila[3] = lista.get(i).getTiempoMaximo();
            fila[4] = lista.get(i).getEstado();
            modeloOrdenes.addRow(fila);
        }
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
    
    //Metodo para mostrar el nombre de los Electrodomésticos
    public void getElectroNombre() {
        List<Electrodomesticos> lista = electroDao.listaElectrodomesticosQuery("");
        for (int i=0; i< lista.size(); i++) {
            int id=lista.get(i).getId();
            String nombre = lista.get(i).getNombre();
            vista.cmb_ordenes_producto.addItem(new DynamicComboBox(id, nombre));            
        }
    }
    
    //Metodo para mostrar las sucursales de Destino
    public void getSucursalDestinoNombre() {
        List<Sucursales> lista = sucursalDao.listaSucursalesQuery("");
        for (int i=0; i< lista.size(); i++) {
            int id=lista.get(i).getId();
            String nombre = lista.get(i).getNombre();
            vista.cmb_ordenes_sucursal_destino.addItem(new DynamicComboBox(id, nombre));            
        }
    }
    
    private String obtenerNombreProductoId(int id) {
        List<Electrodomesticos> lista = electroDao.listaElectrodomesticosQuery("");
        for (int i=0; i< lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                return lista.get(i).getNombre();
            }
        }
        return "";
    }
       
    
}
