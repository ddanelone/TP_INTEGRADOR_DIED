package Controladores;

import Modelos.Electrodomesticos;
import Modelos.ElectrodomesticosDao;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ElectrodomesticosControlador implements ActionListener, MouseListener, KeyListener {

    private Electrodomesticos electro;
    private ElectrodomesticosDao electroDao;
    private SystemView vista;
    DefaultTableModel modelo = new DefaultTableModel();
    private Object[] options = {"Sí", "No"};

    public ElectrodomesticosControlador(Electrodomesticos electro, ElectrodomesticosDao electroDao, SystemView vista) {
        this.electro = electro;
        this.electroDao = electroDao;
        this.vista = vista;
        //Botón de registrar electro
        this.vista.btn_electrodomesticos_crear.addActionListener(this);
        //Ponemos a la tabla en escucha
        this.vista.tabla_electrodomesticos.addMouseListener(this);
        //Ponemos el botón de buscar en escucha del teclado
        this.vista.electrodomesticos_search.addKeyListener(this);
        //Ponemos a la escucha el botón de modificar producto
        this.vista.btn_electrodomesticos_modificar.addActionListener(this);
        //Botón de elminar producto y de cancelar
        this.vista.btn_electrodomesticos_eliminar.addActionListener(this);
        this.vista.btn_electrodomesticos_cancelar.addActionListener(this);
        //Ponemos en escucha el Label
        this.vista.jLabelElectrodomesticos.addMouseListener(this);
        //Validaciones
        this.vista.txt_electrodomesticos_codigo.addKeyListener(this);
        this.vista.txt_electrodomesticos_peso.addKeyListener(this);
        this.vista.txt_electrodomesticos_precio.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_electrodomesticos_crear) {
            //verificamos si los campos están vacíos
            if (vista.txt_electrodomesticos_codigo.getText().equals("")
                    || vista.txt_electrodomesticos_nombre.getText().equals("")
                    || vista.txt_electrodomesticos_descripcion.getText().equals("")
                    || vista.txt_electrodomesticos_peso.getText().equals("")
                    || vista.txt_electrodomesticos_precio.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //Realizar la inserción
                electro.setCodigo(Integer.parseInt(vista.txt_electrodomesticos_codigo.getText().trim()));
                electro.setNombre(vista.txt_electrodomesticos_nombre.getText().trim());
                electro.setDescripcion(vista.txt_electrodomesticos_descripcion.getText().trim());
                String pesoStr = vista.txt_electrodomesticos_peso.getText().trim();
                double pesoKg = Double.parseDouble(pesoStr);
                electro.setPesoKg(pesoKg);
                String precioUni = vista.txt_electrodomesticos_precio.getText().trim();
                double precio = Double.parseDouble(precioUni);
                electro.setPrecioUnitario(precio);

                if (electroDao.registrarElectrodomesticoQuery(electro)) {
                    limpiarTabla();
                    limpiarCampos();
                    listarTodosLosElectrodomesticos();
                    JOptionPane.showMessageDialog(null, "Electrodoméstico registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error, electrodométsico no registradoa");
                }
            }
        } else if (e.getSource() == vista.btn_electrodomesticos_modificar) {
            if (vista.txt_electrodomesticos_id.equals("")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un Electrodomestico para ser modificado");
            } else {
                //Verificamos si los campos están vacíos
                if (vista.txt_electrodomesticos_codigo.getText().equals("")
                        || vista.txt_electrodomesticos_nombre.getText().equals("")
                        || vista.txt_electrodomesticos_descripcion.getText().equals("")
                        || vista.txt_electrodomesticos_peso.getText().equals("")
                        || vista.txt_electrodomesticos_precio.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    //Realizar la actualización
                    electro.setId(Integer.parseInt(vista.txt_electrodomesticos_id.getText().trim()));
                    electro.setCodigo(Integer.parseInt(vista.txt_electrodomesticos_codigo.getText().trim()));
                    electro.setNombre(vista.txt_electrodomesticos_nombre.getText().trim());
                    electro.setDescripcion(vista.txt_electrodomesticos_descripcion.getText().trim());
                    electro.setPesoKg(Double.parseDouble(vista.txt_electrodomesticos_peso.getText().trim()));
                    electro.setPrecioUnitario(Double.parseDouble(vista.txt_electrodomesticos_precio.getText().trim()));

                    if (electroDao.modificarElectrodomesticoQuery(electro)) {
                        limpiarTabla();
                        limpiarCampos();
                        listarTodosLosElectrodomesticos();
                        JOptionPane.showMessageDialog(null, "Electrodoméstico modificado con éxito");
                        vista.btn_electrodomesticos_crear.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido modificar el producto");
                    }
                }
            }
        } else if (e.getSource() == vista.btn_electrodomesticos_eliminar) {
            int fila = vista.tabla_electrodomesticos.getSelectedRow();

            //Si el usuario no seleccionó nada, el método devuelve -1
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un item para eliminar");
            } else {
                int id = Integer.parseInt(vista.tabla_electrodomesticos.getValueAt(fila, 0).toString());
                int confirmacion = JOptionPane.showOptionDialog(null, "¿Seguro de eliminar este electrodoméstico?", "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (confirmacion == 0 && electroDao.borrarElectrodomesticoQuery(id) != false) {
                    limpiarCampos();
                    limpiarTabla();
                    vista.btn_electrodomesticos_crear.setEnabled(true);
                    listarTodosLosElectrodomesticos();
                    JOptionPane.showMessageDialog(null, "Electrodoméstico eliminado exitosamente");
                }
            }
        } else if (e.getSource() == vista.btn_electrodomesticos_cancelar) {
            limpiarCampos();
            vista.btn_electrodomesticos_crear.setEnabled(true);
        }

    }

    //Listar todos las productos
    public void listarTodosLosElectrodomesticos() {
        List<Electrodomesticos> lista = electroDao.listaElectrodomesticosQuery(vista.electrodomesticos_search.getText());
        modelo = (DefaultTableModel) vista.tabla_electrodomesticos.getModel();
        Object[] col = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            col[0] = lista.get(i).getId();
            col[1] = lista.get(i).getCodigo();
            col[2] = lista.get(i).getNombre();
            col[3] = lista.get(i).getDescripcion();
            col[4] = lista.get(i).getPrecioUnitario();
            col[5] = lista.get(i).getPesoKg();
            modelo.addRow(col);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.tabla_electrodomesticos) {
            int fila = vista.tabla_electrodomesticos.rowAtPoint(e.getPoint());
            //La fila la proveé el método de arriba, pero la columna la sacamos de la posiición en la tabla
            vista.txt_electrodomesticos_id.setText(vista.tabla_electrodomesticos.getValueAt(fila, 0).toString());
            vista.txt_electrodomesticos_codigo.setText(vista.tabla_electrodomesticos.getValueAt(fila, 1).toString());
            vista.txt_electrodomesticos_nombre.setText(vista.tabla_electrodomesticos.getValueAt(fila, 2).toString());
            vista.txt_electrodomesticos_descripcion.setText(vista.tabla_electrodomesticos.getValueAt(fila, 3).toString());
            vista.txt_electrodomesticos_precio.setText(vista.tabla_electrodomesticos.getValueAt(fila, 4).toString());
            vista.txt_electrodomesticos_peso.setText(vista.tabla_electrodomesticos.getValueAt(fila, 5).toString());

            //Desahbilitar
            vista.txt_electrodomesticos_id.setEditable(false);
            vista.btn_electrodomesticos_crear.setEnabled(false);
        } else if (e.getSource() == vista.jLabelElectrodomesticos) {
            vista.jTabbedPane1.setSelectedIndex(1);
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
        if (e.getSource() == vista.txt_electrodomesticos_codigo) {
            // Este método se llama cada vez que el usuario ingresa una tecla en el JTextField
            // Obtener la tecla ingresada por el usuario
            char c = e.getKeyChar();
            // Definir la expresión regular para solo permitir números enteros
            String regex = "\\d";
            // Verificar si la tecla ingresada coincide con la expresión regular
            if (!Character.toString(c).matches(regex)) {
                // Si la tecla no coincide, se consume el evento, evitando que se agregue al JTextField
                e.consume();
            }
        } else if (e.getSource() == vista.txt_electrodomesticos_peso) {
            // Este método se llama cada vez que el usuario ingresa una tecla en el JTextField
            // Obtener la tecla ingresada por el usuario
            char c = e.getKeyChar();
            // Definir la expresión regular para permitir números con punto flotante (separador decimal '.')
            String regex = "^[0-9]*\\.?[0-9]*$";
            // Verificar si la tecla ingresada coincide con la expresión regular
            if (!Character.toString(c).matches(regex)) {
                // Si la tecla no coincide, se consume el evento, evitando que se agregue al JTextField
                e.consume();
            }
        } else if (e.getSource() == vista.txt_electrodomesticos_precio) {
            // Este método se llama cada vez que el usuario ingresa una tecla en el JTextField

            // Obtener la tecla ingresada por el usuario
            char c = e.getKeyChar();

            // Definir la expresión regular para permitir números con punto flotante (separador decimal '.')
            String regex = "^[0-9]*\\.?[0-9]*$";

            // Verificar si la tecla ingresada coincide con la expresión regular
            if (!Character.toString(c).matches(regex)) {
                // Si la tecla no coincide, se consume el evento, evitando que se agregue al JTextField
                e.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == vista.electrodomesticos_search) {
            limpiarTabla();
            listarTodosLosElectrodomesticos();
        }
    }

    //Método para limpiar los campos de la pantalla
    public void limpiarCampos() {
        vista.txt_electrodomesticos_id.setText("");
        vista.txt_electrodomesticos_codigo.setText("");
        vista.txt_electrodomesticos_nombre.setText("");
        vista.txt_electrodomesticos_descripcion.setText("");
        vista.txt_electrodomesticos_precio.setText("");
        vista.txt_electrodomesticos_peso.setText("");
    }

    //Método para limpiar la tabla
    public void limpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i--;
        }
    }
}
