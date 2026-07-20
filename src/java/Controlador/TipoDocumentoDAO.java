package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.TipoDocumento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla tipoDocumento. */
public class TipoDocumentoDAO {
    private static final String COLUMNS = "idTipoDocumento, descripcion";
    private static final String INSERT = "INSERT INTO tipoDocumento (descripcion) VALUES (?)";
    private static final String UPDATE = "UPDATE tipoDocumento SET descripcion = ? WHERE idTipoDocumento = ?";
    private static final String DELETE = "DELETE FROM tipoDocumento WHERE idTipoDocumento = ?";
    private static final String SELECT_BY_ID = "SELECT " + COLUMNS + " FROM tipoDocumento WHERE idTipoDocumento = ?";
    private static final String SELECT_ALL = "SELECT " + COLUMNS + " FROM tipoDocumento";
    public boolean insertar(TipoDocumento modelo) { try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) { ps.setString(1, modelo.getDescripcion()); SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "TipoDocumento.insertar"); asignarIdGenerado(ps, modelo); return true; } catch (SQLException e) { throw SQLExceptionTranslator.translate("TipoDocumento.insertar", e); } }
    public boolean actualizar(TipoDocumento modelo) { try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(UPDATE)) { ps.setString(1, modelo.getDescripcion()); ps.setInt(2, modelo.getIdTipoDocumento()); SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "TipoDocumento.actualizar"); return true; } catch (SQLException e) { throw SQLExceptionTranslator.translate("TipoDocumento.actualizar", e); } }
    public boolean eliminar(int idTipoDocumento) { try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(DELETE)) { ps.setInt(1, idTipoDocumento); SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "TipoDocumento.eliminar"); return true; } catch (SQLException e) { throw SQLExceptionTranslator.translate("TipoDocumento.eliminar", e); } }
    public TipoDocumento consultarPorId(int idTipoDocumento) { try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) { ps.setInt(1, idTipoDocumento); try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; } } catch (SQLException e) { throw SQLExceptionTranslator.translate("TipoDocumento.consultarPorId", e); } }
    public List<TipoDocumento> listarTodos() { List<TipoDocumento> lista = new ArrayList<>(); try (Connection conn = Conexion.getConn(); PreparedStatement ps = conn.prepareStatement(SELECT_ALL); ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(mapear(rs)); return lista; } catch (SQLException e) { throw SQLExceptionTranslator.translate("TipoDocumento.listarTodos", e); } }
    private void asignarIdGenerado(PreparedStatement ps, TipoDocumento m) throws SQLException { try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) m.setIdTipoDocumento(keys.getInt(1)); } }
    private TipoDocumento mapear(ResultSet rs) throws SQLException { TipoDocumento m = new TipoDocumento(); m.setIdTipoDocumento(rs.getInt("idTipoDocumento")); m.setDescripcion(rs.getString("descripcion")); return m; }
}
