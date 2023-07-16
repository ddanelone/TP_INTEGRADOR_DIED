package Controladores;

import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Vistas.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class SucursalesControlador implements ActionListener {
    private Sucursales sucursal;
    private SucursalesDao sucursalDao;
    private SystemView vista;

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== vista.btn_sucursales_crear) {
            //verificamos si los campos están vacíos
            if(vista.txt_sucursales_codigo.getText().equals("") 
                    || vista.txt_sucursales_nombre.getText().equals("")
                    || vista.txt_sucursales_apertura.getText().equals("") 
                    || vista.txt_sucursales_cierre.getText().equals("")
                    || vista.cmb_estado_sucursal.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //Realizar la inserción
                sucursal.setCodigo(Integer.parseInt(vista.txt_sucursales_codigo.getText().trim()));
                sucursal.setNombre(vista.txt_sucursales_nombre.getText().trim());
                sucursal.setHorarioApertura(vista.txt_sucursales_apertura.getText().trim());
                sucursal.setHorarioCierre(vista.txt_sucursales_cierre.getText().trim());
                boolean operativa;
                if (vista.cmb_estado_sucursal.getSelectedItem().toString()=="Operativa")
                    operativa = true;
                else
                    operativa = false;
                
                sucursal.setOperativa(operativa);
                
                if(sucursalDao.registrarSucursalQuery(sucursal)) {
                    JOptionPane.showMessageDialog(null, "Sucursal registrada con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error, sucursal no registrada");
                }
            }
        }
        
    }

    public SucursalesControlador(Sucursales sucursal, SucursalesDao sucursalDao, SystemView vista) {
        this.sucursal = sucursal;
        this.sucursalDao = sucursalDao;
        this.vista = vista;
        //Botón de registrar sucuarsal
        this.vista.btn_sucursales_crear.addActionListener(this);
    }
}
