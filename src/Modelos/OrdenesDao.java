package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdenesDao {
    //Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //Variables para enviear datos entre interfaces
    public static int id_ordenes = 0;
    public static int sucursalOrigenId_ordenes=0;
    public static int sucursalDestinoId_ordenes=0;
    public static String fechaOrden_ordenes="";
    public static int tiempoMaximo_ordenes=0;
    public static String estado_ordenes="";
    public static List<ProductoCantidad> listaProductos; 
    
     //Método para registrar una orden;
    public boolean registrarCaminoQuery(Ordenes orden) {
        String query = "INSERT INTO ordenes_provision (id, sucursal_origen_id, sucursal_destino_id, fecha_orden, tiempo_maximo, estado) VALUES(?,?,?,?,?,?)";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, orden.getId());
            pst.setInt(2, orden.getSucursalOrigenId());
            pst.setInt(3, orden.getSucursalDestinoId());
            java.sql.Date fechaSql = java.sql.Date.valueOf(orden.getFechaOrden());
            pst.setDate(4, fechaSql);
            pst.setInt(5, orden.getTiempoMaximo());
            pst.setString(6,orden.getEstado());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la orden de provisión" + e);
            return false;        
        }         
    }
    
    //Método para listar órdenes de provisión
    public List listaOrdenesQuery(String valor) {
        List<Ordenes> lista_ordenes = new ArrayList();
        String query = "SELECT * FROM ordenes_provision ORDER BY id ASC";
        String query_search_orden = "SELECT * FROM ordenes_provision WHERE id LIKE '%" + valor + "%'";
        
        try{
            conn = cn.getConnection();
            if (valor.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
            } else {
                pst = conn.prepareStatement(query_search_orden);
            }
            rs = pst.executeQuery();
            
            while(rs.next()) {
                Ordenes orden  = new Ordenes();
                orden.setId(rs.getInt("id"));
                orden.setSucursalOrigenId(rs.getInt("sucursal_origen_id"));
                orden.setSucursalDestinoId(rs.getInt("sucursal_destino_id"));
                // Conversión de java.sql.Date a LocalDate
                java.sql.Date fechaSql = rs.getDate("fecha_orden");
                LocalDate fechaOrden = fechaSql.toLocalDate();
                orden.setFechaOrden(fechaOrden);
                orden.setTiempoMaximo(rs.getInt("tiempo_maximo"));
                orden.setEstado(rs.getString("estado"));
                lista_ordenes.add(orden);
            }            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar la información de las órdenes " + e);                   
        } 
        return lista_ordenes;
    }
    
    //Método para modificar una orden;
    public boolean modificarOrdenQuery(Ordenes orden) {
        String query = "UPDATE ordenes_provision SET sucursal_origen_id = ?, sucursal_destino_id = ?, fecha_orden= ?, tiempo_maximo= ?, estado =?, "
                + "WHERE id = ?";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, orden.getSucursalOrigenId());
            pst.setInt(2, orden.getSucursalDestinoId());
            java.sql.Date fechaSql = java.sql.Date.valueOf(orden.getFechaOrden());
            pst.setDate(3, fechaSql);
            pst.setInt(4, orden.getTiempoMaximo());
            pst.setString(5, orden.getEstado());
            pst.setInt(6, orden.getId());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos de la orden " + e);
            return false;        
        }         
    }
    
    //Método para eliminar orden   OJOTA!!! REVISAR QUE SEA CORRECTO LA FORMA EN QUE IMPLEMENTÉ PRIMERO ELIMINAR LAS TUPLAS EN ORDENS_PRODUCTOS
    public boolean borrarOrdenQuery(int id) {
        String query = "DELETE FROM ordenes_provision WHERE id = " + id;
        String query2 = "DELETE FROM ordenes_productos WHERE orden_id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query2);
            pst.execute();
            //conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;        
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede elminar esta orden porque está referenciada en otra tabla.");
            return false;
        }    
    }
}
