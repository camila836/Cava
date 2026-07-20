package Controlador.excepciones;

/** Categorias estables de fallos de persistencia para la capa DAO. */
public enum DAOErrorType {
    CONNECTION,
    DUPLICATE,
    FOREIGN_KEY,
    INVALID_DATA,
    NOT_FOUND,
    OPERATION_NOT_ALLOWED,
    SQL_ERROR
}
