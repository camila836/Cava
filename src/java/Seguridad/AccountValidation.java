package Seguridad;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/** Validación autoritativa para registro y campos opcionales de cuenta. */
public final class AccountValidation {

    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private AccountValidation() {
    }

    public static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    public static Map<String, String> registrationErrors(String names, String lastNames,
            String email, char[] password, char[] confirmation, boolean consent,
            boolean privilegeParametersPresent) {
        Map<String, String> errors = new LinkedHashMap<>();
        validateText(errors, "nombres", names, "Los nombres son obligatorios.");
        validateText(errors, "apellidos", lastNames, "Los apellidos son obligatorios.");
        if (email == null || email.isBlank() || email.length() > 100 || !EMAIL.matcher(email).matches()) {
            errors.put("correo", "Ingresa un correo válido.");
        }
        if (password == null || password.length < PasswordSecurity.MIN_LENGTH
                || password.length > PasswordSecurity.MAX_LENGTH) {
            errors.put("clave", "La contraseña debe tener entre 15 y 128 caracteres.");
        } else if (confirmation == null || !java.util.Arrays.equals(password, confirmation)) {
            errors.put("confirmarClave", "Las contraseñas no coinciden.");
        }
        if (!consent) {
            errors.put("consentimiento", "Debes autorizar el tratamiento de datos.");
        }
        if (privilegeParametersPresent) {
            errors.put("formulario", "La solicitud contiene parámetros no permitidos.");
        }
        return errors;
    }

    public static boolean documentPairIsValid(Integer documentType, String identification) {
        return (documentType == null) == (identification == null || identification.isBlank());
    }

    private static void validateText(Map<String, String> errors, String field,
            String value, String requiredMessage) {
        if (value == null || value.isBlank()) {
            errors.put(field, requiredMessage);
        } else if (value.trim().length() > 45) {
            errors.put(field, "El valor supera la longitud permitida.");
        }
    }
}
