package Controlador;

import Conexion.Conexion;
import Modelo.TipoDocumento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "tipoDocumento".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class TipoDocumentoDAO {

    public boolean insertar(TipoDocumento modelo) {
        String sql = "INSERT INTO tipoDocumento (descripcion) VALUES (?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en tipoDocumento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(TipoDocumento modelo) {
        String sql = "UPDATE tipoDocumento SET descripcion = ? WHERE idTipoDocumento = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcion());
            ps.setInt(2, modelo.getIdTipoDocumento());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en tipoDocumento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idTipoDocumento) {
        String sql = "DELETE FROM tipoDocumento WHERE idTipoDocumento = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoDocumento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en tipoDocumento: " + e.getMessage());
            return false;
        }
    }

    public TipoDocumento consultarPorId(int idTipoDocumento) {
        String sql = "SELECT * FROM tipoDocumento WHERE idTipoDocumento = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar tipoDocumento por id: " + e.getMessage());
        }
        return null;
    }

    public List<TipoDocumento> listarTodos() {
        List<TipoDocumento> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipoDocumento";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tipoDocumento: " + e.getMessage());
        }
        return lista;
    }

    private TipoDocumento mapear(ResultSet rs) throws SQLException {
        TipoDocumento modelo = new TipoDocumento();
        modelo.setIdTipoDocumento(rs.getInt("idTipoDocumento"));
        modelo.setDescripcion(rs.getString("descripcion"));
        return modelo;
    }
}
