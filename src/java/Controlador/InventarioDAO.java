package Controlador;

import Conexion.Conexion;
import Modelo.Inventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "inventario".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class InventarioDAO {

    public boolean insertar(Inventario modelo) {
        String sql = "INSERT INTO inventario (descripcionInventario, stock, idProductos) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionInventario());
            ps.setBigDecimal(2, modelo.getStock());
            ps.setInt(3, modelo.getIdProductos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en inventario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Inventario modelo) {
        String sql = "UPDATE inventario SET descripcionInventario = ?, stock = ?, idProductos = ? WHERE idInventario = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionInventario());
            ps.setBigDecimal(2, modelo.getStock());
            ps.setInt(3, modelo.getIdProductos());
            ps.setInt(4, modelo.getIdInventario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en inventario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idInventario) {
        String sql = "DELETE FROM inventario WHERE idInventario = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idInventario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en inventario: " + e.getMessage());
            return false;
        }
    }

    public Inventario consultarPorId(int idInventario) {
        String sql = "SELECT * FROM inventario WHERE idInventario = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idInventario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar inventario por id: " + e.getMessage());
        }
        return null;
    }

    public List<Inventario> listarTodos() {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar inventario: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Consulta específica requerida por el storefront (Fase 4) y por la
     * validación de stock antes de confirmar un pedido (Fase 6).
     */
    public Inventario consultarPorProducto(int idProductos) {
        String sql = "SELECT * FROM inventario WHERE idProductos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProductos);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar inventario por producto: " + e.getMessage());
        }
        return null;
    }

    private Inventario mapear(ResultSet rs) throws SQLException {
        Inventario modelo = new Inventario();
        modelo.setIdInventario(rs.getInt("idInventario"));
        modelo.setDescripcionInventario(rs.getString("descripcionInventario"));
        modelo.setStock(rs.getBigDecimal("stock"));
        modelo.setIdProductos(rs.getInt("idProductos"));
        return modelo;
    }
}
