package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SucursalesDao {
    //Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //Variables para enviear datos entre interfaces
    public static int id_sucursal = 0;
    public static int codigo = 0;
    public static String nombre = "";
    public static  String horarioApertura="";
    public static  String horarioCierre="";
    public static  boolean operativa;
    
    //Método para registrar una sucursal;
    public boolean registrarSucursalQuery(Sucursales sucursal) {
        String query = "INSERT INTO sucursales(id, codigo, nombre, horario_apertura,horario_cierre, operativa) VALUES(?,?,?,?,?,?)";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, sucursal.getId());
            pst.setInt(2, sucursal.getCodigo());
            pst.setString(3, sucursal.getNombre());
            pst.setString(4, sucursal.getHorarioApertura());
            pst.setString(5, sucursal.getHorarioCierre());
            pst.setBoolean(6, sucursal.isOperativa());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la sucursal " + e);
            return false;        
        }         
    }
    
    //Método para listar sucursal
    public List listaSucursalesQuery(String valor) {
        List<Sucursales> lista_sucursales = new ArrayList();
        String query = "SELECT * FROM sucursales ORDER BY nombre ASC";
        String query_search_sucursal = "SELECT * FROM sucursales WHERE id LIKE '%" + valor + "%'";
        
        try{
            conn = cn.getConnection();
            if (valor.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
            } else {
                pst = conn.prepareStatement(query_search_sucursal);
            }
            rs = pst.executeQuery();
            
            while(rs.next()) {
                Sucursales sucursal = new Sucursales();
                sucursal.setId(rs.getInt("id"));
                sucursal.setCodigo(rs.getInt("codigo"));
                sucursal.setNombre(rs.getString("nombre"));
                sucursal.setHorarioApertura(rs.getString("horario_apertura"));
                sucursal.setHorarioCierre(rs.getString("horario_cierre"));
                sucursal.setOperativa(rs.getBoolean("operativa"));
                lista_sucursales.add(sucursal);
            }
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar los datos de sucursales");                   
        } 
        return lista_sucursales;
    }
    
    //Método para modificar una sucursal;
    public boolean modificarSucursalQuery(Sucursales sucursal) {
        String query = "UPDATE sucursales SET codigo= ?, nombre = ?, horario_apertura= ?,horario_cierre= ?, operativa =? "
                + "WHERE id = ?";
           
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, sucursal.getCodigo());
            pst.setString(2, sucursal.getNombre());
            pst.setString(3, sucursal.getHorarioApertura());
            pst.setString(4, sucursal.getHorarioCierre());
            pst.setBoolean(5, sucursal.isOperativa());
            pst.setInt(6, sucursal.getId());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos de la sucursal " + e);
            return false;        
        }         
    }
    
    //Método para modificar el estado Operativo de una sucursal;
    public boolean modificarSucursalEstadoQuery(Sucursales sucursal) {
        String query = "UPDATE sucursales SET operativa =? WHERE id = ?";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setBoolean(1, sucursal.isOperativa());
            pst.setInt(2, sucursal.getId());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el Eatado Operativo de la sucursal " + e);
            return false;        
        }         
    }
    
    //Método para eliminar sucursal
    public boolean borrarSucursalQuery(int id) {
        String query = "DELETE FROM sucursales WHERE id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;        
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede elminar esta sucursal, porque está referenciada en otra tabla.");
            return false;
        }    
    }    
}
