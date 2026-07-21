package Seguridad;

import java.util.Arrays;

/** Benchmark manual separado de la suite rápida; no imprime datos criptográficos. */
public final class PasswordSecurityBenchmark {

    private PasswordSecurityBenchmark() {
    }

    public static void main(String[] args) {
        char[] correct = "contraseña sintética benchmark".toCharArray();
        char[] incorrect = "contraseña sintética distinta".toCharArray();
        try {
            for (int i = 0; i < 2; i++) {
                String warmup = PasswordSecurity.hash(correct);
                PasswordSecurity.verify(correct, warmup);
            }
            for (int i = 1; i <= 5; i++) {
                long startHash = System.nanoTime();
                String encoded = PasswordSecurity.hash(correct);
                long endHash = System.nanoTime();
                boolean accepted = PasswordSecurity.verify(correct, encoded);
                long endValid = System.nanoTime();
                boolean rejected = !PasswordSecurity.verify(incorrect, encoded);
                long endInvalid = System.nanoTime();
                if (!accepted || !rejected) {
                    throw new AssertionError("resultado criptográfico inesperado");
                }
                System.out.printf("run=%d hash_ms=%.2f verify_ok_ms=%.2f verify_bad_ms=%.2f%n",
                        i, millis(startHash, endHash), millis(endHash, endValid), millis(endValid, endInvalid));
            }
            System.out.println("algorithm=PBKDF2WithHmacSHA256 iterations=" + PasswordSecurity.ITERATIONS
                    + " jdk=" + System.getProperty("java.version"));
        } finally {
            Arrays.fill(correct, '\0');
            Arrays.fill(incorrect, '\0');
        }
    }

    private static double millis(long start, long end) {
        return (end - start) / 1_000_000.0;
    }
}
