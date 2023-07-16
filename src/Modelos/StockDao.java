package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDao {
    //Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //Variables para enviear datos entre interfaces
    public static int id_sucursal_stock=0;
    public static int id_producto_stock=0;
    public static int stock_stock=0;
    
    //Método para registrar stock en una sucursal;
    public boolean registrarStockQuery(Stock stock) {
        String query = "INSERT INTO stock(id_sucursal, id_producto, stock) VALUES(?,?,?)";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, stock.getId_sucursal());
            pst.setInt(2, stock.getId_producto());
            pst.setInt(3, stock.getStock());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar existencia de producto en la sucursal " + e);
            return false;        
        }         
    }
    
    //Método para listar existencia de productos en sucursal
    public List listaStockQuery(String valor) {
        List<Stock> lista_stock = new ArrayList();
        String query = "SELECT * FROM stock ORDER BY id_sucursal ASC";
        String query_search_sucursal = "SELECT * FROM stock WHERE id_sucursal LIKE '%" + valor + "%'";
        
        try{
            conn = cn.getConnection();
            if (valor.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
            } else {
                pst = conn.prepareStatement(query_search_sucursal);
            }
            rs = pst.executeQuery();
            
            while(rs.next()) {
                Stock stock = new Stock();
                stock.setId_sucursal(rs.getInt("id_sucursal"));
                stock.setId_producto(rs.getInt("id_producto"));
                stock.setStock(rs.getInt("stock"));
                lista_stock.add(stock);
            }
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar los datos de stock en sucursales " + e);                   
        } 
        return lista_stock;
    }
    
    //Método para modificar una existencia sucursal;
    public boolean modificarStockQuery(Stock stock) {
        String query = "UPDATE stock SET stock = ?"
                + "WHERE id_sucursal = ? AND id_producto = ?";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, stock.getStock());
            pst.setInt(2, stock.getId_sucursal());
            pst.setInt(3, stock.getId_producto());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos de existencia de producto en la sucursal " + e);
            return false;        
        }         
    }
    
    //Método para eliminar la existencia en sucursal
    public boolean borrarStockQuery(int id_sucursal, int id_producto) {
        String query = "DELETE FROM stock WHERE id_sucursal = " + id_sucursal + "AND id_producto = " + id_producto;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;        
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede elminar esta existencia de producto en sucursal, porque está referenciada en otra tabla.");
            return false;
        }    
    }
    
}
