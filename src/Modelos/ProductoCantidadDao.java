package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoCantidadDao {
    //Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //Variables para enviar datos entre interfaces
    public static int productoId_productoCantidad = 0;
    public static int cantidad_productoCantidad =0;
    
    public boolean registrarPoductoCantidadQuery(ProductoCantidad produC) {
        String query = "INSERT INTO ordenes_productos (orden_id, producto_id, cantidad) VALUES(?,?,?)";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, produC.getId());
            pst.setInt(2, produC.getProductoId());
            pst.setInt(3, produC.getCantidad());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el Producto/Cantidad" + e);
            return false;        
        }         
    }
    
    public List listaProductoCantidadQuery(String valor) {
        List<ProductoCantidad> lista_produC = new ArrayList();
        String query = "SELECT * FROM ordenes_productos ORDER BY orden_id ASC";
        String query_search_produC = "SELECT * FROM ordenes_productos WHERE orden_id LIKE '%" + valor + "%'";
        
        try{
            conn = cn.getConnection();
            if (valor.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
            } else {
                pst = conn.prepareStatement(query_search_produC);
            }
            rs = pst.executeQuery();
            
            while(rs.next()) {
                ProductoCantidad produC = new ProductoCantidad();
                produC.setId(rs.getInt("orden_id"));
                produC.setProductoId(rs.getInt("producto_id"));
                produC.setCantidad(rs.getInt("cantidad"));
                lista_produC.add(produC);
            }            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar los datos de productos/cantidad " + e);                   
        } 
        return lista_produC;
    } 
    
    public boolean modificarPoductoCantidadQuery(ProductoCantidad produC) {
        String query = "UPDATE ordenes_productos SET producto_id = ?, cantidad = ? "
                + "WHERE orden_id = ?";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, produC.getProductoId());
            pst.setInt(2, produC.getCantidad());
            pst.setInt(3, produC.getId());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos de producto/cantidad " + e);
            return false;        
        }         
    }
    
    public boolean borrarPoductoCantidadQuery(int id) {
        String query = "DELETE FROM ordenes_productos WHERE orden_id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;        
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede elminar esta tupla porque está referenciada en otra tabla.");
            return false;
        }    
    }
}
