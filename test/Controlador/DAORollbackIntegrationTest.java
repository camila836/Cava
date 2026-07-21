package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.DAOErrorType;
import Controlador.excepciones.DAOException;
import Modelo.CategoriaProductos;
import Modelo.Ciudades;
import Modelo.Envios;
import Modelo.EstadoEnvio;
import Modelo.Inventario;
import Modelo.MediosPagos;
import Modelo.Pagos;
import Modelo.PedidosCabeza;
import Modelo.PedidosDetalle;
import Modelo.Productos;
import Modelo.Roles;
import Modelo.TipoDocumento;
import Modelo.Transportadoras;
import Modelo.UnidadesMedida;
import Modelo.Usuarios;
import Seguridad.PasswordSecurity;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Prueba de integracion ejecutable solo dentro de GlassFish: usa jdbc/CavaDS,
 * datos con prefijo temporal y rollback para la unidad de pedido.
 */
public final class DAORollbackIntegrationTest {
    private DAORollbackIntegrationTest() { }

    public static void run() throws Exception {
        String tag = "F6B" + (System.nanoTime() % 100000000L);
        try (Connection control = Conexion.getConn()) {
            verificarTemporalAusente(control, tag);
            ejecutar(control, tag);
            verificarTemporalAusente(control, tag);
        }
    }

    private static void ejecutar(Connection control, String tag) throws Exception {
        RolesDAO rolesDao = new RolesDAO(); TipoDocumentoDAO tiposDao = new TipoDocumentoDAO();
        CiudadesDAO ciudadesDao = new CiudadesDAO(); UnidadesMedidaDAO unidadesDao = new UnidadesMedidaDAO();
        CategoriaProductosDAO categoriasDao = new CategoriaProductosDAO(); EstadoEnvioDAO estadosDao = new EstadoEnvioDAO();
        MediosPagosDAO mediosDao = new MediosPagosDAO(); TransportadorasDAO transportadorasDao = new TransportadorasDAO();
        UsuariosDAO usuariosDao = new UsuariosDAO(); ProductosDAO productosDao = new ProductosDAO();
        InventarioDAO inventarioDao = new InventarioDAO();
        Roles rol = rol(tag); TipoDocumento tipo = tipo(tag); Ciudades ciudad = ciudad(tag);
        UnidadesMedida unidad = unidad(tag); CategoriaProductos categoria = categoria(tag);
        EstadoEnvio estado = estado(tag); MediosPagos medio = medio(tag); Transportadoras transportadora = transportadora(tag);
        try {
            rolesDao.insertar(rol); tiposDao.insertar(tipo); ciudadesDao.insertar(ciudad); unidadesDao.insertar(unidad);
            categoriasDao.insertar(categoria); estadosDao.insertar(estado); mediosDao.insertar(medio); transportadorasDao.insertar(transportadora);
            comprobar(rol.getIdRoles() > 0 && ciudad.getCodigoPostal() == null && transportadora.getCorreo() == null, "catalogos y nulos");
            comprobar(tiposDao.consultarPorId(tipo.getIdTipoDocumento()) != null && ciudadesDao.consultarPorId(ciudad.getIdCiudades()).getCodigoPostal() == null && unidadesDao.consultarPorId(unidad.getIdUnidadesMedida()) != null && categoriasDao.consultarPorId(categoria.getIdCategoriaProductos()) != null && estadosDao.consultarPorId(estado.getIdEstadoEnvio()) != null && mediosDao.consultarPorId(medio.getIdMediosPagos()) != null && transportadorasDao.consultarPorId(transportadora.getIdTransportadoras()).getTelefono() == null, "mapeo catalogos");
            rol.setDescripcionRol(tag + "R2"); rolesDao.actualizar(rol); comprobar(rolesDao.consultarPorId(rol.getIdRoles()).getDescripcionRol().endsWith("R2"), "actualizacion catalogo");
            tipo.setDescripcion(tag + "T2"); tiposDao.actualizar(tipo); ciudad.setNombreCiudad(tag + "C2"); ciudadesDao.actualizar(ciudad); unidad.setDescripcionUnidadesM(tag + "U2"); unidadesDao.actualizar(unidad); categoria.setDescripcionCategoriaP(tag + "K2"); categoriasDao.actualizar(categoria); estado.setDescripcionEstadoEnvio(tag + "E2"); estadosDao.actualizar(estado); medio.setDescripcionMediosPagos(tag + "M2"); mediosDao.actualizar(medio); transportadora.setNombreTransportadoras(tag + "X2"); transportadorasDao.actualizar(transportadora);
            espera(DAOErrorType.NOT_FOUND, () -> rolesDao.actualizar(rol(-1, tag)));
            Roles borrable = rol(tag + "D"); rolesDao.insertar(borrable); rolesDao.eliminar(borrable.getIdRoles()); comprobar(rolesDao.consultarPorId(borrable.getIdRoles()) == null, "eliminacion catalogo sin referencias");

            Usuarios usuario = usuario(tag, rol, tipo, ciudad); usuariosDao.insertar(usuario);
            comprobar(usuariosDao.consultarPorId(usuario.getIdUsuarios()).getFechaNacimiento() == null, "fecha nullable");
            espera(DAOErrorType.DUPLICATE, () -> usuariosDao.insertar(usuario(tag, rol, tipo, ciudad)));
            espera(DAOErrorType.FOREIGN_KEY, () -> rolesDao.eliminar(rol.getIdRoles()));
            usuario.setNombres(tag + "U2"); usuariosDao.actualizar(usuario);
            usuariosDao.eliminar(usuario.getIdUsuarios()); comprobar(!usuariosDao.consultarPorId(usuario.getIdUsuarios()).isActivo(), "baja logica usuario");
            Usuarios usuarioActivo = usuario(tag + "A", rol, tipo, ciudad); usuariosDao.insertar(usuarioActivo);

            Productos producto = producto(tag, unidad, categoria); productosDao.insertar(producto);
            comprobar(productosDao.consultarPorId(producto.getIdProductos()).getPrecioProductos().compareTo(new BigDecimal("12.50")) == 0, "BigDecimal producto");
            producto.setDescripcionProductos(tag + "P2"); productosDao.actualizar(producto);
            espera(DAOErrorType.FOREIGN_KEY, () -> productosDao.insertar(productoInvalido(tag)));
            espera(DAOErrorType.OPERATION_NOT_ALLOWED, () -> productosDao.eliminar(producto.getIdProductos()));
            Inventario inventario = inventario(tag, producto); inventarioDao.insertar(inventario);
            comprobar(inventarioDao.consultarPorProducto(producto.getIdProductos()).getStock().compareTo(new BigDecimal("7.25")) == 0, "BigDecimal inventario");
            inventario.setStock(new BigDecimal("8.00")); inventarioDao.actualizar(inventario);
            espera(DAOErrorType.OPERATION_NOT_ALLOWED, () -> inventarioDao.eliminar(inventario.getIdInventario()));

            probarUnidadPedido(control, tag, usuarioActivo, producto, medio, estado, transportadora);
            probarHistoricosIndependientes(tag, usuarioActivo, producto, medio, estado, transportadora);
            espera(DAOErrorType.OPERATION_NOT_ALLOWED, () -> new PedidosCabezaDAO().eliminar(1));
            espera(DAOErrorType.OPERATION_NOT_ALLOWED, () -> new PedidosDetalleDAO().eliminar(1));
            espera(DAOErrorType.OPERATION_NOT_ALLOWED, () -> new PagosDAO().eliminar(1));
            espera(DAOErrorType.OPERATION_NOT_ALLOWED, () -> new EnviosDAO().eliminar(1));
            comprobar(usuariosDao.consultarPorId(-1) == null, "registro inexistente");
        } finally {
            String pattern = tag + "%";
            borrar(control, "DELETE FROM envios WHERE numeroGuia LIKE ?", pattern);
            borrar(control, "DELETE FROM pagos WHERE referenciaPago LIKE ?", pattern);
            borrar(control, "DELETE d FROM pedidosDetalle d JOIN pedidosCabeza p ON p.idPedidosCabeza=d.idPedidosCabeza WHERE p.numeroPedido LIKE ?", pattern);
            borrar(control, "DELETE FROM pedidosCabeza WHERE numeroPedido LIKE ?", pattern);
            borrar(control, "DELETE i FROM inventario i JOIN productos p ON p.idProductos=i.idProductos WHERE p.descripcionProductos LIKE ?", pattern);
            borrar(control, "DELETE FROM productos WHERE descripcionProductos LIKE ?", pattern);
            borrar(control, "DELETE FROM usuarios WHERE correo LIKE ?", pattern);
            borrar(control, "DELETE FROM transportadoras WHERE nit LIKE ?", pattern);
            borrar(control, "DELETE FROM mediosPagos WHERE descripcionMediosPagos LIKE ?", pattern);
            borrar(control, "DELETE FROM estadoEnvio WHERE descripcionEstadoEnvio LIKE ?", pattern);
            borrar(control, "DELETE FROM categoriaProductos WHERE descripcionCategoriaProductos LIKE ?", pattern);
            borrar(control, "DELETE FROM unidadesMedida WHERE descripcionUnidadesMed LIKE ?", pattern);
            borrar(control, "DELETE FROM ciudades WHERE codigoCiudad LIKE ?", pattern);
            borrar(control, "DELETE FROM tipoDocumento WHERE descripcion LIKE ?", pattern);
            borrar(control, "DELETE FROM roles WHERE codigoRol LIKE ?", pattern);
        }
    }

    private static void probarUnidadPedido(Connection control, String tag, Usuarios usuario, Productos producto, MediosPagos medio, EstadoEnvio estado, Transportadoras transportadora) throws Exception {
        boolean autoCommit = control.getAutoCommit(); control.setAutoCommit(false);
        try {
            PedidosCabeza pedido = new PedidosCabeza(); pedido.setNumeroPedido(tag); pedido.setFechaPedido(LocalDateTime.of(2026, 7, 20, 12, 0)); pedido.setDescripcionPedido(null); pedido.setValorTotal(new BigDecimal("12.50")); pedido.setIdUsuarios(usuario.getIdUsuarios());
            new PedidosCabezaDAO().insertar(control, pedido);
            PedidosDetalle detalle = new PedidosDetalle(); detalle.setCantidadUnitaria(new BigDecimal("1.00")); detalle.setSubtotalPed(new BigDecimal("12.50")); detalle.setIdPedidosCabeza(pedido.getIdPedidosCabeza()); detalle.setIdProductos(producto.getIdProductos()); new PedidosDetalleDAO().insertar(control, detalle);
            Pagos pago = new Pagos(); pago.setFechaPagos(LocalDateTime.of(2026, 7, 20, 12, 1)); pago.setDescripcionPagos(null); pago.setMonto(new BigDecimal("12.50")); pago.setReferenciaPago(tag); pago.setComprobantePago(null); pago.setIdMediosPagos(medio.getIdMediosPagos()); pago.setIdPedidosCabeza(pedido.getIdPedidosCabeza()); new PagosDAO().insertar(control, pago);
            Envios envio = new Envios(); envio.setFechaEnvios(LocalDateTime.of(2026, 7, 20, 12, 2)); envio.setDescripcionEnvios(null); envio.setNumeroGuia(tag); envio.setIdPedidosCabeza(pedido.getIdPedidosCabeza()); envio.setIdEstadoEnvio(estado.getIdEstadoEnvio()); envio.setIdTransportadoras(transportadora.getIdTransportadoras()); new EnviosDAO().insertar(control, envio);
            comprobar(pedido.getIdPedidosCabeza() > 0 && detalle.getIdPedidosDetalle() > 0 && pago.getIdPagos() > 0 && envio.getIdEnvios() > 0, "claves generadas unidad pedido");
            control.rollback();
            comprobar(contar(control, "pedidosCabeza") == 0 && contar(control, "pedidosDetalle") == 0 && contar(control, "pagos") == 0 && contar(control, "envios") == 0, "rollback unidad pedido");
        } finally { if (!control.getAutoCommit()) control.rollback(); control.setAutoCommit(autoCommit); }
    }

    private static void probarHistoricosIndependientes(String tag, Usuarios usuario, Productos producto, MediosPagos medio, EstadoEnvio estado, Transportadoras transportadora) {
        PedidosCabeza pedido = pedido(tag + "I", usuario); PedidosCabezaDAO pedidos = new PedidosCabezaDAO(); pedidos.insertar(pedido); comprobar(pedidos.consultarPorId(pedido.getIdPedidosCabeza()).getValorTotal().compareTo(new BigDecimal("12.50")) == 0, "mapeo pedido"); pedido.setDescripcionPedido(tag); pedidos.actualizar(pedido);
        PedidosDetalle detalle = detalle(pedido, producto); PedidosDetalleDAO detalles = new PedidosDetalleDAO(); detalles.insertar(detalle); comprobar(detalles.consultarPorId(detalle.getIdPedidosDetalle()).getCantidadUnitaria().compareTo(new BigDecimal("1.00")) == 0, "mapeo detalle"); detalle.setSubtotalPed(new BigDecimal("13.00")); detalles.actualizar(detalle);
        Pagos pago = pago(tag + "I", pedido, medio); PagosDAO pagos = new PagosDAO(); pagos.insertar(pago); comprobar(pagos.consultarPorId(pago.getIdPagos()).getFechaPagos() != null, "mapeo pago"); pago.setDescripcionPagos(tag); pagos.actualizar(pago);
        Envios envio = envio(tag + "I", pedido, estado, transportadora); EnviosDAO envios = new EnviosDAO(); envios.insertar(envio); comprobar(envios.consultarPorId(envio.getIdEnvios()).getFechaEnvios() != null, "mapeo envio"); envio.setDescripcionEnvios(tag); envios.actualizar(envio);
    }

    private static Roles rol(String tag){Roles m=new Roles();m.setCodigoRol(tag.toUpperCase());m.setDescripcionRol(tag);return m;} private static Roles rol(int id,String tag){Roles m=rol(tag);m.setIdRoles(id);return m;}
    private static TipoDocumento tipo(String tag){TipoDocumento m=new TipoDocumento();m.setDescripcion(tag);return m;}
    private static Ciudades ciudad(String tag){Ciudades m=new Ciudades();m.setCodigoCiudad(tag);m.setNombreCiudad(tag);m.setCodigoPostal(null);return m;}
    private static UnidadesMedida unidad(String tag){UnidadesMedida m=new UnidadesMedida();m.setDescripcionUnidadesM(tag);return m;}
    private static CategoriaProductos categoria(String tag){CategoriaProductos m=new CategoriaProductos();m.setDescripcionCategoriaP(tag);return m;}
    private static EstadoEnvio estado(String tag){EstadoEnvio m=new EstadoEnvio();m.setDescripcionEstadoEnvio(tag);return m;}
    private static MediosPagos medio(String tag){MediosPagos m=new MediosPagos();m.setDescripcionMediosPagos(tag);return m;}
    private static Transportadoras transportadora(String tag){Transportadoras m=new Transportadoras();m.setNombreTransportadoras(tag);m.setNit(tag);m.setCorreo(null);m.setTelefono(null);return m;}
    private static Usuarios usuario(String tag,Roles r,TipoDocumento t,Ciudades c){Usuarios m=new Usuarios();m.setNombres(tag);m.setApellidos(tag);m.setIdentificacion(tag);m.setCorreo(tag+"@test.local");m.setDireccion(null);m.setTelefono(null);char[] password=(tag+"-credencial-sintetica-segura").toCharArray();try{m.setClave(PasswordSecurity.hash(password));}finally{Arrays.fill(password,'\0');}m.setIsActivo(true);m.setFechaNacimiento(null);m.setFechaVencimientoClave(null);m.setAutorizacionTratamientoDatos(false);m.setIdRoles(r.getIdRoles());m.setIdTipoDocumento(t.getIdTipoDocumento());m.setIdCiudades(c.getIdCiudades());return m;}
    private static Productos producto(String tag,UnidadesMedida u,CategoriaProductos c){Productos m=new Productos();m.setDescripcionProductos(tag);m.setPrecioProductos(new BigDecimal("12.50"));m.setIdUnidadesMedida(u.getIdUnidadesMedida());m.setIdCategoriaProductos(c.getIdCategoriaProductos());return m;}
    private static Productos productoInvalido(String tag){Productos m=new Productos();m.setDescripcionProductos(tag);m.setPrecioProductos(new BigDecimal("1.00"));m.setIdUnidadesMedida(-1);m.setIdCategoriaProductos(-1);return m;}
    private static Inventario inventario(String tag,Productos p){Inventario m=new Inventario();m.setDescripcionInventario(null);m.setStock(new BigDecimal("7.25"));m.setIdProductos(p.getIdProductos());return m;}
    private static PedidosCabeza pedido(String tag,Usuarios u){PedidosCabeza m=new PedidosCabeza();m.setNumeroPedido(tag);m.setFechaPedido(LocalDateTime.of(2026,7,20,12,0));m.setDescripcionPedido(null);m.setValorTotal(new BigDecimal("12.50"));m.setIdUsuarios(u.getIdUsuarios());return m;}
    private static PedidosDetalle detalle(PedidosCabeza p,Productos producto){PedidosDetalle m=new PedidosDetalle();m.setCantidadUnitaria(new BigDecimal("1.00"));m.setSubtotalPed(new BigDecimal("12.50"));m.setIdPedidosCabeza(p.getIdPedidosCabeza());m.setIdProductos(producto.getIdProductos());return m;}
    private static Pagos pago(String tag,PedidosCabeza pedido,MediosPagos medio){Pagos m=new Pagos();m.setFechaPagos(LocalDateTime.of(2026,7,20,12,1));m.setDescripcionPagos(null);m.setMonto(new BigDecimal("12.50"));m.setReferenciaPago(tag);m.setComprobantePago(null);m.setIdMediosPagos(medio.getIdMediosPagos());m.setIdPedidosCabeza(pedido.getIdPedidosCabeza());return m;}
    private static Envios envio(String tag,PedidosCabeza pedido,EstadoEnvio estado,Transportadoras transportadora){Envios m=new Envios();m.setFechaEnvios(LocalDateTime.of(2026,7,20,12,2));m.setDescripcionEnvios(null);m.setNumeroGuia(tag);m.setIdPedidosCabeza(pedido.getIdPedidosCabeza());m.setIdEstadoEnvio(estado.getIdEstadoEnvio());m.setIdTransportadoras(transportadora.getIdTransportadoras());return m;}
    private static void verificarTemporalAusente(Connection c,String tag)throws Exception{String pattern=tag+"%";comprobar(contarCoincidencias(c,"SELECT COUNT(*) FROM usuarios WHERE correo LIKE ?",pattern)==0,"usuarios temporales");comprobar(contarCoincidencias(c,"SELECT COUNT(*) FROM productos WHERE descripcionProductos LIKE ?",pattern)==0,"productos temporales");comprobar(contarCoincidencias(c,"SELECT COUNT(*) FROM pedidosCabeza WHERE numeroPedido LIKE ?",pattern)==0,"pedidos temporales");comprobar(contarCoincidencias(c,"SELECT COUNT(*) FROM roles WHERE codigoRol LIKE ?",pattern)==0,"roles temporales");try(PreparedStatement ps=c.prepareStatement("SELECT COUNT(*) FROM roles WHERE codigoRol IN ('CLIENTE','ADMINISTRADOR')");ResultSet rs=ps.executeQuery()){rs.next();comprobar(rs.getInt(1)==2,"roles autoritativos");}}
    private static int contar(Connection c,String tabla)throws Exception{try(PreparedStatement ps=c.prepareStatement("SELECT COUNT(*) FROM "+tabla);ResultSet rs=ps.executeQuery()){rs.next();return rs.getInt(1);}}
    private static int contarCoincidencias(Connection c,String sql,String pattern)throws Exception{try(PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,pattern);try(ResultSet rs=ps.executeQuery()){rs.next();return rs.getInt(1);}}}
    private static void borrar(Connection c,String sql,String pattern)throws Exception{try(PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,pattern);ps.executeUpdate();}}
    private static void espera(DAOErrorType tipo,Accion accion)throws Exception{try{accion.ejecutar();throw new AssertionError("Se esperaba "+tipo);}catch(DAOException e){comprobar(e.getErrorType()==tipo,"tipo "+tipo);}}
    private static void comprobar(boolean condicion,String nombre){if(!condicion)throw new AssertionError(nombre);}
    @FunctionalInterface private interface Accion{void ejecutar()throws Exception;}
}
