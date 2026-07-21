package Servlets;

import Modelo.CredencialesUsuario;
import Seguridad.CsrfProtection;
import Seguridad.PasswordSecurity;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/** Prueba dirigida de login, mensaje neutro y renovación de sesión. */
public final class LoginServletTest {

    private LoginServletTest() {
    }

    public static void main(String[] args) throws Exception {
        char[] password = "contraseña sintética login".toCharArray();
        try {
            String hash = PasswordSecurity.hash(password);
            testSuccessfulLogin(password, hash);
            testInvalidCases(password, hash);
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private static void testSuccessfulLogin(char[] password, String hash) throws Exception {
        SessionDouble session = new SessionDouble();
        String token = CsrfProtection.getOrCreate(session.proxy());
        Exchange exchange = new Exchange(session, Map.of("correo", " ADMIN@CAVA.TEST ",
                "clave", new String(password), "csrfToken", token));
        LoginServlet servlet = new StubLoginServlet(new CredencialesUsuario(7, "Ana", hash, true, "ADMINISTRADOR"));
        servlet.doPost(exchange.request(), exchange.response());
        verify(exchange.changedSessionId, "changeSessionId obligatorio");
        verify("/Cava/admin".equals(exchange.redirect), "redirección admin");
        verify(Integer.valueOf(7).equals(session.attributes.get("idUsuarios")), "id mínimo");
        verify("ADMINISTRADOR".equals(session.attributes.get("codigoRol")), "rol autorizado");
        verify(!session.attributes.containsKey("clave") && !session.attributes.containsKey("hash"), "sin secretos en sesión");
        verify(!token.equals(session.attributes.get("csrfToken")), "rotación CSRF");
    }

    private static void testInvalidCases(char[] password, String validHash) throws Exception {
        String[] messages = new String[3];
        CredencialesUsuario[] cases = {
            null,
            new CredencialesUsuario(1, "Inactiva", validHash, false, "CLIENTE"),
            new CredencialesUsuario(2, "Activa", validHash, true, "CLIENTE")
        };
        String[] submitted = {new String(password), new String(password), "contraseña sintética falsa"};
        for (int i = 0; i < cases.length; i++) {
            SessionDouble session = new SessionDouble();
            String token = CsrfProtection.getOrCreate(session.proxy());
            Exchange exchange = new Exchange(session, Map.of("correo", "nadie@cava.test",
                    "clave", submitted[i], "csrfToken", token));
            new StubLoginServlet(cases[i]).doPost(exchange.request(), exchange.response());
            messages[i] = (String) exchange.requestAttributes.get("loginError");
            verify(exchange.status == HttpServletResponse.SC_UNAUTHORIZED, "HTTP 401 neutro");
            verify(LoginServlet.VIEW.equals(exchange.forward), "forward login");
        }
        verify(messages[0].equals(messages[1]) && messages[1].equals(messages[2]), "mensaje idéntico");
    }

    private static final class StubLoginServlet extends LoginServlet {
        private final CredencialesUsuario credentials;
        private StubLoginServlet(CredencialesUsuario credentials) { this.credentials = credentials; }
        @Override protected CredencialesUsuario findCredentials(String email) { return credentials; }
    }

    private static final class SessionDouble {
        private final Map<String, Object> attributes = new HashMap<>();
        private HttpSession proxy() {
            return LoginServletTest.proxy(HttpSession.class, (method, args) -> {
                if ("getAttribute".equals(method)) return attributes.get((String) args[0]);
                if ("setAttribute".equals(method)) { attributes.put((String) args[0], args[1]); return null; }
                if ("getId".equals(method)) return "session-test";
                return defaultValue(method);
            });
        }
    }

    private static final class Exchange {
        private final SessionDouble session;
        private final Map<String,String> parameters;
        private final Map<String,Object> requestAttributes = new HashMap<>();
        private boolean changedSessionId;
        private int status;
        private String redirect;
        private String forward;
        private Exchange(SessionDouble session, Map<String,String> parameters) { this.session=session;this.parameters=parameters; }
        private HttpServletRequest request() {
            HttpSession sessionProxy=session.proxy();
            return proxy(HttpServletRequest.class,(method,args)->{
                switch(method){
                    case "getParameter": return parameters.get((String)args[0]);
                    case "getSession": return sessionProxy;
                    case "changeSessionId": changedSessionId=true; return "renovada";
                    case "getContextPath": return "/Cava";
                    case "isSecure": return false;
                    case "setAttribute": requestAttributes.put((String)args[0],args[1]); return null;
                    case "getAttribute": return requestAttributes.get((String)args[0]);
                    case "getRequestDispatcher": String path=(String)args[0];return proxy(RequestDispatcher.class,(m,a)->{if("forward".equals(m))forward=path;return null;});
                    default:return defaultValue(method);
                }
            });
        }
        private HttpServletResponse response(){return proxy(HttpServletResponse.class,(method,args)->{
            if("setStatus".equals(method)){status=(Integer)args[0];return null;}
            if("sendRedirect".equals(method)){redirect=(String)args[0];return null;}
            return defaultValue(method);
        });}
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, Invocation invocation){return (T)Proxy.newProxyInstance(type.getClassLoader(),new Class<?>[]{type},(o,m,a)->invocation.call(m.getName(),a));}
    private static Object defaultValue(String method){return null;}
    private static void verify(boolean value,String message){if(!value)throw new AssertionError(message);}
    @FunctionalInterface private interface Invocation{Object call(String method,Object[] args)throws Throwable;}
}
