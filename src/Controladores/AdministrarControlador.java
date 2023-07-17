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
import java.util.ArrayList;
import java.util.List;
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
        //Botoes habilitar/deshabilitar Sucursal
        this.vista.btn_admnistracion_habilitar_suc.addActionListener(this);
        this.vista.btn_admnistracion_habilitar_suc.addActionListener(this);
        //Botones habilitar/deshabilitar Camino
        this.vista.btn_admnistracion_habilitar_cam.addActionListener(this);
        this.vista.btn_admnistracion_habilitar_cam.addActionListener(this);
        //jLabel en escucha
        this.vista.jLabelAdministracion.addMouseListener(this);
        //Poner en escucha los comboBox para actualizar el estado del objeto
        this.vista.cmb_administracion_sucursal.addMouseListener(this);
        this.vista.cmb_administracion_camino.addMouseListener(this);

        //Recuperar las sucursales para mostrar en el comboBox
        //Paso 1: Obtener la lista de sucursales desde la base de datos
        List<Sucursales> listaSucursales = sucursalDao.listaSucursalesQuery("");
        // Paso 2: Crear una lista de nombres de sucursales
        List<String> nombresSucursales = new ArrayList<>();
        for (Sucursales unaSucursal : listaSucursales) {
            nombresSucursales.add(unaSucursal.getNombre());
        }
        // Paso 3: Configurar el ComboBox con el modelo de nombres
        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>(nombresSucursales.toArray(new String[0]));
        vista.cmb_administracion_sucursal.setModel(modeloCombo);

        //Recuperar los caminos para mostrar en el comboBox
        //Paso 1: Obtener la lista de caminos desde la base de datos
        List<Caminos> listaCaminos = caminoDao.listaCaminosQuery("");
        // Paso 2: Crear una lista de nombres de sucursales
        List<String> observacionesCaminos = new ArrayList<>();
        for (Caminos unCamino : listaCaminos) {
            observacionesCaminos.add(unCamino.getObservaciones());
        }
        // Paso 3: Configurar el ComboBox con el modelo de nombres
        DefaultComboBoxModel<String> modeloCombo2 = new DefaultComboBoxModel<>(observacionesCaminos.toArray(new String[0]));
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
                    limpiarCampos();
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
                    limpiarCampos();
                    JOptionPane.showMessageDialog(null, "Estado de Sucursal puesto en No Operativo exitosamente");

                } else {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido cambiar el estado");
                }
            }
            if (e.getSource() == vista.btn_admnistracion_habilitar_cam) {
                if (vista.cmb_administracion_camino.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un camino...");
                } else {
                    String nombreCamino = (String) vista.cmb_administracion_camino.getSelectedItem();
               
                    int idCamino = obtenerIdCaminoPorDescripcion(nombreCamino);
                    // Asignar el ID al objeto correspondiente
                    camino.setId(idCamino);
                    camino.setOperativo(true);

                    if (caminoDao.modificarCaminoEstadoEstadoQuery(camino)) {
                        limpiarCampos();
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
                        limpiarCampos();
                        JOptionPane.showMessageDialog(null, "Estado del Camino puesto en No Operativo exitosamente");

                    } else {
                        JOptionPane.showMessageDialog(null, "Se ha producido un error, no se ha podido cambiar el estado");
                    }
                }
            } 
        }
    }

    public void limpiarCampos() {
        vista.txt_administracion_estado_suc.setText("");
        vista.txt_administracion_estado_cam.setText("");
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
        if(e.getSource() == vista.cmb_administracion_sucursal) {
                String estadoSuc = (sucursal.isOperativa() ? "Operativa" : "No Operativa");
                vista.txt_administracion_estado_suc.setText(estadoSuc);
            } else if(e.getSource() == vista.cmb_administracion_camino) {
                    String estadoCam = (camino.isOperativo() ? "Operativo" : "No Operativo");
                    vista.txt_administracion_estado_cam.setText(estadoCam);   
            }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
    
    private int obtenerIdCaminoPorDescripcion(String nombreCamino) {
        List<Caminos> listaCaminos = caminoDao.listaCaminosQuery("");

        for (Caminos unCamino : listaCaminos) {
            if (camino.getObservaciones().trim().equals(nombreCamino.trim())) {
                return camino.getId();
            }
        }
        return -1; // Retorna un valor por defecto si no se encuentra la sucursal
    }
}
