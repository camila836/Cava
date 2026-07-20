# Informe de Fase 6A - Auditoria y planificacion de DAO

**FASE 6A CERRADA - FASE 6B NO INICIADA**

## 1. Alcance, limite y precondiciones

Se realizo una auditoria estatica, sin cambios de Java, SQL, configuracion ni
servicios, sobre los 15 DAO y sus 15 Models. El objetivo fue preparar una
correccion unica y verificable para Fase 6B; esta fase no ejecuta CRUD,
compilacion, despliegue, ping JNDI, consultas a MariaDB ni pruebas de
integracion.

Precondiciones Git verificadas antes de crear la rama:

| Dato | Resultado |
| --- | --- |
| Raiz Git | `C:/Users/Maria Camila R/Documents/Cava/Cava` |
| Rama de partida | `main` |
| `HEAD` y `origin/main` | `c43e2171eeb94629c8503cf4bacb0f5b7d94a6a2` en ambos casos |
| Divergencia | `0/0` |
| Arbol inicial | limpio |
| Rama local de 6A | `feature/fase-6a-auditoria-dao` |
| Push / PR | no realizados |

Fuentes locales revisadas: `docs/05_ROADMAP.md`,
`docs/02_PROJECT_STATE.md`, `docs/03_ARCHITECTURE.md`,
`docs/06_AGENTS.md`, `database/cava.sql`, los 15 Models, los 15 DAO,
`test/Modelo/BigDecimalModelsTest.java`, `INFORME_FASE4B.md`,
`INFORME_FASE5.md` e `INFORME_RECUPERACION_DB_DEFINITIVA.md`.

Huella de las fuentes de control antes de esta documentacion:

| Archivo | SHA-256 |
| --- | --- |
| `database/cava.sql` | `A1DE14ED70524D55EECB9B64143C0C482034C65979DA9D0436B2B256E1ADCF2F` |
| `docs/05_ROADMAP.md` | `EB7F169151F6AE6A36D7699808E5D1AD24FA59A52A0B36918087EE619EE64BA5` |
| `docs/03_ARCHITECTURE.md` | `0E7DA14C50D0889C8C2DA5958CB0DFEFB18EAE57B54BC02D7FC5F0B0A431C545` |
| `docs/06_AGENTS.md` | `B8DE7C94BB1973927536F97DE6105C16938D373879850AC3046C218E20EB2EA1` |
| `INFORME_FASE4B.md` | `39DE67F5662F3A87488337F98EC594F35957CF22F5ECE407F870D3827B4C6D83` |
| `INFORME_FASE5.md` | `7EC8197BAABDB39156D2B9BDA400BCF40650A276607A8835782EE17FAFAE65C1` |
| `INFORME_RECUPERACION_DB_DEFINITIVA.md` | `2DA097887EA27D2B39E385F0D8F07CB6608DBC34EC194869FD998EAE230708FD` |

## 2. Metodo y criterio de evidencia

Se hizo una lectura estructurada unica de las clases para extraer firmas,
literales SQL, llamadas JDBC, columnas de mapeo y manejadores de errores. Cada
literal SQL se contrasto con `database/cava.sql`; cada columna mapeada se
contrasto con el Model correspondiente. Se verificaron 15 archivos DAO y 15
Models, sin ausencias ni ambiguedades de tabla.

Los terminos de esta auditoria son deliberadamente distintos:

- **Defecto demostrado:** el codigo actual prueba el hecho, sin necesitar una
  ejecucion (por ejemplo, reducir cualquier `SQLException` a `false` o `null`).
- **Riesgo potencial:** puede ocurrir al recibir datos o al ejecutar contra la
  base, pero no se afirma que ya ocurrio.
- **Mejora de mantenibilidad:** no es una incompatibilidad SQL demostrada.
- **Decision arquitectonica pendiente:** necesita autorizacion para definir la
  regla, no una correccion automatica.

No se detecto una discrepancia demostrada de tabla, columna, PK, FK, tipo
Java/SQL ni mapeo `ResultSet -> Model`. Las seis columnas `DECIMAL(10,2)` se
leen y escriben con `BigDecimal`, `setBigDecimal` y `getBigDecimal`, tal como
cerro Fase 5.

## 3. Matriz obligatoria de los 15 DAO

Leyenda: `SI` significa comprobado estaticamente; `P` significa pendiente de
prueba de integracion. Las siglas CRUD indican los metodos presentes. Las
severidades no convierten una preferencia en defecto: los `SELECT *` son mejora
de mantenibilidad y la politica de borrado/transaccion es una decision
arquitectonica pendiente.

| DAO | Model | Tabla | Metodos CRUD | SQL valido | SELECT * | PreparedStatement | try-with-resources | Mapeo correcto | Nulos/fechas/BigDecimal | Excepciones | Eliminacion | Transaccion | Severidad | Correccion propuesta |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| `RolesDAO` | `Roles` | `roles` | I/U/D/ID/L | SI | 2 | SI | SI | SI | String; sin fecha/decimal | 5 capturas -> `false`/`null` | fisica directa; FK de usuarios la bloquea | ninguna | Medio | proyeccion explicita; excepcion tipada; bloquear si hay dependencia |
| `TipoDocumentoDAO` | `TipoDocumento` | `tipoDocumento` | I/U/D/ID/L | SI | 2 | SI | SI | SI | String; sin fecha/decimal | 5 capturas -> `false`/`null` | fisica directa; FK de usuarios la bloquea | ninguna | Medio | mismo patron de catalogo |
| `CiudadesDAO` | `Ciudades` | `ciudades` | I/U/D/ID/L | SI | 2 | SI | SI | SI | `codigoPostal` nullable con String | 5 capturas -> `false`/`null` | fisica directa; FK de usuarios la bloquea | ninguna | Medio | mismo patron de catalogo |
| `UnidadesMedidaDAO` | `UnidadesMedida` | `unidadesMedida` | I/U/D/ID/L | SI | 2 | SI | SI | SI | String; sin fecha/decimal | 5 capturas -> `false`/`null` | fisica directa; FK de productos la bloquea | ninguna | Medio | mismo patron de catalogo |
| `CategoriaProductosDAO` | `CategoriaProductos` | `categoriaProductos` | I/U/D/ID/L | SI | 2 | SI | SI | SI | alias Java `descripcionCategoriaP` correcto | 5 capturas -> `false`/`null` | fisica directa; FK de productos la bloquea | ninguna | Medio | mismo patron de catalogo |
| `EstadoEnvioDAO` | `EstadoEnvio` | `estadoEnvio` | I/U/D/ID/L | SI | 2 | SI | SI | SI | String; sin fecha/decimal | 5 capturas -> `false`/`null` | fisica directa; FK de envios la bloquea | ninguna | Medio | mismo patron de catalogo |
| `MediosPagosDAO` | `MediosPagos` | `mediosPagos` | I/U/D/ID/L | SI | 2 | SI | SI | SI | String; sin fecha/decimal | 5 capturas -> `false`/`null` | fisica directa; FK de pagos la bloquea | ninguna | Medio | mismo patron de catalogo |
| `TransportadorasDAO` | `Transportadoras` | `transportadoras` | I/U/D/ID/L | SI | 2 | SI | SI | SI | `correo` y `telefono` nullable con String | 5 capturas -> `false`/`null` | fisica directa; FK de envios la bloquea | ninguna | Medio | mismo patron de catalogo |
| `UsuariosDAO` | `Usuarios` | `usuarios` | I/U/D/ID/L/buscar correo | SI | 3 | SI | SI | SI | dos `DATE` nullable; booleanos y FK correctos | 6 capturas -> `false`/`null` | debe pasar a baja logica `isActivo`; no `DELETE` ordinario | ninguna | Medio | proyeccion; excepcion tipada; separar baja logica |
| `ProductosDAO` | `Productos` | `productos` | I/U/D/ID/L | SI | 2 | SI | SI | SI | `precioProductos` BigDecimal correcto | 5 capturas -> `false`/`null` | bloquear si inventario o detalles existen | ninguna | Medio | proyeccion; excepcion tipada; no borrado ordinario |
| `InventarioDAO` | `Inventario` | `inventario` | I/U/D/ID/L/por producto | SI | 3 | SI | SI | SI | descripcion nullable; `stock` BigDecimal correcto | 6 capturas -> `false`/`null` | fisica solo para correccion controlada | ninguna | Medio | proyeccion; excepcion tipada; preparar sobrecarga con Connection |
| `PedidosCabezaDAO` | `PedidosCabeza` | `pedidosCabeza` | I/U/D/ID/L/por usuario | SI | 3 | SI | SI | SI | `fechaPedido` y `valorTotal` son NOT NULL; el DAO deja que SQL rechace null | 6 capturas -> `false`/`null` | no borrar: cabecera historica | requiere unidad pedido | Alto | proyeccion; excepcion tipada; insertar con Connection compartida |
| `PedidosDetalleDAO` | `PedidosDetalle` | `pedidosDetalle` | I/U/D/ID/L/por pedido | SI | 3 | SI | SI | SI | dos BigDecimal NOT NULL; SQL es quien rechaza null | 6 capturas -> `false`/`null` | no borrar ordinariamente: detalle historico | requiere unidad pedido | Alto | proyeccion; excepcion tipada; lote con Connection compartida |
| `PagosDAO` | `Pagos` | `pagos` | I/U/D/ID/L/por pedido | SI | 3 | SI | SI | SI | `fechaPagos`/`monto` NOT NULL; monto BigDecimal correcto | 6 capturas -> `false`/`null` | no borrar: trazabilidad y referencia unica | se coordina con pedido | Alto | proyeccion; excepcion tipada; Connection compartida si participa en alta de pedido |
| `EnviosDAO` | `Envios` | `envios` | I/U/D/ID/L/por pedido | SI | 3 | SI | SI | SI | `fechaEnvios` NOT NULL; el DAO deja que SQL rechace null | 6 capturas -> `false`/`null` | no borrar: guia y trazabilidad | se coordina con pedido | Alto | proyeccion; excepcion tipada; Connection compartida si participa en alta de pedido |

Todos los ID y FK son `int`/`INT NOT NULL` y todos los mapeos usan nombres de
columna, no posiciones. Los recursos JDBC se cierran por
`try-with-resources`: Connection y PreparedStatement en todos los metodos; los
ResultSet de consultas se cierran en su propio bloque o en la cabecera del
bloque de recursos.

## 4. Hallazgos consolidados

| Severidad | Cantidad | Clasificacion | Evidencia y alcance |
| --- | ---: | --- | --- |
| Critico | 0 | - | No hay incompatibilidad SQL/Model demostrada ni uso de credenciales/DriverManager. |
| Alto | 4 DAO | Decision arquitectonica pendiente con riesgo de trazabilidad | `PedidosCabeza`, `PedidosDetalle`, `Pagos` y `Envios` exponen `DELETE` fisico aunque la arquitectura exige conservar el historial. No se ejecutaron borrados. |
| Medio | 11 DAO | Mejora / riesgo controlado | Los catalogos, usuarios, productos e inventario requieren la misma normalizacion de proyecciones, excepciones y reglas de borrado. |
| Transversal medio | 15 DAO | Defecto demostrado | 81 bloques `catch (SQLException)` imprimen el mensaje y devuelven `false` o `null`; no permiten distinguir sin cambios, inexistencia, duplicado, FK, conexion o SQL general. |
| Transversal bajo | 36 consultas | Mejora de mantenibilidad | Las 36 consultas `SELECT *` son reales y estan aplazadas desde Fase 4B. No causan hoy un mapeo incorrecto porque este usa etiquetas, pero acoplan la lectura al orden/extension de tabla. |
| Potencial medio | 4 DAO | Riesgo potencial | `PedidosCabeza`, `PedidosDetalle`, `Pagos` y `Envios` delegan a MariaDB el rechazo de campos NOT NULL nulos. Esto no prueba que se haya enviado un null, pero la API actual lo reduce a `false`. |

No existen literales SQL exactamente duplicados entre los DAO. La repeticion
observada es el patron CRUD por entidad; debe conservarse simple y no justifica
una abstraccion generica. Si se introducen constantes en 6B, deben ser privadas
por DAO y solo para las proyecciones que ese DAO reutiliza.

## 5. Sustitucion planificada de las 36 consultas `SELECT *`

Cada entrada siguiente identifica todas las apariciones del DAO entre
parentesis y una unica proyeccion explicita, en el mismo conjunto que consume
su `mapear(ResultSet)`. Los mapeos son por etiqueta, por lo que no dependen de
la posicion; se recomienda mantener el orden de columnas indicado para facilitar
lectura y revision.

| DAO y apariciones | Proyeccion propuesta |
| --- | --- |
| `RolesDAO` (`consultarPorId`, `listarTodos`) | `idRoles, descripcionRol` |
| `TipoDocumentoDAO` (`consultarPorId`, `listarTodos`) | `idTipoDocumento, descripcion` |
| `CiudadesDAO` (`consultarPorId`, `listarTodos`) | `idCiudades, codigoCiudad, nombreCiudad, codigoPostal` |
| `UnidadesMedidaDAO` (`consultarPorId`, `listarTodos`) | `idUnidadesMedida, descripcionUnidadesMed` |
| `CategoriaProductosDAO` (`consultarPorId`, `listarTodos`) | `idCategoriaProductos, descripcionCategoriaProductos` |
| `EstadoEnvioDAO` (`consultarPorId`, `listarTodos`) | `idEstadoEnvio, descripcionEstadoEnvio` |
| `MediosPagosDAO` (`consultarPorId`, `listarTodos`) | `idMediosPagos, descripcionMediosPagos` |
| `TransportadorasDAO` (`consultarPorId`, `listarTodos`) | `idTransportadoras, nombreTransportadoras, nit, correo, telefono` |
| `UsuariosDAO` (`consultarPorId`, `listarTodos`, `buscarPorCorreo`) | `idUsuarios, nombres, apellidos, identificacion, correo, direccion, telefono, clave, isActivo, fechaNacimiento, fechaVencimientoClave, autorizacionTratamientoDatos, idRoles, idTipoDocumento, idCiudades` |
| `ProductosDAO` (`consultarPorId`, `listarTodos`) | `idProductos, descripcionProductos, precioProductos, idUnidadesMedida, idCategoriaProductos` |
| `InventarioDAO` (`consultarPorId`, `listarTodos`, `consultarPorProducto`) | `idInventario, descripcionInventario, stock, idProductos` |
| `PedidosCabezaDAO` (`consultarPorId`, `listarTodos`, `listarPorUsuario`) | `idPedidosCabeza, numeroPedido, fechaPedido, descripcionPedido, valorTotal, idUsuarios` |
| `PedidosDetalleDAO` (`consultarPorId`, `listarTodos`, `listarPorPedido`) | `idPedidosDetalle, cantidadUnitaria, subtotalPed, idPedidosCabeza, idProductos` |
| `PagosDAO` (`consultarPorId`, `listarTodos`, `listarPorPedido`) | `idPagos, fechaPagos, descripcionPagos, monto, referenciaPago, comprobantePago, idMediosPagos, idPedidosCabeza` |
| `EnviosDAO` (`consultarPorId`, `listarTodos`, `listarPorPedido`) | `idEnvios, fechaEnvios, descripcionEnvios, numeroGuia, idPedidosCabeza, idEstadoEnvio, idTransportadoras` |

El total es `2 x 9 DAO + 3 x 6 DAO = 36`; ninguna proyeccion incorpora
columnas que no esten en el esquema canonico o en su Model.

## 6. Decisiones preparadas para autorizacion de 6B

### 6.1 Excepciones DAO

Se recomienda crear una unica excepcion de persistencia, privada de la capa
web, con una categoria estable y la causa `SQLException` preservada para log
tecnico. Los DAO deben traducir por `SQLState`/codigo de proveedor sin publicar
SQL ni detalles JDBC al usuario final:

| Categoria | Regla de traduccion propuesta | Mensaje seguro de capa web |
| --- | --- | --- |
| `CONNECTION` | clase/SQLState de conexion o timeout | No fue posible completar la operacion. Intente de nuevo. |
| `UNIQUE_VIOLATION` | SQLState de integridad para UNIQUE | Ya existe un registro con ese dato. |
| `FOREIGN_KEY_VIOLATION` | SQLState de integridad para FK | La operacion no es valida por dependencias existentes. |
| `INVALID_DATA` | conversion, nulabilidad o rango de datos | Revise los datos ingresados. |
| `NOT_FOUND` | `executeUpdate() == 0` o consulta sin fila, no un SQLException | El registro solicitado no existe. |
| `GENERAL_SQL` | resto de `SQLException` | No fue posible completar la operacion. |

Esto es un cambio transversal autorizado solo para DAO en 6B. El logging debe
registrar operacion, DAO, categoria y SQLState, nunca claves, hashes,
credenciales, tokens, cookies ni datos financieros sensibles. `System.err` no
debe seguir siendo el canal de manejo final.

### 6.2 Politica de eliminacion con el esquema actual

No se proponen columnas ni cambios SQL. La unica baja logica soportada por el
esquema actual es `usuarios.isActivo`.

| Entidad | Politica recomendada | Justificacion verificable |
| --- | --- | --- |
| Roles, TipoDocumento, Ciudades, UnidadesMedida, CategoriaProductos, EstadoEnvio, MediosPagos, Transportadoras | Fisica solo si no hay dependencias; bloquear si existen | Las FK `RESTRICT` existentes impiden referencias huerfanas. |
| Usuarios | Logica mediante `isActivo`; no `DELETE` ordinario | La columna existente permite conservar identidad y pedidos. |
| Productos | Bloquear borrado ordinario | Inventario y detalle de pedidos lo referencian; no hay soporte de baja logica. |
| Inventario | Fisica solo para correccion controlada y autorizada; operacion normal es actualizar stock | No tiene dependientes directos, pero forma parte de la trazabilidad operativa. |
| PedidosCabeza, PedidosDetalle, Pagos, Envios | Bloquear borrado funcional | Son datos historicos; pago y envio contienen referencias/guia unicas y la arquitectura exige trazabilidad. |

La decision pendiente es definir el flujo funcional de anulacion/cancelacion de
pedidos, pagos y envios, porque el esquema actual no tiene estado de pedido ni
columnas de baja para esas entidades. No debe simularse con `DELETE`.

### 6.3 Transacciones y Connection compartida

Los CRUD de catalogos y lecturas individuales pueden seguir abriendo y cerrando
su propia Connection. La unidad de trabajo de pedido no puede hacerlo: debe
adquirir una sola Connection, desactivar autocommit, invocar sobrecargas DAO que
la reciban, hacer `commit` una sola vez y ejecutar `rollback` ante cualquier
fallo; finalmente debe restaurar autocommit y cerrar la conexion.

| Caso | DAO que comparten Connection | Limite correcto |
| --- | --- | --- |
| Alta de pedido | `PedidosCabezaDAO`, `PedidosDetalleDAO`, `InventarioDAO` | cabecera + detalles + descuento de stock son una unica transaccion |
| Alta de pedido con pago | los anteriores + `PagosDAO` | incluir pago solo cuando la regla de negocio exija confirmarlo en la misma unidad |
| Alta de pedido con envio | los anteriores + `EnviosDAO` | incluir envio solo si se crea al mismo tiempo; de otro modo es transaccion propia de despacho |
| Operacion individual | un solo DAO | la Connection local del metodo sigue siendo correcta |

No se debe mover esta coordinacion a Servlet ni crear una capa nueva en 6B sin
autorizacion adicional. Una sobrecarga DAO que acepte `Connection` es el minimo
necesario; los metodos actuales pueden conservarse para operaciones aisladas.

## 7. Plan ejecutable de Fase 6B (no iniciado)

1. Crear un checkpoint local y trabajar solo en la rama de 6A o una rama de
   implementacion autorizada. Corregir primero el patron transversal de
   excepciones, proyecciones y mapeo de nulos, sin cambiar firmas publicas salvo
   las sobrecargas `Connection` justificadas.
2. Aplicar el patron comun a los ocho catalogos: `RolesDAO`,
   `TipoDocumentoDAO`, `CiudadesDAO`, `UnidadesMedidaDAO`,
   `CategoriaProductosDAO`, `EstadoEnvioDAO`, `MediosPagosDAO` y
   `TransportadorasDAO`.
3. Aplicarlo a `UsuariosDAO`, con baja logica usando solamente `isActivo`, y a
   `ProductosDAO` e `InventarioDAO`, preservando `BigDecimal` y su FK.
4. Corregir finalmente el grupo de pedido: `PedidosCabezaDAO`,
   `PedidosDetalleDAO`, `PagosDAO` y `EnviosDAO`; introducir sobrecargas con
   `Connection` solo para las unidades de trabajo aprobadas y eliminar el
   `DELETE` funcional de entidades historicas.
5. Archivos que se modificarian si se autoriza: los 15 archivos bajo
   `src/java/Controlador/`; pruebas DAO nuevas o ampliadas bajo `test/`; y
   `docs/auditorias/INFORME_FASE6B.md` junto con `docs/02_PROJECT_STATE.md`.
   No se modificarian Models, `database/cava.sql`, migraciones, configuracion
   GlassFish, JSP, Servlets, JavaScript ni CSS sin autorizacion independiente.
6. Pruebas necesarias: mapeo de cada proyeccion; `NOT_FOUND` por ID inexistente;
   UNIQUE de `usuarios.identificacion`, `usuarios.correo`, `numeroPedido`,
   `referenciaPago` y `numeroGuia`; FK RESTRICT; nulos/longitudes; limites y
   escala de los seis `DECIMAL(10,2)`; y rollback de cabecera/detalle/inventario
   (mas pago o envio cuando pertenezcan a la misma unidad).
7. Datos temporales controlados: crear por la cuenta restringida un juego con
   un usuario, catalogos, producto, inventario, pedido, detalle, pago y envio;
   usar identificadores prefijados de prueba; ejecutar cada caso en transaccion
   y `ROLLBACK`, o eliminar en orden inverso solo los registros temporales
   conocidos. Nunca usar ni modificar datos reales sin autorizacion.
8. Validaciones: `ant clean`, `ant compile` y `ant dist`; despues, con
   autorizacion, pruebas de integracion mediante `Conexion.getConn()`,
   `CavaPool` y `jdbc/CavaDS`, sin crear una segunda fuente de conexion.
9. Rollback: un commit por grupo coherente; si falla una prueba, revertir el
   commit local del grupo y no ejecutar cambios de esquema. Las pruebas de
   datos se revierten antes de cerrar la sesion. No hay migracion que revertir.
10. Criterios de aceptacion: 0 `SELECT *` en los 15 DAO; proyecciones y tipos
    validados; recursos cerrados; errores diferenciados; politica de borrado
    aplicada; transaccion pedido demostrada con rollback; Ant exitoso; prueba
    real por JNDI documentada y sin secretos.
11. Autorizaciones necesarias antes de implementar: adoptar la excepcion DAO y
    el mecanismo de logging; activar baja logica de usuarios; retirar/bloquear
    `DELETE` funcional historico; introducir sobrecargas con `Connection`; usar
    datos temporales y ejecutar pruebas contra MariaDB/CavaPool.

## 8. Cierre de 6A

- DAO auditados: 15 de 15.
- `SELECT *` confirmado: 36 de 36; no hay SQL literal exactamente duplicado.
- Codigo, Models, base, migraciones, servicios MariaDB/GlassFish, XAMPP y
  configuracion: no modificados ni utilizados.
- Compilacion, despliegue y CRUD real: no repetidos por ser ajenos al alcance
  estatico de 6A.
- Archivos autorizados modificados: este informe y `docs/02_PROJECT_STATE.md`.
- No se crearon auxiliares temporales en el repositorio.

Fase 6B es segura de preparar, pero no de iniciar automaticamente: requiere la
autorizacion explicita de las decisiones enumeradas en la seccion 7.11.
