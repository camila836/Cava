package Seguridad;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Refuerza explícitamente la cookie del contenedor tras crear o renovar sesión. */
public final class SessionCookieSecurity {

    private SessionCookieSecurity() {
    }

    public static void refresh(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }
}
