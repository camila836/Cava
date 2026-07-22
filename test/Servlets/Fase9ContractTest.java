package Servlets;

import java.nio.file.Files;
import java.nio.file.Path;

/** Contratos estáticos de interfaz, seguridad y empaquetado de Fase 9. */
public final class Fase9ContractTest {

    private Fase9ContractTest() {
    }

    public static void main(String[] args) throws Exception {
        String jsp=Files.readString(Path.of("web","WEB-INF","views","admin.jsp"));
        String js=Files.readString(Path.of("web","js","admin-unidades-medida.js"));
        String webXml=Files.readString(Path.of("web","WEB-INF","web.xml"));
        String migration=Files.readString(Path.of("database","migrations","F009__unidades_medida_unicas.sql"));
        verify(jsp.contains("admin-unidades-medida.js") && jsp.contains("empty-state")
                && jsp.contains("delete-dialog") && jsp.contains("csrf-token"), "interfaz completa");
        verify(js.contains("fetch(") && js.contains("response.ok")
                && js.contains("textContent") && !js.contains("innerHTML"), "cliente asíncrono/XSS");
        verify(js.contains("X-CSRF-Token") && js.contains("button.disabled")
                && js.contains("response.redirected"), "CSRF, doble envío y sesión");
        verify(webXml.contains("<url-pattern>/admin/*</url-pattern>"), "protección administrativa");
        verify(migration.contains("UNIQUE (descripcionUnidadesMed)")
                && !migration.matches("(?is).*\\b(DROP|TRUNCATE)\\b.*"), "migración segura");
    }

    private static void verify(boolean condition,String message){if(!condition)throw new AssertionError(message);}
}
