package Seguridad;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** PBKDF2 versionado y autocontenido para contraseñas de CAVA. */
public final class PasswordSecurity {

    public static final int MIN_LENGTH = 15;
    public static final int MAX_LENGTH = 128;
    public static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final String PREFIX = "pbkdf2_sha256";
    private static final String JCA_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String DUMMY_HASH = createDummyHash();

    private PasswordSecurity() {
    }

    public static String hash(char[] password) {
        validateLength(password);
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] derived = derive(password, salt, ITERATIONS, KEY_BITS);
        try {
            return PREFIX + "$" + ITERATIONS + "$"
                    + Base64.getEncoder().encodeToString(salt) + "$"
                    + Base64.getEncoder().encodeToString(derived);
        } finally {
            Arrays.fill(salt, (byte) 0);
            Arrays.fill(derived, (byte) 0);
        }
    }

    public static boolean verify(char[] password, String encoded) {
        if (password == null || password.length > MAX_LENGTH || encoded == null) {
            return false;
        }
        byte[] salt = null;
        byte[] expected = null;
        byte[] actual = null;
        try {
            String[] parts = encoded.split("\\$", -1);
            if (parts.length != 4 || !PREFIX.equals(parts[0])) {
                return false;
            }
            int iterations = Integer.parseInt(parts[1]);
            if (iterations < ITERATIONS || iterations > 2_000_000) {
                return false;
            }
            salt = Base64.getDecoder().decode(parts[2].getBytes(StandardCharsets.US_ASCII));
            expected = Base64.getDecoder().decode(parts[3].getBytes(StandardCharsets.US_ASCII));
            if (salt.length < SALT_BYTES || expected.length != KEY_BITS / 8) {
                return false;
            }
            actual = derive(password, salt, iterations, expected.length * 8);
            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException error) {
            return false;
        } finally {
            clear(salt);
            clear(expected);
            clear(actual);
        }
    }

    public static String dummyHash() {
        return DUMMY_HASH;
    }

    private static byte[] derive(char[] password, byte[] salt, int iterations, int keyBits) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyBits);
        try {
            return SecretKeyFactory.getInstance(JCA_ALGORITHM).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException error) {
            throw new IllegalStateException("PBKDF2WithHmacSHA256 no está disponible", error);
        } finally {
            spec.clearPassword();
        }
    }

    private static void validateLength(char[] password) {
        if (password == null || password.length < MIN_LENGTH || password.length > MAX_LENGTH) {
            throw new IllegalArgumentException("Longitud de contraseña no permitida");
        }
    }

    private static String createDummyHash() {
        char[] synthetic = "credencial-ficticia-interna".toCharArray();
        try {
            return hash(synthetic);
        } finally {
            Arrays.fill(synthetic, '\0');
        }
    }

    private static void clear(byte[] value) {
        if (value != null) {
            Arrays.fill(value, (byte) 0);
        }
    }
}
