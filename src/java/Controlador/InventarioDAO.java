package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.Inventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de inventario; admite Connection externa para una unidad coordinada de pedido. */
public class InventarioDAO {
    private static final String COLUMNS="idInventario, descripcionInventario, stock, idProductos";
    private static final String INSERT="INSERT INTO inventario (descripcionInventario, stock, idProductos) VALUES (?, ?, ?)";
    private static final String UPDATE="UPDATE inventario SET descripcionInventario=?, stock=?, idProductos=? WHERE idInventario=?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM inventario WHERE idInventario = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM inventario";
    private static final String SELECT_BY_PRODUCT="SELECT "+COLUMNS+" FROM inventario WHERE idProductos = ?";
    public boolean insertar(Inventario m){try(Connection c=Conexion.getConn()){return insertar(c,m);}catch(SQLException e){throw SQLExceptionTranslator.translate("Inventario.insertar",e);}}
    public boolean insertar(Connection c,Inventario m){try(PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){parametros(ps,m);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Inventario.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Inventario.insertar",e);}}
    public boolean actualizar(Inventario m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){parametros(ps,m);ps.setInt(4,m.getIdInventario());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Inventario.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Inventario.actualizar",e);}}
    public boolean eliminar(int idInventario){throw SQLExceptionTranslator.operationNotAllowed("Inventario.eliminar");}
    public Inventario consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Inventario.consultarPorId",e);}}
    public List<Inventario> listarTodos(){List<Inventario> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("Inventario.listarTodos",e);}}
    public Inventario consultarPorProducto(int idProducto){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_PRODUCT)){ps.setInt(1,idProducto);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Inventario.consultarPorProducto",e);}}
    private void parametros(PreparedStatement ps,Inventario m)throws SQLException{ps.setString(1,m.getDescripcionInventario());ps.setBigDecimal(2,m.getStock());ps.setInt(3,m.getIdProductos());}
    private void id(PreparedStatement ps,Inventario m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdInventario(k.getInt(1));}}
    private Inventario mapear(ResultSet rs)throws SQLException{Inventario m=new Inventario();m.setIdInventario(rs.getInt("idInventario"));m.setDescripcionInventario(rs.getString("descripcionInventario"));m.setStock(rs.getBigDecimal("stock"));m.setIdProductos(rs.getInt("idProductos"));return m;}
}
