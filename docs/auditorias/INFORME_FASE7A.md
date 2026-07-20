# Informe de Fase 7A - Auditoría y planificación de Servlets, rutas y contratos HTTP

**FASE 7A CERRADA - FASE 7B NO INICIADA**

El nombre documental autoritativo es **Fase 7 — Infraestructura web**. Esta
subfase conserva ese nombre y audita exclusivamente su contrato de Servlets,
rutas, HTTP, validación, navegación JSP e integración `Servlet -> DAO`.

## 1. Alcance, límites y precondiciones

Se realizó una auditoría estática. No se modificaron Java productivo, DAO,
Models, SQL, migraciones, JSP, JavaScript, CSS, `web.xml`, GlassFish, XAMPP,
credenciales ni servicios. Tampoco se compiló, desplegó, consultó MariaDB ni se
repitieron las pruebas DAO cerradas en Fase 6.

Precondiciones Git verificadas antes de crear la rama:

| Dato | Resultado |
| --- | --- |
| Raíz Git | `C:/Users/Maria Camila R/Documents/Cava/Cava` |
| Rama de partida | `main` |
| `main` y `origin/main` | `607445d4decd1c70b5db5b77e27bdc8ef0709ad8` |
| Divergencia | `0/0` |
| Árbol inicial | limpio |
| PR abiertos | ninguno |
| Rama local de 7A | `feature/fase-7a-auditoria-servlets` |
| Push / PR | no realizados |

La ruta temporal autorizada
`C:\Users\Maria Camila R\AppData\Local\Temp\cava-fase6-final-check` está fuera
del repositorio y ya se encontraba ausente al iniciar 7A. Por ello no fue
posible volver a inventariar su contenido ni se ejecutó una segunda eliminación;
su inexistencia final quedó confirmada. No se inspeccionó ni eliminó ninguna
otra carpeta de `Temp`.

Fuentes revisadas: `docs/05_ROADMAP.md`, `docs/02_PROJECT_STATE.md`,
`docs/03_ARCHITECTURE.md`, `docs/06_AGENTS.md`, los informes de Fases 6A y 6B,
`src/java/Servlets/`, los 15 DAO, las excepciones tipadas de DAO, las cuatro
JSP y `web/WEB-INF/web.xml`.

## 2. Método y criterio de evidencia

Se hicieron búsquedas dirigidas sobre archivos Servlet, anotaciones
`@WebServlet`, extensiones de `HttpServlet`, filtros, despachos, redirecciones,
parámetros, formularios, enlaces, scripts y llamadas asíncronas. La ausencia de
una implementación actual no se atribuye como defecto a una fase posterior:
cada contrato se marca como 7B, Fase 8, Fase 9/10 o Fase 11 según el roadmap.

| Elemento | Cantidad comprobada |
| --- | ---: |
| Clases Servlet existentes | 0 |
| Mapeos Servlet en `web.xml` o `@WebServlet` | 0 |
| Filtros existentes | 0 |
| Páginas de error configuradas | 0 |
| JSP existentes | 4 |
| Formularios | 2 |
| Formularios con método HTTP semánticamente incorrecto | 0 |
| Destinos Servlet de formulario inexistentes | 2 |
| Archivos JavaScript existentes bajo `web/` | 0 |
| Referencias JSP a JavaScript inexistente | 4 |
| DAO disponibles para integración | 15 |

No existen rutas Servlet duplicadas porque no existe ningún mapeo. Tampoco hay
uso web actual de DAO, `getParameter`, `forward`, `sendRedirect`, `fetch`, AJAX
o equivalentes. `web.xml` solo define `Index.jsp` como `welcome-file`.

## 3. Inventario de rutas y recursos actuales

### 3.1 Rutas válidas

- `/` resuelve a `Index.jsp` mediante `welcome-file`.
- `Index.jsp`, `InicioSesion.jsp`, `RegistrarUsuario.jsp` y `Admin.jsp` existen
  físicamente y pueden ser solicitadas directamente; esto prueba existencia,
  no un contrato MVC completo.
- Las cuatro JSP declaran respuesta y `pageEncoding` UTF-8.

### 3.2 Rutas ausentes o conflictivas

- `POST /InicioSesion` y `POST /RegistrarUsuario` están referenciadas por los
  dos formularios, pero no existe Servlet ni mapeo que las atienda. Los métodos
  POST son correctos para sus intenciones; el defecto actual es el destino.
- `Index.jsp:45` enlaza `RegistrarUsusario.jsp`, nombre inexistente; el archivo
  real es `RegistrarUsuario.jsp`.
- No hay conflicto por duplicidad de rutas. Sí hay doble vía potencial entre
  acceso directo a JSP y futuros controladores; 7B debe declarar una única
  entrada pública por funcionalidad y usar `forward` interno a la JSP.
- `InicioSesion.jsp:62` conserva `href="#"` para recuperación de contraseña;
  es un marcador legítimamente pendiente de Fase 8, no una ruta funcional.

### 3.3 Recursos estáticos ausentes

- No existen `javaScript/Admin.js`, `javaScript/Script.js`,
  `javascript/InicioSesion.js` ni `javascript/RegistrarUsuario.js`, aunque las
  JSP los referencian.
- `InicioSesion.jsp` solicita además `/InicioSesion.css`, que no existe; la
  segunda referencia `css/InicioSesion.css` sí existe.
- `Index.jsp` solicita `CAVA.mp4`; el archivo real está en `Videos/CAVA.mp4`.
- `RegistrarUsuario.jsp` incluye dos veces su hoja de estilo por rutas
  equivalentes. Es una duplicidad de recurso, no de controlador.

Estos recursos afectan navegación y experiencia, pero su creación o corrección
queda fuera de 7A. Los scripts del dashboard pertenecen a la expansión de las
Fases 9/10 y los de tienda a Fase 11.

## 4. Servlets requeridos y límite de fases

Se identifican **10 controladores lógicos ausentes**. Solo dos son seguros para
la implementación inicial de 7B; los otros ocho se documentan ahora, pero no
deben exponerse antes de sus dependencias:

| Servlet lógico | Momento autorizado recomendado | Justificación |
| --- | --- | --- |
| `InicioServlet` | Fase 7B | Entrada MVC pública y navegación tradicional. |
| `ProductosServlet` | Fase 7B, solo lectura pública | Proporciona la prueba mínima real `Servlet -> ProductosDAO`; las mutaciones esperan Fases 9/10. |
| `InicioSesionServlet` | Fase 8 | Login, hash, sesión y mensajes seguros pertenecen a autenticación. |
| `RegistrarUsuarioServlet` | Fase 8 | Registro, rol, catálogos de registro y datos sensibles pertenecen a autenticación. |
| `AdminServlet` | Fase 8 y luego 10 | `/admin` no puede publicarse sin sesión y rol administrador. |
| `CatalogosServlet` | Fase 9/10 | El primer CRUD vertical debe aprobar el patrón antes de extenderlo. |
| `InventarioServlet` | Fase 10 | Es módulo administrativo y requiere permisos. |
| `PedidosServlet` | Fase 10/11 | Agrupa cabecera y detalles; requiere usuario autenticado y Service transaccional. |
| `PagosServlet` | Fase 10/11 | Requiere trazabilidad, autorización y coordinación de negocio. |
| `EnviosServlet` | Fase 10/11 | Requiere autorización y reglas de estado/despacho. |

`PedidosDetalleServlet` no se recomienda: los detalles no constituyen una raíz
HTTP independiente en las vistas o el roadmap. Deben quedar subordinados a
`PedidosServlet` y a una unidad de trabajo de pedido. Esto evita controladores
duplicados sin introducir un framework genérico.

## 5. Matriz obligatoria de funcionalidades y contratos

Leyenda de estado: **actual** describe lo que hoy resuelve el proyecto;
**7B** es el mínimo implementable sin seguridad provisional; **diferido** marca
una dependencia legítima. Las rutas propuestas se apoyan en formularios,
navegación visible, módulos del panel, DAO existentes o el roadmap.

| Funcionalidad | Servlet existente/requerido | Ruta | Método HTTP | Parámetros | Validación | DAO | Resultado | Forward/redirect | Error DAO | Estado | Severidad |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Inicio público | `InicioServlet` requerido | `/inicio` | GET | ninguno | no aplica | ninguno | página inicial | forward a `/Index.jsp` | no aplica | 7B | Medio |
| Login: mostrar | `InicioSesionServlet` requerido | `/InicioSesion` | GET | `msg` opcional, solo código seguro | allowlist de mensajes | ninguno | formulario | forward a `/InicioSesion.jsp` | no aplica | diferido Fase 8 | Sin defecto de 7A |
| Login: enviar | `InicioSesionServlet` requerido | `/InicioSesion` | POST | `correo`, `clave`; `recordarme` opcional | obligatorios, formato, límites, hash, usuario activo | `UsuariosDAO` | sesión e inicio | PRG a `/inicio` o forward 400 al formulario | 400/401; 500 seguro | destino actual ausente; Fase 8 | Alto |
| Registro: mostrar | `RegistrarUsuarioServlet` requerido | `/RegistrarUsuario` | GET | ninguno | no confiar en IDs del cliente | `TipoDocumentoDAO`, `CiudadesDAO` | formulario con catálogos reales | forward a `/RegistrarUsuario.jsp` | 500 seguro | diferido Fase 8 | Alto |
| Registro: enviar | `RegistrarUsuarioServlet` requerido | `/RegistrarUsuario` | POST | nombres, apellidos, tipo, identificación, correo, ciudad, clave, confirmación, autorización; teléfono, fecha y dirección opcionales | obligatoriedad, longitudes, formato, mayoría/regla por definir, coincidencia de clave, FK, duplicados; rol solo servidor | `UsuariosDAO` | usuario registrado | PRG a `/InicioSesion`; forward 400 al formulario | 409 duplicado/FK; 400 datos; 500 seguro | destino actual ausente; Fase 8 | Alto |
| Administración | `AdminServlet` requerido | `/admin` | GET | ninguno | sesión y rol administrador | ninguno inicialmente | panel | forward a `/Admin.jsp` | 500 seguro si luego carga datos | diferido Fase 8/10 | Sin defecto de 7A |
| Catálogos: consulta | `CatalogosServlet` requerido | `/catalogos` | GET | `tipo` obligatorio; `id` opcional | allowlist de ocho catálogos; entero positivo | ocho DAO de catálogo | lista o registro | JSON para panel; forward solo si una JSP lo requiere | 400/404/500 seguro | diferido Fase 9/10 | Sin defecto de 7A |
| Catálogos: mutación | `CatalogosServlet` requerido | `/catalogos` | POST/PATCH/DELETE | `tipo`, identificador y campos del catálogo | servidor, duplicado, FK, política de eliminación, CSRF | DAO del catálogo permitido | alta/cambio/baja | JSON; PRG si se adopta formulario JSP | 400/404/409/500 seguro | diferido Fase 9/10 | Sin defecto de 7A |
| Productos: lista | `ProductosServlet` requerido | `/productos` | GET | ninguno | no exponer campos internos | `ProductosDAO` | catálogo vacío o lista | forward a `/Index.jsp` con atributo; JSON solo cuando exista cliente asíncrono | 500 seguro | 7B lectura | Medio |
| Productos: detalle/gestión | `ProductosServlet` requerido | `/productos` | GET/POST/PATCH/DELETE | `idProductos` y campos según operación | ID, texto, `BigDecimal`, unidad, categoría, permisos y CSRF | `ProductosDAO` | detalle o mutación | JSON para panel; PRG si hay formulario | 400/404/409/500 seguro | detalle Fase 11; CRUD Fase 9/10 | Sin defecto de 7A |
| Inventario | `InventarioServlet` requerido | `/inventario` | GET/PATCH | `idProductos`; stock y descripción al cambiar | ID, `BigDecimal` no negativo, permisos, concurrencia | `InventarioDAO` | consulta o ajuste | JSON | 400/404/409/500 seguro | diferido Fase 10 | Sin defecto de 7A |
| Pedidos: historial | `PedidosServlet` requerido | `/pedidos` | GET | usuario desde sesión; ID opcional | nunca aceptar otro usuario sin autorización | `PedidosCabezaDAO`, `PedidosDetalleDAO` | historial/detalle | forward a vista futura o JSON | 401/403 Fase 8; 404/500 seguro | diferido Fase 8/11 | Sin defecto de 7A |
| Pedidos: alta | `PedidosServlet` requerido | `/pedidos` | POST | productos y cantidades; usuario desde sesión | existencia, stock, cantidades, precio calculado en servidor | DAO de cabecera, detalle e inventario mediante `PedidoService` | pedido completo | PRG al detalle; nunca reenviar POST | 400/404/409/500 seguro | diferido Fase 8/11 | Alto por transacción futura |
| Detalles de pedido | agrupado en `PedidosServlet` | `/pedidos` | GET/POST | identificador de pedido o líneas del alta | pertenencia, producto, cantidad, subtotal calculado | `PedidosDetalleDAO` dentro de Service | detalle asociado | mismo flujo del pedido | mismo mapeo del pedido | diferido Fase 10/11 | Alto por transacción futura |
| Pagos | `PagosServlet` requerido | `/pagos` | GET/POST | pedido; al crear: medio, monto, referencia y comprobante opcional | pertenencia, monto, referencia única, no confiar total cliente | `PagosDAO` y Service si integra pedido | estado o registro de pago | JSON o PRG a pedido | 400/404/409/500 seguro | diferido Fase 8/10/11 | Alto por trazabilidad futura |
| Envíos | `EnviosServlet` requerido | `/envios` | GET/PATCH | pedido; al cambiar: estado, transportadora, guía y descripción | pertenencia, transición de estado, guía única, permisos | `EnviosDAO` | estado de envío | JSON o PRG a pedido | 400/404/409/500 seguro | diferido Fase 8/10/11 | Alto por trazabilidad futura |

## 6. Parámetros y validaciones demostradas en las JSP

`InicioSesion.jsp` envía `correo`, `clave` y el checkbox opcional
`recordarme`. `RegistrarUsuario.jsp` envía como obligatorios `nombres`,
`apellidos`, `idTipoDocumento`, `identificacion`, `correo`, `idCiudades`,
`clave`, `confirmarClave` y `autorizacionTratamientoDatos`; `telefono`,
`fechaNacimiento` y `direccion` son opcionales.

Hay validación HTML (`required`, `type=email`, `minlength`), pero no existe
validación de servidor. Además:

- el registro envía `idRoles=2` como campo oculto; el servidor de Fase 8 debe
  asignar el rol permitido y no confiar en ese valor;
- ciudades y tipos de documento están codificados en la JSP con IDs 1 a 4;
  la vista de Fase 8 debe cargarlos desde DAO y validar que existan;
- la contraseña y su confirmación nunca deben llegar a logs, query strings,
  mensajes de error o respuestas;
- los IDs deben analizarse como enteros positivos; decimales con `BigDecimal`;
  fechas con tipo temporal y límites explícitos;
- una validación fallida debe detener el flujo antes de invocar DAO.

## 7. Navegación, UTF-8 y control de respuesta

### 7.1 Forward, redirect y PRG

- GET exitoso de una página: cargar atributos y hacer `forward` interno a la
  JSP. El navegador conserva la URL del controlador.
- POST inválido: responder 400 y hacer `forward` al mismo formulario para
  conservar errores y valores no sensibles en `request`.
- POST exitoso: `sendRedirect` a un GET estable, patrón
  POST/Redirect/GET, para evitar reenvío al actualizar.
- Después de `forward`, `sendRedirect` o una respuesta JSON, el método debe
  ejecutar `return`. No debe escribir ni intentar otra navegación después de
  confirmar la respuesta. Hoy el riesgo de doble envío es cero porque no hay
  Servlets; esta es una condición de aceptación de 7B.

Las JSP ya leen `requestScope.error` y `param.msg`. Los errores de validación
pueden viajar por `request` con forward. Un mensaje posterior a redirect exige
flash; como las sesiones pertenecen a Fase 8, 7B no debe crear sesión solo para
mensajes. En 7B basta con un código de resultado no sensible en query string o
sin mensaje; Fase 8 podrá adoptar flash de una sola lectura.

### 7.2 UTF-8

Las respuestas JSP declaran UTF-8, pero las solicitudes POST no tienen filtro.
7B debe crear un `EncodingFilter` central que configure request y response
antes de leer parámetros. JSON, cuando exista, debe usar
`application/json;charset=UTF-8`.

## 8. Mapeo central de `DAOException`

La capa web debe consumir `DAOErrorType`, registrar solo diagnóstico técnico
sanitizado y devolver un mensaje estable. Nunca debe exponer causa, SQL,
SQLState, rutas, stack trace, credenciales ni datos sensibles.

| Categoría DAO | HTTP recomendado | Comportamiento web |
| --- | ---: | --- |
| `CONNECTION` | 500 por contrato documental actual | Mensaje genérico y log sanitizado. Evaluar 503 antes de codificar si se aprueba ampliar la arquitectura. |
| `DUPLICATE` | 409 | Conflicto con mensaje seguro; asociar al campo solo si no filtra datos. |
| `FOREIGN_KEY` | 409 | Dependencia o relación inválida sin nombres SQL. |
| `INVALID_DATA` | 400 | Error de datos; la validación previa debe cubrir los casos previsibles. |
| `NOT_FOUND` | 404 | Recurso inexistente sin revelar recursos de terceros. |
| `OPERATION_NOT_ALLOWED` | 409 | Conflicto de estado/regla. Un rechazo de permisos es 403 y pertenece a Fase 8. |
| `SQL_ERROR` | 500 | Mensaje genérico; detalle solo en log técnico sanitizado. |

Para navegación tradicional se fija el status y se hace forward a una vista de
error común. Para JSON se responde una sola vez con `{ok, message, errors}`.
No debe capturarse `Exception` para devolver siempre 200.

## 9. Hallazgos consolidados

| Severidad | Cantidad | Evidencia y alcance |
| --- | ---: | --- |
| Crítico | 0 | No existe capa web que hoy exponga SQL, credenciales o stack traces. |
| Alto | 3 | Los dos POST apuntan a rutas inexistentes; registro confía visualmente en un rol e IDs de catálogo enviados por cliente; pedido/detalle/pago/envío requerirán coordinación transaccional y no pueden implementarse directamente en Servlet. Los dos últimos riesgos son dependencias futuras, no fallos ejecutados. |
| Medio | 5 | No hay entrada MVC ni lectura pública de productos; faltan cuatro JS; hay un enlace JSP mal escrito y dos referencias estáticas inexistentes; falta filtro UTF-8; no hay manejo/páginas globales de error. |
| Bajo | 2 | Hay acceso directo a JSP y una hoja de estilo duplicada; deben normalizarse al conectar controladores, sin ampliar el alcance visual. |

Contradicciones documentales observadas, no corregidas por límite de archivos:
`docs/02_PROJECT_STATE.md` dice que existe JavaScript aunque el árbol actual
contiene cero `.js`; el roadmap ubica filtros de sesión y rol dentro de Fase 7,
pero su propia Fase 8 asigna sesión, permisos y protección de rutas. Para evitar
seguridad provisional, 7B implementará solo `EncodingFilter`; autenticación y
autorización permanecen en Fase 8.

## 10. Transacciones y dependencias de fases

- Un Servlet no debe abrir `Connection`, hacer `commit` ni `rollback`.
- El alta de pedido requiere una sola transacción para cabecera, detalles y
  descuento de inventario. Pago y envío se incorporan solo si la regla de
  negocio define que nacen en la misma unidad.
- La coordinación corresponde a un `PedidoService`, justificado por la
  arquitectura; no debe crearse durante 7B si solo se implementa lectura.
- Login, registro, sesión, flash, CSRF y protección `/admin/*`: Fase 8.
- Primer CRUD vertical y patrón JSON: Fase 9.
- Extensión del panel a catálogos, productos, inventario, pedidos, pagos y
  envíos: Fase 10.
- Catálogo de cliente, carrito, pedido e historial: Fase 11.

## 11. Decisiones pendientes antes de autorizar 7B

1. Aprobar que el mínimo de 7B sea `InicioServlet` y `ProductosServlet` de solo
   lectura, sin publicar todavía controladores protegidos.
2. Elegir el nombre del atributo de productos que consumirá `Index.jsp`; se
   recomienda `productos`.
3. Confirmar si `CONNECTION` conserva HTTP 500 para coincidir con la
   arquitectura o si se autoriza documentar y usar 503.
4. Aprobar una única vista de error bajo `WEB-INF`, sin información técnica.
5. Definir la biblioteca o mecanismo de logging antes de registrar causas.
6. Mantener `InicioSesion` y `RegistrarUsuario` con los nombres ya usados por
   los formularios o aprobar una normalización posterior de rutas en Fase 8.
7. Definir en Fase 9 el formato JSON final antes del primer CRUD; 7B no debe
   crear una abstracción genérica anticipada.

## 12. Plan ejecutable de Fase 7B (no iniciado)

1. Crear desde el cierre de 7A una rama local independiente, verificar árbol
   limpio y conservar la lista exacta de archivos autorizados.
2. Crear exactamente `src/java/Servlets/InicioServlet.java` con GET `/inicio`
   y `src/java/Servlets/ProductosServlet.java` con GET `/productos`. No crear en
   7B los ocho Servlets diferidos.
3. Crear `src/java/Filter/EncodingFilter.java` para UTF-8. No crear
   `AuthenticationFilter` ni `AdminAuthorizationFilter` antes de Fase 8.
4. Crear solo los soportes necesarios
   `src/java/Util/DAOExceptionHttpMapper.java` y
   `src/java/Util/RequestValidator.java`; `src/java/Servlets/` debe conservar
   exclusivamente clases Servlet. No diseñar framework,
   Service vacío, DTO vacío ni controlador genérico.
5. Ajustar mínimamente `web/Index.jsp` para enlazar `/inicio` y `/productos` y
   consumir el atributo `productos`. No modificar las vistas de login, registro
   o administración en 7B.
6. Crear una vista única `web/WEB-INF/error.jsp` y configurar, si las pruebas lo
   exigen, `web/WEB-INF/web.xml` solo para welcome/error. Las rutas Servlet se
   declararán con una única estrategia, preferiblemente anotaciones, sin
   duplicarlas en XML.
7. Implementar un único punto de traducción de las siete categorías
   `DAOErrorType`; garantizar `return` tras cada respuesta y ausencia de SQL,
   causa y stack trace en navegador.
8. Añadir pruebas unitarias bajo `test/Servlets/` para validación y mapeo de
   errores, y pruebas HTTP para GET `/inicio`, GET `/productos`, UTF-8, status,
   tipo de contenido, 404 y error DAO sanitizado. Ejecutar Ant solo porque 7B
   sí cambiaría código.
9. Usar lectura contra el estado existente para la prueba de integración. 7B
   no necesita insertar datos. Si se autoriza un fixture de producto, debe ser
   identificable, ejecutarse en transacción y terminar en rollback con conteos
   inicial/final iguales.
10. Aceptar 7B solo con compilación y pruebas exitosas, una ruta por contrato,
    integración real `ProductosServlet -> ProductosDAO`, UTF-8 central,
    respuestas únicas, errores seguros, sin sesión provisional y árbol limpio.
11. Autorizar exactamente los archivos de los pasos 2 a 6, sus pruebas,
    `docs/auditorias/INFORME_FASE7B.md` y `docs/02_PROJECT_STATE.md`. DAO,
    Models, SQL, migraciones, JavaScript, CSS, credenciales y configuración de
    GlassFish/XAMPP seguirán fuera de alcance.
12. Hacer commits pequeños por infraestructura, controladores/pruebas y cierre.
    Ante fallo, revertir el commit local afectado; cualquier fixture se revierte
    antes de cerrar. No alterar esquema, recursos JNDI ni servicios para
    recuperar 7B.

## 13. Cierre de 7A

- Servlets auditados: 0 existentes; 10 controladores lógicos requeridos.
- Implementación propuesta para 7B: 2 Servlets públicos; 8 diferidos por Fases
  8 a 11.
- Rutas Servlet actuales: 0; rutas duplicadas: 0; destinos de formulario
  ausentes: 2.
- Formularios: 2, ambos con POST semánticamente correcto.
- Java productivo, DAO, Models, base, migraciones, JSP, recursos web,
  `web.xml`, servicios y configuración: no modificados.
- Compilación, despliegue, HTTP, CRUD y pruebas de conexión: no repetidos por
  ser ajenos al alcance estático de 7A.
- Archivos autorizados modificados: este informe y
  `docs/02_PROJECT_STATE.md`.
- Commit local de cierre: `docs(web): auditar y planificar fase 7a`.
- No se crearon auxiliares temporales, no se hizo push y no se abrió PR.

Es seguro **preparar** Fase 7B con las decisiones de la sección 11, pero no
iniciarla automáticamente. Las rutas protegidas y los flujos de escritura
continúan bloqueados hasta sus fases autoritativas.
