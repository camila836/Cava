package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.UnidadesMedida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla unidadesMedida. */
public class UnidadesMedidaDAO {

    private static final String COLUMNS =
            "idUnidadesMedida, descripcionUnidadesMed";
    private static final String INSERT =
            "INSERT INTO unidadesMedida (descripcionUnidadesMed) VALUES (?)";
    private static final String UPDATE =
            "UPDATE unidadesMedida SET descripcionUnidadesMed = ? WHERE idUnidadesMedida = ?";
    private static final String DELETE =
            "DELETE FROM unidadesMedida WHERE idUnidadesMedida = ?";
    private static final String SELECT_BY_ID =
            "SELECT " + COLUMNS + " FROM unidadesMedida WHERE idUnidadesMedida = ?";
    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM unidadesMedida "
            + "ORDER BY descripcionUnidadesMed, idUnidadesMedida";

    public boolean insertar(UnidadesMedida unidad) {
        try (Connection connection = Conexion.getConn()) {
            return insertar(connection, unidad);
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.insertar", exception);
        }
    }

    public boolean insertar(Connection connection, UnidadesMedida unidad) {
        try (PreparedStatement statement = connection.prepareStatement(
                INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, unidad.getDescripcionUnidadesM());
            SQLExceptionTranslator.requireAffected(statement.executeUpdate(),
                    "UnidadesMedida.insertar");
            assignGeneratedId(statement, unidad);
            return true;
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.insertar", exception);
        }
    }

    public boolean actualizar(UnidadesMedida unidad) {
        try (Connection connection = Conexion.getConn()) {
            return actualizar(connection, unidad);
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.actualizar", exception);
        }
    }

    public boolean actualizar(Connection connection, UnidadesMedida unidad) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, unidad.getDescripcionUnidadesM());
            statement.setInt(2, unidad.getIdUnidadesMedida());
            SQLExceptionTranslator.requireAffected(statement.executeUpdate(),
                    "UnidadesMedida.actualizar");
            return true;
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.actualizar", exception);
        }
    }

    public boolean eliminar(int id) {
        try (Connection connection = Conexion.getConn()) {
            return eliminar(connection, id);
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.eliminar", exception);
        }
    }

    public boolean eliminar(Connection connection, int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            SQLExceptionTranslator.requireAffected(statement.executeUpdate(),
                    "UnidadesMedida.eliminar");
            return true;
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.eliminar", exception);
        }
    }

    public UnidadesMedida consultarPorId(int id) {
        try (Connection connection = Conexion.getConn()) {
            return consultarPorId(connection, id);
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.consultarPorId", exception);
        }
    }

    public UnidadesMedida consultarPorId(Connection connection, int id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.consultarPorId", exception);
        }
    }

    public List<UnidadesMedida> listarTodos() {
        try (Connection connection = Conexion.getConn()) {
            return listarTodos(connection);
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.listarTodos", exception);
        }
    }

    public List<UnidadesMedida> listarTodos(Connection connection) {
        List<UnidadesMedida> unidades = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                unidades.add(mapear(result));
            }
            return unidades;
        } catch (SQLException exception) {
            throw SQLExceptionTranslator.translate("UnidadesMedida.listarTodos", exception);
        }
    }

    private void assignGeneratedId(PreparedStatement statement, UnidadesMedida unidad)
            throws SQLException {
        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (!keys.next()) {
                throw new SQLException("No generated key returned");
            }
            unidad.setIdUnidadesMedida(keys.getInt(1));
        }
    }

    private UnidadesMedida mapear(ResultSet result) throws SQLException {
        UnidadesMedida unidad = new UnidadesMedida();
        unidad.setIdUnidadesMedida(result.getInt("idUnidadesMedida"));
        unidad.setDescripcionUnidadesM(result.getString("descripcionUnidadesMed"));
        return unidad;
    }
}
