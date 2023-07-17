package Controladores;

import Modelos.Electrodomesticos;
import Modelos.ElectrodomesticosDao;
import Modelos.Stock;
import Modelos.StockDao;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class StockControlador implements ActionListener, MouseListener, KeyListener {

    private Stock stock;
    private StockDao stockDao;
    private SystemView vista;
    DefaultTableModel modelo = new DefaultTableModel();

    //Lo uso en el método para recupear nombre de sucursal por Id, y a la inversa.
    private SucursalesDao sucursalDao = new SucursalesDao();
    //Lo usaré para recuperar los datos de todos los productos.
    private ElectrodomesticosDao electroDao = new ElectrodomesticosDao();
    //Recuperar la lista de electrodomesticos.
    List<Electrodomesticos> listaElectro = electroDao.listaElectrodomesticosQuery("");
    Map<Integer, String> nombresElectro = new HashMap<>();

    public StockControlador(Stock stock, StockDao stockDao, SystemView vista) {
        this.stock = stock;
        this.stockDao = stockDao;
        this.vista = vista;
        //Botón de confirmar Stock
        this.vista.btn_stock_confirmar.addActionListener(this);
        //Ponemos a la tabla en escucha
        this.vista.tabla_stock.addMouseListener(this);
        //Ponemos el botón de buscar en escucha del teclado
        this.vista.stock_search.addKeyListener(this);
        //Pondemos a la escucha el botón de cancelar
        this.vista.btn_stock_cancelar.addActionListener(this);
        //Ponemos en escucha el Label
        this.vista.jLabelStock.addMouseListener(this);

        //Recuperar las sucursales para mostrar en el comboBox
        //Paso 1: Obtener la lista de sucursales desde la base de datos
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");
        // Paso 2: Crear una lista de nombres de sucursales
        List<String> nombresSucursales = new ArrayList<>();
        for (Sucursales sucursal : listaSucursales) {
            nombresSucursales.add(sucursal.getNombre());
        }
        // Paso 3: Configurar el ComboBox con el modelo de nombres
        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>(nombresSucursales.toArray(new String[0]));
        vista.cmb_stock_sucursal.setModel(modeloCombo);

        for (Electrodomesticos electro : listaElectro) {
            nombresElectro.put(electro.getId(), electro.getNombre());
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_stock_confirmar) {
            if (vista.cmb_stock_sucursal.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para ser actualizada");
            } else {
                //Verificamos si los campos están vacíos
                if (vista.txt_stock_id_producto.getText().equals("")
                        || vista.txt_stock_stock.getText().equals("")
                        || vista.cmb_stock_sucursal.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    //Realizar la actualización
                    // Obtener el nombre de la sucursal seleccionada
                    String nombreSucursal = (String) vista.cmb_stock_sucursal.getSelectedItem();
                    int idSucursal = obtenerIdSucursalPorNombre(nombreSucursal);
                    // Asignar el ID al objeto correspondiente
                    stock.setId_sucursal(idSucursal);
                    stock.setId_producto(Integer.parseInt(vista.txt_stock_id_producto.getText().trim()));
                    stock.setStock(Integer.parseInt(vista.txt_stock_stock.getText().trim()));
                    
                    if (stockDao.modificarStockQuery(stock)) {
                        limpiarTabla();
                        limpiarCampos();
                        listarTodosLosStock();
                        JOptionPane.showMessageDialog(null, "Stock actualizado con éxito");

                    } else {
                        JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido actualizar el stock");
                    }
                }
            }
        } else if (e.getSource() == vista.btn_stock_cancelar) {
            limpiarCampos();
        }

    }

    //Listar todos las productos
    public void listarTodosLosStock() {
        List<Stock> lista = stockDao.listaStockQuery(vista.stock_search.getText());
        modelo = (DefaultTableModel) vista.tabla_stock.getModel();
        Object[] fila = new Object[5];
        for (int i = 0; i < lista.size(); i++) {
            fila[0] = lista.get(i).getId_sucursal();
            //Acomodar para que aparezca nombres de las sucursales, no los Id ************************
            int origenId = lista.get(i).getId_sucursal();
            String nombreOrigen = obtenerNombreSucursalPorId(origenId);
            fila[1] = nombreOrigen;
            fila[2] = lista.get(i).getId_producto();
            // Obtener el nombre del producto utilizando el ID del producto como clave en el mapa
            int productoId = lista.get(i).getId_producto();
            String nombreProducto = nombresElectro.get(Integer.valueOf(productoId));
            fila[3] = nombreProducto;
            fila[4] = lista.get(i).getStock();
            modelo.addRow(fila);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.tabla_stock) {
            int fila = vista.tabla_stock.rowAtPoint(e.getPoint());
            //Método para recupear el nombre de la sucursal en el comboBox
            String nombreSucursal = (String) vista.tabla_stock.getValueAt(fila,1);
            vista.cmb_stock_sucursal.setSelectedItem(nombreSucursal);
            vista.txt_stock_id_producto.setText(vista.tabla_stock.getValueAt(fila, 2).toString());
            vista.txt_stock_nombre_producto.setText(vista.tabla_stock.getValueAt(fila, 3).toString());
            vista.txt_stock_stock.setText(vista.tabla_stock.getValueAt(fila, 4).toString());
        } else if (e.getSource() == vista.jLabelStock) {
            vista.jTabbedPane1.setSelectedIndex(2);
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
        if (e.getSource() == vista.stock_search) {
            limpiarTabla();
            listarTodosLosStock();
        }
    }

    //Método para limpiar los campos de la pantalla
    public void limpiarCampos() {
        vista.txt_stock_id_producto.setText("");
        vista.txt_stock_nombre_producto.setText("");
        vista.txt_stock_stock.setText("");
        vista.cmb_stock_sucursal.setSelectedItem("");
    }

    //Método para limpiar la tabla
    public void limpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i--;
        }
    }

    private int obtenerIdSucursalPorNombre(String nombreSucursal) {
        //SucursalesDao sucursalDao = new SucursalesDao();
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");

        for (Sucursales sucursal : listaSucursales) {
            if (sucursal.getNombre().trim().equals(nombreSucursal.trim())) {
                return sucursal.getId();
            }
        }
        return -1; // Retorna un valor por defecto si no se encuentra la sucursal
    }

    private String obtenerNombreSucursalPorId(int idSucursal) {
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");
        for (Sucursales sucursal : listaSucursales) {
            if (sucursal.getId() == idSucursal) {
                return sucursal.getNombre().trim();
            }
        }
        return ""; // Retorna un valor por defecto si no se encuentra la sucursal
    }

}
