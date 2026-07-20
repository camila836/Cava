package Controlador.excepciones;

import java.sql.SQLException;

/** Traduce SQLState y codigos de MariaDB sin exponer SQL ni datos sensibles. */
public final class SQLExceptionTranslator {

    private SQLExceptionTranslator() {
    }

    public static DAOException translate(String operation, SQLException cause) {
        DAOErrorType type = classify(cause);
        return new DAOException(type, operation, safeMessage(type), cause);
    }

    public static DAOException notFound(String operation) {
        return new DAOException(DAOErrorType.NOT_FOUND, operation,
                safeMessage(DAOErrorType.NOT_FOUND), null);
    }

    public static DAOException operationNotAllowed(String operation) {
        return new DAOException(DAOErrorType.OPERATION_NOT_ALLOWED, operation,
                safeMessage(DAOErrorType.OPERATION_NOT_ALLOWED), null);
    }

    public static void requireAffected(int rows, String operation) {
        if (rows != 1) {
            throw notFound(operation);
        }
    }

    public static DAOErrorType classify(SQLException exception) {
        SQLException current = exception;
        while (current != null) {
            String state = current.getSQLState();
            int code = current.getErrorCode();
            if (state != null && state.startsWith("08")) {
                return DAOErrorType.CONNECTION;
            }
            if (code == 1062 || "23505".equals(state)) {
                return DAOErrorType.DUPLICATE;
            }
            if (code == 1451 || code == 1452 || code == 1216 || code == 1217) {
                return DAOErrorType.FOREIGN_KEY;
            }
            if (state != null && (state.startsWith("22") || "23000".equals(state))) {
                return DAOErrorType.INVALID_DATA;
            }
            current = current.getNextException();
        }
        return DAOErrorType.SQL_ERROR;
    }

    private static String safeMessage(DAOErrorType type) {
        switch (type) {
            case CONNECTION:
                return "No fue posible completar la operacion de persistencia.";
            case DUPLICATE:
                return "Ya existe un registro con ese dato.";
            case FOREIGN_KEY:
                return "La operacion no es valida por dependencias existentes.";
            case INVALID_DATA:
                return "Los datos no son validos para la operacion solicitada.";
            case NOT_FOUND:
                return "El registro solicitado no existe.";
            case OPERATION_NOT_ALLOWED:
                return "La operacion no esta permitida para este registro.";
            default:
                return "No fue posible completar la operacion de persistencia.";
        }
    }
}
