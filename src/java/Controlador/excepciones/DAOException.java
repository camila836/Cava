package Controlador.excepciones;

/**
 * Excepcion de persistencia con un mensaje seguro para capas superiores.
 * La causa se conserva exclusivamente para diagnostico tecnico autorizado.
 */
public final class DAOException extends RuntimeException {

    private final DAOErrorType errorType;
    private final String operation;

    public DAOException(DAOErrorType errorType, String operation, String safeMessage,
            Throwable cause) {
        super(safeMessage, cause);
        this.errorType = errorType;
        this.operation = operation;
    }

    public DAOErrorType getErrorType() {
        return errorType;
    }

    public String getOperation() {
        return operation;
    }
}
