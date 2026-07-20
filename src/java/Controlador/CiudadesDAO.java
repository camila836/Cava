package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.Ciudades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla ciudades. */
public class CiudadesDAO {
    private static final String COLUMNS = "idCiudades, codigoCiudad, nombreCiudad, codigoPostal";
    private static final String INSERT = "INSERT INTO ciudades (codigoCiudad, nombreCiudad, codigoPostal) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE ciudades SET codigoCiudad = ?, nombreCiudad = ?, codigoPostal = ? WHERE idCiudades = ?";
    private static final String DELETE = "DELETE FROM ciudades WHERE idCiudades = ?";
    private static final String SELECT_BY_ID = "SELECT " + COLUMNS + " FROM ciudades WHERE idCiudades = ?";
    private static final String SELECT_ALL = "SELECT " + COLUMNS + " FROM ciudades";
    public boolean insertar(Ciudades m) { try (Connection c = Conexion.getConn(); PreparedStatement ps = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) { ps.setString(1,m.getCodigoCiudad()); ps.setString(2,m.getNombreCiudad()); ps.setString(3,m.getCodigoPostal()); SQLExceptionTranslator.requireAffected(ps.executeUpdate(), "Ciudades.insertar"); id(ps,m); return true; } catch(SQLException e){throw SQLExceptionTranslator.translate("Ciudades.insertar",e);} }
    public boolean actualizar(Ciudades m) { try(Connection c=Conexion.getConn(); PreparedStatement ps=c.prepareStatement(UPDATE)){ps.setString(1,m.getCodigoCiudad());ps.setString(2,m.getNombreCiudad());ps.setString(3,m.getCodigoPostal());ps.setInt(4,m.getIdCiudades());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Ciudades.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Ciudades.actualizar",e);} }
    public boolean eliminar(int id) { try(Connection c=Conexion.getConn(); PreparedStatement ps=c.prepareStatement(DELETE)){ps.setInt(1,id);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Ciudades.eliminar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Ciudades.eliminar",e);} }
    public Ciudades consultarPorId(int id) { try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Ciudades.consultarPorId",e);} }
    public List<Ciudades> listarTodos(){List<Ciudades> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("Ciudades.listarTodos",e);} }
    private void id(PreparedStatement ps,Ciudades m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdCiudades(k.getInt(1));}}
    private Ciudades mapear(ResultSet rs)throws SQLException{Ciudades m=new Ciudades();m.setIdCiudades(rs.getInt("idCiudades"));m.setCodigoCiudad(rs.getString("codigoCiudad"));m.setNombreCiudad(rs.getString("nombreCiudad"));m.setCodigoPostal(rs.getString("codigoPostal"));return m;}
}
