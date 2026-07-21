package Servlets;

import java.nio.file.Files;
import java.nio.file.Path;

/** Contratos estáticos de rutas, formularios y configuración de sesión. */
public final class AuthenticationContractTest {

    private AuthenticationContractTest() {
    }

    public static void main(String[] args) throws Exception {
        String registration = Files.readString(Path.of("web", "WEB-INF", "views", "registro.jsp"));
        String login = Files.readString(Path.of("web", "WEB-INF", "views", "login.jsp"));
        String admin = Files.readString(Path.of("web", "WEB-INF", "views", "admin.jsp"));
        String webXml = Files.readString(Path.of("web", "WEB-INF", "web.xml"));
        verify(registration.contains("/registro") && registration.contains("csrfToken"), "registro MVC/CSRF");
        verify(!registration.contains("idRoles") && !registration.contains("idCiudades")
                && !registration.contains("idTipoDocumento"), "sin IDs confiados al navegador");
        verify(registration.contains("minlength=\"15\"") && registration.contains("maxlength=\"128\""), "política HTML");
        verify(login.contains("/login") && login.contains("csrfToken")
                && !login.contains("recordarme") && !login.contains("Olvidaste"), "login mínimo");
        verify(admin.contains("/logout") && admin.contains("csrfToken"), "logout POST/CSRF");
        verify(!Files.exists(Path.of("web", "Admin.jsp")), "vista admin no accesible directamente");
        verify(webXml.contains("<session-timeout>30</session-timeout>")
                && webXml.contains("<tracking-mode>COOKIE</tracking-mode>")
                && webXml.contains("<error-code>403</error-code>"), "sesión y 403");
    }

    private static void verify(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
