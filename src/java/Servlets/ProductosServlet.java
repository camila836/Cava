package Servlets;

import Controlador.ProductosDAO;
import Modelo.Productos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/** Listado público de solo lectura de chocolates CAVA. */
@WebServlet(name = "ProductosServlet", urlPatterns = {"/productos"})
public class ProductosServlet extends HttpServlet {

    private static final String VIEW = "/Productos.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("productos", loadProducts());
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    protected List<Productos> loadProducts() {
        return new ProductosDAO().listarTodos();
    }
}
