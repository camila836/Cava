package Controlador;

import Conexion.Conexion;
import Modelo.Usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "usuarios".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class UsuariosDAO {

    public boolean insertar(Usuarios modelo) {
        String sql = "INSERT INTO usuarios (nombres, apellidos, identificacion, correo, direccion, telefono, clave, isActivo, fechaNacimiento, fechaVencimientoClave, autorizacionTratamientoDatos, idRoles, idTipoDocumento, idCiudades) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getNombres());
            ps.setString(2, modelo.getApellidos());
            ps.setString(3, modelo.getIdentificacion());
            ps.setString(4, modelo.getCorreo());
            ps.setString(5, modelo.getDireccion());
            ps.setString(6, modelo.getTelefono());
            ps.setString(7, modelo.getClave());
            ps.setBoolean(8, modelo.isActivo());
            ps.setDate(9, modelo.getFechaNacimiento() != null ? java.sql.Date.valueOf(modelo.getFechaNacimiento()) : null);
            ps.setDate(10, modelo.getFechaVencimientoClave() != null ? java.sql.Date.valueOf(modelo.getFechaVencimientoClave()) : null);
            ps.setBoolean(11, modelo.isAutorizacionTratamientoDatos());
            ps.setInt(12, modelo.getIdRoles());
            ps.setInt(13, modelo.getIdTipoDocumento());
            ps.setInt(14, modelo.getIdCiudades());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en usuarios: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Usuarios modelo) {
        String sql = "UPDATE usuarios SET nombres = ?, apellidos = ?, identificacion = ?, correo = ?, direccion = ?, telefono = ?, clave = ?, isActivo = ?, fechaNacimiento = ?, fechaVencimientoClave = ?, autorizacionTratamientoDatos = ?, idRoles = ?, idTipoDocumento = ?, idCiudades = ? WHERE idUsuarios = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getNombres());
            ps.setString(2, modelo.getApellidos());
            ps.setString(3, modelo.getIdentificacion());
            ps.setString(4, modelo.getCorreo());
            ps.setString(5, modelo.getDireccion());
            ps.setString(6, modelo.getTelefono());
            ps.setString(7, modelo.getClave());
            ps.setBoolean(8, modelo.isActivo());
            ps.setDate(9, modelo.getFechaNacimiento() != null ? java.sql.Date.valueOf(modelo.getFechaNacimiento()) : null);
            ps.setDate(10, modelo.getFechaVencimientoClave() != null ? java.sql.Date.valueOf(modelo.getFechaVencimientoClave()) : null);
            ps.setBoolean(11, modelo.isAutorizacionTratamientoDatos());
            ps.setInt(12, modelo.getIdRoles());
            ps.setInt(13, modelo.getIdTipoDocumento());
            ps.setInt(14, modelo.getIdCiudades());
            ps.setInt(15, modelo.getIdUsuarios());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en usuarios: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idUsuarios) {
        String sql = "DELETE FROM usuarios WHERE idUsuarios = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuarios);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en usuarios: " + e.getMessage());
            return false;
        }
    }

    public Usuarios consultarPorId(int idUsuarios) {
        String sql = "SELECT * FROM usuarios WHERE idUsuarios = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuarios);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar usuarios por id: " + e.getMessage());
        }
        return null;
    }

    public List<Usuarios> listarTodos() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Consulta específica requerida por el flujo de login (Fase 3 - Autenticación).
     * El correo es UNIQUE en la tabla usuarios, por eso retorna un único registro.
     */
    public Usuarios buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por correo: " + e.getMessage());
        }
        return null;
    }

    private Usuarios mapear(ResultSet rs) throws SQLException {
        Usuarios modelo = new Usuarios();
        modelo.setIdUsuarios(rs.getInt("idUsuarios"));
        modelo.setNombres(rs.getString("nombres"));
        modelo.setApellidos(rs.getString("apellidos"));
        modelo.setIdentificacion(rs.getString("identificacion"));
        modelo.setCorreo(rs.getString("correo"));
        modelo.setDireccion(rs.getString("direccion"));
        modelo.setTelefono(rs.getString("telefono"));
        modelo.setClave(rs.getString("clave"));
        modelo.setIsActivo(rs.getBoolean("isActivo"));
        modelo.setFechaNacimiento((rs.getDate("fechaNacimiento") != null ? rs.getDate("fechaNacimiento").toLocalDate() : null));
        modelo.setFechaVencimientoClave((rs.getDate("fechaVencimientoClave") != null ? rs.getDate("fechaVencimientoClave").toLocalDate() : null));
        modelo.setAutorizacionTratamientoDatos(rs.getBoolean("autorizacionTratamientoDatos"));
        modelo.setIdRoles(rs.getInt("idRoles"));
        modelo.setIdTipoDocumento(rs.getInt("idTipoDocumento"));
        modelo.setIdCiudades(rs.getInt("idCiudades"));
        return modelo;
    }
}
