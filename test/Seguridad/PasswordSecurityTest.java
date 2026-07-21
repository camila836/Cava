package Seguridad;

import java.util.Arrays;

/** Suite rápida de formato, salt, límites y verificación PBKDF2. */
public final class PasswordSecurityTest {

    private PasswordSecurityTest() {
    }

    public static void main(String[] args) {
        char[] password = "contraseña sintética larga".toCharArray();
        char[] wrong = "contraseña sintética falsa".toCharArray();
        try {
            String first = PasswordSecurity.hash(password);
            String second = PasswordSecurity.hash(password);
            verify(first.startsWith("pbkdf2_sha256$600000$"), "formato versionado");
            verify(first.length() <= 255, "VARCHAR(255) suficiente");
            verify(!first.equals(second), "salt único");
            verify(PasswordSecurity.verify(password, first), "verificación correcta");
            verify(!PasswordSecurity.verify(wrong, first), "rechazo incorrecta");
            verify(!PasswordSecurity.verify(password, "hash-malformado"), "hash malformado seguro");
            expectInvalid(new char[14]);
            expectInvalid(new char[129]);
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(wrong, '\0');
        }
    }

    private static void expectInvalid(char[] value) {
        try {
            PasswordSecurity.hash(value);
            throw new AssertionError("longitud inválida aceptada");
        } catch (IllegalArgumentException expected) {
            // esperado
        } finally {
            Arrays.fill(value, '\0');
        }
    }

    private static void verify(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
