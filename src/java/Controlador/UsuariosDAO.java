package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.SQLExceptionTranslator;
import Modelo.Usuarios;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de usuarios; eliminar aplica baja logica con el campo isActivo existente. */
public class UsuariosDAO {
    private static final String COLUMNS="idUsuarios, nombres, apellidos, identificacion, correo, direccion, telefono, clave, isActivo, fechaNacimiento, fechaVencimientoClave, autorizacionTratamientoDatos, idRoles, idTipoDocumento, idCiudades";
    private static final String INSERT="INSERT INTO usuarios (nombres, apellidos, identificacion, correo, direccion, telefono, clave, isActivo, fechaNacimiento, fechaVencimientoClave, autorizacionTratamientoDatos, idRoles, idTipoDocumento, idCiudades) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE="UPDATE usuarios SET nombres=?, apellidos=?, identificacion=?, correo=?, direccion=?, telefono=?, clave=?, isActivo=?, fechaNacimiento=?, fechaVencimientoClave=?, autorizacionTratamientoDatos=?, idRoles=?, idTipoDocumento=?, idCiudades=? WHERE idUsuarios=?";
    private static final String DEACTIVATE="UPDATE usuarios SET isActivo = FALSE WHERE idUsuarios = ? AND isActivo = TRUE";
    private static final String SELECT_BY_ID="SELECT "+COLUMNS+" FROM usuarios WHERE idUsuarios = ?";
    private static final String SELECT_ALL="SELECT "+COLUMNS+" FROM usuarios";
    private static final String SELECT_BY_EMAIL="SELECT "+COLUMNS+" FROM usuarios WHERE correo = ?";
    public boolean insertar(Usuarios m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)){parametros(ps,m);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Usuarios.insertar");id(ps,m);return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Usuarios.insertar",e);}}
    public boolean actualizar(Usuarios m){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(UPDATE)){parametros(ps,m);ps.setInt(15,m.getIdUsuarios());SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Usuarios.actualizar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Usuarios.actualizar",e);}}
    public boolean eliminar(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(DEACTIVATE)){ps.setInt(1,id);SQLExceptionTranslator.requireAffected(ps.executeUpdate(),"Usuarios.eliminar");return true;}catch(SQLException e){throw SQLExceptionTranslator.translate("Usuarios.eliminar",e);}}
    public Usuarios consultarPorId(int id){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_ID)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Usuarios.consultarPorId",e);}}
    public List<Usuarios> listarTodos(){List<Usuarios> l=new ArrayList<>();try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_ALL);ResultSet rs=ps.executeQuery()){while(rs.next())l.add(mapear(rs));return l;}catch(SQLException e){throw SQLExceptionTranslator.translate("Usuarios.listarTodos",e);}}
    public Usuarios buscarPorCorreo(String correo){try(Connection c=Conexion.getConn();PreparedStatement ps=c.prepareStatement(SELECT_BY_EMAIL)){ps.setString(1,correo);try(ResultSet rs=ps.executeQuery()){return rs.next()?mapear(rs):null;}}catch(SQLException e){throw SQLExceptionTranslator.translate("Usuarios.buscarPorCorreo",e);}}
    private void parametros(PreparedStatement ps,Usuarios m)throws SQLException{ps.setString(1,m.getNombres());ps.setString(2,m.getApellidos());ps.setString(3,m.getIdentificacion());ps.setString(4,m.getCorreo());ps.setString(5,m.getDireccion());ps.setString(6,m.getTelefono());ps.setString(7,m.getClave());ps.setBoolean(8,m.isActivo());ps.setDate(9,m.getFechaNacimiento()==null?null:Date.valueOf(m.getFechaNacimiento()));ps.setDate(10,m.getFechaVencimientoClave()==null?null:Date.valueOf(m.getFechaVencimientoClave()));ps.setBoolean(11,m.isAutorizacionTratamientoDatos());ps.setInt(12,m.getIdRoles());ps.setInt(13,m.getIdTipoDocumento());ps.setInt(14,m.getIdCiudades());}
    private void id(PreparedStatement ps,Usuarios m)throws SQLException{try(ResultSet k=ps.getGeneratedKeys()){if(k.next())m.setIdUsuarios(k.getInt(1));}}
    private Usuarios mapear(ResultSet rs)throws SQLException{Usuarios m=new Usuarios();m.setIdUsuarios(rs.getInt("idUsuarios"));m.setNombres(rs.getString("nombres"));m.setApellidos(rs.getString("apellidos"));m.setIdentificacion(rs.getString("identificacion"));m.setCorreo(rs.getString("correo"));m.setDireccion(rs.getString("direccion"));m.setTelefono(rs.getString("telefono"));m.setClave(rs.getString("clave"));m.setIsActivo(rs.getBoolean("isActivo"));Date nacimiento=rs.getDate("fechaNacimiento");m.setFechaNacimiento(nacimiento==null?null:nacimiento.toLocalDate());Date vencimiento=rs.getDate("fechaVencimientoClave");m.setFechaVencimientoClave(vencimiento==null?null:vencimiento.toLocalDate());m.setAutorizacionTratamientoDatos(rs.getBoolean("autorizacionTratamientoDatos"));m.setIdRoles(rs.getInt("idRoles"));m.setIdTipoDocumento(rs.getInt("idTipoDocumento"));m.setIdCiudades(rs.getInt("idCiudades"));return m;}
}
