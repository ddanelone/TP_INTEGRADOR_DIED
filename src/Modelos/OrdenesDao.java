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
    public static int sucursalOrigenId_ordenes = 0;
    public static int sucursalDestinoId_ordenes = 0;
    public static int id_caminos = 0;
    public static String fechaOrden_ordenes = "";
    public static int tiempoMaximo_ordenes = 0;
    public static String estado_ordenes = "";
    public static List<ProductoCantidad> listaProductos;

    //Método para registrar una orden;
    public boolean registrarOrdenQuery(Ordenes orden) {
        String query = "INSERT INTO ordenes_provision (sucursal_origen_id, sucursal_destino_id, fecha_orden, tiempo_maximo, estado, peso_total) VALUES(?,?,?,?,?,?)";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            //pst.setInt(1, orden.getId());
            pst.setInt(1, orden.getSucursalOrigenId());
            pst.setInt(2, orden.getSucursalDestinoId());
            java.sql.Date fechaSql = java.sql.Date.valueOf(orden.getFechaOrden());
            pst.setDate(3, fechaSql);
            pst.setInt(4, orden.getTiempoMaximo());
            pst.setString(5, orden.getEstado());
            pst.setDouble(6, orden.getPesoTotal());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la orden de provisión " + e);
            return false;
        }
    }

    //Método para registrar una orden;
    public boolean registrarDetalleOrdenQuery(int id_orden, int id_prod, int cant) {
        String query = "INSERT INTO ordenes_productos (orden_id, producto_id, cantidad) VALUES(?,?,?)";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id_orden);
            pst.setInt(2, id_prod);
            pst.setInt(3, cant);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el detalle de la orden de provisión" + e);
            return false;
        }
    }

    //Método para listar órdenes de provisión
    public List listaOrdenesQuery(String valor) {
        List<Ordenes> lista_ordenes = new ArrayList();
        String query = "SELECT * FROM ordenes_provision ORDER BY id ASC";
        String query_search_orden = "SELECT * FROM ordenes_provision WHERE id LIKE '%" + valor + "%'";

        try {
            conn = cn.getConnection();
            if (valor.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
            } else {
                pst = conn.prepareStatement(query_search_orden);
            }
            rs = pst.executeQuery();

            while (rs.next()) {
                Ordenes orden = new Ordenes();
                orden.setId(rs.getInt("id"));
                orden.setSucursalOrigenId(rs.getInt("sucursal_origen_id"));
                orden.setSucursalDestinoId(rs.getInt("sucursal_destino_id"));
                // Conversión de java.sql.Date a LocalDate
                java.sql.Date fechaSql = rs.getDate("fecha_orden");
                LocalDate fechaOrden = fechaSql.toLocalDate();
                orden.setFechaOrden(fechaOrden);
                orden.setTiempoMaximo(rs.getInt("tiempo_maximo"));
                orden.setEstado(rs.getString("estado"));
                orden.setCaminoId(rs.getInt("camino_seleccionado_id"));
                orden.setPesoTotal(rs.getDouble("peso_total"));
                lista_ordenes.add(orden);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar la información de las órdenes " + e);
        }
        return lista_ordenes;
    }

    //Método para listar órdenes de provisión
    public List<Ordenes> listarDetallesOrdenesQuery(int id_orden) {
        List<Ordenes> lista_ordenes = new ArrayList<>();
        String query = "SELECT op.id AS orden_id, op.sucursal_origen_id, op.sucursal_destino_id, op.fecha_orden, op.tiempo_maximo, op.estado,\n"
                + "       op.camino_seleccionado_id, o.producto_id, o.cantidad,\n"
                + "       e.id AS electrodomestico_id, e.nombre AS electrodomestico_nombre, e.descripcion AS electrodomestico_descripcion\n"
                + "FROM ordenes_provision op\n"
                + "JOIN ordenes_productos o ON op.id = o.orden_id\n"
                + "JOIN electrodomesticos e ON o.producto_id = e.id\n"
                + "WHERE op.id = ?";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id_orden); // Establecer el parámetro de la consulta
            rs = pst.executeQuery();

            while (rs.next()) {
                int ordenId = rs.getInt("orden_id");
                int productoId = rs.getInt("producto_id");
                int cantidad = rs.getInt("cantidad");

                // Buscar la orden correspondiente en la lista
                Ordenes orden = null;
                for (Ordenes existingOrden : lista_ordenes) {
                    if (existingOrden.getId() == ordenId) {
                        orden = existingOrden;
                        break;
                    }
                }

                // Si la orden no existe en la lista, crearla y agregarla
                if (orden == null) {
                    orden = new Ordenes();
                    orden.setId(ordenId);
                    orden.setSucursalOrigenId(rs.getInt("op.sucursal_origen_id"));
                    orden.setSucursalDestinoId(rs.getInt("op.sucursal_destino_id"));
                    java.sql.Date fechaSql = rs.getDate("op.fecha_orden");
                    LocalDate fechaOrden = fechaSql.toLocalDate();
                    orden.setFechaOrden(fechaOrden);
                    orden.setTiempoMaximo(rs.getInt("op.tiempo_maximo"));
                    orden.setEstado(rs.getString("op.estado"));
                    orden.setCaminoId(rs.getInt("op.camino_seleccionado_id"));
                    lista_ordenes.add(orden);
                }

                // Agregar el producto a la lista de productos de la orden
                ProductoCantidad productoCantidad = new ProductoCantidad(ordenId, productoId, cantidad);
                orden.getListaProductos().add(productoCantidad);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar la información de las órdenes: " + e);
        }
        return lista_ordenes;
    }

    //Método para modificar una orden;
    public boolean modificarOrdenQuery(Ordenes orden) {
        String query = "UPDATE ordenes_provision SET sucursal_origen_id = ?, sucursal_destino_id = ?, fecha_orden= ?, tiempo_maximo= ?, estado =?, camino_seleccionado_id=?, peso_total=? "
                + "WHERE id = ?";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, orden.getSucursalOrigenId());
            pst.setInt(2, orden.getSucursalDestinoId());
            java.sql.Date fechaSql = java.sql.Date.valueOf(orden.getFechaOrden());
            pst.setDate(3, fechaSql);
            pst.setInt(4, orden.getTiempoMaximo());
            pst.setString(5, orden.getEstado());
            pst.setInt(6, orden.getCaminoId());
            pst.setDouble(7, orden.getPesoTotal());
            pst.setInt(8, orden.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede elminar esta orden porque está referenciada en otra tabla.");
            return false;
        }
    }

    //Como el id es autoincremental, previo a asignar el detalle de los articulos correspondientes, debo recuperar el id.
    public int recuperarIdUltimaOrden() {
        int id = 0;

        String query = "SELECT MAX(id) FROM ordenes_provision";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar el ID de la última orden: " + e);
        }

        return id;
    }

}
