package Util;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/** Contrato JSON uniforme para los endpoints administrativos. */
public final class JsonResponse {

    private JsonResponse() {
    }

    public static void write(HttpServletResponse response, int status, boolean ok,
            String message, JsonValue data, Map<String, String> errors) throws IOException {
        JsonObjectBuilder body = Json.createObjectBuilder()
                .add("ok", ok)
                .add("message", message == null ? "" : message);
        if (data == null) {
            body.addNull("data");
        } else {
            body.add("data", data);
        }
        JsonObjectBuilder errorBody = Json.createObjectBuilder();
        if (errors != null) {
            errors.forEach(errorBody::add);
        }
        body.add("errors", errorBody);
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(body.build().toString());
    }
}
