package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.Transportadoras;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla transportadoras. */
public class TransportadorasDAO {
    private static final String COLUMNS="idTransportadoras, nombreTransportadoras, nit, correo, telefono";
    private static final String INSERT="INSERT INTO transportadoras (nombreTransportadoras, nit, correo, telefono) VALUES (?, ?, ?, ?)";
    private static final String UPDATE="UPDATE transportadoras SET nombreTransportadoras = ?, nit = ?, correo = ?, telefono = ? WHERE idTransportadoras = ?";
    private static final String DELETE="DELETE FROM transportadoras WHERE idTransportadoras = ?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM transportadoras WHERE idTransportadoras = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM transportadoras";
    public boolean insertar(Transportadoras m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){ps.setString(1,m.getNombreTransportadoras());ps.setString(2,m.getNit());ps.setString(3,m.getCorreo());ps.setString(4,m.getTelefono());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Transportadoras.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Transportadoras.insertar",e);}}
    public boolean actualizar(Transportadoras m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){ps.setString(1,m.getNombreTransportadoras());ps.setString(2,m.getNit());ps.setString(3,m.getCorreo());ps.setString(4,m.getTelefono());ps.setInt(5,m.getIdTransportadoras());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Transportadoras.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Transportadoras.actualizar",e);}}
    public boolean eliminar(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(DELETE)){ps.setInt(1,id);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Transportadoras.eliminar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Transportadoras.eliminar",e);}}
    public Transportadoras consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Transportadoras.consultarPorId",e);}}
    public List<Transportadoras> listarTodos(){List<Transportadoras> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("Transportadoras.listarTodos",e);}}
    private void id(PreparedStatement ps,Transportadoras m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdTransportadoras(k.getInt(1));}}
    private Transportadoras mapear(ResultSet rs)throws SQLException{Transportadoras m=new Transportadoras();m.setIdTransportadoras(rs.getInt("idTransportadoras"));m.setNombreTransportadoras(rs.getString("nombreTransportadoras"));m.setNit(rs.getString("nit"));m.setCorreo(rs.getString("correo"));m.setTelefono(rs.getString("telefono"));return m;}
}
