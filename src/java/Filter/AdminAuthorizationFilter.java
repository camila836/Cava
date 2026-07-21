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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Autoriza /admin por codigoRol controlado por el servidor. */
public final class AdminAuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuarios") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!"ADMINISTRADOR".equals(session.getAttribute("codigoRol"))) {
            WebErrorDescriptor error = WebErrorMapper.descriptorFor(HttpServletResponse.SC_FORBIDDEN);
            response.setStatus(error.getStatus());
            request.setAttribute("errorStatus", error.getStatus());
            request.setAttribute("errorTitle", error.getTitle());
            request.setAttribute("errorMessage", error.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
