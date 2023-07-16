package Controladores;

import Vistas.SystemView;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SettingsController implements MouseListener {

     private SystemView views;
     
     public SettingsController(SystemView views) {
         this.views = views;
         this.views.jLabelSucursales.addMouseListener(this);
         this.views.jLabelElectrodomesticos.addMouseListener(this);
         this.views.jLabelStock.addMouseListener(this);
         this.views.jLabelCaminos.addMouseListener(this);
         this.views.jLabelOrdenes.addMouseListener(this);
         this.views.jLabelAdministracion.addMouseListener(this);
         this.views.jLabelInformes.addMouseListener(this);
     }
     
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
     
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(e.getSource()==views.jLabelSucursales) {
            views.jPanelSucursales.setBackground(new Color(152,202,63));
        } else if(e.getSource()== views.jLabelElectrodomesticos) {
            views.jPanelElectrodomesticos.setBackground(new Color(152,202,63));
        } else if(e.getSource()== views.jLabelStock) {
            views.jPanelStock.setBackground(new Color(152,202,63));    
        } else if(e.getSource()== views.jLabelCaminos) {
            views.jPanelCaminos.setBackground(new Color(152,202,63));
        } else if(e.getSource()== views.jLabelOrdenes) {
            views.jPanelOrdenes.setBackground(new Color(152,202,63));
        } else if(e.getSource()== views.jLabelAdministracion) {
            views.jPanelAdministracion.setBackground(new Color(152,202,63));
        } else if(e.getSource()== views.jLabelInformes) {
            views.jPanelInformes.setBackground(new Color(152,202,63));
        }
    
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(e.getSource()==views.jLabelSucursales) {
            views.jPanelSucursales.setBackground(new Color(18,45,61)); //[18,45,61]
        } else if(e.getSource()== views.jLabelElectrodomesticos) {
            views.jPanelElectrodomesticos.setBackground(new Color(18,45,61));
        } else if(e.getSource()== views.jLabelStock) {
            views.jPanelStock.setBackground(new Color(18,45,61));
        } else if(e.getSource()== views.jLabelCaminos) {
            views.jPanelCaminos.setBackground(new Color(18,45,61));
        } else if(e.getSource()== views.jLabelOrdenes) {
            views.jPanelOrdenes.setBackground(new Color(18,45,61));
        } else if(e.getSource()== views.jLabelAdministracion) {
            views.jPanelAdministracion.setBackground(new Color(18,45,61));
        } else if(e.getSource()== views.jLabelInformes) {
            views.jPanelInformes.setBackground(new Color(18,45,61));
        }
    }
}
