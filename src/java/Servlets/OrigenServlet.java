package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Controlador de la historia y el origen público de CAVA. */
@WebServlet(name = "OrigenServlet", urlPatterns = {"/origen"})
public final class OrigenServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/origen.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
