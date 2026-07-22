package Servlets;

import Controlador.excepciones.DAOErrorType;
import Controlador.excepciones.DAOException;
import Modelo.UnidadesMedida;
import Seguridad.CsrfProtection;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Contrato HTTP/JSON, validación, CSRF y errores del CRUD. */
public final class UnidadesMedidaServletTest {

    private UnidadesMedidaServletTest() {
    }

    public static void main(String[] args) throws Exception {
        listAndFind();
        createAndNormalize();
        validationAndMassAssignment();
        csrfAndContentType();
        updateDeleteAndDaoErrors();
        xssAndSqlInjectionRemainData();
    }

    private static void listAndFind() throws Exception {
        Stub servlet = new Stub();
        servlet.rows.add(unit(7, "Gramo"));
        Exchange list = new Exchange("GET", null, true);
        servlet.doGet(list.request(), list.response());
        verify(list.status == 200 && list.body().contains("Gramo")
                && list.contentType.startsWith("application/json"), "listado JSON 200");
        Exchange missing = new Exchange("GET", null, true);
        missing.parameters.put("id", new String[]{"999"});
        servlet.doGet(missing.request(), missing.response());
        verify(missing.status == 404, "consulta inexistente 404");
        Exchange invalid = new Exchange("GET", null, true);
        invalid.parameters.put("id", new String[]{"0"});
        servlet.doGet(invalid.request(), invalid.response());
        verify(invalid.status == 400, "ID cero 400");
    }

    private static void createAndNormalize() throws Exception {
        Stub servlet = new Stub();
        Exchange exchange = new Exchange("POST", "{\"descripcion\":\"  Kilogramo  \"}", true);
        servlet.doPost(exchange.request(), exchange.response());
        verify(exchange.status == 201 && servlet.last != null
                && "Kilogramo".equals(servlet.last.getDescripcionUnidadesM())
                && exchange.body().contains("Kilogramo"), "creación 201 normalizada");
    }

    private static void validationAndMassAssignment() throws Exception {
        Stub servlet = new Stub();
        for (String body : List.of("{\"descripcion\":\"\"}",
                "{\"descripcion\":\"   \"}",
                "{\"descripcion\":\"" + "x".repeat(46) + "\"}",
                "{\"descripcion\":\"Gramo\",\"interno\":true}")) {
            Exchange exchange = new Exchange("POST", body, true);
            servlet.doPost(exchange.request(), exchange.response());
            verify(exchange.status == 400 && servlet.last == null, "payload inválido 400");
        }
        Exchange badId = new Exchange("PUT", "{\"id\":-1,\"descripcion\":\"Gramo\"}", true);
        servlet.doPut(badId.request(), badId.response());
        verify(badId.status == 400, "ID negativo 400");
        Exchange fractional = new Exchange("DELETE", null, true);
        fractional.parameters.put("id", new String[]{"1.5"});
        servlet.doDelete(fractional.request(), fractional.response());
        verify(fractional.status == 400, "ID fraccionario 400");
    }

    private static void csrfAndContentType() throws Exception {
        Stub servlet = new Stub();
        Exchange absent = new Exchange("POST", "{\"descripcion\":\"Gramo\"}", false);
        servlet.doPost(absent.request(), absent.response());
        verify(absent.status == 400 && servlet.last == null, "CSRF ausente 400");
        Exchange invalid = new Exchange("POST", "{\"descripcion\":\"Gramo\"}", true);
        invalid.csrfHeader = "incorrecto";
        servlet.doPost(invalid.request(), invalid.response());
        verify(invalid.status == 400 && servlet.last == null, "CSRF inválido 400");
        Exchange wrongType = new Exchange("POST", "{\"descripcion\":\"Gramo\"}", true);
        wrongType.contentType = "text/plain";
        servlet.doPost(wrongType.request(), wrongType.response());
        verify(wrongType.status == 400, "Content-Type inválido 400");
    }

    private static void updateDeleteAndDaoErrors() throws Exception {
        Stub servlet = new Stub();
        Exchange update = new Exchange("PUT", "{\"id\":4,\"descripcion\":\"Caja\"}", true);
        servlet.doPut(update.request(), update.response());
        verify(update.status == 200 && servlet.last.getIdUnidadesMedida() == 4, "actualización 200");
        Exchange delete = new Exchange("DELETE", null, true);
        delete.parameters.put("id", new String[]{"4"});
        servlet.doDelete(delete.request(), delete.response());
        verify(delete.status == 200 && servlet.deleted == 4, "eliminación 200");
        for (DAOErrorType type : List.of(DAOErrorType.DUPLICATE, DAOErrorType.NOT_FOUND,
                DAOErrorType.FOREIGN_KEY, DAOErrorType.CONNECTION)) {
            servlet.failure = type;
            Exchange exchange = new Exchange("POST", "{\"descripcion\":\"Gramo\"}", true);
            servlet.doPost(exchange.request(), exchange.response());
            int expected = type == DAOErrorType.DUPLICATE || type == DAOErrorType.FOREIGN_KEY
                    ? 409 : type == DAOErrorType.NOT_FOUND ? 404 : 503;
            verify(exchange.status == expected && !exchange.body().contains("SQLException"),
                    "mapeo seguro " + type);
        }
    }

    private static void xssAndSqlInjectionRemainData() throws Exception {
        Stub servlet = new Stub();
        for (String value : List.of("<script>alert('x')</script>", "x' OR '1'='1")) {
            Exchange exchange = new Exchange("POST",
                    "{\"descripcion\":\"" + value.replace("'", "'") + "\"}", true);
            servlet.doPost(exchange.request(), exchange.response());
            verify(exchange.status == 201 && value.equals(servlet.last.getDescripcionUnidadesM()),
                    "entrada tratada como dato");
        }
    }

    private static UnidadesMedida unit(int id, String description) {
        UnidadesMedida unit = new UnidadesMedida();
        unit.setIdUnidadesMedida(id);
        unit.setDescripcionUnidadesM(description);
        return unit;
    }

    private static final class Stub extends UnidadesMedidaServlet {
        private final List<UnidadesMedida> rows = new ArrayList<>();
        private UnidadesMedida last;
        private int deleted;
        private DAOErrorType failure;
        @Override protected List<UnidadesMedida> list() { return rows; }
        @Override protected UnidadesMedida find(int id) {
            return rows.stream().filter(row -> row.getIdUnidadesMedida() == id).findFirst().orElse(null);
        }
        @Override protected void insert(UnidadesMedida unit) { fail(); last=unit; unit.setIdUnidadesMedida(41); }
        @Override protected void update(UnidadesMedida unit) { fail(); last=unit; }
        @Override protected void delete(int id) { fail(); deleted=id; }
        private void fail() {
            if (failure != null) throw new DAOException(failure, "test", "detalle interno", null);
        }
    }

    private static final class Exchange {
        private final String method;
        private final String input;
        private final Map<String,String[]> parameters = new HashMap<>();
        private final Map<String,Object> sessionValues = new HashMap<>();
        private final StringWriter output = new StringWriter();
        private int status;
        private String contentType = "application/json;charset=UTF-8";
        private String csrfHeader;
        private Exchange(String method, String input, boolean csrf) {
            this.method=method;this.input=input;
            HttpSession session=session();
            String token=CsrfProtection.getOrCreate(session);
            if(csrf)csrfHeader=token;
        }
        private HttpSession session() {
            return proxy(HttpSession.class,(name,args)->{
                if("getAttribute".equals(name))return sessionValues.get((String)args[0]);
                if("setAttribute".equals(name)){sessionValues.put((String)args[0],args[1]);return null;}
                return null;
            });
        }
        private HttpServletRequest request() {
            HttpSession session=session();
            return proxy(HttpServletRequest.class,(name,args)->{
                switch(name){
                    case "getMethod":return method;
                    case "getParameterMap":return Collections.unmodifiableMap(parameters);
                    case "getParameter":String[] values=parameters.get((String)args[0]);return values==null?null:values[0];
                    case "getParameterValues":return parameters.get((String)args[0]);
                    case "getHeader":return CsrfProtection.HEADER.equals(args[0])?csrfHeader:null;
                    case "getSession":return session;
                    case "getContentType":return contentType;
                    case "getReader":return new BufferedReader(new StringReader(input==null?"":input));
                    default:return null;
                }
            });
        }
        private HttpServletResponse response() {
            PrintWriter writer=new PrintWriter(output,true);
            return proxy(HttpServletResponse.class,(name,args)->{
                switch(name){
                    case "setStatus":status=(Integer)args[0];return null;
                    case "setContentType":contentType=(String)args[0];return null;
                    case "getWriter":return writer;
                    default:return null;
                }
            });
        }
        private String body(){return output.toString();}
    }

    @SuppressWarnings("unchecked")
    private static <T>T proxy(Class<T> type, Invocation invocation){
        return (T)Proxy.newProxyInstance(type.getClassLoader(),new Class<?>[]{type},
                (object,method,args)->invocation.call(method.getName(),args));
    }
    private static void verify(boolean condition,String message){if(!condition)throw new AssertionError(message);}
    @FunctionalInterface private interface Invocation{Object call(String name,Object[] args)throws Throwable;}
}
