package Controlador;

import Conexion.Conexion;
import Modelo.Transportadoras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "transportadoras".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class TransportadorasDAO {

    public boolean insertar(Transportadoras modelo) {
        String sql = "INSERT INTO transportadoras (nombreTransportadoras, nit, correo, telefono) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getNombreTransportadoras());
            ps.setString(2, modelo.getNit());
            ps.setString(3, modelo.getCorreo());
            ps.setString(4, modelo.getTelefono());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en transportadoras: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Transportadoras modelo) {
        String sql = "UPDATE transportadoras SET nombreTransportadoras = ?, nit = ?, correo = ?, telefono = ? WHERE idTransportadoras = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getNombreTransportadoras());
            ps.setString(2, modelo.getNit());
            ps.setString(3, modelo.getCorreo());
            ps.setString(4, modelo.getTelefono());
            ps.setInt(5, modelo.getIdTransportadoras());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en transportadoras: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idTransportadoras) {
        String sql = "DELETE FROM transportadoras WHERE idTransportadoras = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTransportadoras);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en transportadoras: " + e.getMessage());
            return false;
        }
    }

    public Transportadoras consultarPorId(int idTransportadoras) {
        String sql = "SELECT * FROM transportadoras WHERE idTransportadoras = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTransportadoras);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar transportadoras por id: " + e.getMessage());
        }
        return null;
    }

    public List<Transportadoras> listarTodos() {
        List<Transportadoras> lista = new ArrayList<>();
        String sql = "SELECT * FROM transportadoras";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar transportadoras: " + e.getMessage());
        }
        return lista;
    }

    private Transportadoras mapear(ResultSet rs) throws SQLException {
        Transportadoras modelo = new Transportadoras();
        modelo.setIdTransportadoras(rs.getInt("idTransportadoras"));
        modelo.setNombreTransportadoras(rs.getString("nombreTransportadoras"));
        modelo.setNit(rs.getString("nit"));
        modelo.setCorreo(rs.getString("correo"));
        modelo.setTelefono(rs.getString("telefono"));
        return modelo;
    }
}
