package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.Envios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** DAO de envios historicos; la insercion acepta Connection externa de pedido. */
public class EnviosDAO {
    private static final String COLUMNS="idEnvios, fechaEnvios, descripcionEnvios, numeroGuia, idPedidosCabeza, idEstadoEnvio, idTransportadoras";
    private static final String INSERT="INSERT INTO envios (fechaEnvios, descripcionEnvios, numeroGuia, idPedidosCabeza, idEstadoEnvio, idTransportadoras) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE="UPDATE envios SET fechaEnvios=?, descripcionEnvios=?, numeroGuia=?, idPedidosCabeza=?, idEstadoEnvio=?, idTransportadoras=? WHERE idEnvios=?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM envios WHERE idEnvios = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM envios";
    private static final String SELECT_BY_ORDER="SELECT "+COLUMNS+" FROM envios WHERE idPedidosCabeza = ?";
    public boolean insertar(Envios m){try(Connection c=Conexion.getConn()){return insertar(c,m);}catch(SQLException e){throw SQLExceptionTranslator.translate("Envios.insertar",e);}}
    public boolean insertar(Connection c,Envios m){try(PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){parametros(ps,m);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Envios.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Envios.insertar",e);}}
    public boolean actualizar(Envios m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){parametros(ps,m);ps.setInt(7,m.getIdEnvios());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Envios.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Envios.actualizar",e);}}
    public boolean eliminar(int idEnvios){throw SQLExceptionTranslator.operationNotAllowed("Envios.eliminar");}
    public Envios consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Envios.consultarPorId",e);}}
    public List<Envios> listarTodos(){List<Envios> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("Envios.listarTodos",e);}}
    public List<Envios> listarPorPedido(int idPedido){List<Envios> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ORDER)){ps.setInt(1,idPedido);try(ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Envios.listarPorPedido",e);}}
    private void parametros(PreparedStatement ps,Envios m)throws SQLException{ps.setTimestamp(1,m.getFechaEnvios()==null?null:Timestamp.valueOf(m.getFechaEnvios()));ps.setString(2,m.getDescripcionEnvios());ps.setString(3,m.getNumeroGuia());ps.setInt(4,m.getIdPedidosCabeza());ps.setInt(5,m.getIdEstadoEnvio());ps.setInt(6,m.getIdTransportadoras());}
    private void id(PreparedStatement ps,Envios m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdEnvios(k.getInt(1));}}
    private Envios mapear(ResultSet rs)throws SQLException{Envios m=new Envios();m.setIdEnvios(rs.getInt("idEnvios"));Timestamp fecha=rs.getTimestamp("fechaEnvios");m.setFechaEnvios(fecha==null?null:fecha.toLocalDateTime());m.setDescripcionEnvios(rs.getString("descripcionEnvios"));m.setNumeroGuia(rs.getString("numeroGuia"));m.setIdPedidosCabeza(rs.getInt("idPedidosCabeza"));m.setIdEstadoEnvio(rs.getInt("idEstadoEnvio"));m.setIdTransportadoras(rs.getInt("idTransportadoras"));return m;}
}
