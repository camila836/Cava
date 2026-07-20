# DATABASE.md

# Base de datos oficial de CAVA

> **Motor:** MySQL  
> **Estado:** Esquema de primera versión consolidado y validado en Fase 4B
> **Fuente de verdad:** instalador SQL `00`–`03`, validado estructuralmente;
> la precisión Java de las seis columnas `DECIMAL(10,2)` fue corregida en
> Fase 5; la auditoría funcional general de DAO corresponde a Fase 6

---

## 1. Propósito

Este documento define las reglas de persistencia de CAVA.

Debe utilizarse para:

- diseñar el esquema;
- revisar tablas y relaciones;
- comparar MySQL con los Models;
- diseñar y auditar DAO;
- controlar migraciones;
- evitar inconsistencias entre Java y SQL.

Este documento no reemplaza los scripts SQL ejecutables.

---

## 2. Principios

### Integridad primero

MySQL debe proteger la información mediante:

- claves primarias;
- claves foráneas;
- restricciones;
- índices;
- tipos correctos;
- campos obligatorios;
- valores predeterminados justificados.

### Una fuente de verdad

Debe existir un esquema inicial oficial y un orden claro de migraciones.

No se deben mantener varios scripts contradictorios que representen estados distintos sin identificar cuál es el vigente.

### Correspondencia con Java

Cada columna persistida debe tener una correspondencia clara con:

- Model;
- atributo;
- tipo Java;
- DAO;
- operación SQL.

### Trazabilidad

Las entidades históricas no deben perder información de forma accidental.

Pedidos, pagos, envíos y movimientos de inventario deben conservar trazabilidad.

---

## 3. Estado conocido

El proyecto contiene un instalador principal separado en `00`–`03`, una
instantánea de compatibilidad `cava.sql` y cuatro migraciones futuras ordenadas.

También existen tablas o propuestas relacionadas con:

- favoritos;
- puntos de usuario;
- reseñas;
- verificación de cuenta.

Estas cuatro funciones no tienen todavía Model y DAO y permanecen aplazadas
para Fase 12.

La Fase 4 definió:

1. las 15 tablas actuales pertenecen a la primera versión;
2. el instalador oficial usa `00_create_database.sql` a
   `03_seed_catalogs.sql`;
3. las migraciones V001–V004 son futuras y no se ejecutan en la instalación;
4. el orden obligatorio está en `database/README.md`;
5. no existen todavía valores de catálogo autorizados.

---

## 4. Organización recomendada

```text
database/
├── README.md
├── cava.sql                    # compatibilidad; no ejecutar con 01
├── 00_create_database.sql
├── 01_schema.sql
├── 02_indexes.sql
├── 03_seed_catalogs.sql
├── migrations/
│   ├── V001__favoritos.sql
│   ├── V002__puntos_usuario.sql
│   ├── V003__resenas.sql
│   ├── V004__verificacion_cuenta.sql
│   └── README.md
```

Los backups reales no deben almacenarse en Git si contienen información personal o credenciales.

---

## 5. Convenciones de nombres

> **Confirmado contra el esquema real (`database/cava.sql`):** el proyecto
> usa **camelCase** de forma consistente en tablas y columnas, no snake_case.
> Esta sección documenta la convención realmente vigente. No debe forzarse
> snake_case sobre el esquema existente sin una razón que justifique
> reescribir las 15 tablas y sus 15 DAO correspondientes.

### Tablas

```text
camelCase
```

Ejemplo (nombres reales del proyecto):

```text
categoriaProductos
pedidosCabeza
pedidosDetalle
unidadesMedida
tipoDocumento
estadoEnvio
mediosPagos
```

Tablas de una sola palabra se mantienen en minúsculas: `usuarios`, `productos`, `roles`, `ciudades`, `envios`, `pagos`, `inventario`, `transportadoras`.

### Columnas

```text
camelCase
```

Ejemplo (columnas reales del proyecto):

```text
idProductos
descripcionProductos
precioProductos
idUnidadesMedida
idCategoriaProductos
```

### Claves primarias

```text
id<NombreEntidad>
```

Ejemplo:

```text
idProductos
idUsuarios
idPedidosCabeza
```

### Claves foráneas

```text
id<EntidadReferenciada>
```

Ejemplo:

```text
idCategoriaProductos
idUnidadesMedida
```

Es decir, en este proyecto la clave foránea usa el mismo patrón `id<Entidad>` que la clave primaria de la tabla referenciada, en vez de `<entidad>_id`.

### Clases Java

```text
PascalCase
```

Ejemplo:

```text
Productos
ProductosDAO
UnidadesMedida
CategoriaProductos
```

### Atributos, métodos y variables Java

```text
camelCase
```

Ejemplo:

```text
precioProductos
idCategoriaProductos
consultarPorId()
```

No se debe cambiar la convención de tablas o columnas existentes sin revisar el impacto sobre los 15 Models, los 15 DAO y las vistas JSP que ya dependen de estos nombres.

---

## 6. Tipos de datos

### Identificadores

Usar `INT` o `BIGINT` según el crecimiento esperado.

Java:

```java
Integer
Long
```

### Dinero

Usar:

```sql
DECIMAL(p, s)
```

Java:

```java
BigDecimal
```

Nunca usar `FLOAT`, `DOUBLE` ni `double` para valores monetarios.

### Cantidades

Si la cantidad puede ser fraccionaria:

```sql
DECIMAL(p, s)
```

Si siempre es entera:

```sql
INT
```

### Texto

Elegir entre:

- `VARCHAR`;
- `TEXT`;
- `CHAR`.

No asignar tamaños arbitrarios sin revisar el uso real.

### Fechas

Usar:

- `DATE` para fecha sin hora;
- `DATETIME` o `TIMESTAMP` para fecha y hora.

La estrategia debe corresponder con `java.time`.

### Booleanos

MySQL puede representarlos con:

```sql
BOOLEAN
```

o:

```sql
TINYINT(1)
```

La convención debe ser uniforme.

---

## 7. Restricciones

### `NOT NULL`

Debe aplicarse cuando el dato sea obligatorio.

### `UNIQUE`

Debe evaluarse para:

- correo;
- número de documento;
- códigos internos;
- referencias de pago;
- identificadores externos.

### `CHECK`

Puede utilizarse cuando la versión de MySQL lo soporte y la regla sea estable.

Ejemplos:

- cantidad no negativa;
- precio mayor o igual a cero;
- porcentaje dentro de rango.

### Claves foráneas

Cada relación debe declarar:

- tabla origen;
- columna;
- tabla destino;
- política de actualización;
- política de eliminación.

No usar `CASCADE` automáticamente. En datos históricos puede causar pérdida de información.

---

## 8. Índices

Se deben crear índices para:

- claves foráneas;
- correo;
- estados;
- fechas de búsqueda;
- nombres usados en filtros;
- combinaciones frecuentes.

No se debe crear un índice para cada columna. Cada índice tiene costo de escritura y almacenamiento.

---

## 9. Entidades principales

La lista debe confirmarse contra los 15 Models reales.

Como base conocida:

- roles;
- tipos de documento;
- ciudades;
- usuarios;
- unidades de medida;
- categorías de productos;
- productos;
- inventario;
- estados de envío;
- medios de pago;
- transportadoras;
- pedidos cabecera;
- pedidos detalle;
- pagos;
- envíos.

La denominación exacta debe validarse contra el código y el script aprobado.

---

## 10. Relaciones generales

```text
roles
└── usuarios

tipoDocumento
└── usuarios

ciudades
├── usuarios
└── envios

categoriaProductos
└── productos

unidadesMedida
├── productos
└── inventario

usuarios
└── pedidosCabeza

pedidosCabeza
├── pedidosDetalle
├── pagos
└── envios

productos
├── inventario
└── pedidosDetalle
```

Este diagrama es conceptual pero ya usa los nombres reales de tabla (camelCase) confirmados contra `database/cava.sql`.

---

## 11. Productos

Los productos deben conservar como mínimo:

- identificador;
- nombre;
- descripción;
- categoría;
- unidad de medida;
- precio;
- estado;
- imagen o referencia;
- fechas de creación y actualización, si se aprueban.

El precio debe usar `DECIMAL`.

Los productos vendidos no deberían eliminarse físicamente si existen pedidos históricos. Se recomienda desactivación.

---

## 12. Inventario

El inventario debe distinguir entre:

- existencia actual;
- unidad;
- producto;
- fecha de actualización;
- cantidad mínima;
- estado, si se necesita.

Para una versión robusta se recomienda registrar movimientos de inventario en una tabla separada.

Ejemplo:

```text
movimientos_inventario
├── producto_id
├── tipo_movimiento
├── cantidad
├── motivo
├── referencia
├── usuario_id
└── fecha
```

No se debe modificar el stock sin una operación controlada.

---

## 13. Pedidos

### Cabecera

Debe contener:

- cliente;
- fecha;
- estado;
- total histórico;
- datos de entrega;
- observaciones;
- referencia.

### Detalle

Debe contener:

- pedido;
- producto;
- cantidad;
- precio unitario histórico;
- subtotal.

El precio del detalle no debe depender del precio actual del producto.

---

## 14. Pagos

Debe documentarse:

- pedido;
- medio de pago;
- valor;
- fecha;
- estado;
- referencia externa;
- observaciones.

No se deben almacenar datos completos de tarjetas.

---

## 15. Envíos

Debe documentarse:

- pedido;
- transportadora;
- estado;
- ciudad;
- dirección;
- número de guía;
- fechas;
- observaciones.

Los estados deben ser controlados mediante catálogo o regla estable.

---

## 16. Usuarios

Debe contemplarse:

- rol;
- tipo y número de documento;
- nombres;
- correo;
- contraseña con hash;
- estado;
- datos de contacto;
- fechas necesarias.

Nunca debe almacenarse una contraseña en texto plano.

---

## 17. Datos iniciales

Los catálogos necesarios para arrancar el sistema deben estar en un script reproducible.

Ejemplos:

- roles;
- tipos de documento;
- estados;
- unidades;
- medios de pago.

Los datos iniciales no deben depender de inserciones manuales.

---

## 18. Migraciones

Cada migración debe contener:

- identificador;
- propósito;
- fecha;
- dependencia;
- cambios;
- reversión, cuando sea viable;
- estado de ejecución.

No se debe editar una migración ya aplicada en un entorno compartido. Debe crearse una nueva.

---

## 19. Auditoría de correspondencia

Antes de aprobar Models y DAO se debe construir una matriz:

| Entidad | Atributo Java | Columna SQL | Tipo Java | Tipo SQL | Nulo | Estado |
|---|---|---|---|---|---|---|
| Productos | precioProductos | precioProductos | BigDecimal | DECIMAL(10,2) | No | Validado en Fase 5 |

La matriz debe cubrir todas las entidades.

---

## 20. Pruebas mínimas

- Crear base desde cero.
- Ejecutar todos los scripts.
- Verificar claves foráneas.
- Insertar catálogos.
- Insertar registros válidos.
- Probar duplicados.
- Probar valores nulos.
- Probar relaciones inválidas.
- Probar decimales.
- Probar transacciones.
- Validar consultas DAO.

---

## 21. Criterios de aceptación

La base se considera estable cuando:

- [x] Existe un instalador inicial oficial separado en `00`–`03`.
- [x] Las migraciones tienen orden V001–V004 y están aplazadas.
- [x] Todas las tablas se crean desde cero.
- [x] No hay scripts contradictorios; `cava.sql` coincide con `01_schema.sql`.
- [x] Las claves foráneas y sus reglas fueron validadas.
- [x] Los índices principales existen.
- [x] Los tipos del esquema consolidado coinciden con los 15 Models.
- [x] Dinero y cantidades exactas usan `DECIMAL(10,2)` y `BigDecimal`.
- [x] Los datos iniciales son reproducibles: no existen valores autorizados y
  el script reservado no ejecuta DML.
- [x] Los 15 DAO usan nombres reales, confirmado en Fase 4A.
- [x] Las pruebas de instalación temporal fueron ejecutadas.
- [x] La documentación fue actualizada.

---

## 22. Pendientes actuales

- ~~Consolidar el esquema principal.~~ Cerrado en Fase 4B.
- ~~Definir tablas de la primera versión.~~ Cerrado: 15 tablas.
- ~~Ordenar migraciones.~~ Cerrado documentalmente; ejecución aplazada a Fase 12.
- ~~Revisar tipos monetarios.~~ Cerrado en Fase 5 para las seis columnas
  `DECIMAL(10,2)`.
- Revisar claves foráneas.
- Revisar eliminación física o lógica.
- ~~Crear matriz Model–tabla–columna.~~ Cerrado en Fase 4A.
- Validar funcionalmente migraciones adicionales en Fase 12.

---

## 23. Correspondencia decimal cerrada en Fase 5

La Fase 5 conservó intacto el esquema SQL y alineó los contratos Java:

| Tabla | Columna | Model | DAO/JDBC |
|---|---|---|---|
| `productos` | `precioProductos` | `BigDecimal` | `setBigDecimal` / `getBigDecimal` |
| `inventario` | `stock` | `BigDecimal` | `setBigDecimal` / `getBigDecimal` |
| `pedidosCabeza` | `valorTotal` | `BigDecimal` | `setBigDecimal` / `getBigDecimal` |
| `pedidosDetalle` | `cantidadUnitaria` | `BigDecimal` | `setBigDecimal` / `getBigDecimal` |
| `pedidosDetalle` | `subtotalPed` | `BigDecimal` | `setBigDecimal` / `getBigDecimal` |
| `pagos` | `monto` | `BigDecimal` | `setBigDecimal` / `getBigDecimal` |

No se aplicó `setScale`, `RoundingMode` ni una restricción Java nueva sobre
signos porque no existe una regla funcional autoritativa. Las restricciones
`NOT NULL`, precisión y escala permanecen definidas en SQL; la futura capa web
deberá validar texto y límites antes de invocar los DAO.
