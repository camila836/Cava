# Informe de Fase 4A — Auditoría y planificación de consolidación de base de datos

## Estado

**FASE 4A CERRADA — FASE 4B PENDIENTE DE AUTORIZACIÓN**

Fecha de auditoría: 19 de julio de 2026.

## 1. Alcance confirmado

La Fase 4 es **Consolidación de base de datos**. Esta subfase se limitó a
diagnosticar, contrastar y planificar: no se alteraron esquema, datos,
migraciones, Models, DAO, Servlets, JSP, credenciales, recursos JDBC,
GlassFish ni `my.ini`.

No se inició la Fase 4B ni se implementaron storefront, carrito,
autenticación, dashboard o funcionalidades de usuario. La rama de trabajo es
local: `feature/fase-4-db`.

## 2. Fuentes autoritativas y método

Fuentes consultadas de forma dirigida:

- `docs/02_PROJECT_STATE.md` — estado, script canónico y pendientes.
- `docs/04_DATABASE.md` — reglas de persistencia, organización objetivo y
  criterios de aceptación.
- `docs/05_ROADMAP.md` — definición y actividades de la Fase 4; las funciones
  adicionales están previstas después de la primera versión.
- `docs/auditorias/INFORME_FASE3.md`, sección 23 — cierre de la cadena JDBC y
  base real de 15 tablas.
- `database/cava.sql` y las cuatro migraciones `cava_migracion_*.sql`.
- Los 15 Models en `src/java/Modelo/` y los 15 DAO en
  `src/java/Controlador/`.

No existe un informe local separado denominado «Paso 3.5» ni
`docs/07_TECHNICAL_DEBT.md`; el checkpoint Git y el cierre de Fase 3 fueron
contrastados como evidencia local disponible. No se releyeron informes
históricos completos ni se repitieron pruebas JDBC, compilación o despliegue.

MariaDB ya estaba iniciada mediante XAMPP. Se confirmó una única instancia
`mysqld.exe` escuchando en 3306. La credencial existente de `cava_app` se usó
de forma transitoria desde el alias cifrado de GlassFish, exclusivamente en
memoria, para ejecutar `SELECT 1`, consultas a `information_schema` y conteos
de filas. Su valor no se mostró, almacenó ni incluyó en este informe. No se
inició GlassFish.

## 3. Esquema real y fuente de verdad

La consulta de lectura confirmó MariaDB `10.4.32-MariaDB`, base seleccionada
`cava`, motor InnoDB y colación `utf8mb4_unicode_ci` para sus 15 tablas. Todas
tenían **0 filas** al momento de la auditoría.

`database/cava.sql` es el único script principal canónico, conforme a
`docs/02_PROJECT_STATE.md`. El esquema real coincide estructuralmente con sus
15 `CREATE TABLE`: columnas, tipos, nulabilidad, predeterminados, claves,
restricciones UNIQUE y claves foráneas. `information_schema` muestra los
nombres de tabla en minúscula; es la normalización observada de MariaDB sobre
esta instalación Windows y no una segunda convención del proyecto.

El script canónico crea la base y el esquema juntos. Las cuatro migraciones
son propuestas posteriores, no están presentes en `cava` y no forman parte de
la primera versión.

## 4. Inventario del esquema real

| Tabla | Columnas y reglas relevantes | Relaciones, UNIQUE e índices | Registros |
|---|---|---|---:|
| `categoriaProductos` | `idCategoriaProductos` INT AI PK; `descripcionCategoriaProductos` VARCHAR(45) NN | PK | 0 |
| `ciudades` | `idCiudades` INT AI PK; `codigoCiudad`, `nombreCiudad` VARCHAR(45) NN; `codigoPostal` nullable | PK | 0 |
| `roles` | `idRoles` INT AI PK; `descripcionRol` VARCHAR(45) NN | PK | 0 |
| `tipoDocumento` | `idTipoDocumento` INT AI PK; `descripcion` VARCHAR(45) NN | PK | 0 |
| `unidadesMedida` | `idUnidadesMedida` INT AI PK; `descripcionUnidadesMed` VARCHAR(45) NN | PK | 0 |
| `transportadoras` | `idTransportadoras` INT AI PK; nombre y NIT NN; correo y teléfono nullable | PK | 0 |
| `estadoEnvio` | `idEstadoEnvio` INT AI PK; descripción NN | PK | 0 |
| `mediosPagos` | `idMediosPagos` INT AI PK; descripción NN | PK | 0 |
| `usuarios` | Identificación VARCHAR(45) NN; correo VARCHAR(100) NN; clave VARCHAR(255) NN; booleanos `isActivo=1` y `autorizacionTratamientoDatos=0`; dos DATE opcionales | UNIQUE identificación/correo; FK a roles, tipo de documento y ciudades; índices FK | 0 |
| `productos` | Descripción VARCHAR(45) NN; `precioProductos` DECIMAL(10,2) NN | FK a unidades y categorías; índices FK | 0 |
| `inventario` | Descripción nullable; `stock` DECIMAL(10,2) NN DEFAULT 0.00 | FK a productos; índice FK | 0 |
| `pedidosCabeza` | `numeroPedido` VARCHAR(45) NN; DATETIME NN; descripción nullable; `valorTotal` DECIMAL(10,2) NN | UNIQUE número de pedido; FK a usuarios; índice FK | 0 |
| `pedidosDetalle` | `cantidadUnitaria` y `subtotalPed` DECIMAL(10,2) NN | FK a pedido y producto; índices FK | 0 |
| `pagos` | DATETIME NN; descripción y comprobante nullable; `monto` DECIMAL(10,2) NN; referencia NN | UNIQUE referencia; FK a medio de pago y pedido; índices FK | 0 |
| `envios` | DATETIME NN; descripción nullable; guía NN | UNIQUE guía; FK a pedido, estado y transportadora; índices FK | 0 |

Los índices existentes son las PK, los índices UNIQUE y los índices de las 14
FK. No hay índices de negocio adicionales declarados explícitamente.

## 5. Matriz SQL–Model–DAO

| Tabla / script | Model y DAO | Estado de correspondencia | Diferencia o riesgo | Acción propuesta para 4B |
|---|---|---|---|---|
| `categoriaProductos` / canónico | `CategoriaProductos`, `CategoriaProductosDAO` | Columnas y operaciones cubiertas | Atributo Java abreviado `descripcionCategoriaP`; DAO lo mapea correctamente | Documentar el alias; no renombrar Java en 4B |
| `ciudades` / canónico | `Ciudades`, `CiudadesDAO` | Compatible | Sin UNIQUE para código de ciudad o postal; decisión de negocio pendiente | Revisar necesidad con el catálogo aprobado |
| `roles` / canónico | `Roles`, `RolesDAO` | Compatible | Sin semilla reproducible | Definir catálogo inicial |
| `tipoDocumento` / canónico | `TipoDocumento`, `TipoDocumentoDAO` | Compatible | Sin semilla reproducible | Definir catálogo inicial |
| `unidadesMedida` / canónico | `UnidadesMedida`, `UnidadesMedidaDAO` | Columnas y operaciones cubiertas | Atributo Java abreviado `descripcionUnidadesM`; DAO lo mapea correctamente | Documentar el alias; no renombrar Java en 4B |
| `transportadoras` / canónico | `Transportadoras`, `TransportadorasDAO` | Compatible | NIT no es UNIQUE; no hay catálogo inicial | Confirmar regla de unicidad y semillas |
| `estadoEnvio` / canónico | `EstadoEnvio`, `EstadoEnvioDAO` | Compatible | Sin catálogo inicial; pedido no tiene estado propio | Definir catálogo y límites de estados |
| `mediosPagos` / canónico | `MediosPagos`, `MediosPagosDAO` | Compatible | Sin catálogo inicial | Definir catálogo inicial |
| `usuarios` / canónico | `Usuarios`, `UsuariosDAO` | DATE y TINYINT(1) se mapean a `LocalDate` y `boolean` | DAO borra físicamente; las capturas de error retornan valores vacíos | Definir política de eliminación y manejo de errores posterior |
| `productos` / canónico | `Productos`, `ProductosDAO` | Relaciones y texto compatibles | `DECIMAL(10,2)` se representa con `double`/`setDouble`/`getDouble` | Mantener SQL; migrar Java a `BigDecimal` en Fase 5 |
| `inventario` / canónico | `Inventario`, `InventarioDAO` | FK y default coinciden | `stock` DECIMAL se representa con `double`; admite varias filas por producto | Decidir precisión y unicidad/modelo de movimientos |
| `pedidosCabeza` / canónico | `PedidosCabeza`, `PedidosCabezaDAO` | DATETIME se mapea a `LocalDateTime` | `valorTotal` usa `double`; sin estado de pedido | Preparar decisión de ciclo de vida y dinero |
| `pedidosDetalle` / canónico | `PedidosDetalle`, `PedidosDetalleDAO` | FK y estructura coinciden | Cantidad y subtotal usan `double`; falta precio unitario histórico explícito | Preparar decisión de trazabilidad antes de cambiar esquema |
| `pagos` / canónico | `Pagos`, `PagosDAO` | DATETIME y FK compatibles | `monto` usa `double`; no hay estado/proveedor/operación de pago | Definir trazabilidad de pagos antes de 4B |
| `envios` / canónico | `Envios`, `EnviosDAO` | DATETIME y FK compatibles | No hay política de eliminación ni reglas de transición documentadas | Definir conservación y transiciones |

Los 15 DAO usan `PreparedStatement` y `try-with-resources`, pero contienen 36
consultas `SELECT *`. Los 15 exponen `DELETE FROM` físico y, ante errores SQL,
imprimen en `System.err` y devuelven `false`, `null` o una lista vacía: el
llamador no puede distinguir «sin resultado» de «fallo». Son hallazgos para
fases de Models/DAO; en 4A no se modificaron.

## 6. Diferencias y riesgos priorizados

1. **Alto — precisión monetaria y de cantidades.** Cinco Models usan `double`
   para seis columnas `DECIMAL(10,2)`; los DAO contienen 12 `setDouble` y 6
   `getDouble`. Afecta precios, inventario, totales, detalle y pagos. La
   corrección pertenece a Fase 5 y debe usar `BigDecimal` sin cambiar todavía
   el contrato SQL.
2. **Alto — instalación nueva incompleta.** No hay datos iniciales y todas las
   tablas reales están vacías. Las FK hacen imposible crear usuarios, productos
   y operaciones sin catálogos previos.
3. **Medio — repetibilidad parcial.** `cava.sql` separa ni base, esquema,
   índices y semillas; sus `CREATE TABLE` no son reejecutables sobre una base
   parcialmente creada. Las migraciones usan `IF NOT EXISTS`, pero carecen de
   versión, orden y registro de aplicación.
4. **Medio — eliminación y trazabilidad sin decisión.** Los DAO eliminan
   físicamente. Pedidos no tienen estado propio; pagos no registran estado ni
   proveedor; inventario no tiene movimientos; detalle de pedido no preserva
   un precio unitario histórico. No deben añadirse columnas sin decisión de
   negocio explícita.
5. **Medio — relaciones e índices.** Las FK, UNIQUE y sus índices existen. No
   procede añadir índices de negocio por intuición: primero deben aprobarse
   consultas, cardinalidades y `EXPLAIN` sobre datos de prueba.
6. **Bajo — coherencia documental.** `docs/04_DATABASE.md` ya propone la
   organización destino y exige una fuente de verdad, pero todavía describe el
   estado como pendiente de consolidación. No hay contradicción material con el
   roadmap ni con el script canónico.

## 7. Clasificación de migraciones adicionales

| Archivo actual | Función | Primera versión | Dependencias y cobertura Java | Riesgo de incluirla ahora | Fase prevista |
|---|---|---|---|---|---|
| `cava_migracion_favoritos.sql` | Wishlist usuario–producto | No; aplazar | Requiere usuarios y productos; no hay Model ni DAO | `ON DELETE CASCADE` sin política aprobada | 12 — funciones adicionales |
| `cava_migracion_fidelidad.sql` | Historial de puntos | No; aplazar | Requiere usuarios y pedidos; no hay Model ni DAO | Reglas de acumulación, redención y auditoría no definidas | 12 — funciones adicionales |
| `cava_migracion_resenas.sql` | Reseñas y calificación | No; aplazar | Requiere usuarios y productos; no hay Model ni DAO | Falta validar compra; cascada y moderación sin definir | 12 — funciones adicionales |
| `cava_migracion_verificacion.sql` | Tokens de verificación | No; aplazar | Requiere usuarios; no hay Model ni DAO | Caducidad, entrega, reenvío y protección de token sin definir | 12 — funciones adicionales |

La clasificación esperada queda confirmada: las 15 tablas principales son la
primera versión; favoritos, fidelidad, reseñas y verificación son funciones
futuras. Ninguna migración se ejecutó.

## 8. Propuesta para la Fase 4B

La organización objetivo, todavía no creada, es:

```text
database/
├── 00_create_database.sql
├── 01_schema.sql
├── 02_indexes.sql
├── 03_seed_catalogs.sql
└── migrations/
    ├── V001__favoritos.sql
    ├── V002__puntos_usuario.sql
    ├── V003__resenas.sql
    └── V004__verificacion_cuenta.sql
```

| Archivo propuesto | Objetivo y contenido | Dependencias, orden y validación |
|---|---|---|
| `00_create_database.sql` | Crear únicamente `cava`, charset y colación aprobados | Primero; validar creación en una base temporal, nunca sobre la real |
| `01_schema.sql` | Las 15 tablas, PK, FK, UNIQUE, tipos y defaults canónicos | Después de 00; ejecutar desde cero y contrastar `information_schema` |
| `02_indexes.sql` | Índices no implícitos que estén justificados por consultas aprobadas | Después de 01; validar nombres y planes `EXPLAIN` con datos de prueba |
| `03_seed_catalogs.sql` | Roles, tipos de documento, ciudades, categorías, unidades, estados de envío, medios de pago y transportadoras aprobados | Después de 01/02; validar conteos y FK; no incluir usuario administrador ni credenciales sin autorización |
| `migrations/V001` a `V004` | Renombrar y documentar las cuatro funciones futuras, una por versión | Solo tras cerrar primera versión y sus dependencias funcionales; registrar aplicación y definir reversión viable |

Antes de 4B requieren decisión explícita: catálogo inicial y sus valores;
unicidad de ciudad/código postal/NIT; precisión de stock; política de borrado;
estados de pedido, pago y envío; movimientos de inventario; y trazabilidad del
precio histórico. La propuesta no autoriza ninguna de esas modificaciones.

## 9. Plan seguro para ejecutar la Fase 4B

1. Obtener autorización específica para crear scripts y una base temporal
   independiente, por ejemplo `cava_fase4a_tmp`; nunca reutilizar `cava`.
2. Tomar, antes de cualquier DDL sobre `cava`, un respaldo verificable de la
   base real y registrar hash, tamaño y método de restauración.
3. Separar el canónico en los cuatro scripts propuestos sin mover ni sustituir
   el original hasta validar la instalación temporal.
4. Ejecutar el orden 00 → 01 → 02 → 03 solo en la base temporal; comparar
   tablas, columnas, PK, FK, UNIQUE, índices y datos de catálogo.
5. Probar idempotencia y fallo controlado en un entorno temporal nuevo. Validar
   referencias inválidas, nulos, duplicados y DECIMAL sin tocar código Java.
6. Revisar el resultado, documentar diferencias y pedir autorización adicional
   antes de promover cualquier DDL a `cava`.

## 10. Acciones prohibidas hasta nueva autorización

- Ejecutar DDL o DML sobre `cava`, sus tablas o las migraciones.
- Cambiar datos, usuarios, privilegios, contraseñas, `my.ini`, GlassFish,
  `CavaPool`, `jdbc/CavaDS` o Connector/J.
- Modificar Models, DAO, Servlets, JSP, frontend o pruebas de despliegue.
- Crear la estructura propuesta, mover/reemplazar SQL, iniciar Fase 4B,
  publicar la rama, abrir PR o integrar en `main`.

## 11. Confirmación de cierre

Durante Fase 4A se modificaron únicamente este informe y la referencia mínima
en `docs/02_PROJECT_STATE.md`. No se modificaron esquema, datos, código ni
configuración. Las consultas realizadas fueron exclusivamente `SELECT 1`,
consultas de `information_schema` y `SELECT COUNT(*)` por tabla.

```text
FASE 4A CERRADA; FASE 4B PENDIENTE DE AUTORIZACIÓN
```
