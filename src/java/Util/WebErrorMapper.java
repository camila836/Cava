package Util;

import Controlador.excepciones.DAOErrorType;
import Controlador.excepciones.DAOException;

/** Traduce fallos técnicos a estados y mensajes públicos estables. */
public final class WebErrorMapper {

    private WebErrorMapper() {
    }

    public static WebErrorDescriptor describe(Throwable error) {
        DAOException daoError = findDaoError(error);
        int status = daoError == null ? 500 : statusFor(daoError.getErrorType());
        return descriptorFor(status);
    }

    public static int statusFor(DAOErrorType type) {
        if (type == null) {
            return 500;
        }
        switch (type) {
            case INVALID_DATA:
                return 400;
            case NOT_FOUND:
                return 404;
            case DUPLICATE:
            case FOREIGN_KEY:
            case OPERATION_NOT_ALLOWED:
                return 409;
            case CONNECTION:
                return 503;
            case SQL_ERROR:
            default:
                return 500;
        }
    }

    public static WebErrorDescriptor descriptorFor(int status) {
        switch (status) {
            case 403:
                return new WebErrorDescriptor(403, "Acceso no autorizado",
                        "No tienes permiso para acceder a este recurso.");
            case 400:
                return new WebErrorDescriptor(400, "Solicitud no válida",
                        "Revisa los datos enviados e inténtalo nuevamente.");
            case 404:
                return new WebErrorDescriptor(404, "Página no encontrada",
                        "No encontramos el recurso solicitado.");
            case 405:
                return new WebErrorDescriptor(405, "Método no permitido",
                        "La operación solicitada no está disponible.");
            case 409:
                return new WebErrorDescriptor(409, "No fue posible completar la operación",
                        "El estado actual de los datos no permite realizarla.");
            case 503:
                return new WebErrorDescriptor(503, "Servicio temporalmente no disponible",
                        "Inténtalo nuevamente dentro de unos minutos.");
            default:
                return new WebErrorDescriptor(500, "Ocurrió un error",
                        "No pudimos completar tu solicitud en este momento.");
        }
    }

    private static DAOException findDaoError(Throwable error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof DAOException) {
                return (DAOException) current;
            }
            Throwable cause = current.getCause();
            current = cause == current ? null : cause;
        }
        return null;
    }
}
