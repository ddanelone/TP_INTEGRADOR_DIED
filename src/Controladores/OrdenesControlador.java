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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class OrdenesControlador implements ActionListener, MouseListener, KeyListener {

    private SystemView vista;
    private Ordenes orden;
    private OrdenesDao ordenDao;

    DefaultTableModel modeloProductos = new DefaultTableModel();
    DefaultTableModel modeloOrdenes = new DefaultTableModel();
    DefaultTableModel temporal = new DefaultTableModel();
    private int id_sucursal_destino = 0;
    private double peso_total = 0.0;
    private LocalDate fechaActual;

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
        fechaActual = LocalDate.now();
        this.vista.txt_ordenes_fecha.setText(fechaActual.toString());
        //Deshabilitamos los botones que de entrada deben estar fuera de servicio
        this.vista.btn_ordenes_producto_eliminar.setEnabled(false);
        vista.btn_ordenes_eliminar.setEnabled(false);
        vista.btn_ordenes_modificar.setEnabled(false);

        this.getElectroNombre();
        this.getSucursalDestinoNombre();
       
        //Ponemos en escucha el Label
        this.vista.jLabelOrdenes.addMouseListener(this);
        //Ponemos la tabla de ordenes a la escucha
        this.vista.tabla_ordenes.addMouseListener(this);
        //Botón de confirmar Orden
        this.vista.btn_ordenes_confirmar.addActionListener(this);
        //Botón de agregar producot a la orden
        this.vista.btn_ordenes_producto_agregar.addActionListener(this);
        //Botón de cancelar
        this.vista.btn_ordenes_cancelar.addActionListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_ordenes_producto_agregar) {
            //Verificamos que tengamos ya completados la sucursal de destino, fecha y tiempo máximo.
            if (vista.txt_ordenes_tiempo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "No puede agregar productos a la orden antes de especificar un tiempo de la orden");
                vista.cmb_ordenes_sucursal_destino.requestFocus();
            } else {
                vista.cmb_ordenes_sucursal_destino.setEnabled(false);
                DynamicComboBox electro_cmb = (DynamicComboBox) vista.cmb_ordenes_producto.getSelectedItem();
                int electro_id = electro_cmb.getId();
                if (vista.txt_ordenes_cantidad_producto.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "La Cantidad es obligatoria");
                    vista.txt_ordenes_cantidad_producto.requestFocus();
                } else {
                    //Realizar la inserción en la tabla de electrodomesticos
                    String nombre = electro_cmb.getNombre();
                    int cantidad = Integer.parseInt(vista.txt_ordenes_cantidad_producto.getText().trim());

                    temporal = (DefaultTableModel) vista.tabla_ordenes_productos.getModel();
                    for (int i = 0; i < vista.tabla_ordenes_productos.getRowCount(); i++) {
                        if (vista.tabla_ordenes_productos.getValueAt(i, 1).equals(nombre)) {
                            JOptionPane.showMessageDialog(null, "El producto ya esta agregado en la tabla de compras");
                            return;
                        }
                    }
                    ArrayList lista = new ArrayList();
                    lista.add(electro_id);
                    lista.add(nombre);
                    lista.add(cantidad);
                    peso_total += obtenerPesoProductoId(electro_id) * cantidad;
                    lista.add(obtenerPesoProductoId(electro_id) * cantidad);

                    Object[] obj = new Object[4];
                    obj[0] = lista.get(0);
                    obj[1] = lista.get(1);
                    obj[2] = lista.get(2);
                    obj[3] = lista.get(3);
                    temporal.addRow(obj);
                    vista.tabla_ordenes_productos.setModel(temporal);
                    vista.txt_ordenes_cantidad_producto.setText("");
                    vista.cmb_ordenes_producto.requestFocus();
                }
            }
        } else if (e.getSource() == vista.btn_ordenes_confirmar) {
            //asignar los atributos a la orden
            if (temporal.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Debe agregar productos a la orden para poder confirmarla");
            } else {
                id_sucursal_destino = obtenerIdSucursalPorNombre(vista.cmb_ordenes_sucursal_destino.getSelectedItem().toString().trim());
                orden.setSucursalOrigenId(0); //No tengo aun sucursal de origen.
                orden.setCaminoId(0); //Tampoco tengo camino, porque se asignar luego.
                orden.setPesoTotal(peso_total);
                orden.setSucursalDestinoId(id_sucursal_destino);
                orden.setEstado("PENDIENTE");
                orden.setFechaOrden(fechaActual);
                orden.setTiempoMaximo(id_sucursal_destino);
                insertarOrden(orden);
                vista.cmb_ordenes_sucursal_destino.setEnabled(true);
            }
        } else if(e.getSource() == vista.btn_ordenes_cancelar) {
            //Habilitamos todos los botones y limpiamos las tablas y campos
            vista.cmb_ordenes_sucursal_destino.setEnabled(true);
            fechaActual = LocalDate.now();
            this.vista.txt_ordenes_fecha.setText(fechaActual.toString());   
            limpiarTablaTemporal();
            limpiarTablaArticulos();
            limpiarCampos();
        }
    }

    //Listar todos los electros agregados OJO! ES LA TABLA DE PRODUCTOS QUE ESTÁ ASOCIADA A LA ORDEN, BOLUDIN!!!
    public void listarTodosLosElectros(int id_orden) {
        limpiarTablaArticulos();
        //Recupero todos los registros de la tabla y los filtro para dejar lo que correspondan a la orden actual
        List<ProductoCantidad> listaP = produCantDao.listaProductoCantidadQuery("");
        List<ProductoCantidad> lista = new ArrayList<>();
        
        for(ProductoCantidad unProducto: listaP) {
            if (unProducto.getId() == id_orden) 
                lista.add(unProducto);
        } 

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
        vista.tabla_ordenes_productos.setModel(modeloProductos);
    }
    
    //Método pararecuperar y mostrar en pantalla todas las ordenes cargadas. 
    public void listarTodasLasOrdenes() {
        List<Ordenes> lista = ordenDao.listaOrdenesQuery(vista.ordenes_search.getText());
        modeloOrdenes = (DefaultTableModel) vista.tabla_ordenes.getModel();
        Object[] fila = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            fila[0] = lista.get(i).getId();
            fila[1] = lista.get(i).getSucursalOrigenId();
            fila[2] = lista.get(i).getSucursalDestinoId();
            fila[3] = lista.get(i).getPesoTotal();
            fila[4] = lista.get(i).getTiempoMaximo();
            fila[5] = lista.get(i).getEstado();
            modeloOrdenes.addRow(fila);
        }
        vista.tabla_ordenes.setModel(modeloOrdenes);
    }
    

    //Método para agregar la orden. Ojo, actualizamos dos tablas. Crucemos los dedos...
    public void insertarOrden(Ordenes orden) {
        if (ordenDao.registrarOrdenQuery(orden)) {
            int orden_id = ordenDao.recuperarIdUltimaOrden();
            //Si se actualizó correctamente la tabla de ordenes, vamos a actualizar los detalles
            for (int i = 0; i < vista.tabla_ordenes_productos.getRowCount(); i++) {
                int electro_id = Integer.parseInt(vista.tabla_ordenes_productos.getValueAt(i, 0).toString());
                int cantidad = Integer.parseInt(vista.tabla_ordenes_productos.getValueAt(i, 2).toString());

                //Registrar detalles de la orden
                ordenDao.registrarDetalleOrdenQuery(orden_id, electro_id, cantidad);
            }
            JOptionPane.showMessageDialog(null, "Orden de provision cargada correctamente");
            limpiarTablaTemporal();
            limpiarTablaArticulos();
            limpiarTablaOrdenes();
            limpiarCampos();
            listarTodasLasOrdenes();
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar la  orden de provision correctamente");
        }
    }

    public void limpiarTablaTemporal() {
        for (int i = 0; i < temporal.getRowCount(); i++) {
            temporal.removeRow(i);
            i = i - 1;
        }

    }

    public void limpiarTablaArticulos() {
        for (int i = 0; i < modeloProductos.getRowCount(); i++) {
            modeloProductos.removeRow(i);
            i = i - 1;
        }
    }

    public void limpiarTablaOrdenes() {
        for (int i = 0; i < modeloOrdenes.getRowCount(); i++) {
            modeloOrdenes.removeRow(i);
            i = i - 1;
        }
    }

    public void limpiarCampos() {
        vista.txt_ordenes_tiempo.setText("");
        vista.txt_ordenes_cantidad_producto.setText("");
    }

    //Metodo para mostrar el nombre de los Electrodomésticos
    public void getElectroNombre() {
        List<Electrodomesticos> lista = electroDao.listaElectrodomesticosQuery("");
        for (int i = 0; i < lista.size(); i++) {
            int id = lista.get(i).getId();
            String nombre = lista.get(i).getNombre();
            vista.cmb_ordenes_producto.addItem(new DynamicComboBox(id, nombre));
        }
    }

    //Metodo para mostrar las sucursales de Destino
    public void getSucursalDestinoNombre() {
        List<Sucursales> lista = sucursalDao.listaSucursalesQuery("");
        for (int i = 0; i < lista.size(); i++) {
            int id = lista.get(i).getId();
            String nombre = lista.get(i).getNombre();
            vista.cmb_ordenes_sucursal_destino.addItem(new DynamicComboBox(id, nombre));
        }
    }

    private String obtenerNombreProductoId(int id) {
        List<Electrodomesticos> lista = electroDao.listaElectrodomesticosQuery("");
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                return lista.get(i).getNombre();
            }
        }
        return "";
    }

    private Double obtenerPesoProductoId(int id) {
        List<Electrodomesticos> lista = electroDao.listaElectrodomesticosQuery("");
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                return lista.get(i).getPesoKg();
            }
        }
        return 0.0;
    }

    private int obtenerIdSucursalPorNombre(String nombreSucursal) {
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");

        for (Sucursales sucursal : listaSucursales) {
            if (sucursal.getNombre().trim().equals(nombreSucursal.trim())) {
                return sucursal.getId();
            }
        }
        return -1; // Retorna un valor por defecto si no se encuentra la sucursal
    }
    
    private String obtenerNombreSucursal(int id) {
         List<Sucursales> lista = sucursalDao.listaSucursalesQuery("");
         for(Sucursales unaSucursal: lista) {
             if(unaSucursal.getId()==id)
                 return unaSucursal.getNombre().trim();
         }
         return "";
    }
    
    private String obtenerFechaOrden(int id) {
        List<Ordenes> lista = ordenDao.listaOrdenesQuery("");
        for (Ordenes unaOrden: lista) {
            if (unaOrden.getId()==id)
                return unaOrden.getFechaOrden().toString();
        }
        return "";
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.jLabelOrdenes) {
            vista.jTabbedPane1.setSelectedIndex(4);
        } else if (e.getSource() == vista.tabla_ordenes) {
            //Recupero en el comboBox la Sucursal de Destino, la fecha, el tiempo y la lista de productos.
            int fila = vista.tabla_ordenes.rowAtPoint(e.getPoint());
            //Método para recupear el nombre de la sucursal en el comboBox
            int id_suc = (int) vista.tabla_ordenes.getValueAt(fila,2);        
            //JOptionPane.showMessageDialog(null, obtenerNombreSucursal(id_suc));
            String nombreSucursalSeleccionada = obtenerNombreSucursal(id_suc);
            for (int i = 0; i < vista.cmb_ordenes_sucursal_destino.getItemCount(); i++) {
                Object item = vista.cmb_ordenes_sucursal_destino.getItemAt(i);
                if (item instanceof DynamicComboBox) {
                    DynamicComboBox comboBoxItem = (DynamicComboBox) item;
                    if (comboBoxItem.getNombre().equals(nombreSucursalSeleccionada)) {
                        vista.cmb_ordenes_sucursal_destino.setSelectedIndex(i);
                        break;
                    }
                }
            }
            vista.txt_ordenes_id.setText(vista.tabla_ordenes.getValueAt(fila,0).toString());
            int id_orden = (int) vista.tabla_ordenes.getValueAt(fila,0);
            vista.txt_ordenes_fecha.setText(obtenerFechaOrden(id_orden));
            vista.txt_ordenes_tiempo.setText(vista.tabla_ordenes.getValueAt(fila, 3).toString());
            listarTodosLosElectros(id_orden);
            vista.btn_ordenes_modificar.setEnabled(true);
            vista.btn_ordenes_eliminar.setEnabled(true);
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
