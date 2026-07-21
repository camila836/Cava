package Servlets;

import Controlador.RolesDAO;
import Controlador.UsuariosDAO;
import Controlador.excepciones.DAOErrorType;
import Controlador.excepciones.DAOException;
import Modelo.Roles;
import Modelo.Usuarios;
import Seguridad.AccountValidation;
import Seguridad.CsrfProtection;
import Seguridad.PasswordSecurity;
import Seguridad.SessionCookieSecurity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/** Registro mínimo de cuentas; el rol CLIENTE se resuelve exclusivamente en servidor. */
@WebServlet(name = "RegistroServlet", urlPatterns = {"/registro"})
public class RegistroServlet extends HttpServlet {

    static final String VIEW = "/WEB-INF/views/registro.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        jakarta.servlet.http.HttpSession session = request.getSession(true);
        request.setAttribute("csrfToken", CsrfProtection.getOrCreate(session));
        SessionCookieSecurity.refresh(request, response, session);
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!CsrfProtection.isValid(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String names = trim(request.getParameter("nombres"));
        String lastNames = trim(request.getParameter("apellidos"));
        String email = AccountValidation.normalizeEmail(request.getParameter("correo"));
        char[] password = chars(request.getParameter("clave"));
        char[] confirmation = chars(request.getParameter("confirmarClave"));
        boolean consent = "true".equals(request.getParameter("autorizacionTratamientoDatos"));
        boolean privilegeParameters = request.getParameter("idRoles") != null
                || request.getParameter("codigoRol") != null;

        try {
            Map<String, String> errors = AccountValidation.registrationErrors(names, lastNames,
                    email, password, confirmation, consent, privilegeParameters);
            if (!errors.isEmpty()) {
                showErrors(request, response, errors, names, lastNames, email);
                return;
            }
            if (findByEmail(email) != null) {
                errors.put("correo", "No fue posible registrar la cuenta con los datos proporcionados.");
                showErrors(request, response, errors, names, lastNames, email);
                return;
            }
            Roles clientRole = findRole("CLIENTE");
            if (clientRole == null) {
                throw new IllegalStateException("El rol público autorizado no está configurado");
            }

            Usuarios user = new Usuarios();
            user.setNombres(names);
            user.setApellidos(lastNames);
            user.setIdentificacion(null);
            user.setCorreo(email);
            user.setDireccion(null);
            user.setTelefono(null);
            user.setClave(PasswordSecurity.hash(password));
            user.setIsActivo(true);
            user.setFechaNacimiento(null);
            user.setFechaVencimientoClave(null);
            user.setAutorizacionTratamientoDatos(true);
            user.setIdRoles(clientRole.getIdRoles());
            user.setIdTipoDocumento(null);
            user.setIdCiudades(null);
            try {
                insert(user);
            } catch (DAOException error) {
                if (error.getErrorType() != DAOErrorType.DUPLICATE) {
                    throw error;
                }
                errors.put("correo", "No fue posible registrar la cuenta con los datos proporcionados.");
                showErrors(request, response, errors, names, lastNames, email);
                return;
            }
            CsrfProtection.rotate(request.getSession(false));
            response.sendRedirect(request.getContextPath() + "/login?registro=ok");
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirmation, '\0');
        }
    }

    protected Usuarios findByEmail(String email) {
        return new UsuariosDAO().buscarPorCorreo(email);
    }

    protected Roles findRole(String code) {
        return new RolesDAO().buscarPorCodigo(code);
    }

    protected void insert(Usuarios user) {
        new UsuariosDAO().insertar(user);
    }

    private static void showErrors(HttpServletRequest request, HttpServletResponse response,
            Map<String, String> errors, String names, String lastNames, String email)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        request.setAttribute("errors", errors);
        request.setAttribute("nombres", names);
        request.setAttribute("apellidos", lastNames);
        request.setAttribute("correo", email);
        request.setAttribute("csrfToken", CsrfProtection.getOrCreate(request.getSession(true)));
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private static char[] chars(String value) {
        return value == null ? new char[0] : value.toCharArray();
    }
}
