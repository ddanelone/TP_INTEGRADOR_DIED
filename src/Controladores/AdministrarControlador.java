package Controladores;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Vistas.SystemView;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class AdministrarControlador implements MouseListener {
    
    private SystemView vista;
    //Instanciamos el modelo de Camnos y Sucursales.
    Caminos camino = new Caminos();
    CaminosDao caminoDao = new CaminosDao();
    Sucursales sucursal = new Sucursales();
    SucursalesDao sucursalDao = new SucursalesDao();
    DefaultTableModel modeloSucursal = new DefaultTableModel();
    DefaultTableModel modeloCamino = new DefaultTableModel();
    private Object[] options = {"Sí", "No"};
    

    public AdministrarControlador(SystemView vista) {
        this.vista = vista;          
        // Recuperar las sucursales para mostrar en el comboBox
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");
        // Recuperar los caminos para mostrar en el comboBox
        List<Caminos> listaCaminos = caminoDao.listaCaminosQuery("");
        
        this.vista.tabla_administracion_caminos.addMouseListener(this);
        this.vista.tabla_administracion_sucursales.addMouseListener(this);
        this.vista.jLabelAdministracion.addMouseListener(this);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       if (e.getSource() == vista.jLabelAdministracion) {
            vista.jTabbedPane1.setSelectedIndex(5);
        } else if (e.getSource() == vista.tabla_administracion_sucursales) {
            int fila = vista.tabla_administracion_sucursales.rowAtPoint(e.getPoint());
            sucursal.setId(Integer.parseInt(vista.tabla_administracion_sucursales.getValueAt(fila, 0).toString()));
            sucursal.setCodigo(Integer.parseInt(vista.tabla_administracion_sucursales.getValueAt(fila, 1).toString()));
            sucursal.setNombre(vista.tabla_administracion_sucursales.getValueAt(fila,2).toString());
            boolean estado = vista.tabla_administracion_sucursales.getValueAt(fila, 3).toString().equals("Operativa") ? true : false;
            sucursal.setOperativa(!estado);
            sucursal.setHorarioApertura(vista.tabla_administracion_sucursales.getValueAt(fila, 4).toString());
            sucursal.setHorarioCierre(vista.tabla_administracion_sucursales.getValueAt(fila,5).toString());
            int confirmacion = JOptionPane.showOptionDialog(null, "¿Seguro de cambiar el ESTADO OPERATIVO de esta Sucursal?", "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (confirmacion == 0 && sucursalDao.modificarSucursalQuery(sucursal)) {
                    limpiarTabla(modeloSucursal);
                    listarTodasLasSucursales();
                    JOptionPane.showMessageDialog(null, "Estado de Sucursal modificado exitosamente");
                }
            
        } else if (e.getSource() == vista.tabla_administracion_caminos) {
            int fila = vista.tabla_administracion_caminos.rowAtPoint(e.getPoint());
            camino.setId(Integer.parseInt(vista.tabla_administracion_caminos.getValueAt(fila, 0).toString()));
            camino.setOrigenId(obtenerIdSucursalPorNombre(vista.tabla_administracion_caminos.getValueAt(fila,1).toString()));
            camino.setDestinoId(obtenerIdSucursalPorNombre(vista.tabla_administracion_caminos.getValueAt(fila,2).toString()));
            camino.setTiempo(Integer.parseInt(vista.tabla_administracion_caminos.getValueAt(fila, 3).toString()));
            camino.setCapacidad(Integer.parseInt(vista.tabla_administracion_caminos.getValueAt(fila, 4).toString()));
            boolean estado = vista.tabla_administracion_caminos.getValueAt(fila, 5).toString().equals("Operativo") ? true : false;
            camino.setOperativo(!estado);
            camino.setObservaciones(vista.tabla_administracion_caminos.getValueAt(fila, 6).toString());
            int confirmacion = JOptionPane.showOptionDialog(null, "¿Seguro de cambiar el ESTADO OPERATIVO de este Camino?", "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (confirmacion == 0 && caminoDao.modificarCaminoQuery(camino)) {
                    limpiarTabla(modeloCamino);
                    listarTodosLosCaminos();
                    JOptionPane.showMessageDialog(null, "Camino Actualizado exitosamente");
                }
        }
    }
    
    //Listar todas las sucursales
    public void listarTodasLasSucursales() {
        List<Sucursales> lista = sucursalDao.listaSucursalesQuery("");
        modeloSucursal = (DefaultTableModel) vista.tabla_administracion_sucursales.getModel();
        Object[] col = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            col[0] = lista.get(i).getId();
            col[1] = lista.get(i).getCodigo();
            col[2] = lista.get(i).getNombre();
            //Lógica para que ponga "Operativa / No Operativa en la tabla
            col[3] = lista.get(i).isOperativa() ? "Operativa" : "No Operativa";
            col[4] = lista.get(i).getHorarioApertura();
            col[5] = lista.get(i).getHorarioCierre();
            modeloSucursal.addRow(col);
        }
    }
    
    //Listar todos las Caminos
    public void listarTodosLosCaminos() {
        List<Caminos> lista = caminoDao.listaCaminosQuery("");
        modeloCamino = (DefaultTableModel) vista.tabla_administracion_caminos.getModel();
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
            modeloCamino.addRow(col);
        }
    }
    
    //Método para limpiar la tabla
    public void limpiarTabla(DefaultTableModel modelo) {
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
