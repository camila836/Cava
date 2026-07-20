package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.CategoriaProductos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla categoriaProductos. */
public class CategoriaProductosDAO {
    private static final String COLUMNS="idCategoriaProductos, descripcionCategoriaProductos";
    private static final String INSERT="INSERT INTO categoriaProductos (descripcionCategoriaProductos) VALUES (?)";
    private static final String UPDATE="UPDATE categoriaProductos SET descripcionCategoriaProductos = ? WHERE idCategoriaProductos = ?";
    private static final String DELETE="DELETE FROM categoriaProductos WHERE idCategoriaProductos = ?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM categoriaProductos WHERE idCategoriaProductos = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM categoriaProductos";
    public boolean insertar(CategoriaProductos m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){ps.setString(1,m.getDescripcionCategoriaP());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"CategoriaProductos.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("CategoriaProductos.insertar",e);}}
    public boolean actualizar(CategoriaProductos m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){ps.setString(1,m.getDescripcionCategoriaP());ps.setInt(2,m.getIdCategoriaProductos());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"CategoriaProductos.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("CategoriaProductos.actualizar",e);}}
    public boolean eliminar(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(DELETE)){ps.setInt(1,id);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"CategoriaProductos.eliminar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("CategoriaProductos.eliminar",e);}}
    public CategoriaProductos consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("CategoriaProductos.consultarPorId",e);}}
    public List<CategoriaProductos> listarTodos(){List<CategoriaProductos> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("CategoriaProductos.listarTodos",e);}}
    private void id(PreparedStatement ps,CategoriaProductos m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdCategoriaProductos(k.getInt(1));}}
    private CategoriaProductos mapear(ResultSet rs)throws SQLException{CategoriaProductos m=new CategoriaProductos();m.setIdCategoriaProductos(rs.getInt("idCategoriaProductos"));m.setDescripcionCategoriaP(rs.getString("descripcionCategoriaProductos"));return m;}
}
