package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.UnidadesMedida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla unidadesMedida. */
public class UnidadesMedidaDAO {
    private static final String COLUMNS="idUnidadesMedida, descripcionUnidadesMed";
    private static final String INSERT="INSERT INTO unidadesMedida (descripcionUnidadesMed) VALUES (?)";
    private static final String UPDATE="UPDATE unidadesMedida SET descripcionUnidadesMed = ? WHERE idUnidadesMedida = ?";
    private static final String DELETE="DELETE FROM unidadesMedida WHERE idUnidadesMedida = ?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM unidadesMedida WHERE idUnidadesMedida = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM unidadesMedida";
    public boolean insertar(UnidadesMedida m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){ps.setString(1,m.getDescripcionUnidadesM());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"UnidadesMedida.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("UnidadesMedida.insertar",e);}}
    public boolean actualizar(UnidadesMedida m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){ps.setString(1,m.getDescripcionUnidadesM());ps.setInt(2,m.getIdUnidadesMedida());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"UnidadesMedida.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("UnidadesMedida.actualizar",e);}}
    public boolean eliminar(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(DELETE)){ps.setInt(1,id);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"UnidadesMedida.eliminar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("UnidadesMedida.eliminar",e);}}
    public UnidadesMedida consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("UnidadesMedida.consultarPorId",e);}}
    public List<UnidadesMedida> listarTodos(){List<UnidadesMedida> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("UnidadesMedida.listarTodos",e);}}
    private void id(PreparedStatement ps,UnidadesMedida m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdUnidadesMedida(k.getInt(1));}}
    private UnidadesMedida mapear(ResultSet rs)throws SQLException{UnidadesMedida m=new UnidadesMedida();m.setIdUnidadesMedida(rs.getInt("idUnidadesMedida"));m.setDescripcionUnidadesM(rs.getString("descripcionUnidadesMed"));return m;}
}
