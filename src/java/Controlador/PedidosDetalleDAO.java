package Controlador;

import Conexion.Conexion;
import Modelo.PedidosDetalle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "pedidosDetalle".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class PedidosDetalleDAO {

    public boolean insertar(PedidosDetalle modelo) {
        String sql = "INSERT INTO pedidosDetalle (cantidadUnitaria, subtotalPed, idPedidosCabeza, idProductos) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, modelo.getCantidadUnitaria());
            ps.setDouble(2, modelo.getSubtotalPed());
            ps.setInt(3, modelo.getIdPedidosCabeza());
            ps.setInt(4, modelo.getIdProductos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en pedidosDetalle: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(PedidosDetalle modelo) {
        String sql = "UPDATE pedidosDetalle SET cantidadUnitaria = ?, subtotalPed = ?, idPedidosCabeza = ?, idProductos = ? WHERE idPedidosDetalle = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, modelo.getCantidadUnitaria());
            ps.setDouble(2, modelo.getSubtotalPed());
            ps.setInt(3, modelo.getIdPedidosCabeza());
            ps.setInt(4, modelo.getIdProductos());
            ps.setInt(5, modelo.getIdPedidosDetalle());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en pedidosDetalle: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPedidosDetalle) {
        String sql = "DELETE FROM pedidosDetalle WHERE idPedidosDetalle = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedidosDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en pedidosDetalle: " + e.getMessage());
            return false;
        }
    }

    public PedidosDetalle consultarPorId(int idPedidosDetalle) {
        String sql = "SELECT * FROM pedidosDetalle WHERE idPedidosDetalle = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedidosDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar pedidosDetalle por id: " + e.getMessage());
        }
        return null;
    }

    public List<PedidosDetalle> listarTodos() {
        List<PedidosDetalle> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidosDetalle";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidosDetalle: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Consulta específica requerida por el storefront (Fase 4) para mostrar
     * el detalle completo de un pedido.
     */
    public List<PedidosDetalle> listarPorPedido(int idPedidosCabeza) {
        List<PedidosDetalle> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidosDetalle WHERE idPedidosCabeza = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedidosCabeza);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidosDetalle por pedido: " + e.getMessage());
        }
        return lista;
    }

    private PedidosDetalle mapear(ResultSet rs) throws SQLException {
        PedidosDetalle modelo = new PedidosDetalle();
        modelo.setIdPedidosDetalle(rs.getInt("idPedidosDetalle"));
        modelo.setCantidadUnitaria(rs.getDouble("cantidadUnitaria"));
        modelo.setSubtotalPed(rs.getDouble("subtotalPed"));
        modelo.setIdPedidosCabeza(rs.getInt("idPedidosCabeza"));
        modelo.setIdProductos(rs.getInt("idProductos"));
        return modelo;
    }
}
