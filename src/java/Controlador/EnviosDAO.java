package Controlador;

import Conexion.Conexion;
import Modelo.Envios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "envios".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class EnviosDAO {

    public boolean insertar(Envios modelo) {
        String sql = "INSERT INTO envios (fechaEnvios, descripcionEnvios, numeroGuia, idPedidosCabeza, idEstadoEnvio, idTransportadoras) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, modelo.getFechaEnvios() != null ? java.sql.Timestamp.valueOf(modelo.getFechaEnvios()) : null);
            ps.setString(2, modelo.getDescripcionEnvios());
            ps.setString(3, modelo.getNumeroGuia());
            ps.setInt(4, modelo.getIdPedidosCabeza());
            ps.setInt(5, modelo.getIdEstadoEnvio());
            ps.setInt(6, modelo.getIdTransportadoras());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en envios: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Envios modelo) {
        String sql = "UPDATE envios SET fechaEnvios = ?, descripcionEnvios = ?, numeroGuia = ?, idPedidosCabeza = ?, idEstadoEnvio = ?, idTransportadoras = ? WHERE idEnvios = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, modelo.getFechaEnvios() != null ? java.sql.Timestamp.valueOf(modelo.getFechaEnvios()) : null);
            ps.setString(2, modelo.getDescripcionEnvios());
            ps.setString(3, modelo.getNumeroGuia());
            ps.setInt(4, modelo.getIdPedidosCabeza());
            ps.setInt(5, modelo.getIdEstadoEnvio());
            ps.setInt(6, modelo.getIdTransportadoras());
            ps.setInt(7, modelo.getIdEnvios());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en envios: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idEnvios) {
        String sql = "DELETE FROM envios WHERE idEnvios = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEnvios);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en envios: " + e.getMessage());
            return false;
        }
    }

    public Envios consultarPorId(int idEnvios) {
        String sql = "SELECT * FROM envios WHERE idEnvios = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEnvios);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar envios por id: " + e.getMessage());
        }
        return null;
    }

    public List<Envios> listarTodos() {
        List<Envios> lista = new ArrayList<>();
        String sql = "SELECT * FROM envios";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar envios: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Consulta específica requerida por el storefront (Fase 4) para mostrar
     * el envío asociado a un pedido.
     */
    public List<Envios> listarPorPedido(int idPedidosCabeza) {
        List<Envios> lista = new ArrayList<>();
        String sql = "SELECT * FROM envios WHERE idPedidosCabeza = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedidosCabeza);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar envios por pedido: " + e.getMessage());
        }
        return lista;
    }

    private Envios mapear(ResultSet rs) throws SQLException {
        Envios modelo = new Envios();
        modelo.setIdEnvios(rs.getInt("idEnvios"));
        modelo.setFechaEnvios((rs.getTimestamp("fechaEnvios") != null ? rs.getTimestamp("fechaEnvios").toLocalDateTime() : null));
        modelo.setDescripcionEnvios(rs.getString("descripcionEnvios"));
        modelo.setNumeroGuia(rs.getString("numeroGuia"));
        modelo.setIdPedidosCabeza(rs.getInt("idPedidosCabeza"));
        modelo.setIdEstadoEnvio(rs.getInt("idEstadoEnvio"));
        modelo.setIdTransportadoras(rs.getInt("idTransportadoras"));
        return modelo;
    }
}
