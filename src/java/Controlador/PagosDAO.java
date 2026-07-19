package Controlador;

import Conexion.Conexion;
import Modelo.Pagos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "pagos".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class PagosDAO {

    public boolean insertar(Pagos modelo) {
        String sql = "INSERT INTO pagos (fechaPagos, descripcionPagos, monto, referenciaPago, comprobantePago, idMediosPagos, idPedidosCabeza) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, modelo.getFechaPagos() != null ? java.sql.Timestamp.valueOf(modelo.getFechaPagos()) : null);
            ps.setString(2, modelo.getDescripcionPagos());
            ps.setDouble(3, modelo.getMonto());
            ps.setString(4, modelo.getReferenciaPago());
            ps.setString(5, modelo.getComprobantePago());
            ps.setInt(6, modelo.getIdMediosPagos());
            ps.setInt(7, modelo.getIdPedidosCabeza());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en pagos: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Pagos modelo) {
        String sql = "UPDATE pagos SET fechaPagos = ?, descripcionPagos = ?, monto = ?, referenciaPago = ?, comprobantePago = ?, idMediosPagos = ?, idPedidosCabeza = ? WHERE idPagos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, modelo.getFechaPagos() != null ? java.sql.Timestamp.valueOf(modelo.getFechaPagos()) : null);
            ps.setString(2, modelo.getDescripcionPagos());
            ps.setDouble(3, modelo.getMonto());
            ps.setString(4, modelo.getReferenciaPago());
            ps.setString(5, modelo.getComprobantePago());
            ps.setInt(6, modelo.getIdMediosPagos());
            ps.setInt(7, modelo.getIdPedidosCabeza());
            ps.setInt(8, modelo.getIdPagos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en pagos: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPagos) {
        String sql = "DELETE FROM pagos WHERE idPagos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPagos);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en pagos: " + e.getMessage());
            return false;
        }
    }

    public Pagos consultarPorId(int idPagos) {
        String sql = "SELECT * FROM pagos WHERE idPagos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPagos);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar pagos por id: " + e.getMessage());
        }
        return null;
    }

    public List<Pagos> listarTodos() {
        List<Pagos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Consulta específica requerida por el storefront (Fase 4) para mostrar
     * los pagos asociados a un pedido.
     */
    public List<Pagos> listarPorPedido(int idPedidosCabeza) {
        List<Pagos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE idPedidosCabeza = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedidosCabeza);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pagos por pedido: " + e.getMessage());
        }
        return lista;
    }

    private Pagos mapear(ResultSet rs) throws SQLException {
        Pagos modelo = new Pagos();
        modelo.setIdPagos(rs.getInt("idPagos"));
        modelo.setFechaPagos((rs.getTimestamp("fechaPagos") != null ? rs.getTimestamp("fechaPagos").toLocalDateTime() : null));
        modelo.setDescripcionPagos(rs.getString("descripcionPagos"));
        modelo.setMonto(rs.getDouble("monto"));
        modelo.setReferenciaPago(rs.getString("referenciaPago"));
        modelo.setComprobantePago(rs.getString("comprobantePago"));
        modelo.setIdMediosPagos(rs.getInt("idMediosPagos"));
        modelo.setIdPedidosCabeza(rs.getInt("idPedidosCabeza"));
        return modelo;
    }
}
