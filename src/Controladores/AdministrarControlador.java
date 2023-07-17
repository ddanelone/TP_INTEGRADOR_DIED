package Controladores;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class AdministrarControlador implements ActionListener, MouseListener {

    private Caminos camino;
    private Sucursales sucursal;
    private SucursalesDao sucursalDao;
    private CaminosDao caminoDao;
    private SystemView vista;

    public AdministrarControlador(Caminos camino, Sucursales sucursal, CaminosDao caminoDao, SucursalesDao sucursalDao, SystemView vista) {
        this.camino = camino;
        this.sucursal = sucursal;
        this.caminoDao = caminoDao;
        this.sucursalDao = sucursalDao;
        this.vista = vista;
        // Botones habilitar/deshabilitar Sucursal
        this.vista.btn_admnistracion_habilitar_suc.addActionListener(this);
        this.vista.btn_admnistracion_deshabilitar_suc.addActionListener(this);
        // Botones habilitar/deshabilitar Camino
        this.vista.btn_admnistracion_habilitar_cam.addActionListener(this);
        this.vista.btn_admnistracion_deshabilitar_cam.addActionListener(this);
        // JLabel en escucha
        this.vista.jLabelAdministracion.addMouseListener(this);
        
        // Recuperar las sucursales para mostrar en el comboBox
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");
        Map<Integer, String> nombresSucursalesMap = new HashMap<>();
        for (Sucursales unaSucursal : listaSucursales) {
            nombresSucursalesMap.put(unaSucursal.getId(), unaSucursal.getNombre());
        }
        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>(nombresSucursalesMap.values().toArray(new String[0]));
        vista.cmb_administracion_sucursal.setModel(modeloCombo);

        // Recuperar los caminos para mostrar en el comboBox
        List<Caminos> listaCaminos = caminoDao.listaCaminosQuery("");
        Map<Integer, String> observacionesCaminosMap = new HashMap<>();
        for (Caminos unCamino : listaCaminos) {
            observacionesCaminosMap.put(unCamino.getId(), unCamino.getObservaciones());
        }
        DefaultComboBoxModel<String> modeloCombo2 = new DefaultComboBoxModel<>(observacionesCaminosMap.values().toArray(new String[0]));
        vista.cmb_administracion_camino.setModel(modeloCombo2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_admnistracion_habilitar_suc) {
            if (vista.cmb_administracion_sucursal.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una sucursal...");
            } else {
                String nombreSucursal = (String) vista.cmb_administracion_sucursal.getSelectedItem();
                int idSucursal = obtenerIdSucursalPorNombre(nombreSucursal);

                // Asignar el ID al objeto correspondiente
                sucursal.setId(idSucursal);
                sucursal.setOperativa(true);

                if (sucursalDao.modificarSucursalEstadoQuery(sucursal)) {
                    JOptionPane.showMessageDialog(null, "Estado de Sucursal puesto en Operativo exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido cambiar el estado");
                }
            }
        } else if (e.getSource() == vista.btn_admnistracion_deshabilitar_suc) {
            if (vista.cmb_administracion_sucursal.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una sucursal...");
            } else {
                String nombreSucursal = (String) vista.cmb_administracion_sucursal.getSelectedItem();
                int idSucursal = obtenerIdSucursalPorNombre(nombreSucursal);
                // Asignar el ID al objeto correspondiente
                sucursal.setId(idSucursal);
                sucursal.setOperativa(false);

                if (sucursalDao.modificarSucursalEstadoQuery(sucursal)) {
                    JOptionPane.showMessageDialog(null, "Estado de Sucursal puesto en No Operativo exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido cambiar el estado");
                }
            }
        } else if (e.getSource() == vista.btn_admnistracion_habilitar_cam) {
            if (vista.cmb_administracion_camino.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un camino...");
            } else {
                String nombreCamino = (String) vista.cmb_administracion_camino.getSelectedItem();
                int idCamino = obtenerIdCaminoPorDescripcion(nombreCamino);
                // Asignar el ID al objeto correspondiente
                camino.setId(idCamino);
                camino.setOperativo(true);

                if (caminoDao.modificarCaminoEstadoEstadoQuery(camino)) {
                    JOptionPane.showMessageDialog(null, "Estado del Camino puesto en Operativo exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido cambiar el estado");
                }
            }
        } else if (e.getSource() == vista.btn_admnistracion_deshabilitar_cam) {
            if (vista.cmb_administracion_sucursal.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un camino...");
            } else {
                String nombreCamino = (String) vista.cmb_administracion_camino.getSelectedItem();
                int idCamino = obtenerIdCaminoPorDescripcion(nombreCamino);
                // Asignar el ID al objeto correspondiente
                camino.setId(idCamino);
                camino.setOperativo(false);

                if (caminoDao.modificarCaminoEstadoEstadoQuery(camino)) {
                    JOptionPane.showMessageDialog(null, "Estado del Camino puesto en No Operativo exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido cambiar el estado");
                }
            }
        } 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.jLabelAdministracion) {
            vista.jTabbedPane1.setSelectedIndex(5);
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
          

    private int obtenerIdSucursalPorNombre(String nombreSucursal) {
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");

        for (Sucursales sucursal : listaSucursales) {
            if (sucursal.getNombre().trim().equals(nombreSucursal.trim())) {
                return sucursal.getId();
            }
        }
        return -1; // Retorna un valor por defecto si no se encuentra la sucursal
    }
   
    private int obtenerIdCaminoPorDescripcion(String nombreCamino) {
        List<Caminos> listaCaminos = caminoDao.listaCaminosQuery("");

        for (Caminos unCamino : listaCaminos) {
            if (unCamino.getObservaciones().trim().equals(nombreCamino.trim())) {
                return unCamino.getId();
            }
        }
        return -1; // Retorna un valor por defecto si no se encuentra el camino
    }
}
