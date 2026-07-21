package Servlets;

import Modelo.Roles;
import Modelo.Usuarios;
import Seguridad.CsrfProtection;
import Seguridad.PasswordSecurity;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/** Registro válido, asignación CLIENTE y rechazo de manipulación de rol. */
public final class RegistroServletTest {

    private RegistroServletTest() {
    }

    public static void main(String[] args) throws Exception {
        testValidRegistration();
        testManipulatedRole();
    }

    private static void testValidRegistration() throws Exception {
        Exchange exchange = new Exchange(baseParameters());
        String token = CsrfProtection.getOrCreate(exchange.session());
        exchange.parameters.put("csrfToken", token);
        StubRegistro servlet = new StubRegistro();
        servlet.doPost(exchange.request(), exchange.response());
        verify("CLIENTE".equals(servlet.requestedRole), "rol resuelto por código");
        verify(servlet.inserted != null && servlet.inserted.getIdRoles() == 37, "ID obtenido del servidor");
        verify(servlet.inserted.getIdTipoDocumento() == null && servlet.inserted.getIdCiudades() == null, "perfil opcional");
        verify(!"frase de paso sintética".equals(servlet.inserted.getClave()), "sin texto plano");
        verify(PasswordSecurity.verify("frase de paso sintética".toCharArray(), servlet.inserted.getClave()), "hash válido");
        verify("/Cava/login?registro=ok".equals(exchange.redirect), "PRG");
    }

    private static void testManipulatedRole() throws Exception {
        Map<String,String> parameters = baseParameters();
        parameters.put("codigoRol", "ADMINISTRADOR");
        Exchange exchange = new Exchange(parameters);
        exchange.parameters.put("csrfToken", CsrfProtection.getOrCreate(exchange.session()));
        StubRegistro servlet = new StubRegistro();
        servlet.doPost(exchange.request(), exchange.response());
        verify(servlet.inserted == null && exchange.status == 400, "manipulación rechazada sin asignación");
    }

    private static Map<String,String> baseParameters(){Map<String,String> values=new HashMap<>();values.put("nombres","Ana");values.put("apellidos","Cava");values.put("correo"," ANA@CAVA.TEST ");values.put("clave","frase de paso sintética");values.put("confirmarClave","frase de paso sintética");values.put("autorizacionTratamientoDatos","true");return values;}

    private static final class StubRegistro extends RegistroServlet {
        private String requestedRole;private Usuarios inserted;
        @Override protected Usuarios findByEmail(String email){return null;}
        @Override protected Roles findRole(String code){requestedRole=code;Roles role=new Roles();role.setIdRoles(37);role.setCodigoRol(code);return role;}
        @Override protected void insert(Usuarios user){inserted=user;}
    }

    private static final class Exchange {
        private final Map<String,String> parameters;private final Map<String,Object> sessionValues=new HashMap<>();private final Map<String,Object> attrs=new HashMap<>();private String redirect;private String forward;private int status;
        private Exchange(Map<String,String> parameters){this.parameters=parameters;}
        private HttpSession session(){return proxy(HttpSession.class,(m,a)->{if("getAttribute".equals(m))return sessionValues.get((String)a[0]);if("setAttribute".equals(m)){sessionValues.put((String)a[0],a[1]);return null;}return null;});}
        private HttpServletRequest request(){HttpSession s=session();return proxy(HttpServletRequest.class,(m,a)->{switch(m){case "getParameter":return parameters.get((String)a[0]);case "getSession":return s;case "getContextPath":return "/Cava";case "setAttribute":attrs.put((String)a[0],a[1]);return null;case "getRequestDispatcher":String path=(String)a[0];return proxy(RequestDispatcher.class,(dm,da)->{if("forward".equals(dm))forward=path;return null;});default:return null;}});}
        private HttpServletResponse response(){return proxy(HttpServletResponse.class,(m,a)->{if("sendRedirect".equals(m))redirect=(String)a[0];if("setStatus".equals(m))status=(Integer)a[0];return null;});}
    }

    @SuppressWarnings("unchecked") private static <T>T proxy(Class<T> type,Invocation call){return (T)Proxy.newProxyInstance(type.getClassLoader(),new Class<?>[]{type},(o,m,a)->call.invoke(m.getName(),a));}
    private static void verify(boolean value,String message){if(!value)throw new AssertionError(message);}
    @FunctionalInterface private interface Invocation{Object invoke(String method,Object[] args)throws Throwable;}
}
