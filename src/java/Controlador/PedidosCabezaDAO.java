package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.PedidosCabeza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** DAO de cabeceras historicas; la insercion acepta Connection externa de pedido. */
public class PedidosCabezaDAO {
    private static final String COLUMNS="idPedidosCabeza, numeroPedido, fechaPedido, descripcionPedido, valorTotal, idUsuarios";
    private static final String INSERT="INSERT INTO pedidosCabeza (numeroPedido, fechaPedido, descripcionPedido, valorTotal, idUsuarios) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE="UPDATE pedidosCabeza SET numeroPedido=?, fechaPedido=?, descripcionPedido=?, valorTotal=?, idUsuarios=? WHERE idPedidosCabeza=?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM pedidosCabeza WHERE idPedidosCabeza = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM pedidosCabeza";
    private static final String SELECT_BY_USER="SELECT "+COLUMNS+" FROM pedidosCabeza WHERE idUsuarios = ?";
    public boolean insertar(PedidosCabeza m){try(Connection c=Conexion.getConn()){return insertar(c,m);}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosCabeza.insertar",e);}}
    public boolean insertar(Connection c,PedidosCabeza m){try(PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){parametros(ps,m);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"PedidosCabeza.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosCabeza.insertar",e);}}
    public boolean actualizar(PedidosCabeza m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){parametros(ps,m);ps.setInt(6,m.getIdPedidosCabeza());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"PedidosCabeza.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosCabeza.actualizar",e);}}
    public boolean eliminar(int idPedidosCabeza){throw SQLExceptionTranslator.operationNotAllowed("PedidosCabeza.eliminar");}
    public PedidosCabeza consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosCabeza.consultarPorId",e);}}
    public List<PedidosCabeza> listarTodos(){List<PedidosCabeza> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosCabeza.listarTodos",e);}}
    public List<PedidosCabeza> listarPorUsuario(int idUsuario){List<PedidosCabeza> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_USER)){ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosCabeza.listarPorUsuario",e);}}
    private void parametros(PreparedStatement ps,PedidosCabeza m)throws SQLException{ps.setString(1,m.getNumeroPedido());ps.setTimestamp(2,m.getFechaPedido()==null?null:Timestamp.valueOf(m.getFechaPedido()));ps.setString(3,m.getDescripcionPedido());ps.setBigDecimal(4,m.getValorTotal());ps.setInt(5,m.getIdUsuarios());}
    private void id(PreparedStatement ps,PedidosCabeza m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdPedidosCabeza(k.getInt(1));}}
    private PedidosCabeza mapear(ResultSet rs)throws SQLException{PedidosCabeza m=new PedidosCabeza();m.setIdPedidosCabeza(rs.getInt("idPedidosCabeza"));m.setNumeroPedido(rs.getString("numeroPedido"));Timestamp fecha=rs.getTimestamp("fechaPedido");m.setFechaPedido(fecha==null?null:fecha.toLocalDateTime());m.setDescripcionPedido(rs.getString("descripcionPedido"));m.setValorTotal(rs.getBigDecimal("valorTotal"));m.setIdUsuarios(rs.getInt("idUsuarios"));return m;}
}
