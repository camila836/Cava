package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.Pagos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** DAO de pagos historicos; la insercion acepta Connection externa de pedido. */
public class PagosDAO {
    private static final String COLUMNS="idPagos, fechaPagos, descripcionPagos, monto, referenciaPago, comprobantePago, idMediosPagos, idPedidosCabeza";
    private static final String INSERT="INSERT INTO pagos (fechaPagos, descripcionPagos, monto, referenciaPago, comprobantePago, idMediosPagos, idPedidosCabeza) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE="UPDATE pagos SET fechaPagos=?, descripcionPagos=?, monto=?, referenciaPago=?, comprobantePago=?, idMediosPagos=?, idPedidosCabeza=? WHERE idPagos=?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM pagos WHERE idPagos = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM pagos";
    private static final String SELECT_BY_ORDER="SELECT "+COLUMNS+" FROM pagos WHERE idPedidosCabeza = ?";
    public boolean insertar(Pagos m){try(Connection c=Conexion.getConn()){return insertar(c,m);}catch(SQLException e){throw SQLExceptionTranslator.translate("Pagos.insertar",e);}}
    public boolean insertar(Connection c,Pagos m){try(PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){parametros(ps,m);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Pagos.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Pagos.insertar",e);}}
    public boolean actualizar(Pagos m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){parametros(ps,m);ps.setInt(8,m.getIdPagos());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Pagos.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Pagos.actualizar",e);}}
    public boolean eliminar(int idPagos){throw SQLExceptionTranslator.operationNotAllowed("Pagos.eliminar");}
    public Pagos consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Pagos.consultarPorId",e);}}
    public List<Pagos> listarTodos(){List<Pagos> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("Pagos.listarTodos",e);}}
    public List<Pagos> listarPorPedido(int idPedido){List<Pagos> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ORDER)){ps.setInt(1,idPedido);try(ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Pagos.listarPorPedido",e);}}
    private void parametros(PreparedStatement ps,Pagos m)throws SQLException{ps.setTimestamp(1,m.getFechaPagos()==null?null:Timestamp.valueOf(m.getFechaPagos()));ps.setString(2,m.getDescripcionPagos());ps.setBigDecimal(3,m.getMonto());ps.setString(4,m.getReferenciaPago());ps.setString(5,m.getComprobantePago());ps.setInt(6,m.getIdMediosPagos());ps.setInt(7,m.getIdPedidosCabeza());}
    private void id(PreparedStatement ps,Pagos m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdPagos(k.getInt(1));}}
    private Pagos mapear(ResultSet rs)throws SQLException{Pagos m=new Pagos();m.setIdPagos(rs.getInt("idPagos"));Timestamp fecha=rs.getTimestamp("fechaPagos");m.setFechaPagos(fecha==null?null:fecha.toLocalDateTime());m.setDescripcionPagos(rs.getString("descripcionPagos"));m.setMonto(rs.getBigDecimal("monto"));m.setReferenciaPago(rs.getString("referenciaPago"));m.setComprobantePago(rs.getString("comprobantePago"));m.setIdMediosPagos(rs.getInt("idMediosPagos"));m.setIdPedidosCabeza(rs.getInt("idPedidosCabeza"));return m;}
}
