package Controlador;

import Conexion.Conexion;
import Modelo.MediosPagos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "mediosPagos".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class MediosPagosDAO {

    public boolean insertar(MediosPagos modelo) {
        String sql = "INSERT INTO mediosPagos (descripcionMediosPagos) VALUES (?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionMediosPagos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en mediosPagos: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(MediosPagos modelo) {
        String sql = "UPDATE mediosPagos SET descripcionMediosPagos = ? WHERE idMediosPagos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionMediosPagos());
            ps.setInt(2, modelo.getIdMediosPagos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en mediosPagos: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idMediosPagos) {
        String sql = "DELETE FROM mediosPagos WHERE idMediosPagos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMediosPagos);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en mediosPagos: " + e.getMessage());
            return false;
        }
    }

    public MediosPagos consultarPorId(int idMediosPagos) {
        String sql = "SELECT * FROM mediosPagos WHERE idMediosPagos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMediosPagos);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar mediosPagos por id: " + e.getMessage());
        }
        return null;
    }

    public List<MediosPagos> listarTodos() {
        List<MediosPagos> lista = new ArrayList<>();
        String sql = "SELECT * FROM mediosPagos";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mediosPagos: " + e.getMessage());
        }
        return lista;
    }

    private MediosPagos mapear(ResultSet rs) throws SQLException {
        MediosPagos modelo = new MediosPagos();
        modelo.setIdMediosPagos(rs.getInt("idMediosPagos"));
        modelo.setDescripcionMediosPagos(rs.getString("descripcionMediosPagos"));
        return modelo;
    }
}
