package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de productos; conserva las referencias y no permite borrado ordinario. */
public class ProductosDAO {
    private static final String COLUMNS="idProductos, descripcionProductos, precioProductos, idUnidadesMedida, idCategoriaProductos";
    private static final String INSERT="INSERT INTO productos (descripcionProductos, precioProductos, idUnidadesMedida, idCategoriaProductos) VALUES (?, ?, ?, ?)";
    private static final String UPDATE="UPDATE productos SET descripcionProductos=?, precioProductos=?, idUnidadesMedida=?, idCategoriaProductos=? WHERE idProductos=?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM productos WHERE idProductos = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM productos";
    public boolean insertar(Productos m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){parametros(ps,m);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Productos.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Productos.insertar",e);}}
    public boolean actualizar(Productos m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){parametros(ps,m);ps.setInt(5,m.getIdProductos());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Productos.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Productos.actualizar",e);}}
    public boolean eliminar(int idProductos){throw SQLExceptionTranslator.operationNotAllowed("Productos.eliminar");}
    public Productos consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Productos.consultarPorId",e);}}
    public List<Productos> listarTodos(){List<Productos> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("Productos.listarTodos",e);}}
    private void parametros(PreparedStatement ps,Productos m)throws SQLException{ps.setString(1,m.getDescripcionProductos());ps.setBigDecimal(2,m.getPrecioProductos());ps.setInt(3,m.getIdUnidadesMedida());ps.setInt(4,m.getIdCategoriaProductos());}
    private void id(PreparedStatement ps,Productos m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdProductos(k.getInt(1));}}
    private Productos mapear(ResultSet rs)throws SQLException{Productos m=new Productos();m.setIdProductos(rs.getInt("idProductos"));m.setDescripcionProductos(rs.getString("descripcionProductos"));m.setPrecioProductos(rs.getBigDecimal("precioProductos"));m.setIdUnidadesMedida(rs.getInt("idUnidadesMedida"));m.setIdCategoriaProductos(rs.getInt("idCategoriaProductos"));return m;}
}
