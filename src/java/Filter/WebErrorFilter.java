package Filter;

import Util.WebErrorDescriptor;
import Util.WebErrorMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Frontera única de errores para las rutas públicas implementadas en Fase 7. */
public final class WebErrorFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(WebErrorFilter.class.getName());
    private static final String ERROR_VIEW = "/WEB-INF/views/error.jsp";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            chain.doFilter(request, response);
        } catch (Exception error) {
            WebErrorDescriptor descriptor = WebErrorMapper.describe(error);
            LOGGER.log(Level.SEVERE,
                    "Public web request failed with HTTP " + descriptor.getStatus(), error);
            if (response.isCommitted()) {
                rethrow(error);
            }
            response.reset();
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(descriptor.getStatus());
            request.setAttribute("errorStatus", descriptor.getStatus());
            request.setAttribute("errorTitle", descriptor.getTitle());
            request.setAttribute("errorMessage", descriptor.getMessage());
            request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
        }
    }

    private static void rethrow(Exception error) throws IOException, ServletException {
        if (error instanceof IOException) {
            throw (IOException) error;
        }
        if (error instanceof ServletException) {
            throw (ServletException) error;
        }
        throw new ServletException("Public web request failed after response commit", error);
    }
}
