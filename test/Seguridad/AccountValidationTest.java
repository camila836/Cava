package Seguridad;

import java.util.Arrays;
import java.util.Map;

/** Validación dirigida del registro mínimo y la pareja documental opcional. */
public final class AccountValidationTest {

    private AccountValidationTest() {
    }

    public static void main(String[] args) {
        char[] valid = "frase de paso válida".toCharArray();
        try {
            verify("cliente@ejemplo.com".equals(AccountValidation.normalizeEmail(" Cliente@Ejemplo.COM ")), "normalización");
            verify(AccountValidation.registrationErrors("Ana", "Cava", "ana@cava.test", valid, valid, true, false).isEmpty(), "registro válido");
            Map<String, String> manipulated = AccountValidation.registrationErrors("Ana", "Cava", "ana@cava.test", valid, valid, true, true);
            verify(manipulated.containsKey("formulario"), "parámetro de privilegio rechazado");
            verify(AccountValidation.documentPairIsValid(null, null), "documento ausente");
            verify(AccountValidation.documentPairIsValid(1, "123"), "documento completo");
            verify(!AccountValidation.documentPairIsValid(1, null), "falta identificación");
            verify(!AccountValidation.documentPairIsValid(null, "123"), "falta tipo");
        } finally {
            Arrays.fill(valid, '\0');
        }
    }

    private static void verify(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
