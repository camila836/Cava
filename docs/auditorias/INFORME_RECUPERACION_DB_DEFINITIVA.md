# Informe de recuperación definitiva de base de datos CAVA

## Estado

**RECUPERACIÓN COMPLETADA — CADENA JDBC/JNDI VALIDADA**

Ejecución: 19–20 de julio de 2026.

Rama local: `fix/recuperacion-db-definitiva`.

## 1. Causa y diagnóstico real

El estado supuesto indicaba que la base `cava` había sido eliminada. La
comprobación en vivo previa a la reconstrucción encontró, en cambio, una base
existente con 15 tablas y 0 filas. Su estructura coincidía con el contrato
aprobado de las Fases 4 y 5.

Los problemas confirmados fueron:

1. `database/cava.sql` seguía documentado como instantánea de compatibilidad,
   no como fuente canónica definitiva.
2. El SQL dependía de defaults del servidor para motor, charset, collation y
   reglas FK, aunque el estado vivo era correcto.
3. `/Cava/` devolvía HTTP 404 por falta de `welcome-file`; las JSP concretas sí
   respondían HTTP 200.

Como `cava` estaba vacía, se creó un respaldo recuperable y se reconstruyó
exclusivamente esa base con el script canónico corregido. Ninguna otra base fue
creada, eliminada o modificada.

## 2. Seguridad y respaldo

Respaldo externo protegido:

`C:\Users\Maria Camila R\CAVA_Backups\Recovery\recuperacion_db_definitiva_20260719-234534`

Contiene 54 entradas registradas en `SHA256SUMS.txt`, incluidas las fuentes SQL,
Models, DAO, conexión, configuración web, NetBeans, GlassFish, `my.ini`,
phpMyAdmin y el esquema vivo anterior a la reconstrucción.

| Evidencia | Resultado |
|---|---|
| `cava_live_before_rebuild.sql` | 7.675 bytes; 15 `CREATE TABLE` |
| SHA-256 del respaldo vivo | `5B574C6043C12B8AC6E1A1742F57E3328A26A479151946C6FBED29334236C8AC` |
| SHA-256 del manifiesto final | `1A0564FFAD5053F0F5F81F2AC34E8F1DD624D3CBA5579482231311909D651876` |
| ACL | Herencia protegida; cuenta actual y Administradores |

No se incluyeron contraseñas ni hashes de autenticación. Los hashes anteriores
son únicamente controles de integridad de archivos.

## 3. Fuente canónica e instalador

- `database/cava.sql` es la fuente canónica autocontenida.
- `database/00_create_database.sql` a `03_seed_catalogs.sql` forman el
  instalador modular equivalente.
- Los 15 bloques `CREATE TABLE` de `cava.sql` y `01_schema.sql` son
  estructuralmente equivalentes.
- `02_indexes.sql` ejecuta 0 sentencias porque PK, UNIQUE e índices FK ya
  cubren el contrato actual.
- `03_seed_catalogs.sql` ejecuta 0 sentencias porque no existen valores de
  catálogo autorizados.
- V001–V004 permanecen aplazadas y no fueron ejecutadas.

Cada tabla declara explícitamente:

```sql
ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
```

Las 14 FK declaran `ON UPDATE RESTRICT ON DELETE RESTRICT`.

## 4. Mapa Models → tablas

| Model | Tabla lógica | PK | Columnas propias | FK y relación |
|---|---|---|---|---|
| `CategoriaProductos` | `categoriaProductos` | `idCategoriaProductos` | `descripcionCategoriaProductos` | — |
| `Ciudades` | `ciudades` | `idCiudades` | `codigoCiudad`, `nombreCiudad`, `codigoPostal` | — |
| `TipoDocumento` | `tipoDocumento` | `idTipoDocumento` | `descripcion` | — |
| `Roles` | `roles` | `idRoles` | `descripcionRol` | — |
| `UnidadesMedida` | `unidadesMedida` | `idUnidadesMedida` | `descripcionUnidadesMed` | — |
| `Transportadoras` | `transportadoras` | `idTransportadoras` | `nombreTransportadoras`, `nit`, `correo`, `telefono` | — |
| `EstadoEnvio` | `estadoEnvio` | `idEstadoEnvio` | `descripcionEstadoEnvio` | — |
| `MediosPagos` | `mediosPagos` | `idMediosPagos` | `descripcionMediosPagos` | — |
| `Usuarios` | `usuarios` | `idUsuarios` | identidad, contacto, seguridad, fechas y consentimiento | `roles`, `tipoDocumento`, `ciudades` |
| `Productos` | `productos` | `idProductos` | descripción y precio | `unidadesMedida`, `categoriaProductos` |
| `Inventario` | `inventario` | `idInventario` | descripción y stock | `productos` |
| `PedidosCabeza` | `pedidosCabeza` | `idPedidosCabeza` | número, fecha, descripción y total | `usuarios` |
| `PedidosDetalle` | `pedidosDetalle` | `idPedidosDetalle` | cantidad y subtotal | `pedidosCabeza`, `productos` |
| `Pagos` | `pagos` | `idPagos` | fecha, descripción, monto, referencia y comprobante | `mediosPagos`, `pedidosCabeza` |
| `Envios` | `envios` | `idEnvios` | fecha, descripción y guía | `pedidosCabeza`, `estadoEnvio`, `transportadoras` |

Los aliases históricos de Java permanecen correctamente mapeados por DAO:

- `CategoriaProductos.descripcionCategoriaP` →
  `descripcionCategoriaProductos`.
- `UnidadesMedida.descripcionUnidadesM` → `descripcionUnidadesMed`.

## 5. Claves y relaciones

Todas las PK son `INT AUTO_INCREMENT`:

`idCategoriaProductos`, `idCiudades`, `idTipoDocumento`, `idRoles`,
`idUnidadesMedida`, `idTransportadoras`, `idEstadoEnvio`, `idMediosPagos`,
`idUsuarios`, `idProductos`, `idInventario`, `idPedidosCabeza`,
`idPedidosDetalle`, `idPagos` e `idEnvios`.

FK definitivas:

1. `usuarios.idRoles` → `roles.idRoles`.
2. `usuarios.idTipoDocumento` → `tipoDocumento.idTipoDocumento`.
3. `usuarios.idCiudades` → `ciudades.idCiudades`.
4. `productos.idUnidadesMedida` → `unidadesMedida.idUnidadesMedida`.
5. `productos.idCategoriaProductos` →
   `categoriaProductos.idCategoriaProductos`.
6. `inventario.idProductos` → `productos.idProductos`.
7. `pedidosCabeza.idUsuarios` → `usuarios.idUsuarios`.
8. `pedidosDetalle.idPedidosCabeza` →
   `pedidosCabeza.idPedidosCabeza`.
9. `pedidosDetalle.idProductos` → `productos.idProductos`.
10. `pagos.idMediosPagos` → `mediosPagos.idMediosPagos`.
11. `pagos.idPedidosCabeza` → `pedidosCabeza.idPedidosCabeza`.
12. `envios.idPedidosCabeza` → `pedidosCabeza.idPedidosCabeza`.
13. `envios.idEstadoEnvio` → `estadoEnvio.idEstadoEnvio`.
14. `envios.idTransportadoras` →
    `transportadoras.idTransportadoras`.

Restricciones UNIQUE: `usuarios.identificacion`, `usuarios.correo`,
`pedidosCabeza.numeroPedido`, `pagos.referenciaPago` y `envios.numeroGuia`.

## 6. BigDecimal y tipos SQL

Los seis campos exactos conservan `DECIMAL(10,2)` y se leen/escriben mediante
`BigDecimal`, `setBigDecimal` y `getBigDecimal`:

| Tabla.columna | Model |
|---|---|
| `productos.precioProductos` | `Productos` |
| `inventario.stock` | `Inventario` |
| `pedidosCabeza.valorTotal` | `PedidosCabeza` |
| `pedidosDetalle.cantidadUnitaria` | `PedidosDetalle` |
| `pedidosDetalle.subtotalPed` | `PedidosDetalle` |
| `pagos.monto` | `Pagos` |

No existen `FLOAT`, `DOUBLE`, `double`, `getDouble` ni `setDouble` para estos
valores.

## 7. Convención camelCase

Los nombres lógicos de tablas, columnas, FK y restricciones propias usan
camelCase en SQL, Models y DAO. MariaDB para Windows opera con
`lower_case_table_names=1`, por lo que `information_schema` normaliza los
nombres físicos de tabla a minúsculas; no se modificó esa directiva porque el
contrato lógico y las consultas camelCase funcionan correctamente.

## 8. Reconstrucción y pruebas de base

Antes del cambio:

- 15 tablas;
- 71 columnas;
- 15 PK;
- 14 FK;
- 5 UNIQUE;
- 34 entradas de índice;
- 15 tablas InnoDB con `utf8mb4_unicode_ci`;
- 6 columnas `DECIMAL(10,2)`;
- 0 filas exactas.

Se ejecutaron 17 sentencias del canónico: creación/selección de `cava` y 15
`CREATE TABLE`. La firma estructural posterior coincidió con la previa.

Prueba con `cava_app`, dentro de una transacción con `ROLLBACK`:

1. `INSERT` en `roles`.
2. `SELECT` del registro generado.
3. `UPDATE` del registro.
4. `DELETE` del registro.
5. `ROLLBACK`.

Resultado final: 0 filas; no quedaron datos de prueba.

## 9. Seguridad de cuentas

- Única cuenta administrativa local: `root@localhost`.
- Plugin conservado: `mysql_native_password`.
- Acceso root sin contraseña rechazado con error 1045 mediante `localhost`,
  `127.0.0.1` y `::1`.
- `cava_app@localhost` autentica correctamente.
- Privilegios finales de `cava_app` sobre `cava`: `SELECT`, `INSERT`, `UPDATE`
  y `DELETE`.
- No tiene privilegios globales ni `GRANT OPTION`.

## 10. JDBC, JNDI y aplicación

| Componente | Resultado |
|---|---|
| Connector/J | 8.0.12 cargable desde `domain1/lib`; no se duplica en el WAR |
| SHA-256 del JAR | `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2` |
| Driver/DataSource | `com.mysql.cj.jdbc.MysqlDataSource` |
| `CavaPool` | Ping correcto antes y después de reinicios |
| `jdbc/CavaDS` | Recurso único, asociado a `CavaPool` |
| `Conexion.getConn()` | Conexión real correcta dentro de GlassFish |
| DAO real | `ProductosDAO.listarTodos()` devolvió 0 productos |
| Identidad SQL desde la aplicación | `cava_app@localhost` |

El despliegue empaquetado actualizó únicamente la entrada de la aplicación
`Cava` en `domain.xml`: sustituyó la referencia anterior al directorio
`build/web` por la ubicación administrada dentro de `domain1/applications`.
`CavaPool`, `jdbc/CavaDS`, el alias y sus propiedades no cambiaron.

La comprobación en aplicación se hizo con una JSP temporal sin credenciales,
antes y después del reinicio. Fue eliminada y no aparece en el WAR final.

## 11. Compilación, despliegue y HTTP

- Ant de NetBeans 20: `clean compile compile-test dist`.
- Resultado: `BUILD SUCCESSFUL`.
- Fuentes: 33 de producción y 1 de prueba.
- Prueba: `BIGDECIMAL_MODELS_OK`.
- WAR final: 6.329.707 bytes.
- SHA-256 del WAR:
  `1B24119708DCBF259F0271FF92643218AECEFD087F806D9DAC76CDA4B157F4C2`.
- Despliegue: aplicación `Cava` activa.

URLs finales:

| URL | HTTP |
|---|---:|
| `http://localhost:8080/Cava/` | 200 |
| `http://localhost:8080/Cava/Index.jsp` | 200 |
| `http://localhost:8080/Cava/InicioSesion.jsp` | 200 |
| `http://localhost:8080/Cava/RegistrarUsuario.jsp` | 200 |
| `http://localhost:8080/Cava/Admin.jsp` | 200 |

`web/WEB-INF/web.xml` define `Index.jsp` como `welcome-file`, corrigiendo el
404 del contexto raíz.

## 12. Reinicios y runtime final

MariaDB fue reiniciado únicamente mediante los mecanismos existentes de
XAMPP. Después del reinicio:

- una instancia `C:\xampp8\mysql\bin\mysqld.exe`;
- PID observado: `6212`;
- un listener en el puerto 3306;
- nueva entrada `ready for connections`;
- `cava_app`, 15 tablas y 0 filas confirmados.

GlassFish fue detenido e iniciado mediante `asadmin`:

- versión efectiva: Eclipse GlassFish 7.0.9;
- dominio `domain1` activo;
- listeners 4848, 8080 y 8181 activos;
- `CavaPool`, JNDI, despliegue y consulta real validados después del reinicio.

El warning nuevo de MariaDB corresponde al intento deliberado de root sin
contraseña. Las entradas `SEVERE` nuevas de GlassFish correspondieron a pedir
la JSP temporal después de eliminarla; las URL finales válidas responden 200.

## 13. Archivos versionados

- `database/cava.sql`.
- `database/01_schema.sql`.
- `database/README.md`.
- `web/WEB-INF/web.xml`.
- `docs/auditorias/INFORME_RECUPERACION_DB_DEFINITIVA.md`.

No fue necesario modificar Models, DAO, `Conexion`, `CavaPool`, `jdbc/CavaDS`,
`my.ini`, phpMyAdmin ni credenciales. GlassFish actualizó automáticamente la
ubicación de despliegue de `Cava` en `domain.xml`, exclusivamente dentro del
alcance autorizado y conservando intactos los recursos JDBC.

## 14. Cierre

La cadena quedó validada de extremo a extremo:

```text
MariaDB XAMPP → cava → cava_app → Connector/J → CavaPool → jdbc/CavaDS
→ Conexion.getConn() → ProductosDAO → aplicación CAVA en GlassFish
```

No quedan pendientes técnicos de base de datos dentro de este alcance. Es
seguro continuar con la siguiente fase una vez confirmado el commit local y
el árbol limpio. No se hizo push ni se abrió pull request.
