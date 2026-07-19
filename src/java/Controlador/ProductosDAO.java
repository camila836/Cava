package Controlador;

import Conexion.Conexion;
import Modelo.Productos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "productos".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class ProductosDAO {

    public boolean insertar(Productos modelo) {
        String sql = "INSERT INTO productos (descripcionProductos, precioProductos, idUnidadesMedida, idCategoriaProductos) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionProductos());
            ps.setDouble(2, modelo.getPrecioProductos());
            ps.setInt(3, modelo.getIdUnidadesMedida());
            ps.setInt(4, modelo.getIdCategoriaProductos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en productos: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Productos modelo) {
        String sql = "UPDATE productos SET descripcionProductos = ?, precioProductos = ?, idUnidadesMedida = ?, idCategoriaProductos = ? WHERE idProductos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionProductos());
            ps.setDouble(2, modelo.getPrecioProductos());
            ps.setInt(3, modelo.getIdUnidadesMedida());
            ps.setInt(4, modelo.getIdCategoriaProductos());
            ps.setInt(5, modelo.getIdProductos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en productos: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idProductos) {
        String sql = "DELETE FROM productos WHERE idProductos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProductos);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en productos: " + e.getMessage());
            return false;
        }
    }

    public Productos consultarPorId(int idProductos) {
        String sql = "SELECT * FROM productos WHERE idProductos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProductos);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar productos por id: " + e.getMessage());
        }
        return null;
    }

    public List<Productos> listarTodos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    private Productos mapear(ResultSet rs) throws SQLException {
        Productos modelo = new Productos();
        modelo.setIdProductos(rs.getInt("idProductos"));
        modelo.setDescripcionProductos(rs.getString("descripcionProductos"));
        modelo.setPrecioProductos(rs.getDouble("precioProductos"));
        modelo.setIdUnidadesMedida(rs.getInt("idUnidadesMedida"));
        modelo.setIdCategoriaProductos(rs.getInt("idCategoriaProductos"));
        return modelo;
    }
}
