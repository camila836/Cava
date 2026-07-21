package Servlets;

import Seguridad.CsrfProtection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/** Logout POST con CSRF e invalidación completa. */
public final class LogoutServletTest {

    private LogoutServletTest() {
    }

    public static void main(String[] args) throws Exception {
        Map<String,Object> attributes=new HashMap<>();boolean[] invalidated={false};
        HttpSession session=proxy(HttpSession.class,(m,a)->{if("getAttribute".equals(m))return attributes.get((String)a[0]);if("setAttribute".equals(m)){attributes.put((String)a[0],a[1]);return null;}if("invalidate".equals(m)){invalidated[0]=true;return null;}return null;});
        String token=CsrfProtection.getOrCreate(session);String[] redirect={null};
        HttpServletRequest request=proxy(HttpServletRequest.class,(m,a)->{if("getSession".equals(m))return session;if("getParameter".equals(m))return "csrfToken".equals(a[0])?token:null;if("getContextPath".equals(m))return "/Cava";return null;});
        HttpServletResponse response=proxy(HttpServletResponse.class,(m,a)->{if("sendRedirect".equals(m))redirect[0]=(String)a[0];return null;});
        new LogoutServlet().doPost(request,response);
        verify(invalidated[0],"sesión invalidada");verify("/Cava/inicio".equals(redirect[0]),"redirección pública");
    }

    @SuppressWarnings("unchecked") private static <T>T proxy(Class<T> type,Invocation call){return (T)Proxy.newProxyInstance(type.getClassLoader(),new Class<?>[]{type},(o,m,a)->call.invoke(m.getName(),a));}
    private static void verify(boolean value,String message){if(!value)throw new AssertionError(message);}
    @FunctionalInterface private interface Invocation{Object invoke(String method,Object[] args)throws Throwable;}
}
