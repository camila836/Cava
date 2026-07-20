package Util;

import Controlador.excepciones.DAOErrorType;
import Controlador.excepciones.DAOException;
import java.sql.SQLException;

/** Pruebas unitarias sin contenedor para el contrato seguro de errores web. */
public final class WebErrorMapperTest {

    private static final String INTERNAL_DETAIL = "sql-interno contraseña-secreta C:\\servidor";

    private WebErrorMapperTest() {
    }

    public static void main(String[] args) {
        espera(DAOErrorType.INVALID_DATA, 400);
        espera(DAOErrorType.NOT_FOUND, 404);
        espera(DAOErrorType.DUPLICATE, 409);
        espera(DAOErrorType.FOREIGN_KEY, 409);
        espera(DAOErrorType.OPERATION_NOT_ALLOWED, 409);
        espera(DAOErrorType.CONNECTION, 503);
        espera(DAOErrorType.SQL_ERROR, 500);

        WebErrorDescriptor unexpected = WebErrorMapper.describe(
                new IllegalStateException(INTERNAL_DETAIL));
        verifica(unexpected.getStatus() == 500, "Error inesperado debe ser 500");
        verificaSeguro(unexpected);

        WebErrorDescriptor wrapped = WebErrorMapper.describe(
                new RuntimeException("envoltura", daoError(DAOErrorType.CONNECTION)));
        verifica(wrapped.getStatus() == 503, "Debe localizar DAOException envuelta");
        verificaSeguro(wrapped);
    }

    private static void espera(DAOErrorType type, int status) {
        WebErrorDescriptor descriptor = WebErrorMapper.describe(daoError(type));
        verifica(descriptor.getStatus() == status,
                type + " debía producir HTTP " + status);
        verificaSeguro(descriptor);
    }

    private static DAOException daoError(DAOErrorType type) {
        return new DAOException(type, "operacion-interna", INTERNAL_DETAIL,
                new SQLException(INTERNAL_DETAIL, "42000", 1064));
    }

    private static void verificaSeguro(WebErrorDescriptor descriptor) {
        String publicText = descriptor.getTitle() + " " + descriptor.getMessage();
        verifica(!publicText.contains("sql-interno")
                && !publicText.contains("contraseña-secreta")
                && !publicText.contains("C:\\servidor")
                && !publicText.contains("SQLException"),
                "La respuesta pública contiene detalles internos");
    }

    private static void verifica(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
