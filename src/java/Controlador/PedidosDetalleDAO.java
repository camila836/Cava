package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.PedidosDetalle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de detalles historicos; la insercion acepta Connection externa de pedido. */
public class PedidosDetalleDAO {
    private static final String COLUMNS="idPedidosDetalle, cantidadUnitaria, subtotalPed, idPedidosCabeza, idProductos";
    private static final String INSERT="INSERT INTO pedidosDetalle (cantidadUnitaria, subtotalPed, idPedidosCabeza, idProductos) VALUES (?, ?, ?, ?)";
    private static final String UPDATE="UPDATE pedidosDetalle SET cantidadUnitaria=?, subtotalPed=?, idPedidosCabeza=?, idProductos=? WHERE idPedidosDetalle=?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM pedidosDetalle WHERE idPedidosDetalle = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM pedidosDetalle";
    private static final String SELECT_BY_ORDER="SELECT "+COLUMNS+" FROM pedidosDetalle WHERE idPedidosCabeza = ?";
    public boolean insertar(PedidosDetalle m){try(Connection c=Conexion.getConn()){return insertar(c,m);}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosDetalle.insertar",e);}}
    public boolean insertar(Connection c,PedidosDetalle m){try(PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){parametros(ps,m);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"PedidosDetalle.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosDetalle.insertar",e);}}
    public boolean actualizar(PedidosDetalle m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){parametros(ps,m);ps.setInt(5,m.getIdPedidosDetalle());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"PedidosDetalle.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosDetalle.actualizar",e);}}
    public boolean eliminar(int idPedidosDetalle){throw SQLExceptionTranslator.operationNotAllowed("PedidosDetalle.eliminar");}
    public PedidosDetalle consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosDetalle.consultarPorId",e);}}
    public List<PedidosDetalle> listarTodos(){List<PedidosDetalle> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosDetalle.listarTodos",e);}}
    public List<PedidosDetalle> listarPorPedido(int idPedido){List<PedidosDetalle> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ORDER)){ps.setInt(1,idPedido);try(ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}}catch(SQLException e){throw SQLExceptionTranslator.translate("PedidosDetalle.listarPorPedido",e);}}
    private void parametros(PreparedStatement ps,PedidosDetalle m)throws SQLException{ps.setBigDecimal(1,m.getCantidadUnitaria());ps.setBigDecimal(2,m.getSubtotalPed());ps.setInt(3,m.getIdPedidosCabeza());ps.setInt(4,m.getIdProductos());}
    private void id(PreparedStatement ps,PedidosDetalle m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdPedidosDetalle(k.getInt(1));}}
    private PedidosDetalle mapear(ResultSet rs)throws SQLException{PedidosDetalle m=new PedidosDetalle();m.setIdPedidosDetalle(rs.getInt("idPedidosDetalle"));m.setCantidadUnitaria(rs.getBigDecimal("cantidadUnitaria"));m.setSubtotalPed(rs.getBigDecimal("subtotalPed"));m.setIdPedidosCabeza(rs.getInt("idPedidosCabeza"));m.setIdProductos(rs.getInt("idProductos"));return m;}
}
