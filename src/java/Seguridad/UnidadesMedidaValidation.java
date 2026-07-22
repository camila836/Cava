package Seguridad;

import java.util.LinkedHashMap;
import java.util.Map;

/** Validación autoritativa del catálogo de unidades de medida. */
public final class UnidadesMedidaValidation {

    public static final int MAX_DESCRIPTION_LENGTH = 45;

    private UnidadesMedidaValidation() {
    }

    public static String normalizeDescription(String value) {
        return value == null ? null : value.strip();
    }

    public static Map<String, String> validateDescription(String value) {
        Map<String, String> errors = new LinkedHashMap<>();
        String normalized = normalizeDescription(value);
        if (normalized == null || normalized.isEmpty()) {
            errors.put("descripcion", "La descripción es obligatoria.");
        } else if (normalized.length() > MAX_DESCRIPTION_LENGTH) {
            errors.put("descripcion", "La descripción admite máximo 45 caracteres.");
        }
        return errors;
    }

    public static boolean isPositiveId(int id) {
        return id > 0;
    }
}
