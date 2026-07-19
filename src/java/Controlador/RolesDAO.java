package Controlador;

import Conexion.Conexion;
import Modelo.Roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "roles".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class RolesDAO {

    public boolean insertar(Roles modelo) {
        String sql = "INSERT INTO roles (descripcionRol) VALUES (?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en roles: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Roles modelo) {
        String sql = "UPDATE roles SET descripcionRol = ? WHERE idRoles = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionRol());
            ps.setInt(2, modelo.getIdRoles());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en roles: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idRoles) {
        String sql = "DELETE FROM roles WHERE idRoles = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRoles);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en roles: " + e.getMessage());
            return false;
        }
    }

    public Roles consultarPorId(int idRoles) {
        String sql = "SELECT * FROM roles WHERE idRoles = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRoles);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar roles por id: " + e.getMessage());
        }
        return null;
    }

    public List<Roles> listarTodos() {
        List<Roles> lista = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar roles: " + e.getMessage());
        }
        return lista;
    }

    private Roles mapear(ResultSet rs) throws SQLException {
        Roles modelo = new Roles();
        modelo.setIdRoles(rs.getInt("idRoles"));
        modelo.setDescripcionRol(rs.getString("descripcionRol"));
        return modelo;
    }
}
