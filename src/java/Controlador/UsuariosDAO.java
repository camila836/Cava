package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.CredencialesUsuario;
import Modelo.Usuarios;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/** DAO de usuarios; eliminar aplica baja lógica con isActivo. */
public class UsuariosDAO {

    private static final String COLUMNS = "idUsuarios, nombres, apellidos, identificacion, correo, direccion, telefono, clave, isActivo, fechaNacimiento, fechaVencimientoClave, autorizacionTratamientoDatos, idRoles, idTipoDocumento, idCiudades";
    private static final String INSERT = "INSERT INTO usuarios (nombres, apellidos, identificacion, correo, direccion, telefono, clave, isActivo, fechaNacimiento, fechaVencimientoClave, autorizacionTratamientoDatos, idRoles, idTipoDocumento, idCiudades) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE usuarios SET nombres=?, apellidos=?, identificacion=?, correo=?, direccion=?, telefono=?, clave=?, isActivo=?, fechaNacimiento=?, fechaVencimientoClave=?, autorizacionTratamientoDatos=?, idRoles=?, idTipoDocumento=?, idCiudades=? WHERE idUsuarios=?";
    private static final String DEACTIVATE = "UPDATE usuarios SET isActivo = FALSE WHERE idUsuarios = ? AND isActivo = TRUE";
    private static final String SELECT_BY_ID = "SELECT " + COLUMNS + " FROM usuarios WHERE idUsuarios = ?";
    private static final String SELECT_ALL = "SELECT " + COLUMNS + " FROM usuarios";
    private static final String SELECT_BY_EMAIL = "SELECT " + COLUMNS + " FROM usuarios WHERE correo = ?";
    private static final String SELECT_CREDENTIALS = "SELECT u.idUsuarios, u.nombres, u.clave, u.isActivo, r.codigoRol FROM usuarios u JOIN roles r ON r.idRoles = u.idRoles WHERE u.correo = ?";

    public boolean insertar(Usuarios modelo) {
        try (Connection conn = Conexion.getConn();
                PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            asignarParametros(ps, modelo);
            SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "Usuarios.insertar");
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    modelo.setIdUsuarios(keys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate("Usuarios.insertar", e);
        }
    }

    public boolean actualizar(Usuarios modelo) {
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            asignarParametros(ps, modelo);
            ps.setInt(15, modelo.getIdUsuarios());
            SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "Usuarios.actualizar");
            return true;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate("Usuarios.actualizar", e);
        }
    }

    public boolean eliminar(int idUsuarios) {
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(DEACTIVATE)) {
            ps.setInt(1, idUsuarios);
            SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "Usuarios.eliminar");
            return true;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate("Usuarios.eliminar", e);
        }
    }

    public Usuarios consultarPorId(int idUsuarios) {
        return consultarUsuario(SELECT_BY_ID, idUsuarios);
    }

    public Usuarios buscarPorCorreo(String correo) {
        return consultarUsuario(SELECT_BY_EMAIL, correo);
    }

    public CredencialesUsuario buscarCredencialesPorCorreo(String correo) {
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(SELECT_CREDENTIALS)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new CredencialesUsuario(rs.getInt("idUsuarios"), rs.getString("nombres"),
                        rs.getString("clave"), rs.getBoolean("isActivo"), rs.getString("codigoRol"));
            }
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate("Usuarios.buscarCredencialesPorCorreo", e);
        }
    }

    public List<Usuarios> listarTodos() {
        List<Usuarios> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate("Usuarios.listarTodos", e);
        }
    }

    private Usuarios consultarUsuario(String sql, Object value) {
        try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw SQLExceptionTranslator.translate("Usuarios.consultar", e);
        }
    }

    private void asignarParametros(PreparedStatement ps, Usuarios modelo) throws SQLException {
        ps.setString(1, modelo.getNombres());
        ps.setString(2, modelo.getApellidos());
        ps.setString(3, modelo.getIdentificacion());
        ps.setString(4, modelo.getCorreo());
        ps.setString(5, modelo.getDireccion());
        ps.setString(6, modelo.getTelefono());
        ps.setString(7, modelo.getClave());
        ps.setBoolean(8, modelo.isActivo());
        ps.setDate(9, modelo.getFechaNacimiento() == null ? null : Date.valueOf(modelo.getFechaNacimiento()));
        ps.setDate(10, modelo.getFechaVencimientoClave() == null ? null : Date.valueOf(modelo.getFechaVencimientoClave()));
        ps.setBoolean(11, modelo.isAutorizacionTratamientoDatos());
        ps.setInt(12, modelo.getIdRoles());
        setNullableInteger(ps, 13, modelo.getIdTipoDocumento());
        setNullableInteger(ps, 14, modelo.getIdCiudades());
    }

    private static void setNullableInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
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
        Date nacimiento = rs.getDate("fechaNacimiento");
        modelo.setFechaNacimiento(nacimiento == null ? null : nacimiento.toLocalDate());
        Date vencimiento = rs.getDate("fechaVencimientoClave");
        modelo.setFechaVencimientoClave(vencimiento == null ? null : vencimiento.toLocalDate());
        modelo.setAutorizacionTratamientoDatos(rs.getBoolean("autorizacionTratamientoDatos"));
        modelo.setIdRoles(rs.getInt("idRoles"));
        modelo.setIdTipoDocumento((Integer) rs.getObject("idTipoDocumento"));
        modelo.setIdCiudades((Integer) rs.getObject("idCiudades"));
        return modelo;
    }
}
