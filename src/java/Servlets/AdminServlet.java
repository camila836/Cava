package Servlets;

import Seguridad.CsrfProtection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Entrada MVC protegida a la vista administrativa. */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("csrfToken", CsrfProtection.getOrCreate(request.getSession(false)));
        request.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(request, response);
    }
}
