# Informe de Fase 5 — Corrección de Models con BigDecimal

## Estado

**FASE 5 CERRADA — FASE 6 NO INICIADA**

Fecha de cierre: 19 de julio de 2026.

## 1. Definición autoritativa y alcance

`docs/05_ROADMAP.md` define la Fase 5 como **Corrección de Models** y ordena
alinear Java con MySQL, construir la correspondencia, migrar dinero a
`BigDecimal`, revisar cantidades, fechas, booleanos, nombres y constructores,
y mantener compatibilidad.

La ejecución se limitó a los seis atributos que corresponden a columnas
`DECIMAL(10,2)`, sus cinco DAO, una prueba de regresión sin dependencias y la
documentación de estado. No se implementó la auditoría general de DAO de Fase
6 ni ninguna función posterior.

## 2. Precondiciones Git verificadas

- Raíz: `C:\Users\Maria Camila R\Documents\Cava\Cava`.
- Rama de partida: `feature/fase-4-db`.
- Commit de partida: `f7f35d62408b942d8dc8ff5409c796276ef68aee`.
- Rama creada: `feature/fase-5-bigdecimal`, sin upstream.
- `main` y `origin/main`: `596273b3ca87def8fcfdace7faddbad4cfdeb417`.
- Árbol inicial limpio y sin merge, rebase, cherry-pick, revert o bisect.

No se hizo merge, rebase, cherry-pick, push ni pull request.

## 3. Auditoría previa y trazabilidad

Las seis coincidencias `double` del código eran decimales exactos confirmados;
no se encontraron `double` no monetarios ni coincidencias ambiguas.

| Tabla.columna | SQL | Model | Flujo previo | Flujo final |
|---|---|---|---|---|
| `productos.precioProductos` | `DECIMAL(10,2) NOT NULL` | `Productos` | `double`, `setDouble`, `getDouble` | `BigDecimal`, `setBigDecimal`, `getBigDecimal` |
| `inventario.stock` | `DECIMAL(10,2) NOT NULL DEFAULT 0` | `Inventario` | `double`, `setDouble`, `getDouble` | `BigDecimal`, `setBigDecimal`, `getBigDecimal` |
| `pedidosCabeza.valorTotal` | `DECIMAL(10,2) NOT NULL` | `PedidosCabeza` | `double`, `setDouble`, `getDouble` | `BigDecimal`, `setBigDecimal`, `getBigDecimal` |
| `pedidosDetalle.cantidadUnitaria` | `DECIMAL(10,2) NOT NULL` | `PedidosDetalle` | `double`, `setDouble`, `getDouble` | `BigDecimal`, `setBigDecimal`, `getBigDecimal` |
| `pedidosDetalle.subtotalPed` | `DECIMAL(10,2) NOT NULL` | `PedidosDetalle` | `double`, `setDouble`, `getDouble` | `BigDecimal`, `setBigDecimal`, `getBigDecimal` |
| `pagos.monto` | `DECIMAL(10,2) NOT NULL` | `Pagos` | `double`, `setDouble`, `getDouble` | `BigDecimal`, `setBigDecimal`, `getBigDecimal` |

No existen Servlets funcionales ni referencias a estos campos en JSP o
JavaScript. No hay parámetros HTTP, validaciones web, cálculos monetarios,
formateo de moneda o constructores parametrizados que debieran migrarse. Los
únicos consumidores Java son los cinco DAO indicados.

## 4. Revisión completa de Models

Los 15 Models se contrastaron con `database/01_schema.sql`. Los tipos `INT`,
`VARCHAR`, `DATE`, `DATETIME` y `TINYINT(1)` continúan representados mediante
`int`, `String`, `LocalDate`, `LocalDateTime` y `boolean`. No se identificó una
corrección adicional segura y necesaria.

Se conservan dos aliases Java históricos ya documentados y correctamente
mapeados por sus DAO:

- `CategoriaProductos.descripcionCategoriaP` →
  `descripcionCategoriaProductos`;
- `UnidadesMedida.descripcionUnidadesM` → `descripcionUnidadesMed`.

Renombrarlos habría ampliado el alcance sin mejorar la precisión decimal. Los
constructores existentes son vacíos explícitos o implícitos y siguen siendo
compatibles con el mapeo DAO mediante setters.

## 5. Archivos Java modificados

Models:

- `src/java/Modelo/Productos.java`;
- `src/java/Modelo/Inventario.java`;
- `src/java/Modelo/PedidosCabeza.java`;
- `src/java/Modelo/PedidosDetalle.java`;
- `src/java/Modelo/Pagos.java`.

DAO:

- `src/java/Controlador/ProductosDAO.java`;
- `src/java/Controlador/InventarioDAO.java`;
- `src/java/Controlador/PedidosCabezaDAO.java`;
- `src/java/Controlador/PedidosDetalleDAO.java`;
- `src/java/Controlador/PagosDAO.java`.

Prueba:

- `test/Modelo/BigDecimalModelsTest.java`.

## 6. Precisión, escala y comparación

- Los valores se construyen desde texto decimal mediante
  `new BigDecimal(String)` únicamente en la prueba; no existe todavía un flujo
  HTTP de producción que convierta texto.
- No se usa `new BigDecimal(double)`, conversión previa con `parseDouble`,
  `setScale` ni `RoundingMode`.
- No se inventó redondeo. SQL conserva `DECIMAL(10,2)`.
- La comparación numérica probada usa `compareTo`; no usa `equals`.
- `null` puede permanecer en el Model y llegar a `setBigDecimal`; las columnas
  reales son `NOT NULL`, por lo que la validación funcional deberá impedirlo
  antes de persistir cuando exista la capa web.
- El texto vacío y la coma decimal se rechazan como texto no válido por el
  constructor directo; el separador esperado es punto.
- Cero, dos decimales y los límites `±99999999.99` son representables.
- Los negativos no se prohibieron: no existe una regla autoritativa uniforme
  para los seis campos. La decisión funcional queda pendiente de fases futuras.
- `100000000.00` y una escala de tres decimales exceden `DECIMAL(10,2)` y se
  comprobaron sin aplicar redondeo.

## 7. Validaciones ejecutadas

Compilación limpia con el Ant existente de NetBeans 20:

```text
ant -f build.xml clean compile compile-test
BUILD SUCCESSFUL
33 fuentes de producción y 1 fuente de prueba compiladas
```

Prueba ejecutada con Java 17:

```text
Modelo.BigDecimalModelsTest
BIGDECIMAL_MODELS_OK
```

Empaquetado sin despliegue:

```text
ant -f build.xml dist
BUILD SUCCESSFUL
dist/Cava.war: 6.329.159 bytes
SHA-256: EBA4D3DC94B4EAAF2A1E039C08DB3CE36BA753DDF3B20ED367EFB0FEF6DF9AF9
```

El WAR contiene las cinco clases Model modificadas, no incluye la prueba y
mantiene cero entradas de Connector/J.

La búsqueda estática final confirmó:

- cero `double` o `Double` en Java;
- cero `parseDouble`, `getDouble` o `setDouble` en Java;
- cero `new BigDecimal(double)`;
- seis atributos `BigDecimal`;
- doce llamadas `setBigDecimal` y seis llamadas `getBigDecimal`;
- cero referencias monetarias en JSP, JavaScript o Servlets.

No había pruebas existentes relacionadas; se agregó y ejecutó la prueba mínima
indicada. La compilación no presentó errores introducidos ni preexistentes.

## 8. Base de datos y servicios

No se ejecutó ninguna consulta o sentencia SQL durante la Fase 5. No se usó
`root`, no se recuperaron credenciales y no se modificaron tablas, columnas,
índices, restricciones, datos, usuarios, privilegios, `my.ini`, `CavaPool` o
`jdbc/CavaDS`.

La estructura SQL permanece en seis columnas `DECIMAL(10,2)`. La referencia
confirmada al cierre de Fase 4 era 15 tablas y 0 filas; esta fase no realizó
ninguna acción capaz de alterar ese estado.

La verificación final encontró una sola instancia XAMPP
`C:\xampp8\mysql\bin\mysqld.exe`, con un único listener en el puerto 3306.
MariaDB no fue iniciado, reiniciado ni detenido y quedó encendido.

GlassFish no presentó proceso Java de `domain1` ni listeners en 4848, 8080 o
8181 al cierre. Estaba apagado en la comprobación final; esta fase no lo
inició, reinició, detuvo, redesplegó ni modificó.

## 9. Elementos aplazados

Permanecen fuera de alcance:

- sustitución de `SELECT *`, borrado y manejo general de errores DAO: Fase 6;
- validaciones HTTP, Servlets y contratos de respuesta: Fase 7;
- reglas comerciales de signo, redondeo, cálculo y transacciones: requieren
  decisiones funcionales en sus fases correspondientes;
- favoritos, fidelidad, reseñas y verificación: Fase 12;
- cualquier cambio de esquema o migración.

## 10. Cierre

```text
FASE 5 CERRADA; FASE 6 NO INICIADA
```
