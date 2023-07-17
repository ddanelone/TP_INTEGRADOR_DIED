package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaminosDao {
    //Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //Variables para enviear datos entre interfaces
    public static int id_camino = 0;
    public static int origenId_camino=0;
    public static int destinoId_camino=0;
    public static  int capacidad_camino=0;
    public static  int tiempo_camino=0;
    public static  boolean operativo_camino;
    public static  String observaciones_camino="";
    
    //Método para registrar un camino;
    public boolean registrarCaminoQuery(Caminos camino) {
        String query = "INSERT INTO caminos(id, origen_id, destino_id, capacidad,tiempo,operativo,observaciones ) VALUES(?,?,?,?,?,?,?)";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, camino.getId());
            pst.setInt(2, camino.getOrigenId());
            pst.setInt(3, camino.getDestinoId());
            pst.setInt(4, camino.getCapacidad());
            pst.setInt(5, camino.getTiempo());
            pst.setBoolean(6, camino.isOperativo());
            pst.setString(7,camino.getObservaciones());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el camino " + e);
            return false;        
        }         
    }
    
    //Método para listar sucursal
    public List listaCaminosQuery(String valor) {
        List<Caminos> lista_caminos = new ArrayList();
        String query = "SELECT * FROM caminos ORDER BY id ASC";
        String query_search_camino = "SELECT * FROM caminos WHERE id LIKE '%" + valor + "%'";
        
        try{
            conn = cn.getConnection();
            if (valor.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
            } else {
                pst = conn.prepareStatement(query_search_camino);
            }
            rs = pst.executeQuery();
            
            while(rs.next()) {
                Caminos camino = new Caminos();
                camino.setId(rs.getInt("id"));
                camino.setOrigenId(rs.getInt("origen_id"));
                camino.setDestinoId(rs.getInt("destino_id"));
                camino.setCapacidad(rs.getInt("capacidad"));
                camino.setTiempo(rs.getInt("tiempo"));
                camino.setOperativo(rs.getBoolean("operativo"));
                camino.setObservaciones(rs.getString("observaciones"));
                lista_caminos.add(camino);
            }            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar los datos de caminos");                   
        } 
        return lista_caminos;
    }
    
    //Método para modificar un camino;
    public boolean modificarCaminoQuery(Caminos camino) {
        String query = "UPDATE caminos SET origen_id = ?, destino_id = ?, capacidad = ?, tiempo= ?, operativo =?, observaciones = ?"
                + "WHERE id = ?";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, camino.getOrigenId());
            pst.setInt(2, camino.getDestinoId());
            pst.setInt(3, camino.getCapacidad());
            pst.setInt(4, camino.getTiempo());
            pst.setBoolean(5, camino.isOperativo());
            pst.setString(6,camino.getObservaciones());
            pst.setInt(7, camino.getId());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del camino " + e);
            return false;        
        }         
    }
   
    
    //Método para modificar un camino;
    public boolean modificarCaminoEstadoEstadoQuery(Caminos camino) {
        String query = "UPDATE caminos SET operativo =? WHERE id = ?";
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setBoolean(1, camino.isOperativo());
            pst.setInt(2, camino.getId());
            pst.execute();
            return true;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar estado Operativo del camino " + e);
            return false;        
        }         
    }
    
    //Método para eliminar camino
    public boolean borrarCaminoQuery(int id) {
        String query = "DELETE FROM caminos WHERE id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;        
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede elminar este camino porque está referenciada en otra tabla.");
            return false;
        }    
    }   
}
