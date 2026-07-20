# Informe de Fase 7B - Implementación y validación de infraestructura web pública

**FASE 7 CERRADA - FASE 8 NO INICIADA**

## 1. Alcance y punto de partida

La implementación partió del cierre de 7A
`82e573c526be00160ea932d533818ab655d3d6cd` en la rama local
`feature/fase-7b-infraestructura-web`. El árbol estaba limpio; `main` y
`origin/main` permanecían en `607445d4decd1c70b5db5b77e27bdc8ef0709ad8`.

Se implementaron únicamente infraestructura web pública, `InicioServlet`,
`ProductosServlet`, navegación HTML/JSP, lectura por `ProductosDAO`, manejo
seguro de errores y pruebas. No se implementaron login, registro, sesiones,
roles, administración, CRUD web, JSON, inventario, pedidos, pagos, envíos ni
funcionalidades de Fases 8 a 12.

Checkpoints de implementación y pruebas:

| Commit | Mensaje |
| --- | --- |
| `8139165` | `feat(web): crear infraestructura publica y manejo de errores` |
| `aaa5e97` | `test(web): validar servlets y contratos http` |
| cierre documental | `docs(web): cerrar fase 7` |

No se hizo push ni se abrió PR.

## 2. Archivos creados y modificados

### Producción

- `src/java/Servlets/InicioServlet.java`.
- `src/java/Servlets/ProductosServlet.java`.
- `src/java/Filter/EncodingFilter.java`.
- `src/java/Filter/WebErrorFilter.java`.
- `src/java/Util/WebErrorDescriptor.java`.
- `src/java/Util/WebErrorMapper.java`.
- `web/Productos.jsp`.
- `web/WEB-INF/views/error.jsp`.
- `web/Index.jsp`.
- `web/WEB-INF/web.xml`.
- `web/css/Style.css`.

### Pruebas

- `test/Servlets/PublicServletContractTest.java`.
- `test/Util/WebErrorMapperTest.java`.

### Documentación

- `docs/auditorias/INFORME_FASE7B.md`.
- `docs/03_ARCHITECTURE.md`.
- `docs/02_PROJECT_STATE.md`.
- `docs/05_ROADMAP.md`.

`ProductosDAO` no fue modificado: su método validado `listarTodos()` ofrece la
proyección explícita necesaria y permite devolver una lista vacía.

## 3. Rutas y contratos finales

| Ruta | Método admitido | Resultado |
| --- | --- | --- |
| `/Cava/` | GET | El `welcome-file` `inicio` resuelve la entrada pública y responde 200. |
| `/Cava/inicio` | GET | `InicioServlet` hace forward interno a `/Index.jsp`; no consulta DAO ni crea sesión. |
| `/Cava/productos` | GET | `ProductosServlet` ejecuta `ProductosDAO.listarTodos()`, establece `productos` y hace forward a `/Productos.jsp`. |
| `/Cava/inicio` | POST y otros no implementados | 405 mediante el comportamiento estándar de `HttpServlet`. |
| `/Cava/productos` | POST y otros no implementados | 405; no existe CRUD ni respuesta JSON. |
| ruta inexistente | cualquiera | 404 con vista común segura. |

No hay parámetros de petición en los dos endpoints. Por tanto no se creó un
validador vacío o especulativo. El resultado de productos puede contener cero
elementos; la JSP muestra un estado vacío comprensible con HTTP 200.

Los Servlets se registran únicamente con `@WebServlet`. Los filtros y el
`welcome-file` se ordenan en `web.xml`; no hay mapeos Servlet duplicados.

## 4. Separación de responsabilidades

- DAO: `ProductosDAO` conserva exclusivamente SQL, conexión, mapeo y traducción
  a `DAOException`.
- Servlet: recibe GET, invoca DAO, establece `productos` y elige el forward. No
  contiene SQL, HTML, credenciales ni reglas de seguridad.
- JSP: renderiza con EL/JSTL, escapa datos mediante `c:out` y presenta lista o
  estado vacío. Las vistas públicas declaran `session="false"`.
- JavaScript: no se agregó JavaScript ni AJAX. Inicio y productos usan enlaces
  tradicionales; la referencia al JS inexistente se retiró. Las interacciones
  de carrito y módulos futuros permanecen diferidas a Fase 11.

`Index.jsp` corrigió dentro del alcance las rutas de CSS, video, inicio,
productos, login y registro. El enlace mal escrito `RegistrarUsusario.jsp` fue
reemplazado por el archivo real `RegistrarUsuario.jsp`. No se modificaron las
JSP de login, registro o administración.

## 5. UTF-8, errores y logging

`EncodingFilter` se ejecuta antes de `WebErrorFilter` para `/inicio` y
`/productos`. Configura request y response en UTF-8 sin imponer un tipo de
contenido a recursos estáticos.

`WebErrorFilter` captura fallos no manejados antes de comprometer la respuesta,
los traduce con `WebErrorMapper`, fija el status y hace forward interno a
`/WEB-INF/views/error.jsp`. Si la respuesta ya está comprometida, no intenta un
segundo forward.

| Error | HTTP |
| --- | ---: |
| `INVALID_DATA` | 400 |
| `NOT_FOUND` | 404 |
| `DUPLICATE` | 409 |
| `FOREIGN_KEY` | 409 |
| `OPERATION_NOT_ALLOWED` | 409 |
| `CONNECTION` | 503 |
| `SQL_ERROR` | 500 |
| inesperado | 500 |

La vista recibe solo `errorStatus`, `errorTitle` y `errorMessage` seguros. No
muestra excepción, SQL, SQLState, clase, stack trace, ruta del sistema ni
credenciales. Los errores se registran una sola vez en la frontera web con
`java.util.logging.Logger`; no se agregaron dependencias ni usos productivos de
`System.out` o `System.err`.

## 6. Pruebas por bloque

### Bloque 1 - infraestructura

- Ant `clean compile compile-test`: `BUILD SUCCESSFUL`.
- 40 fuentes productivas y 4 pruebas compiladas en ese punto.
- `WebErrorMapperTest`: siete categorías DAO, error inesperado, excepción
  envuelta y ausencia de detalles internos, exitosos.

### Bloque 2 - navegación pública

- Ant `compile compile-test compile-jsps`: `BUILD SUCCESSFUL`.
- `PublicServletContractTest`: anotaciones, rutas, forwards, lista vacía,
  ausencia de sesión y UTF-8 antes de la cadena, exitosos.

### Validación final

- Ant `clean compile compile-test dist`: `BUILD SUCCESSFUL`.
- 42 fuentes productivas y 5 pruebas compiladas.
- Pruebas ejecutadas: `SQLExceptionTranslatorTest`, `BigDecimalModelsTest`,
  `WebErrorMapperTest` y `PublicServletContractTest`, todas exitosas.
- WAR: `dist/Cava.war`, 6.354.816 bytes.
- SHA-256 del WAR:
  `84E2D2E19898F13E666B006D8C8E0B9F6512219AA5BC943B30BEDB6C10ABDFC3`.
- Despliegue administrado con `asadmin deploy --force=true`: exitoso, sin
  reiniciar GlassFish.

## 7. Resultados HTTP

| Prueba | Resultado |
| --- | --- |
| `GET /Cava/` | 200, `text/html; charset=UTF-8` |
| `GET /Cava/inicio` | 200, `text/html; charset=UTF-8` |
| `GET /Cava/productos` | 200, `text/html; charset=UTF-8`, estado vacío presente |
| `POST /Cava/inicio` | 405, vista segura |
| `POST /Cava/productos` | 405, vista segura |
| `GET /Cava/no-existe-fase7b` | 404, vista segura |
| `HEAD /Cava/css/Style.css` | 200 |
| `HEAD /Cava/Videos/CAVA.mp4` | 200 |

Las respuestas 404/405 se inspeccionaron sin coincidencias de `SQLException`,
stack trace, clases Java o rutas locales. `CONNECTION -> 503` y error inesperado
`-> 500` fueron simulados unitariamente, sin detener MariaDB ni alterar el pool.

## 8. Integración de solo lectura

- Una sola instancia GlassFish atendió 4848, 8080 y 8181 con el mismo PID.
- `CavaPool`: ping exitoso.
- `jdbc/CavaDS`: listado como recurso global.
- `/productos` ejecutó la lectura real `ProductosDAO.listarTodos()` mediante
  `Conexion.getConn()` y respondió 200 con lista vacía.
- Una sonda temporal externa al repositorio ejecutó solo `SELECT` mediante el
  mismo `jdbc/CavaDS` y devolvió `CAVA_READ_ONLY tables=15 rows=0`.
- La sonda se retiró del servidor y su directorio temporal fue eliminado.

No se ejecutó INSERT, UPDATE, DELETE, DDL ni migración. No se alteraron
`CavaPool`, `jdbc/CavaDS`, `domain.xml`, puertos, credenciales, usuarios o
privilegios.

## 9. Escaneos y límites finales

- 0 imports `javax.servlet` en el alcance nuevo.
- 0 usos nuevos de `System.out` o `System.err`.
- 0 patrones de secretos en archivos modificados.
- 0 cambios en DAO, Models, SQL, migraciones o configuración de servicios.
- Sin auxiliares temporales al cierre.
- Login, registro, sesiones, roles, CSRF, administración y rutas protegidas:
  Fase 8 o posteriores.
- Primer CRUD y contrato JSON: Fase 9.
- Extensión del panel: Fase 10.
- Catálogo interactivo, carrito, pedido e historial: Fase 11.

La Fase 7 queda técnicamente lista para integración. La Fase 8 no fue iniciada.
