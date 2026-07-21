package Servlets;

import Controlador.UsuariosDAO;
import Modelo.CredencialesUsuario;
import Seguridad.AccountValidation;
import Seguridad.CsrfProtection;
import Seguridad.PasswordSecurity;
import Seguridad.SessionCookieSecurity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

/** Autenticación web con mensaje neutro y renovación del ID de sesión. */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    static final String VIEW = "/WEB-INF/views/login.jsp";
    static final String INVALID_CREDENTIALS = "No fue posible iniciar sesión con las credenciales proporcionadas.";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        request.setAttribute("csrfToken", CsrfProtection.getOrCreate(session));
        SessionCookieSecurity.refresh(request, response, session);
        request.setAttribute("registrationCompleted", "ok".equals(request.getParameter("registro")));
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!CsrfProtection.isValid(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String email = AccountValidation.normalizeEmail(request.getParameter("correo"));
        char[] password = request.getParameter("clave") == null
                ? new char[0] : request.getParameter("clave").toCharArray();
        try {
            if (email.isBlank() || email.length() > 100 || password.length > PasswordSecurity.MAX_LENGTH) {
                showInvalid(request, response, email);
                return;
            }
            CredencialesUsuario credentials = findCredentials(email);
            String stored = credentials == null ? PasswordSecurity.dummyHash() : credentials.getClaveHash();
            boolean validPassword = PasswordSecurity.verify(password, stored);
            if (credentials == null || !credentials.isActivo() || !validPassword) {
                showInvalid(request, response, email);
                return;
            }

            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute("idUsuarios", credentials.getIdUsuarios());
            session.setAttribute("nombreUsuario", credentials.getNombres());
            session.setAttribute("codigoRol", credentials.getCodigoRol());
            session.setAttribute("autenticadoEn", Instant.now());
            CsrfProtection.rotate(session);
            SessionCookieSecurity.refresh(request, response, session);
            String destination = "ADMINISTRADOR".equals(credentials.getCodigoRol()) ? "/admin" : "/inicio";
            response.sendRedirect(request.getContextPath() + destination);
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    protected CredencialesUsuario findCredentials(String email) {
        return new UsuariosDAO().buscarCredencialesPorCorreo(email);
    }

    private static void showInvalid(HttpServletRequest request, HttpServletResponse response,
            String email) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        request.setAttribute("loginError", INVALID_CREDENTIALS);
        request.setAttribute("correo", email);
        request.setAttribute("csrfToken", CsrfProtection.getOrCreate(request.getSession(true)));
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
