package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.MediosPagos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla mediosPagos. */
public class MediosPagosDAO {
    private static final String COLUMNS="idMediosPagos, descripcionMediosPagos";
    private static final String INSERT="INSERT INTO mediosPagos (descripcionMediosPagos) VALUES (?)";
    private static final String UPDATE="UPDATE mediosPagos SET descripcionMediosPagos = ? WHERE idMediosPagos = ?";
    private static final String DELETE="DELETE FROM mediosPagos WHERE idMediosPagos = ?";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM mediosPagos WHERE idMediosPagos = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM mediosPagos";
    public boolean insertar(MediosPagos m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){ps.setString(1,m.getDescripcionMediosPagos());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"MediosPagos.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("MediosPagos.insertar",e);}}
    public boolean actualizar(MediosPagos m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){ps.setString(1,m.getDescripcionMediosPagos());ps.setInt(2,m.getIdMediosPagos());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"MediosPagos.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("MediosPagos.actualizar",e);}}
    public boolean eliminar(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(DELETE)){ps.setInt(1,id);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"MediosPagos.eliminar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("MediosPagos.eliminar",e);}}
    public MediosPagos consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("MediosPagos.consultarPorId",e);}}
    public List<MediosPagos> listarTodos(){List<MediosPagos> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("MediosPagos.listarTodos",e);}}
    private void id(PreparedStatement ps,MediosPagos m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdMediosPagos(k.getInt(1));}}
    private MediosPagos mapear(ResultSet rs)throws SQLException{MediosPagos m=new MediosPagos();m.setIdMediosPagos(rs.getInt("idMediosPagos"));m.setDescripcionMediosPagos(rs.getString("descripcionMediosPagos"));return m;}
}
