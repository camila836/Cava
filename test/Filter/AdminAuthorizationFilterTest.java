package Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/** Prueba controlada de autorización sin sesión, CLIENTE y ADMINISTRADOR. */
public final class AdminAuthorizationFilterTest {

    private AdminAuthorizationFilterTest() {
    }

    public static void main(String[] args) throws Exception {
        Exchange anonymous = new Exchange(null);
        new AdminAuthorizationFilter().doFilter(anonymous.request(), anonymous.response(), anonymous.chain());
        verify("/Cava/login".equals(anonymous.redirect), "anónimo redirigido");

        Exchange client = new Exchange(session("CLIENTE"));
        new AdminAuthorizationFilter().doFilter(client.request(), client.response(), client.chain());
        verify(client.status == 403 && "/WEB-INF/views/error.jsp".equals(client.forward), "CLIENTE bloqueado");

        Exchange admin = new Exchange(session("ADMINISTRADOR"));
        new AdminAuthorizationFilter().doFilter(admin.request(), admin.response(), admin.chain());
        verify(admin.chainCalled, "ADMINISTRADOR autorizado");
    }

    private static Map<String,Object> session(String role){Map<String,Object> values=new HashMap<>();values.put("idUsuarios",1);values.put("codigoRol",role);return values;}

    private static final class Exchange {
        private final Map<String,Object> sessionValues;
        private final Map<String,Object> attributes=new HashMap<>();
        private String redirect;private String forward;private int status;private boolean chainCalled;
        private Exchange(Map<String,Object> sessionValues){this.sessionValues=sessionValues;}
        private HttpServletRequest request(){
            HttpSession session=sessionValues==null?null:proxy(HttpSession.class,(m,a)->"getAttribute".equals(m)?sessionValues.get((String)a[0]):null);
            return proxy(HttpServletRequest.class,(m,a)->{
                switch(m){case "getSession":return session;case "getContextPath":return "/Cava";case "setAttribute":attributes.put((String)a[0],a[1]);return null;case "getRequestDispatcher":String path=(String)a[0];return proxy(RequestDispatcher.class,(dm,da)->{if("forward".equals(dm))forward=path;return null;});default:return null;}
            });
        }
        private HttpServletResponse response(){return proxy(HttpServletResponse.class,(m,a)->{if("sendRedirect".equals(m))redirect=(String)a[0];if("setStatus".equals(m))status=(Integer)a[0];return null;});}
        private FilterChain chain(){return (request,response)->chainCalled=true;}
    }

    @SuppressWarnings("unchecked") private static <T>T proxy(Class<T> type,Invocation call){return (T)Proxy.newProxyInstance(type.getClassLoader(),new Class<?>[]{type},(o,m,a)->call.invoke(m.getName(),a));}
    private static void verify(boolean value,String message){if(!value)throw new AssertionError(message);}
    @FunctionalInterface private interface Invocation{Object invoke(String method,Object[] args)throws Throwable;}
}
