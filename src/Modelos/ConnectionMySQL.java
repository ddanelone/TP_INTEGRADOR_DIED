package Modelos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    private String database_name = "died_tp";
    private String user = "root";
    private String password ="root";
    //El puerto va a depender de la configuracón del mySQL Workbench de cada uno. 8800 es el mío.
    private String url = "jdbc:mysql://localhost:8800/" + database_name;
    Connection conn = null;
    
    public Connection getConnection() {
        try {
            //Obtener el valor del driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Obtener la conexión
            conn = DriverManager.getConnection(url,user,password);
        } catch(ClassNotFoundException e) {
            System.err.println("Ha ocurrido un ClassNotFouendExcepción " + e.getMessage());
        } catch (SQLException e) {
        System.err.println("Ha ocurrido un SQLEception " + e.getMessage());
        }
        return conn;
    }
    
}
