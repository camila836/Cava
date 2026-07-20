package Controlador;

import Conexion.Conexion;
import Modelo.PedidosCabeza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "pedidosCabeza".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class PedidosCabezaDAO {

    public boolean insertar(PedidosCabeza modelo) {
        String sql = "INSERT INTO pedidosCabeza (numeroPedido, fechaPedido, descripcionPedido, valorTotal, idUsuarios) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getNumeroPedido());
            ps.setTimestamp(2, modelo.getFechaPedido() != null ? java.sql.Timestamp.valueOf(modelo.getFechaPedido()) : null);
            ps.setString(3, modelo.getDescripcionPedido());
            ps.setBigDecimal(4, modelo.getValorTotal());
            ps.setInt(5, modelo.getIdUsuarios());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en pedidosCabeza: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(PedidosCabeza modelo) {
        String sql = "UPDATE pedidosCabeza SET numeroPedido = ?, fechaPedido = ?, descripcionPedido = ?, valorTotal = ?, idUsuarios = ? WHERE idPedidosCabeza = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getNumeroPedido());
            ps.setTimestamp(2, modelo.getFechaPedido() != null ? java.sql.Timestamp.valueOf(modelo.getFechaPedido()) : null);
            ps.setString(3, modelo.getDescripcionPedido());
            ps.setBigDecimal(4, modelo.getValorTotal());
            ps.setInt(5, modelo.getIdUsuarios());
            ps.setInt(6, modelo.getIdPedidosCabeza());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en pedidosCabeza: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPedidosCabeza) {
        String sql = "DELETE FROM pedidosCabeza WHERE idPedidosCabeza = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedidosCabeza);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en pedidosCabeza: " + e.getMessage());
            return false;
        }
    }

    public PedidosCabeza consultarPorId(int idPedidosCabeza) {
        String sql = "SELECT * FROM pedidosCabeza WHERE idPedidosCabeza = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedidosCabeza);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar pedidosCabeza por id: " + e.getMessage());
        }
        return null;
    }

    public List<PedidosCabeza> listarTodos() {
        List<PedidosCabeza> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidosCabeza";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidosCabeza: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Consulta específica requerida por el storefront (Fase 4) para mostrar
     * el historial de pedidos de un usuario.
     */
    public List<PedidosCabeza> listarPorUsuario(int idUsuarios) {
        List<PedidosCabeza> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidosCabeza WHERE idUsuarios = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuarios);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidosCabeza por usuario: " + e.getMessage());
        }
        return lista;
    }

    private PedidosCabeza mapear(ResultSet rs) throws SQLException {
        PedidosCabeza modelo = new PedidosCabeza();
        modelo.setIdPedidosCabeza(rs.getInt("idPedidosCabeza"));
        modelo.setNumeroPedido(rs.getString("numeroPedido"));
        modelo.setFechaPedido((rs.getTimestamp("fechaPedido") != null ? rs.getTimestamp("fechaPedido").toLocalDateTime() : null));
        modelo.setDescripcionPedido(rs.getString("descripcionPedido"));
        modelo.setValorTotal(rs.getBigDecimal("valorTotal"));
        modelo.setIdUsuarios(rs.getInt("idUsuarios"));
        return modelo;
    }
}
