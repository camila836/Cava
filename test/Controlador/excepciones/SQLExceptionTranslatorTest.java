package Controlador.excepciones;

import java.sql.SQLException;

/** Pruebas unitarias sin base de datos para la clasificacion segura de SQL. */
public final class SQLExceptionTranslatorTest {
    private SQLExceptionTranslatorTest() {
    }

    public static void main(String[] args) {
        espera(DAOErrorType.CONNECTION, new SQLException("detalle-interno", "08001", 0));
        espera(DAOErrorType.DUPLICATE, new SQLException("detalle-interno", "23000", 1062));
        espera(DAOErrorType.FOREIGN_KEY, new SQLException("detalle-interno", "23000", 1452));
        espera(DAOErrorType.INVALID_DATA, new SQLException("detalle-interno", "22001", 1406));
        espera(DAOErrorType.SQL_ERROR, new SQLException("detalle-interno", "42000", 1064));
        if (SQLExceptionTranslator.notFound("test").getErrorType() != DAOErrorType.NOT_FOUND) {
            throw new AssertionError("NOT_FOUND no fue preservado");
        }
        if (SQLExceptionTranslator.operationNotAllowed("test").getErrorType()
                != DAOErrorType.OPERATION_NOT_ALLOWED) {
            throw new AssertionError("OPERATION_NOT_ALLOWED no fue preservado");
        }
        System.out.println("SQL_EXCEPTION_TRANSLATOR_OK");
    }

    private static void espera(DAOErrorType esperado, SQLException causa) {
        DAOException actual = SQLExceptionTranslator.translate("test", causa);
        if (actual.getErrorType() != esperado || actual.getCause() != causa
                || actual.getMessage().contains("detalle-interno")) {
            throw new AssertionError("Clasificacion o mensaje seguro incorrecto: " + esperado);
        }
    }
}
