package Controladores;

import Modelos.Caminos;
import Modelos.CaminosDao;
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
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CaminosControlador implements ActionListener, MouseListener, KeyListener {

    private Caminos camino;
    private CaminosDao caminoDao;
    private SystemView vista;
    DefaultTableModel modelo = new DefaultTableModel();
    private Object[] options = {"Sí", "No"};
 
    //Lo uso en el método para recupear nombre de sucursal por Id, y a la inversa.
    private SucursalesDao sucursalDao = new SucursalesDao();
    

    public CaminosControlador(Caminos camino, CaminosDao caminoDao, SystemView vista) {
        this.camino = camino;
        this.caminoDao = caminoDao;
        this.vista = vista;
        //Botón de registrar camino
        this.vista.btn_caminos_crear.addActionListener(this);
        //Ponemos a la tabla en escucha
        this.vista.tabla_caminos.addMouseListener(this);
        //Ponemos el botón de buscar en escucha del teclado
        this.vista.caminos_search.addKeyListener(this);
        //Ponemos a la escucha el botón de modificar camino
        this.vista.btn_caminos_modificar.addActionListener(this);
        //Botón de elminar CAmino y de cancelar
        this.vista.btn_caminos_eliminar.addActionListener(this);
        this.vista.btn_caminos_cancelar.addActionListener(this);
        //Ponemos en escucha el Label
        this.vista.jLabelCaminos.addMouseListener(this);
        
        
        //Recuperar las sucursales para mostrar en el comboBox
        //Paso 1: Obtener la lista de sucursales desde la base de datos
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");
        // Paso 2: Crear una lista de nombres de sucursales
        List<String> nombresSucursales = new ArrayList<>();
        for (Sucursales sucursal : listaSucursales) {
            nombresSucursales.add(sucursal.getNombre());
        }
        // Paso 3: Configurar el ComboBox con el modelo de nombres
        DefaultComboBoxModel<String> modeloCombo1 = new DefaultComboBoxModel<>(nombresSucursales.toArray(new String[0]));
        DefaultComboBoxModel<String> modeloCombo2 = new DefaultComboBoxModel<>(nombresSucursales.toArray(new String[0]));
        vista.cmb_caminos_sucursal_origen.setModel(modeloCombo1);
        vista.cmb_caminos_sucursal_destino.setModel(modeloCombo2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_caminos_crear) {
            //verificamos si los campos están vacíos
            if (vista.txt_caminos_tiempo_transito.getText().equals("")
                    || vista.txt_caminos_capacidad_max.getText().equals("")
                    || vista.cmb_caminos_sucursal_origen.getSelectedItem().equals("")
                    || vista.cmb_caminos_sucursal_destino.getSelectedItem().equals("")
                    || vista.cmb_caminos_estado.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //Realizar la inserción
                camino.setTiempo(Integer.parseInt(vista.txt_caminos_tiempo_transito.getText().trim()));
                camino.setCapacidad(Integer.parseInt(vista.txt_caminos_capacidad_max.getText().trim()));
                camino.setObservaciones(vista.txt_caminos_observaciones.getText().trim());
                camino.setOperativo(vista.cmb_caminos_estado.getSelectedItem().toString() == "Operativo" ? true : false);
                //Ahora viene el quilombito de selecconar la sucursal del ComboBox, extraer el Id, y setear el Id.*****************
                // Obtener el nombre de la sucursal seleccionada
                String nombreSucursalO = (String) vista.cmb_caminos_sucursal_origen.getSelectedItem();
                String nombreSucursalD = (String) vista.cmb_caminos_sucursal_destino.getSelectedItem();
                // Obtener el ID de la sucursal correspondiente al nombre
                int idSucursalO = obtenerIdSucursalPorNombre(nombreSucursalO);
                int idSucursalD = obtenerIdSucursalPorNombre(nombreSucursalD);
                // Asignar el ID al objeto correspondiente
                camino.setOrigenId(idSucursalO);
                camino.setDestinoId(idSucursalD);

                if (caminoDao.registrarCaminoQuery(camino)) {
                    limpiarTabla();
                    limpiarCampos();
                    listarTodosLosCaminos();
                    JOptionPane.showMessageDialog(null, "Camino registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error, Camino no registrado");
                }
            }
        } else if (e.getSource() == vista.btn_caminos_modificar) {
            if (vista.txt_caminos_id.equals("")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un Camino para ser modificado");
            } else {
                //Verificamos si los campos están vacíos
                if (vista.txt_caminos_tiempo_transito.getText().equals("")
                        || vista.txt_caminos_capacidad_max.getText().equals("")
                        || vista.cmb_caminos_sucursal_origen.getSelectedItem().equals("")
                        || vista.cmb_caminos_sucursal_destino.getSelectedItem().equals("")
                        || vista.cmb_caminos_estado.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    //Realizar la actualización
                    camino.setId(Integer.parseInt(vista.txt_caminos_id.getText().trim()));
                    camino.setTiempo(Integer.parseInt(vista.txt_caminos_tiempo_transito.getText().trim()));
                    camino.setCapacidad(Integer.parseInt(vista.txt_caminos_capacidad_max.getText().trim()));
                    camino.setObservaciones(vista.txt_caminos_observaciones.getText().trim());
                    camino.setOperativo(vista.cmb_caminos_estado.getSelectedItem().toString() == "Operativo" ? true : false);
                    //Ahora viene el quilombito de selecconar la sucursal del ComboBox, extraer el Id, y setear el Id.*****************
                    // Obtener el nombre de la sucursal seleccionada
                    String nombreSucursalO = (String) vista.cmb_caminos_sucursal_origen.getSelectedItem();
                    String nombreSucursalD = (String) vista.cmb_caminos_sucursal_destino.getSelectedItem();
                    // Obtener el ID de la sucursal correspondiente al nombre
                    int idSucursalO = obtenerIdSucursalPorNombre(nombreSucursalO);
                    int idSucursalD = obtenerIdSucursalPorNombre(nombreSucursalD);
                    // Asignar el ID al objeto correspondiente
                    camino.setOrigenId(idSucursalO);
                    camino.setDestinoId(idSucursalD);

                    if (caminoDao.modificarCaminoQuery(camino)) {
                        limpiarTabla();
                        limpiarCampos();
                        listarTodosLosCaminos();
                        JOptionPane.showMessageDialog(null, "Camino modificado con éxito");
                        vista.btn_caminos_crear.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido modificar el Camino");
                    }
                }
            }
        } else if (e.getSource() == vista.btn_caminos_eliminar) {
            int fila = vista.tabla_caminos.getSelectedRow();

            //Si el usuario no seleccionó nada, el método devuelve -1
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un item para eliminar");
            } else {
                int id = Integer.parseInt(vista.tabla_caminos.getValueAt(fila, 0).toString());
                int confirmacion = JOptionPane.showOptionDialog(null, "¿Seguro de eliminar este Camino?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (confirmacion == 0 && caminoDao.borrarCaminoQuery(id) != false) {
                    limpiarCampos();
                    limpiarTabla();
                    vista.btn_caminos_crear.setEnabled(true);
                    listarTodosLosCaminos();
                    JOptionPane.showMessageDialog(null, "Camino eliminado exitosamente");
                }
            }
        } else if (e.getSource() == vista.btn_caminos_cancelar) {
            limpiarCampos();
            vista.btn_caminos_crear.setEnabled(true);
        } 
    }

    //Listar todos las Caminos
    public void listarTodosLosCaminos() {
        List<Caminos> lista = caminoDao.listaCaminosQuery(vista.caminos_search.getText());
        modelo = (DefaultTableModel) vista.tabla_caminos.getModel();
        Object[] col = new Object[7];
        for (int i = 0; i < lista.size(); i++) {
            col[0] = lista.get(i).getId();
            //Acomodar para que aparezca nombres de las sucursales, no los Id ************************
            int origenId = lista.get(i).getOrigenId();
            String nombreOrigen = obtenerNombreSucursalPorId(origenId);
            col[1] = nombreOrigen;
            int destinoId = lista.get(i).getDestinoId();
            String nombreDestino = obtenerNombreSucursalPorId(destinoId);
            col[2] = nombreDestino;
            col[3] = lista.get(i).getTiempo();
            col[4] = lista.get(i).getCapacidad();
            col[5] = lista.get(i).isOperativo() ? "Operativo" : "No Operativo";
            col[6] = lista.get(i).getObservaciones();
            modelo.addRow(col);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.tabla_caminos) {
            int fila = vista.tabla_caminos.rowAtPoint(e.getPoint());
            //La fila la proveé el método de arriba, pero la columna la sacamos de la posiición en la tabla
            vista.txt_caminos_id.setText(vista.tabla_caminos.getValueAt(fila, 0).toString());
            //Lógica para que recupere en el comboBox el nombre de las sucursales de origen y destino FILA 1 Y 2
           
            // Obtener el nombre de la sucursal de origen y destino seleccionada en la tabla
            String nombreSucursalOrigen = (String) vista.tabla_caminos.getValueAt(fila, 1);
            String nombreSucursalDestino = (String) vista.tabla_caminos.getValueAt(fila, 2);
            //JOptionPane.showMessageDialog(null, vista.tabla_sucursales.getValueAt(fila, 1) + " -- " + vista.tabla_sucursales.getValueAt(fila, 2));
            // Establecer la selección en los ComboBox de origen y destino
            vista.cmb_caminos_sucursal_origen.setSelectedItem(nombreSucursalOrigen);
            vista.cmb_caminos_sucursal_destino.setSelectedItem(nombreSucursalDestino);
                      
            vista.txt_caminos_tiempo_transito.setText(vista.tabla_caminos.getValueAt(fila, 3).toString());
            vista.txt_caminos_capacidad_max.setText(vista.tabla_caminos.getValueAt(fila, 4).toString());
            //Lógica para que recupere el booleano de si es operativa o no, y lo muestre en el comboBox FILA 5
            vista.cmb_caminos_estado.setSelectedItem(vista.tabla_caminos.getValueAt(fila, 5).toString());
            vista.txt_caminos_observaciones.setText(vista.tabla_caminos.getValueAt(fila, 6).toString());

            //Desahbilitar
            vista.txt_caminos_id.setEditable(false);
            vista.btn_caminos_crear.setEnabled(false);
        } else if (e.getSource() == vista.jLabelCaminos) {
            vista.jTabbedPane1.setSelectedIndex(3);
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
        if (e.getSource() == vista.caminos_search) {
            limpiarTabla();
            listarTodosLosCaminos();
        }
    }

    //Método para limpiar los campos de la pantalla
    public void limpiarCampos() {
        vista.txt_caminos_id.setText("");
        vista.txt_caminos_tiempo_transito.setText("");
        vista.txt_caminos_capacidad_max.setText("");
        vista.txt_caminos_observaciones.setText("");
        vista.cmb_caminos_estado.setSelectedIndex(0);
        vista.cmb_caminos_sucursal_origen.setSelectedItem("");
        vista.cmb_caminos_sucursal_destino.setSelectedItem("");
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
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("" + idSucursal);
        return listaSucursales.isEmpty() ? "" : listaSucursales.get(0).getNombre();
    }
}
