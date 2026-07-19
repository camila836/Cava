package Controlador;

import Conexion.Conexion;
import Modelo.CategoriaProductos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de acceso a datos para la tabla "categoriaProductos".
 * Usa PreparedStatement y try-with-resources sobre Conexion.getConn().
 */
public class CategoriaProductosDAO {

    public boolean insertar(CategoriaProductos modelo) {
        String sql = "INSERT INTO categoriaProductos (descripcionCategoriaProductos) VALUES (?)";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionCategoriaP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar en categoriaProductos: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(CategoriaProductos modelo) {
        String sql = "UPDATE categoriaProductos SET descripcionCategoriaProductos = ? WHERE idCategoriaProductos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, modelo.getDescripcionCategoriaP());
            ps.setInt(2, modelo.getIdCategoriaProductos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en categoriaProductos: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCategoriaProductos) {
        String sql = "DELETE FROM categoriaProductos WHERE idCategoriaProductos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoriaProductos);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar en categoriaProductos: " + e.getMessage());
            return false;
        }
    }

    public CategoriaProductos consultarPorId(int idCategoriaProductos) {
        String sql = "SELECT * FROM categoriaProductos WHERE idCategoriaProductos = ?";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoriaProductos);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar categoriaProductos por id: " + e.getMessage());
        }
        return null;
    }

    public List<CategoriaProductos> listarTodos() {
        List<CategoriaProductos> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoriaProductos";
        try (Connection conn = Conexion.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categoriaProductos: " + e.getMessage());
        }
        return lista;
    }

    private CategoriaProductos mapear(ResultSet rs) throws SQLException {
        CategoriaProductos modelo = new CategoriaProductos();
        modelo.setIdCategoriaProductos(rs.getInt("idCategoriaProductos"));
        modelo.setDescripcionCategoriaP(rs.getString("descripcionCategoriaProductos"));
        return modelo;
    }
}
