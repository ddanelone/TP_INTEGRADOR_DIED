package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaminoSeleccionadoDao {
//Instanciar la conexión

    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //Variables para enviear datos entre interfaces
    public static int id = 0;
    public static int orden_provision_id = 0;
    public static int sucursal_origen_id = 0;
    public static int sucursal_destino_id = 0;
    public static String camino = "";
    public static int tiempo = 0;

    //No se programan los otros métodos porque al quedar la orden EN PROCESO, sería incosistente modificar o borrar un camino asignado.
    //Método para registrar un camino;
    public boolean registrarCaminoQuery(CaminoSeleccionado camino) {
        String query = "INSERT INTO caminos_seleccionados (id, orden_provision_id, sucursal_origen_id, sucursal_destino_id, camino, tiempo_estimado) VALUES(?,?,?,?,?,?)";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, camino.getId());
            pst.setInt(2, camino.getOrden_provision_id());
            pst.setInt(3, camino.getSucursal_origen_id());
            pst.setInt(4, camino.getSucursal_destino_id());
            pst.setString(5, camino.getCamino());
            pst.setInt(6, camino.getTiempo());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el camino seleccionado" + e);
            return false;
        }
    }

    //Como el id es autoincremental, previo a asignar el detalle de los articulos correspondientes, debo recuperar el id.
    public int recuperarIdUltimoCamino() {
        int id = 0;

        String query = "SELECT MAX(id) FROM caminos_seleccionados";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar el ID del último camino entre sucursales registrado: " + e);
        }

        return id;
    }
}
