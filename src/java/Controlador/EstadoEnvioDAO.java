package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.EstadoEnvio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla estadoEnvio. */
public class EstadoEnvioDAO {
    private static final String COLUMNS="idEstadoEnvio, descripcionEstadoEnvio";
    private static final String INSERT="INSERT INTO estadoEnvio (descripcionEstadoEnvio) VALUES (?)";
    private static final String UPDATE="UPDATE estadoEnvio SET descripcionEstadoEnvio = ? WHERE idEstadoEnvio = ?";
    private static final String DELETE="DELETE FROM estadoEnvio WHERE idEstadoEnvio = ?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM estadoEnvio WHERE idEstadoEnvio = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM estadoEnvio";
    public boolean insertar(EstadoEnvio m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){ps.setString(1,m.getDescripcionEstadoEnvio());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"EstadoEnvio.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("EstadoEnvio.insertar",e);}}
    public boolean actualizar(EstadoEnvio m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){ps.setString(1,m.getDescripcionEstadoEnvio());ps.setInt(2,m.getIdEstadoEnvio());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"EstadoEnvio.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("EstadoEnvio.actualizar",e);}}
    public boolean eliminar(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(DELETE)){ps.setInt(1,id);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"EstadoEnvio.eliminar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("EstadoEnvio.eliminar",e);}}
    public EstadoEnvio consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("EstadoEnvio.consultarPorId",e);}}
    public List<EstadoEnvio> listarTodos(){List<EstadoEnvio> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("EstadoEnvio.listarTodos",e);}}
    private void id(PreparedStatement ps,EstadoEnvio m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdEstadoEnvio(k.getInt(1));}}
    private EstadoEnvio mapear(ResultSet rs)throws SQLException{EstadoEnvio m=new EstadoEnvio();m.setIdEstadoEnvio(rs.getInt("idEstadoEnvio"));m.setDescripcionEstadoEnvio(rs.getString("descripcionEstadoEnvio"));return m;}
}
