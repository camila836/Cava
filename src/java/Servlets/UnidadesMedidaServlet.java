package Servlets;

import Controlador.UnidadesMedidaDAO;
import Controlador.excepciones.DAOErrorType;
import Controlador.excepciones.DAOException;
import Modelo.UnidadesMedida;
import Seguridad.CsrfProtection;
import Seguridad.UnidadesMedidaValidation;
import Util.JsonResponse;
import Util.WebErrorMapper;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonException;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** CRUD JSON administrativo del catálogo unidadesMedida. */
@WebServlet(name = "UnidadesMedidaServlet", urlPatterns = {"/admin/unidades-medida"})
public class UnidadesMedidaServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UnidadesMedidaServlet.class.getName());
    private static final Set<String> CREATE_FIELDS = Set.of("descripcion");
    private static final Set<String> UPDATE_FIELDS = Set.of("id", "descripcion");
    private static final Set<String> ALLOWED_METHODS = Set.of("GET", "POST", "PUT", "DELETE");
    private final UnidadesMedidaDAO dao = new UnidadesMedidaDAO();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!ALLOWED_METHODS.contains(request.getMethod())) {
            JsonResponse.write(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, false,
                    "El método HTTP no está permitido.", null, Collections.emptyMap());
            return;
        }
        super.service(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!hasOnlyQueryParameters(request, Set.of("id"))) {
            invalidRequest(response, "La solicitud contiene campos no autorizados.");
            return;
        }
        try {
            String rawId = request.getParameter("id");
            if (rawId == null) {
                JsonArrayBuilder data = Json.createArrayBuilder();
                for (UnidadesMedida unidad : list()) {
                    data.add(toJson(unidad));
                }
                JsonResponse.write(response, HttpServletResponse.SC_OK, true,
                        "Unidades de medida consultadas.", data.build(), Collections.emptyMap());
                return;
            }
            Integer id = parsePositiveId(rawId);
            if (id == null) {
                invalidField(response, "id", "El identificador debe ser un entero positivo.");
                return;
            }
            UnidadesMedida unidad = find(id);
            if (unidad == null) {
                notFound(response);
                return;
            }
            JsonResponse.write(response, HttpServletResponse.SC_OK, true,
                    "Unidad de medida consultada.", toJson(unidad), Collections.emptyMap());
        } catch (DAOException exception) {
            handleDaoException(response, exception);
        } catch (RuntimeException exception) {
            handleUnexpected(response, exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!verifyMutationRequest(request, response)) {
            return;
        }
        JsonObject body = readObject(request, response);
        if (body == null || !hasExactlyFields(body, CREATE_FIELDS)) {
            if (body != null) {
                invalidRequest(response, "La solicitud contiene campos no autorizados.");
            }
            return;
        }
        String description = readString(body, "descripcion");
        Map<String, String> errors = UnidadesMedidaValidation.validateDescription(description);
        if (!errors.isEmpty()) {
            validationErrors(response, errors);
            return;
        }
        UnidadesMedida unidad = new UnidadesMedida();
        unidad.setDescripcionUnidadesM(
                UnidadesMedidaValidation.normalizeDescription(description));
        try {
            insert(unidad);
            JsonResponse.write(response, HttpServletResponse.SC_CREATED, true,
                    "Unidad de medida creada.", toJson(unidad), Collections.emptyMap());
        } catch (DAOException exception) {
            handleDaoException(response, exception);
        } catch (RuntimeException exception) {
            handleUnexpected(response, exception);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!verifyMutationRequest(request, response)) {
            return;
        }
        JsonObject body = readObject(request, response);
        if (body == null || !hasExactlyFields(body, UPDATE_FIELDS)) {
            if (body != null) {
                invalidRequest(response, "La solicitud contiene campos no autorizados.");
            }
            return;
        }
        Integer id = readPositiveId(body);
        if (id == null) {
            invalidField(response, "id", "El identificador debe ser un entero positivo.");
            return;
        }
        String description = readString(body, "descripcion");
        Map<String, String> errors = UnidadesMedidaValidation.validateDescription(description);
        if (!errors.isEmpty()) {
            validationErrors(response, errors);
            return;
        }
        UnidadesMedida unidad = new UnidadesMedida();
        unidad.setIdUnidadesMedida(id);
        unidad.setDescripcionUnidadesM(
                UnidadesMedidaValidation.normalizeDescription(description));
        try {
            update(unidad);
            JsonResponse.write(response, HttpServletResponse.SC_OK, true,
                    "Unidad de medida actualizada.", toJson(unidad), Collections.emptyMap());
        } catch (DAOException exception) {
            handleDaoException(response, exception);
        } catch (RuntimeException exception) {
            handleUnexpected(response, exception);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!verifyDeleteRequest(request, response)) {
            return;
        }
        Integer id = parsePositiveId(request.getParameter("id"));
        if (id == null) {
            invalidField(response, "id", "El identificador debe ser un entero positivo.");
            return;
        }
        try {
            delete(id);
            JsonResponse.write(response, HttpServletResponse.SC_OK, true,
                    "Unidad de medida eliminada.", Json.createObjectBuilder().add("id", id).build(),
                    Collections.emptyMap());
        } catch (DAOException exception) {
            handleDaoException(response, exception);
        } catch (RuntimeException exception) {
            handleUnexpected(response, exception);
        }
    }

    protected List<UnidadesMedida> list() {
        return dao.listarTodos();
    }

    protected UnidadesMedida find(int id) {
        return dao.consultarPorId(id);
    }

    protected void insert(UnidadesMedida unidad) {
        dao.insertar(unidad);
    }

    protected void update(UnidadesMedida unidad) {
        dao.actualizar(unidad);
    }

    protected void delete(int id) {
        dao.eliminar(id);
    }

    private boolean verifyMutationRequest(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String query = request.getQueryString();
        if (query != null && !query.isBlank()) {
            invalidRequest(response, "La solicitud contiene campos no autorizados.");
            return false;
        }
        if (!CsrfProtection.isValid(request)) {
            invalidRequest(response, "El token CSRF es inválido.");
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null
                || !contentType.toLowerCase().startsWith("application/json")) {
            invalidRequest(response, "El cuerpo debe usar application/json.");
            return false;
        }
        return true;
    }

    private boolean verifyDeleteRequest(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String[] ids = request.getParameterValues("id");
        if (!hasOnlyQueryParameters(request, Set.of("id"))
                || ids == null || ids.length != 1) {
            invalidRequest(response, "La solicitud contiene campos no autorizados.");
            return false;
        }
        if (!CsrfProtection.isValid(request)) {
            invalidRequest(response, "El token CSRF es inválido.");
            return false;
        }
        return true;
    }

    private JsonObject readObject(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try (JsonReader reader = Json.createReader(request.getReader())) {
            return reader.readObject();
        } catch (JsonException | IllegalStateException exception) {
            invalidRequest(response, "El cuerpo JSON no es válido.");
            return null;
        }
    }

    private boolean hasOnlyQueryParameters(HttpServletRequest request, Set<String> allowed) {
        Map<String, String[]> parameters = request.getParameterMap();
        return parameters != null && allowed.containsAll(parameters.keySet());
    }

    private boolean hasExactlyFields(JsonObject object, Set<String> expected) {
        return object.keySet().equals(expected);
    }

    private String readString(JsonObject object, String field) {
        JsonValue value = object.get(field);
        return value != null && value.getValueType() == JsonValue.ValueType.STRING
                ? object.getString(field) : null;
    }

    private Integer readPositiveId(JsonObject object) {
        JsonValue value = object.get("id");
        if (!(value instanceof JsonNumber)) {
            return null;
        }
        JsonNumber number = (JsonNumber) value;
        if (!number.isIntegral()) {
            return null;
        }
        try {
            int id = number.intValueExact();
            return UnidadesMedidaValidation.isPositiveId(id) ? id : null;
        } catch (ArithmeticException exception) {
            return null;
        }
    }

    private Integer parsePositiveId(String value) {
        try {
            int id = Integer.parseInt(value);
            return UnidadesMedidaValidation.isPositiveId(id) ? id : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private JsonObject toJson(UnidadesMedida unidad) {
        return Json.createObjectBuilder()
                .add("id", unidad.getIdUnidadesMedida())
                .add("descripcion", unidad.getDescripcionUnidadesM())
                .build();
    }

    private void validationErrors(HttpServletResponse response, Map<String, String> errors)
            throws IOException {
        JsonResponse.write(response, HttpServletResponse.SC_BAD_REQUEST, false,
                "Revisa los datos enviados.", null, errors);
    }

    private void invalidField(HttpServletResponse response, String field, String message)
            throws IOException {
        validationErrors(response, Map.of(field, message));
    }

    private void invalidRequest(HttpServletResponse response, String message) throws IOException {
        JsonResponse.write(response, HttpServletResponse.SC_BAD_REQUEST, false,
                message, null, Collections.emptyMap());
    }

    private void notFound(HttpServletResponse response) throws IOException {
        JsonResponse.write(response, HttpServletResponse.SC_NOT_FOUND, false,
                "La unidad de medida no existe.", null, Collections.emptyMap());
    }

    private void handleDaoException(HttpServletResponse response, DAOException exception)
            throws IOException {
        int status = WebErrorMapper.statusFor(exception.getErrorType());
        LOGGER.log(Level.WARNING, "Administrative persistence failure: {0}",
                exception.getErrorType());
        String message;
        if (exception.getErrorType() == DAOErrorType.DUPLICATE) {
            message = "Ya existe una unidad de medida con esa descripción.";
        } else if (exception.getErrorType() == DAOErrorType.FOREIGN_KEY) {
            message = "La unidad de medida está asociada a productos y no puede eliminarse.";
        } else if (exception.getErrorType() == DAOErrorType.NOT_FOUND) {
            message = "La unidad de medida no existe.";
        } else if (exception.getErrorType() == DAOErrorType.CONNECTION) {
            message = "El servicio de datos no está disponible temporalmente.";
        } else {
            message = "No fue posible completar la operación.";
        }
        JsonResponse.write(response, status, false, message, null, Collections.emptyMap());
    }

    private void handleUnexpected(HttpServletResponse response, RuntimeException exception)
            throws IOException {
        LOGGER.log(Level.SEVERE, "Unexpected administrative CRUD failure", exception);
        JsonResponse.write(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false,
                "No fue posible completar la operación.", null, Collections.emptyMap());
    }
}
