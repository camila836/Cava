package Seguridad;

/** Casos puros de validación del primer catálogo administrativo. */
public final class UnidadesMedidaValidationTest {

    private UnidadesMedidaValidationTest() {
    }

    public static void main(String[] args) {
        verify(UnidadesMedidaValidation.validateDescription(null).containsKey("descripcion"),
                "nulo rechazado");
        verify(UnidadesMedidaValidation.validateDescription("").containsKey("descripcion"),
                "vacío rechazado");
        verify(UnidadesMedidaValidation.validateDescription("   ").containsKey("descripcion"),
                "espacios rechazados");
        verify(UnidadesMedidaValidation.validateDescription("x".repeat(46))
                .containsKey("descripcion"), "longitud rechazada");
        verify(UnidadesMedidaValidation.validateDescription("  Gramo  ").isEmpty(),
                "valor válido");
        verify("Gramo".equals(UnidadesMedidaValidation.normalizeDescription("  Gramo  ")),
                "normalización limitada a bordes");
        verify(UnidadesMedidaValidation.isPositiveId(1)
                && !UnidadesMedidaValidation.isPositiveId(0)
                && !UnidadesMedidaValidation.isPositiveId(-1), "ID positivo");
    }

    private static void verify(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
