package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ElectrodomesticosDao {
      //Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //Variables para enviear datos entre interfaces
    public static int id_electrodomestico = 0;
    public static int codigo_electrodomestico = 0;
    public static String nombre_electrodomestico = "";
    public static String descripcion_electrodomestico = "";
    public static Double precio_unitario_electrodomestico = 0.0;
    public static Double peso_kg_electrodomestico = 0.0;
    
    //Método para registrar una sucursal;
    public boolean registrarElectrodomesticoQuery(Electrodomesticos electrodomestico) {
        String query = "INSERT INTO electrodomesticos(id, codigo, nombre, descripcion,precio_unitario, peso_kg) VALUES(?,?,?,?,?,?)";
        
        try{ conn = cn.getConnection();
            //conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, electrodomestico.getId());
            pst.setInt(2, electrodomestico.getCodigo());
            pst.setString(3, electrodomestico.getNombre());
            pst.setString(4, electrodomestico.getDescripcion());
            pst.setDouble(5, electrodomestico.getPrecioUnitario());
            pst.setDouble(6, electrodomestico.getPesoKg());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el electrodoméstico " + e);
            return false;        
        }
    }
    
    //Método para listar electrodomésticos
    public List listaElectrodomesticosQuery(String valor) {
        List<Electrodomesticos> lista_electrodomesticos = new ArrayList();
        String query = "SELECT * FROM electrodomesticos ORDER BY nombre ASC";
        String query_search_electrodomestico = "SELECT * FROM electrodomesticos WHERE id LIKE '%" + valor + "%'";
        
        try{ conn = cn.getConnection();             
            if (valor.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
            } else {
                pst = conn.prepareStatement(query_search_electrodomestico);
            }
            rs = pst.executeQuery();
            
            while(rs.next()) {
                Electrodomesticos electrodomestico = new Electrodomesticos();
                electrodomestico.setId(rs.getInt("id"));
                electrodomestico.setCodigo(rs.getInt("codigo"));
                electrodomestico.setNombre(rs.getString("nombre"));
                electrodomestico.setDescripcion(rs.getString("descripcion"));
                electrodomestico.setPrecioUnitario(rs.getDouble("precio_unitario"));
                electrodomestico.setPesoKg(rs.getDouble("peso_kg"));
                lista_electrodomesticos.add(electrodomestico);
            }            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar los datos de electrodomestícos " + e);                   
        } 
        return lista_electrodomesticos;
    }
    
    //Método para modificar un electrodomestico
    public boolean modificarElectrodomesticoQuery(Electrodomesticos electrodomestico) {
        String query = "UPDATE electrodomesticos SET codigo= ?, nombre = ?, descripcion = ?, precio_unitario = ?, peso_kg =? "
                + "WHERE id = ?";
        
        try{ conn = cn.getConnection();            
            pst = conn.prepareStatement(query);
            pst.setInt(1, electrodomestico.getCodigo());
            pst.setString(2, electrodomestico.getNombre());
            pst.setString(3, electrodomestico.getDescripcion());
            pst.setDouble(4, electrodomestico.getPrecioUnitario());
            pst.setDouble(5, electrodomestico.getPesoKg());
            pst.setInt(6, electrodomestico.getId());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del electrodomestico " + e);
            return false;        
        }         
    }
    
    //Método para eliminar sucursal
    public boolean borrarElectrodomesticoQuery(int id) {
        String query = "DELETE FROM electrodomesticos WHERE id = " + id;
        try {             conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;        
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede elminar este producto porque está referenciado en otra tabla.");
            return false;
        }    
    }
}
