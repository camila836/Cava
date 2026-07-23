package Servlets;

import Filter.EncodingFilter;
import Modelo.Productos;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Pruebas unitarias sin librerías externas para los contratos públicos. */
public final class PublicServletContractTest {

    private PublicServletContractTest() {
    }

    public static void main(String[] args) throws Exception {
        verificaRuta(InicioServlet.class, "/inicio");
        verificaRuta(OrigenServlet.class, "/origen");
        verificaRuta(ProductosServlet.class, "/productos");
        verificaForwardInicio();
        verificaForwardOrigen();
        verificaListaVacia();
        verificaEncodingAntesDeLaCadena();
        verificaVistasSinSesionYEstadoVacio();
        verificaContratosVisualesAv03();
    }

    private static void verificaRuta(Class<?> servletClass, String expected) {
        WebServlet mapping = servletClass.getAnnotation(WebServlet.class);
        verifica(mapping != null && mapping.urlPatterns().length == 1
                && expected.equals(mapping.urlPatterns()[0]),
                "Ruta Servlet inesperada para " + servletClass.getSimpleName());
    }

    private static void verificaForwardInicio() throws Exception {
        RequestDouble request = new RequestDouble();
        new InicioServlet().doGet(request.request(), response());
        verifica("/Index.jsp".equals(request.forwardedPath),
                "InicioServlet no hizo forward a Index.jsp");
    }

    private static void verificaForwardOrigen() throws Exception {
        RequestDouble request = new RequestDouble();
        new OrigenServlet().doGet(request.request(), response());
        verifica("/WEB-INF/views/origen.jsp".equals(request.forwardedPath),
                "OrigenServlet no hizo forward a la vista protegida");
    }

    private static void verificaListaVacia() throws Exception {
        RequestDouble request = new RequestDouble();
        ProductosServlet servlet = new ProductosServlet() {
            @Override
            protected List<Productos> loadProducts() {
                return Collections.emptyList();
            }
        };
        servlet.doGet(request.request(), response());
        Object products = request.attributes.get("productos");
        verifica(products instanceof List && ((List<?>) products).isEmpty(),
                "La lista vacía debe conservarse como resultado válido");
        verifica("/Productos.jsp".equals(request.forwardedPath),
                "ProductosServlet no hizo forward a Productos.jsp");
    }

    private static void verificaEncodingAntesDeLaCadena() throws Exception {
        String[] requestEncoding = new String[1];
        String[] responseEncoding = new String[1];
        ServletRequest request = proxy(ServletRequest.class, (method, arguments) -> {
            if ("getCharacterEncoding".equals(method)) {
                return requestEncoding[0];
            }
            if ("setCharacterEncoding".equals(method)) {
                requestEncoding[0] = (String) arguments[0];
            }
            return null;
        });
        ServletResponse response = proxy(ServletResponse.class, (method, arguments) -> {
            if ("setCharacterEncoding".equals(method)) {
                responseEncoding[0] = (String) arguments[0];
            }
            return null;
        });
        FilterChain chain = (currentRequest, currentResponse) -> {
            verifica(StandardCharsets.UTF_8.name().equals(requestEncoding[0]),
                    "UTF-8 debe aplicarse antes de la cadena");
            verifica(StandardCharsets.UTF_8.name().equals(responseEncoding[0]),
                    "UTF-8 debe aplicarse a la respuesta");
        };
        new EncodingFilter().doFilter(request, response, chain);
    }

    private static void verificaVistasSinSesionYEstadoVacio() throws Exception {
        String index = Files.readString(Path.of("web", "Index.jsp"));
        String products = Files.readString(Path.of("web", "Productos.jsp"));
        String error = Files.readString(Path.of("web", "WEB-INF", "views", "error.jsp"));
        String origin = Files.readString(Path.of("web", "WEB-INF", "views", "origen.jsp"));
        verifica(index.contains("session=\"false\"")
                && products.contains("session=\"false\"")
                && error.contains("session=\"false\"")
                && origin.contains("session=\"false\""),
                "Las vistas públicas no deben crear sesiones");
        verifica(products.contains("id=\"products-empty-state\"")
                && products.contains("${empty productos}"),
                "La vista debe renderizar un estado vacío explícito");
    }

    private static void verificaContratosVisualesAv03() throws Exception {
        String index = Files.readString(Path.of("web", "Index.jsp"));
        String origin = Files.readString(Path.of("web", "WEB-INF", "views", "origen.jsp"));
        String script = Files.readString(Path.of("web", "js", "inicio.js"));

        verifica(index.contains("data-carousel")
                && index.contains("data-carousel-prev")
                && index.contains("data-carousel-next")
                && index.contains("data-carousel-indicators"),
                "Inicio debe conservar los controles accesibles del carrusel");
        verifica(index.contains("${pageContext.request.contextPath}/origen")
                && origin.contains("${pageContext.request.contextPath}/productos")
                && origin.contains("${pageContext.request.contextPath}/inicio"),
                "AV-03 debe construir las rutas internas con contextPath");
        verifica(!index.contains("target=\"_blank\"")
                && !origin.contains("target=\"_blank\""),
                "Las rutas internas de AV-03 deben abrir en la misma pestaña");
        verifica(script.contains("ArrowLeft")
                && script.contains("ArrowRight")
                && script.contains("pointerdown")
                && script.contains("pointerup"),
                "El carrusel debe admitir teclado y gesto horizontal");
    }

    private static HttpServletResponse response() {
        return proxy(HttpServletResponse.class, (method, arguments) -> defaultValue(method));
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, Invocation invocation) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type},
                (object, method, arguments) -> invocation.invoke(method.getName(), arguments));
    }

    private static Object defaultValue(String method) {
        return null;
    }

    private static void verifica(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    @FunctionalInterface
    private interface Invocation {
        Object invoke(String method, Object[] arguments) throws Throwable;
    }

    private static final class RequestDouble {

        private final Map<String, Object> attributes = new HashMap<>();
        private String forwardedPath;

        private HttpServletRequest request() {
            return proxy(HttpServletRequest.class, (method, arguments) -> {
                switch (method) {
                    case "setAttribute":
                        attributes.put((String) arguments[0], arguments[1]);
                        return null;
                    case "getAttribute":
                        return attributes.get((String) arguments[0]);
                    case "getRequestDispatcher":
                        String path = (String) arguments[0];
                        return proxy(RequestDispatcher.class, (dispatcherMethod, dispatcherArgs) -> {
                            if ("forward".equals(dispatcherMethod)) {
                                forwardedPath = path;
                            }
                            return null;
                        });
                    default:
                        return defaultValue(method);
                }
            });
        }
    }
}
