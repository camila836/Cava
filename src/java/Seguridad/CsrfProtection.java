package Seguridad;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** Token CSRF nativo asociado a la sesión. */
public final class CsrfProtection {

    public static final String PARAMETER = "csrfToken";
    private static final String SESSION_ATTRIBUTE = "csrfToken";
    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfProtection() {
    }

    public static String getOrCreate(HttpSession session) {
        Object current = session.getAttribute(SESSION_ATTRIBUTE);
        if (current instanceof String) {
            return (String) current;
        }
        return rotate(session);
    }

    public static String rotate(HttpSession session) {
        byte[] random = new byte[32];
        RANDOM.nextBytes(random);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        session.setAttribute(SESSION_ATTRIBUTE, token);
        return token;
    }

    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object expected = session.getAttribute(SESSION_ATTRIBUTE);
        String supplied = request.getParameter(PARAMETER);
        if (!(expected instanceof String) || supplied == null) {
            return false;
        }
        return MessageDigest.isEqual(((String) expected).getBytes(StandardCharsets.UTF_8),
                supplied.getBytes(StandardCharsets.UTF_8));
    }
}
