package Controlador;

import Conexion.Conexion;
import Controlador.excepciones.DAOErrorType;
import Controlador.excepciones.DAOException;
import Modelo.UnidadesMedida;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/** Integración real con rollback; no deja DML persistente. */
public final class UnidadesMedidaDAOIntegrationTest {

    private UnidadesMedidaDAOIntegrationTest() {
    }

    public static Result run() throws Exception {
        String tag = "F9_" + Long.toUnsignedString(System.nanoTime(), 36);
        int firstId;
        int secondId;
        int categoryId;
        int productId;
        try (Connection connection = Conexion.getConn()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                UnidadesMedidaDAO dao = new UnidadesMedidaDAO();
                String firstDescription = tag + " Alpha ñ <>&'";
                UnidadesMedida first = unit(firstDescription);
                UnidadesMedida second = unit(tag + " Beta");
                dao.insertar(connection, first);
                dao.insertar(connection, second);
                firstId = first.getIdUnidadesMedida();
                secondId = second.getIdUnidadesMedida();
                verify(firstId > 0 && secondId > 0, "claves generadas");
                verify(dao.consultarPorId(connection, firstId) != null, "consulta existente");
                verify(dao.consultarPorId(connection, Integer.MAX_VALUE) == null,
                        "consulta inexistente");
                verify(dao.listarTodos(connection).stream()
                        .anyMatch(row -> row.getIdUnidadesMedida() == firstId), "listado");

                expect(DAOErrorType.DUPLICATE,
                        () -> dao.insertar(connection, unit(firstDescription.toLowerCase())));
                second.setDescripcionUnidadesM(firstDescription.toLowerCase());
                expect(DAOErrorType.DUPLICATE, () -> dao.actualizar(connection, second));

                first.setDescripcionUnidadesM(tag + " editada");
                dao.actualizar(connection, first);
                verify(dao.consultarPorId(connection, firstId).getDescripcionUnidadesM()
                        .endsWith("editada"), "actualización");
                expect(DAOErrorType.NOT_FOUND,
                        () -> dao.actualizar(connection, missing(tag)));

                int[] reference = createReference(connection, firstId, tag);
                categoryId = reference[0];
                productId = reference[1];
                expect(DAOErrorType.FOREIGN_KEY, () -> dao.eliminar(connection, firstId));
                deleteReference(connection, productId, categoryId);

                dao.eliminar(connection, secondId);
                expect(DAOErrorType.NOT_FOUND, () -> dao.eliminar(connection, secondId));
                verify(connection.isValid(2), "conexión utilizable tras operaciones");
                connection.rollback();
            } finally {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
                connection.setAutoCommit(autoCommit);
            }
        }
        verifyClean(tag);
        return new Result(tag, List.of(firstId, secondId), categoryId, productId);
    }

    private static int[] createReference(Connection connection, int unitId, String tag)
            throws Exception {
        int categoryId;
        int productId;
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO categoriaProductos (descripcionCategoriaProductos) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, tag);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                verify(keys.next(), "categoría");
                categoryId = keys.getInt(1);
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO productos (descripcionProductos,precioProductos,idUnidadesMedida,idCategoriaProductos) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, tag);
            statement.setBigDecimal(2, new BigDecimal("1.00"));
            statement.setInt(3, unitId);
            statement.setInt(4, categoryId);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                verify(keys.next(), "producto");
                productId = keys.getInt(1);
            }
        }
        return new int[]{categoryId, productId};
    }

    private static void deleteReference(Connection connection, int productId, int categoryId)
            throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM productos WHERE idProductos=?")) {
            statement.setInt(1, productId);
            verify(statement.executeUpdate() == 1, "producto temporal eliminado");
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM categoriaProductos WHERE idCategoriaProductos=?")) {
            statement.setInt(1, categoryId);
            verify(statement.executeUpdate() == 1, "categoría temporal eliminada");
        }
    }

    private static void verifyClean(String tag) throws Exception {
        try (Connection connection = Conexion.getConn()) {
            verify(count(connection,
                    "SELECT COUNT(*) FROM unidadesMedida WHERE descripcionUnidadesMed LIKE ?", tag)
                    == 0, "rollback unidades");
            verify(count(connection,
                    "SELECT COUNT(*) FROM productos WHERE descripcionProductos LIKE ?", tag)
                    == 0, "rollback productos");
            verify(count(connection,
                    "SELECT COUNT(*) FROM categoriaProductos WHERE descripcionCategoriaProductos LIKE ?",
                    tag) == 0, "rollback categorías");
        }
    }

    private static int count(Connection connection, String sql, String tag) throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tag + "%");
            try (ResultSet result = statement.executeQuery()) {
                verify(result.next(), "conteo final");
                return result.getInt(1);
            }
        }
    }

    private static UnidadesMedida unit(String description) {
        UnidadesMedida unit = new UnidadesMedida();
        unit.setDescripcionUnidadesM(description);
        return unit;
    }

    private static UnidadesMedida missing(String tag) {
        UnidadesMedida unit = unit(tag);
        unit.setIdUnidadesMedida(Integer.MAX_VALUE);
        return unit;
    }

    private static void expect(DAOErrorType expected, Action action) {
        try {
            action.run();
            throw new AssertionError("faltó " + expected);
        } catch (DAOException error) {
            verify(error.getErrorType() == expected, "error " + expected);
        }
    }

    private static void verify(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static final class Result {
        public final String tag;
        public final List<Integer> unitIds;
        public final int categoryId;
        public final int productId;

        private Result(String tag, List<Integer> unitIds, int categoryId, int productId) {
            this.tag = tag;
            this.unitIds = unitIds;
            this.categoryId = categoryId;
            this.productId = productId;
        }
    }

    @FunctionalInterface
    private interface Action {
        void run();
    }
}
