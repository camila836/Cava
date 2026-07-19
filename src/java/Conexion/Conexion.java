package Conexion;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Wrapper puro hacia ConexionPool.obtener().
 * No agregar lógica adicional aquí.
 */
public class Conexion {

    private Conexion() {
    }

    public static Connection getConn() throws SQLException {
        return ConexionPool.obtener();
    }
}
