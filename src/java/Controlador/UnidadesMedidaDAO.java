package Controlador;

import Conexion.Conexion;
import Modelo.UnidadesMedida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "unidadesMedida".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class UnidadesMedidaDAO {

    public boolean insertar(UnidadesMedida modelo) {
        String sql = "INSERT INTO unidadesMedida (descripcionUnidadesMed) VALUES (?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionUnidadesM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en unidadesMedida: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(UnidadesMedida modelo) {
        String sql = "UPDATE unidadesMedida SET descripcionUnidadesMed = ? WHERE idUnidadesMedida = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionUnidadesM());
            ps.setInt(2, modelo.getIdUnidadesMedida());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en unidadesMedida: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idUnidadesMedida) {
        String sql = "DELETE FROM unidadesMedida WHERE idUnidadesMedida = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUnidadesMedida);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en unidadesMedida: " + e.getMessage());
            return false;
        }
    }

    public UnidadesMedida consultarPorId(int idUnidadesMedida) {
        String sql = "SELECT * FROM unidadesMedida WHERE idUnidadesMedida = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUnidadesMedida);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar unidadesMedida por id: " + e.getMessage());
        }
        return null;
    }

    public List<UnidadesMedida> listarTodos() {
        List<UnidadesMedida> lista = new ArrayList<>();
        String sql = "SELECT * FROM unidadesMedida";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar unidadesMedida: " + e.getMessage());
        }
        return lista;
    }

    private UnidadesMedida mapear(ResultSet rs) throws SQLException {
        UnidadesMedida modelo = new UnidadesMedida();
        modelo.setIdUnidadesMedida(rs.getInt("idUnidadesMedida"));
        modelo.setDescripcionUnidadesM(rs.getString("descripcionUnidadesMed"));
        return modelo;
    }
}
