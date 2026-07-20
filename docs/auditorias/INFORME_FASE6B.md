# Informe de Fase 6B - Correccion y pruebas de DAO

**FASE 6 CERRADA - FASE 7 NO INICIADA**

## Alcance y fuente de control

Se partio del commit 6A `97c3c26105372a8727522333d3c3fb382c07ae8c` en la
rama local `feature/fase-6b-correccion-dao`. Se aplicaron exclusivamente los
15 DAO, la jerarquia `Controlador.excepciones`, pruebas bajo `test/` y la
documentacion de esta fase. No se modificaron Models, SQL, migraciones,
Servlets, JSP, configuracion GlassFish, `CavaPool`, `jdbc/CavaDS` ni
credenciales.

Las fuentes de contrato fueron `INFORME_FASE6A.md`, `database/cava.sql`, los
15 Models y los documentos de arquitectura y estado indicados para la fase.
El esquema se mantuvo sin cambios.

## Correcciones aplicadas

- Los 15 DAO sustituyeron las 36 consultas `SELECT *` por proyecciones
  explicitas, en el orden y con los nombres verificados en 6A.
- Todos conservan `Conexion.getConn()`, `PreparedStatement` y cierre de
  recursos con `try-with-resources`.
- `DAOException` conserva categoria, operacion segura y causa interna;
  `DAOErrorType` clasifica `CONNECTION`, `DUPLICATE`, `FOREIGN_KEY`,
  `INVALID_DATA`, `NOT_FOUND`, `OPERATION_NOT_ALLOWED` y `SQL_ERROR`.
- `SQLExceptionTranslator` clasifica por SQLState y codigos MariaDB, sin
  depender del texto del error ni exponer SQL, datos o credenciales.
- No se anadio una libreria de logging. No quedan `System.out` ni
  `System.err` en los DAO; la causa queda disponible para una capa superior
  autorizada en la Fase 7.
- Los insert conservan claves generadas en el Model. Los valores monetarios se
  mantienen con `BigDecimal`; fechas y campos anulables se mapean sin
  convertir `NULL` a valores por defecto.

## Resultado por bloque

| Bloque | DAO | Resultado |
| --- | --- | --- |
| Catalogos | Roles, TipoDocumento, Ciudades, UnidadesMedida, CategoriaProductos, EstadoEnvio, MediosPagos, Transportadoras | CRUD, nulos y FK verificados. |
| Nucleo | Usuarios, Productos, Inventario | Baja logica, `BigDecimal`, FK, no encontrado y bloqueos verificados. |
| Historicos | PedidosCabeza, PedidosDetalle, Pagos, Envios | CRUD controlado, claves generadas, transaccion compartida y rollback verificados. |

## Eliminacion y transacciones

- Catalogos: borrado fisico solo sin referencias; una FK se informa como
  `FOREIGN_KEY`.
- Usuarios: `eliminar` es baja logica mediante `isActivo`.
- Productos e inventario: el borrado ordinario se bloquea con
  `OPERATION_NOT_ALLOWED` para no comprometer relaciones ni trazabilidad.
- Pedidos, detalles, pagos y envios: no admiten borrado funcional.
- Las inserciones coordinadas de pedido aceptan una `Connection` externa en
  `PedidosCabezaDAO`, `PedidosDetalleDAO`, `InventarioDAO`, `PagosDAO` y
  `EnviosDAO`. Esos metodos no cierran, no hacen commit/rollback y no cambian
  `autoCommit`; el propietario controla la transaccion.

## Pruebas y evidencia

`SQLExceptionTranslatorTest` cubre conexion simulada, duplicado, FK, datos
invalidos, SQL general, no encontrado y operacion no permitida.
`DAORollbackIntegrationTest` se ejecuto dentro de GlassFish mediante un WAR
temporal fuera del repositorio y `Conexion.getConn()` sobre `jdbc/CavaDS`.
Creo datos controlados con prefijo temporal, valido CRUD, consultas, claves,
`BigDecimal`, fechas, nulos, duplicado, FK, no encontrado y bloqueos. La
unidad pedido comparte una sola conexion, hace rollback y restaura
`autoCommit`. La prueba verifica explicitamente `COUNT(*) = 0` para cada una
de las 15 tablas antes y despues de ejecutarse.

Resultados confirmados el 20 de julio de 2026:

- Ant `clean compile compile-test`: `BUILD SUCCESSFUL` (36 fuentes de
  produccion y 3 pruebas).
- `SQL_EXCEPTION_TRANSLATOR_OK`, `BIGDECIMAL_MODELS_OK` y
  `DAO_INTEGRATION_OK` (HTTP 200).
- `CavaPool` respondio al ping y `jdbc/CavaDS` estuvo presente.
- Escaneo final: 0 `SELECT *`, 0 conversiones silenciosas de `SQLException`
  a `false`/`null`, 0 `System.out`/`System.err` DAO y sin secretos nuevos.

## Pendiente para Fase 7

La capa web debe decidir como presentar `DAOException`, registrar causas de
forma centralizada y coordinar unidades de pedido desde la capa autorizada.
No se iniciaron Servlets, login, frontend ni una capa Service.
