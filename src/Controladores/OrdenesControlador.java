package Controladores;

import Modelos.CaminoSeleccionado;
import Modelos.CaminoSeleccionadoDao;
import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.DynamicComboBox;
import Modelos.Electrodomesticos;
import Modelos.ElectrodomesticosDao;
import Modelos.GrafoCaminos;
import Modelos.Graph;
import Modelos.Ordenes;
import Modelos.OrdenesDao;
import Modelos.ProductoCantidad;
import Modelos.ProductoCantidadDao;
import Modelos.Stock;
import Modelos.StockDao;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Modelos.Vertex;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class OrdenesControlador implements ActionListener, MouseListener {

    private SystemView vista;
    private Ordenes orden;
    private OrdenesDao ordenDao;
    private Object[] options = {"Sí", "No"};
    private GrafoCaminos grafoCamino;
    private Graph grafo;

    DefaultTableModel modeloProductos = new DefaultTableModel();
    DefaultTableModel modeloOrdenes = new DefaultTableModel();
    DefaultTableModel modeloCaminos = new DefaultTableModel();
    private int id_sucursal_destino = 0;
    private double peso_total = 0.0;
    private int id_orden = 0;
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
    //Instanciamos el modelo de Caminos;
    Caminos camino = new Caminos();
    CaminosDao caminoDao = new CaminosDao();
    //Instanciar el modelo de Stock
    Stock stock = new Stock();
    StockDao stockDao = new StockDao();

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
        //Tabla de productos en la orden a la escucha.
        this.vista.tabla_ordenes_productos.addMouseListener(this);
        //Botón de confirmar Orden
        this.vista.btn_ordenes_crear.addActionListener(this);
        //Botón de agregar producot a la orden
        this.vista.btn_ordenes_producto_agregar.addActionListener(this);
        //Botón de cancelar
        this.vista.btn_ordenes_cancelar.addActionListener(this);
        //Botón de eliminar
        this.vista.btn_ordenes_eliminar.addActionListener(this);
        //Boton Elliminar
        this.vista.btn_ordenes_modificar.addActionListener(this);
        //Botón eliminar producto de la orden
        this.vista.btn_ordenes_producto_eliminar.addActionListener(this);
        //Pongo a escuchar la tabla de caminos
        this.vista.tabla_ordenes_caminos.addMouseListener(this);
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

                    modeloProductos = (DefaultTableModel) vista.tabla_ordenes_productos.getModel();
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
                    modeloProductos.addRow(obj);
                    vista.tabla_ordenes_productos.setModel(modeloProductos);
                    vista.txt_ordenes_cantidad_producto.setText("");
                    vista.cmb_ordenes_producto.requestFocus();
                }
            }
        } else if (e.getSource() == vista.btn_ordenes_producto_eliminar) {
            int col = vista.tabla_ordenes_productos.getSelectedRow();
            int id = Integer.parseInt(vista.tabla_ordenes_productos.getValueAt(col, 0).toString());
            int confirmacion = JOptionPane.showOptionDialog(null, "¿Seguro de eliminar este electrodoméstico de la Orden de Provisión?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (confirmacion == 0) {
                // Eliminar el item del modelo de la tabla
                DefaultTableModel modeloTabla = (DefaultTableModel) vista.tabla_ordenes_productos.getModel();
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    int idProductoEnModelo = (int) modeloTabla.getValueAt(i, 0);
                    if (idProductoEnModelo == id) {
                        peso_total -= obtenerPesoProductoId(id) * (int) modeloTabla.getValueAt(i, 2);
                        modeloTabla.removeRow(i);
                        break;
                    }
                }
                vista.btn_ordenes_producto_agregar.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Electrodoméstico eliminado exitósamente de la Orden de Provisión");
            }
        } else if (e.getSource() == vista.btn_ordenes_crear) {
            //asignar los atributos a la orden
            if (modeloProductos.getRowCount() == 0) {
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
                id_sucursal_destino=0;
            }
        } else if (e.getSource() == this.vista.btn_ordenes_modificar) {
            //Verificamos si la tabla de electrodomesticos y el campo de tiempo están vacíos
            if (vista.txt_ordenes_tiempo.getText().equals("")
                    || modeloProductos.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Faltan datos. Todos los campos son obligatorios");
            } else {
                //Realizar la actualización
                orden.setId(Integer.parseInt(vista.txt_ordenes_id.getText().trim()));
                id_sucursal_destino = obtenerIdSucursalPorNombre(vista.cmb_ordenes_sucursal_destino.getSelectedItem().toString().trim());
                orden.setSucursalOrigenId(0); //No tengo aun sucursal de origen.
                orden.setCaminoId(0); //Tampoco tengo camino, porque se asignar luego.
                orden.setPesoTotal(peso_total);
                orden.setSucursalDestinoId(id_sucursal_destino);
                orden.setEstado("PENDIENTE");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fechaOrden = LocalDate.parse(vista.txt_ordenes_fecha.getText().trim(), formatter);
                orden.setFechaOrden(fechaOrden);
                orden.setTiempoMaximo((int) Double.parseDouble(vista.txt_ordenes_tiempo.getText().trim()));
                vista.btn_ordenes_producto_eliminar.setEnabled(false);
                vista.btn_ordenes_crear.setEnabled(true);
                vista.btn_ordenes_modificar.setEnabled(false);
                vista.btn_ordenes_eliminar.setEnabled(false);
                id_sucursal_destino=0;
                modificarOrden(orden);
                limpiarTablas(modeloProductos);
                limpiarTablas(modeloCaminos);
                limpiarCampos();
            }
        } else if (e.getSource() == this.vista.btn_ordenes_eliminar) {
            int fila = vista.tabla_ordenes.getSelectedRow();
            int id = Integer.parseInt(vista.tabla_ordenes.getValueAt(fila, 0).toString());
            int confirmacion = JOptionPane.showOptionDialog(null, "¿Seguro de elminar esta Orden de Provisión?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (confirmacion == 0 && ordenDao.borrarOrdenQuery(id) != false) {
                limpiarCampos();
                limpiarTablas(modeloOrdenes);
                limpiarTablas(modeloProductos);
                vista.btn_ordenes_crear.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Orden de Provisión eliminado exitosamente");
                listarTodasLasOrdenes();
                vista.btn_ordenes_modificar.setEnabled(false);
                vista.btn_ordenes_eliminar.setEnabled(false);
            }
        } else if (e.getSource() == vista.btn_ordenes_cancelar) {
            //Habilitamos todos los botones y limpiamos las tablas y campos
            refrescar();
            fechaActual = LocalDate.now();
            this.vista.txt_ordenes_fecha.setText(fechaActual.toString());
            limpiarTablas(modeloProductos);
            limpiarTablas(modeloCaminos);
            limpiarCampos();
        }
    }

    private void refrescar() {
        vista.cmb_ordenes_sucursal_destino.setEnabled(true);
        vista.btn_ordenes_crear.setEnabled(true);
        vista.btn_ordenes_modificar.setEnabled(false);
        vista.btn_ordenes_eliminar.setEnabled(false);
        vista.btn_ordenes_producto_eliminar.setEnabled(false);
        vista.btn_ordenes_producto_agregar.setEnabled(true);
        vista.tabla_ordenes_caminos.setEnabled(false);
    }

    private DefaultTableModel tablaModelo() {
        //Recupero todos los registros de la tabla y los filtro para dejar lo que correspondan a la orden actual
        List<ProductoCantidad> listaP = produCantDao.listaProductoCantidadQuery("");
        List<ProductoCantidad> lista = listaP.stream().filter(p -> p.getId() == id_orden).collect(Collectors.toList());
        modeloProductos = (DefaultTableModel) vista.tabla_ordenes_productos.getModel();
        Object[] fila = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            fila[0] = lista.get(i).getProductoId();
            //Tengo que consultar la tabla de productos, para ver la descripcion del que corresponde al Id. Lo tengo en el comboBox...
            int origenId = lista.get(i).getProductoId();
            String nombre = obtenerNombreProductoId(origenId);
            fila[1] = nombre;
            fila[2] = lista.get(i).getCantidad();
            fila[3] = obtenerPesoProductoId(lista.get(i).getProductoId()) * lista.get(i).getCantidad();
            modeloProductos.addRow(fila);
        }
        return modeloProductos;
    }

    //Listar todos los electros agregados a la orden
    public void listarTodosLosElectros(int id_orden) {
        limpiarTablas(modeloProductos);
        vista.tabla_ordenes_productos.setModel(tablaModelo());
    }

    //Método pararecuperar y mostrar en pantalla todas las ordenes cargadas. 
    public void listarTodasLasOrdenes() {
        List<Ordenes> lista = ordenDao.listaOrdenesQuery("");
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
            limpiarTablas(modeloProductos);
            limpiarTablas(modeloOrdenes);
            limpiarCampos();
            listarTodasLasOrdenes();
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo cargar la  orden de provision correctamente");
        }
    }

    //Método para modifcar la orden. Ojo, actualizamos dos tablas. Crucemos los dedos...
    public void modificarOrden(Ordenes orden) {
        if (ordenDao.modificarOrdenQuery(orden)) {
            int orden_id = orden.getId();
            //Si se actualizó correctamente la tabla de ordenes, vamos a actualizar los detalles
            produCantDao.borrarPoductoCantidadQuery(orden_id);
            for (int i = 0; i < vista.tabla_ordenes_productos.getRowCount(); i++) {
                produCant.setId(orden_id);
                produCant.setProductoId(Integer.parseInt(vista.tabla_ordenes_productos.getValueAt(i, 0).toString()));
                produCant.setCantidad(Integer.parseInt(vista.tabla_ordenes_productos.getValueAt(i, 2).toString()));
                //Registrar detalles de la orden. 
                produCantDao.registrarPoductoCantidadQuery(produCant);
            }
            JOptionPane.showMessageDialog(null, "Orden de provision actualizada correctamente");
            limpiarTablas(modeloProductos);
            limpiarTablas(modeloOrdenes);
            limpiarCampos();
            listarTodasLasOrdenes();
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar la  orden de provision correctamente");
        }
    }

    public void actualizarOrdenProvision(int id_orden) {
        // Recuperar la lista actual de productos asociados a la orden original
        List<ProductoCantidad> lista = produCantDao.listaProductoCantidadQuery("");
        List<ProductoCantidad> listaEnTabla = lista.stream()
                .filter(unProducto -> unProducto.getId() == id_orden)
                .collect(Collectors.toList());

        //Lista actual en memoria
        List<ProductoCantidad> listaActual = new ArrayList<>();
        for (int i = 0; i < vista.tabla_ordenes_productos.getRowCount(); i++) {
            ProductoCantidad prod = new ProductoCantidad();
            prod.setId(id_orden);
            prod.setProductoId(Integer.parseInt(vista.tabla_ordenes_productos.getValueAt(i, 0).toString()));
            prod.setCantidad(Integer.parseInt(vista.tabla_ordenes_productos.getValueAt(i, 2).toString()));
            listaActual.add(prod);
        }
        // Comparar las listas de productos actualizada y actual, e identifico productos a agregar o eliminar
        List<ProductoCantidad> productosEliminar = listaEnTabla.stream()
                .filter(unProducto -> !listaActual.contains(unProducto))
                .collect(Collectors.toList());

        List<ProductoCantidad> productosAgregar = listaEnTabla.stream()
                .filter(unProducto -> !listaActual.contains(unProducto))
                .collect(Collectors.toList());

        // Eliminar productos de la tabla de detalle de electrodomésticos
        for (ProductoCantidad unProducto : productosEliminar) {
            if (!produCantDao.borrarPoductoCantidadQuery(unProducto.getId())) {
                JOptionPane.showMessageDialog(null, "No se ha podido ejecutar la operación de actualizacíón (BORRADO) de " + unProducto.getId());
            }
        }

        // Agregar productos a la tabla de detalle de electrodomésticos
        for (ProductoCantidad unProducto : productosAgregar) {
            if (!produCantDao.registrarPoductoCantidadQuery(unProducto)) {
                JOptionPane.showMessageDialog(null, "No se ha podido ejecutar la operación de actualizacíón (AGREGADO) de " + unProducto.getId());
            }
        }
        // Actualizar la orden de provisión
        for (ProductoCantidad unProducto : listaActual) {
            if (!produCantDao.modificarPoductoCantidadQuery(unProducto)) {
                JOptionPane.showMessageDialog(null, "No se ha podido ejecutar la operación de actualizacíón (MODIFICACION)de " + unProducto.getId());
            }
        }
    }

    public void limpiarTablas(DefaultTableModel modelo) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    public void limpiarCampos() {
        vista.txt_ordenes_tiempo.setText("");
        vista.txt_ordenes_cantidad_producto.setText("");
        id_orden =0;
        peso_total = 0.0;
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

    private String obtenerNombreElectro(int id) {
        List<Electrodomesticos> lista = electroDao.listaElectrodomesticosQuery("");
        return lista.stream()
                .filter(unaSucursal -> unaSucursal.getId() == id)
                .map(unaSucursal -> unaSucursal.getNombre().trim())
                .findFirst()
                .orElse("");
    }

    private String obtenerNombreSucursal(int id) {
        List<Sucursales> lista = sucursalDao.listaSucursalesQuery("");
        return lista.stream()
                .filter(unaSucursal -> unaSucursal.getId() == id)
                .map(unaSucursal -> unaSucursal.getNombre().trim())
                .findFirst()
                .orElse("");
    }

    private String obtenerFechaOrden(int id) {
        List<Ordenes> lista = ordenDao.listaOrdenesQuery("");
        for (Ordenes unaOrden : lista) {
            if (unaOrden.getId() == id) {
                return unaOrden.getFechaOrden().toString();
            }
        }
        return "";
    }

    private DefaultTableModel tablaModeloCaminos(List<List<Integer>> caminos, List<Integer> tiempo) {
        modeloCaminos = (DefaultTableModel) vista.tabla_ordenes_caminos.getModel();
        Object[] col = new Object[4];
        for (int j = 0; j < caminos.size(); j++) {
            List<Integer> camino = caminos.get(j);
            int tiempoTotal = tiempo.get(j);

            col[0] = camino.get(0); // id de la sucursal de origen
            String recorrido = "";
            for (int i = 0; i < camino.size(); i++) {
                recorrido += camino.get(i) + "->";
            }
            col[1] = recorrido;
            col[2] = camino.get(camino.size() - 1); // id de la sucursal de destino
            col[3] = tiempoTotal;

            modeloCaminos.addRow(col);
        }
        return modeloCaminos;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.jLabelOrdenes) {
            vista.jTabbedPane1.setSelectedIndex(4);
        } else if (e.getSource() == vista.tabla_ordenes) {
            // necesito una variable para la sucursal de destino
            limpiarTablas(modeloCaminos);
            int id_suc;
            //Recupero en el comboBox la Sucursal de Destino, la fecha, el tiempo y la lista de productos.
            int fila = vista.tabla_ordenes.rowAtPoint(e.getPoint());
            //Método para recupear el nombre de la sucursal en el comboBox
            id_suc = (int) vista.tabla_ordenes.getValueAt(fila, 2);
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
            vista.txt_ordenes_id.setText(vista.tabla_ordenes.getValueAt(fila, 0).toString());
            id_orden = (int) vista.tabla_ordenes.getValueAt(fila, 0);
            vista.txt_ordenes_fecha.setText(obtenerFechaOrden(id_orden));
            vista.txt_ordenes_tiempo.setText(vista.tabla_ordenes.getValueAt(fila, 4).toString());
            peso_total = Double.parseDouble(vista.tabla_ordenes.getValueAt(fila, 3).toString());
            listarTodosLosElectros(id_orden);
            vista.btn_ordenes_crear.setEnabled(false);
            vista.btn_ordenes_modificar.setEnabled(true);
            vista.btn_ordenes_eliminar.setEnabled(true);
            //Vamos a ver si la orden está pendiente, si es así, permitimos se le asigne un camino. Caso contrario, nope.
            if (vista.tabla_ordenes.getValueAt(fila, 5).equals("PENDIENTE")) {
                //Lógica para calcular los caminos posibles y ver sucursalde origen
                //Recupear los Stock de sucursales
                List<Stock> listaSucursales = stockDao.listaStockQuery("");
                //Recuperar los items incluidos en el pedido actual
                List<ProductoCantidad> listaProduc = produCantDao.listaProductoCantidadQuery("" + id_orden);
                //Crep el grafo con todas las sucursales!!!!
                grafoCamino = new GrafoCaminos(caminoDao.listaCaminosQuery(""), sucursalDao.listaSucursalesQuery(""));
                grafo = grafoCamino.getGrafo();
                // Obtener una lista con los IDs de los productos presentes en listaProduc
                // Filtrar la lista de sucursales
                List<Stock> sucursalesFiltradas = listaSucursales.stream()
                        .filter(sucursal -> cumpleRequisitos(sucursal, listaProduc))
                        .collect(Collectors.toList());
                //recuperamos una lista de caminos, y se la pasamos al instanciar GrafoCaminos
                List<Sucursales> listaTodasLasSuc = sucursalDao.listaSucursalesQuery("");
                List<Sucursales> listaSuc = sucursalesFiltradas.stream()
                        .flatMap(stock -> listaTodasLasSuc.stream()
                        .filter(sucursal -> stock.getId_sucursal() == sucursal.getId()))
                        .collect(Collectors.toList());
                //Tengo una lista de sucursales que tienen el stock requerido por mi pedido. La itero y agrego los caminos posibles a una lista
                if (listaSuc.isEmpty()) {  //No tengo ninguna sucursal con stock suficiente. Le aviso al usuario.
                    JOptionPane.showMessageDialog(null, "No hay sucursales que puedan atender esta orden. No existen caminos posibles aun");
                    limpiarTablas(modeloCaminos);
                }
                List<List<Vertex>> caminos = listaSuc.stream()
                        .flatMap(unaSucursal -> grafo.findAllPaths(grafo.getVertex(unaSucursal.getId()), grafo.getVertex(id_suc)).stream())
                        .collect(Collectors.toList());
                //Tengo todos los caminos posibles, los voy a pasar una lista de listas de enteros para facilitar la exploración.
                if (caminos.isEmpty()) {  //No tengo ningun camino disponible. Le aviso al usuario.
                    JOptionPane.showMessageDialog(null, "No hay caminos que puedan conectar las sucursales.");
                }
                List<List<Integer>> caminosEnteros = grafo.obtenerCaminosComoListaDeEnteros(caminos);
                //en tiempoTotal tengo una lista de igual cantidad de elementos que caminosEnteros, y tiene el tiempo total de cada viaje. 
                List<Integer> tiempoTotal = calcularTiempoTransito(caminosEnteros);
                //Ahora, a cargar los valores en la tabla, mostrarlos al usuario, pedirle confirmación, y si confirma, grabarlos en la tabla. 
                vista.tabla_ordenes_caminos.setModel(tablaModeloCaminos(caminosEnteros, tiempoTotal));
                vista.tabla_ordenes_caminos.setEnabled(true);
            } else {
                limpiarTablas(modeloCaminos);
            }
        } else if (e.getSource() == vista.tabla_ordenes_productos) {
            //Deshabilitar el botón de agregar
            vista.btn_ordenes_producto_agregar.setEnabled(false);
            //Recupero el producto en el comboBox, y la cantidad 
            int fila = vista.tabla_ordenes_productos.rowAtPoint(e.getPoint());
            //Método para recupear el nombre del  producot en el comboBox
            int id_prod = (int) vista.tabla_ordenes_productos.getValueAt(fila, 0);
            String nombreElectroSeleccionado = obtenerNombreElectro(id_prod);
            for (int i = 0; i < vista.cmb_ordenes_producto.getItemCount(); i++) {
                Object item = vista.cmb_ordenes_producto.getItemAt(i);
                if (item instanceof DynamicComboBox) {
                    DynamicComboBox comboBoxItem = (DynamicComboBox) item;
                    if (comboBoxItem.getNombre().equals(nombreElectroSeleccionado)) {
                        vista.cmb_ordenes_producto.setSelectedIndex(i);
                        break;
                    }
                }
            }
            vista.txt_ordenes_cantidad_producto.setText(String.valueOf(vista.tabla_ordenes_productos.getValueAt(fila, 2)));
            vista.btn_ordenes_producto_eliminar.setEnabled(true);
        } else if (e.getSource() == vista.tabla_ordenes_caminos) {
            int fila = vista.tabla_ordenes_caminos.rowAtPoint(e.getPoint());
            //Instanciamos el modelo de caminos  y su clase Dao
            CaminoSeleccionado caminoSel = new CaminoSeleccionado();
            CaminoSeleccionadoDao caminoSelDao = new CaminoSeleccionadoDao();
            caminoSel.setSucursal_origen_id(Integer.parseInt(vista.tabla_ordenes_caminos.getValueAt(fila, 0).toString().trim()));
            caminoSel.setSucursal_destino_id(Integer.parseInt(vista.tabla_ordenes_caminos.getValueAt(fila, 2).toString().trim()));
            caminoSel.setTiempo(Integer.parseInt(vista.tabla_ordenes_caminos.getValueAt(fila, 3).toString().trim()));
            caminoSel.setOrden_provision_id(id_orden);
            int confirmacion = JOptionPane.showOptionDialog(null, "¿Seguro de asignar esta ruta a la Orden de Provisión?", "Confirmar elección",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (confirmacion == 0 && caminoSelDao.registrarCaminoQuery(caminoSel)) {
                orden.setCaminoId(caminoSelDao.recuperarIdUltimoCamino());
                //Necesito recuperar las órdenes para poder cambiarle el estado luego.
                List<Ordenes> listaOrdenes = ordenDao.listaOrdenesQuery(String.valueOf(id_orden));
                orden = listaOrdenes.isEmpty() ? null : listaOrdenes.get(0);
                orden.setSucursalOrigenId(Integer.parseInt(vista.tabla_ordenes_caminos.getValueAt(fila, 0).toString().trim()));
                orden.setEstado("EN PROCESO");
                if(ordenDao.modificarOrdenQuery(orden)) {
                    JOptionPane.showMessageDialog(null, "Camino asignado exitósamente. La orden ahora se encuentra EN PROCESO");
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(null, "El Camino no ha sido asignado.");                
                }
                    limpiarTablas(modeloCaminos);
                    limpiarTablas(modeloOrdenes);
                    limpiarTablas(modeloProductos);
                    refrescar();                
                    listarTodasLasOrdenes();
            }
        }
    }

    private static boolean cumpleRequisitos(Stock sucursal, List<ProductoCantidad> cantidadesRequeridas) {
        return cantidadesRequeridas.stream()
                .anyMatch(cantidadRequerida -> sucursalTieneCantidad(sucursal, cantidadRequerida));
    }

    private static boolean sucursalTieneCantidad(Stock sucursal, ProductoCantidad cantidadRequerida) {
        int productoId = cantidadRequerida.getProductoId();
        int cantidadRequeridaValor = cantidadRequerida.getCantidad();

        // Aquí obtendrías la cantidad de producto en la sucursal a través del productoId
        int cantidadEnSucursal = sucursal.getCantidad(productoId);

        // Verificar si la cantidad en la sucursal es mayor o igual que la requerida
        return cantidadEnSucursal >= cantidadRequeridaValor;
    }

    // Método para calcular el tiempo de tránsito para cada camino en la lista de listas de enteros
    public List<Integer> calcularTiempoTransito(List<List<Integer>> caminosEnteros) {
        return caminosEnteros.stream()
                .map(caminoEntero -> calcularTiempoSublista(caminoEntero))
                .collect(Collectors.toList());
    }

    // Método para calcular el tiempo de tránsito entre los elementos de una sublista
    private int calcularTiempoSublista(List<Integer> caminoEntero) {
        int tiempoSublista = 0;
        for (int i = 0; i < caminoEntero.size() - 1; i++) {
            int origenId = caminoEntero.get(i);
            int destinoId = caminoEntero.get(i + 1);

            // Buscar el tiempo de tránsito para el camino actual
            int tiempoCamino = obtenerTiempoTransito(origenId, destinoId);

            // Acumular el tiempo de tránsito de la sublista
            tiempoSublista += tiempoCamino;
        }
        return tiempoSublista;
    }

    // Método para obtener el tiempo de tránsito entre dos sucursales
    private int obtenerTiempoTransito(int origenId, int destinoId) {
        List<Caminos> lista = caminoDao.listaCaminosQuery("");
        return lista.stream()
                .filter(camino -> camino.getOrigenId() == origenId && camino.getDestinoId() == destinoId)
                .mapToInt(Caminos::getTiempo)
                .sum();
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
