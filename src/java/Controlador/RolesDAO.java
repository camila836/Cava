package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.Roles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla roles. */
public class RolesDAO {
    private static final String COLUMNS = "idRoles, descripcionRol";
    private static final String INSERT = "INSERT INTO roles (descripcionRol) VALUES (?)";
    private static final String UPDATE = "UPDATE roles SET descripcionRol = ? WHERE idRoles = ?";
    private static final String DELETE = "DELETE FROM roles WHERE idRoles = ?";
    private static final String SELECT_BY_ID = "SELECT " + COLUMNS + " FROM roles WHERE idRoles = ?";
    private static final String SELECT_ALL = "SELECT " + COLUMNS + " FROM roles";

    public boolean insertar(Roles modelo) {
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, modelo.getDescripcionRol());
            SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "Roles.insertar");
            asignarIdGenerado(ps, modelo);
            return true;
        } catch (SQLException e) { throw SQLExceptionTranslator.translate("Roles.insertar", e); }
    }
    public boolean actualizar(Roles modelo) {
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, modelo.getDescripcionRol()); ps.setInt(2, modelo.getIdRoles());
            SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "Roles.actualizar"); return true;
        } catch (SQLException e) { throw SQLExceptionTranslator.translate("Roles.actualizar", e); }
    }
    public boolean eliminar(int idRoles) {
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, idRoles); SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "Roles.eliminar"); return true;
        } catch (SQLException e) { throw SQLExceptionTranslator.translate("Roles.eliminar", e); }
    }
    public Roles consultarPorId(int idRoles) {
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, idRoles); try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        } catch (SQLException e) { throw SQLExceptionTranslator.translate("Roles.consultarPorId", e); }
    }
    public List<Roles> listarTodos() {
        List<Roles> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(SELECT_ALL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs)); return lista;
        } catch (SQLException e) { throw SQLExceptionTranslator.translate("Roles.listarTodos", e); }
    }
    private void asignarIdGenerado(PreparedStatement ps, Roles modelo) throws SQLException { try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) modelo.setIdRoles(keys.getInt(1)); } }
    private Roles mapear(ResultSet rs) throws SQLException { Roles m = new Roles(); m.setIdRoles(rs.getInt("idRoles")); m.setDescripcionRol(rs.getString("descripcionRol")); return m; }
}
