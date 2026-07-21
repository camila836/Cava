package Listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.annotation.WebListener;

/** Configura la cookie de sesión con atributos nativos de Servlet 6. */
@WebListener
public final class SessionSecurityListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        SessionCookieConfig cookie = event.getServletContext().getSessionCookieConfig();
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // El entorno validado de desarrollo usa HTTP.
        cookie.setAttribute("SameSite", "Lax");
    }
}
