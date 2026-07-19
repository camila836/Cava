package Controlador;

import Conexion.Conexion;
import Modelo.Ciudades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "ciudades".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class CiudadesDAO {

    public boolean insertar(Ciudades modelo) {
        String sql = "INSERT INTO ciudades (codigoCiudad, nombreCiudad, codigoPostal) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getCodigoCiudad());
            ps.setString(2, modelo.getNombreCiudad());
            ps.setString(3, modelo.getCodigoPostal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en ciudades: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Ciudades modelo) {
        String sql = "UPDATE ciudades SET codigoCiudad = ?, nombreCiudad = ?, codigoPostal = ? WHERE idCiudades = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getCodigoCiudad());
            ps.setString(2, modelo.getNombreCiudad());
            ps.setString(3, modelo.getCodigoPostal());
            ps.setInt(4, modelo.getIdCiudades());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en ciudades: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCiudades) {
        String sql = "DELETE FROM ciudades WHERE idCiudades = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCiudades);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en ciudades: " + e.getMessage());
            return false;
        }
    }

    public Ciudades consultarPorId(int idCiudades) {
        String sql = "SELECT * FROM ciudades WHERE idCiudades = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCiudades);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar ciudades por id: " + e.getMessage());
        }
        return null;
    }

    public List<Ciudades> listarTodos() {
        List<Ciudades> lista = new ArrayList<>();
        String sql = "SELECT * FROM ciudades";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar ciudades: " + e.getMessage());
        }
        return lista;
    }

    private Ciudades mapear(ResultSet rs) throws SQLException {
        Ciudades modelo = new Ciudades();
        modelo.setIdCiudades(rs.getInt("idCiudades"));
        modelo.setCodigoCiudad(rs.getString("codigoCiudad"));
        modelo.setNombreCiudad(rs.getString("nombreCiudad"));
        modelo.setCodigoPostal(rs.getString("codigoPostal"));
        return modelo;
    }
}
