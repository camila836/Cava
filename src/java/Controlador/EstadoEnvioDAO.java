package Controlador;

import Conexion.Conexion;
import Modelo.EstadoEnvio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "estadoEnvio".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class EstadoEnvioDAO {

    public boolean insertar(EstadoEnvio modelo) {
        String sql = "INSERT INTO estadoEnvio (descripcionEstadoEnvio) VALUES (?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionEstadoEnvio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en estadoEnvio: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(EstadoEnvio modelo) {
        String sql = "UPDATE estadoEnvio SET descripcionEstadoEnvio = ? WHERE idEstadoEnvio = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionEstadoEnvio());
            ps.setInt(2, modelo.getIdEstadoEnvio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en estadoEnvio: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idEstadoEnvio) {
        String sql = "DELETE FROM estadoEnvio WHERE idEstadoEnvio = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstadoEnvio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en estadoEnvio: " + e.getMessage());
            return false;
        }
    }

    public EstadoEnvio consultarPorId(int idEstadoEnvio) {
        String sql = "SELECT * FROM estadoEnvio WHERE idEstadoEnvio = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstadoEnvio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar estadoEnvio por id: " + e.getMessage());
        }
        return null;
    }

    public List<EstadoEnvio> listarTodos() {
        List<EstadoEnvio> lista = new ArrayList<>();
        String sql = "SELECT * FROM estadoEnvio";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar estadoEnvio: " + e.getMessage());
        }
        return lista;
    }

    private EstadoEnvio mapear(ResultSet rs) throws SQLException {
        EstadoEnvio modelo = new EstadoEnvio();
        modelo.setIdEstadoEnvio(rs.getInt("idEstadoEnvio"));
        modelo.setDescripcionEstadoEnvio(rs.getString("descripcionEstadoEnvio"));
        return modelo;
    }
}
