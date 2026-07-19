package Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Única puerta de acceso al DataSource del pool JNDI "jdbc/CavaDS".
 * El lookup se hace una sola vez y se cachea en una variable estática.
 */
public class ConexionPool {

    private static volatile DataSource ds;

    private ConexionPool() {
    }

    private static DataSource obtenerDS() throws NamingException {
        if (ds == null) {
            synchronized (ConexionPool.class) {
                if (ds == null) {
                    Context ctx = new InitialContext();
                    ds = (DataSource) ctx.lookup("jdbc/CavaDS");
                }
            }
        }
        return ds;
    }

    public static Connection obtener() throws SQLException {
        try {
            return obtenerDS().getConnection();
        } catch (NamingException e) {
            throw new SQLException("Error JNDI al obtener jdbc/CavaDS: " + e.getMessage(), e);
        }
    }
}
