# Informe de Fase 9 — Primer CRUD vertical

## Alcance cerrado

Se implementó el primer CRUD vertical sobre `unidadesMedida` con el flujo
`JSP admin -> JavaScript -> Servlet -> DAO -> MariaDB -> JSON -> DOM`. No se
extendió el panel a otros catálogos ni se inició la Fase 10.

## Base de datos y F009

- `F009__unidades_medida_unicas.sql` fue inspeccionada antes de ejecutarse: su
  único cambio estructural es la restricción UNIQUE de
  `unidadesMedida.descripcionUnidadesMed`; no contiene DML, `DROP` ni
  `TRUNCATE`.
- La columna real usa `utf8mb4_unicode_ci`. Antes de migrar había cero filas y
  cero grupos duplicados bajo esa colación.
- Se ejecutó exclusivamente F009 mediante el cliente MariaDB con contraseña
  oculta. No se reejecutaron el instalador, F008 ni V001–V004.
- `information_schema` y `SHOW CREATE TABLE unidadesMedida` confirmaron
  `UNIQUE KEY uqUnidadesMedidaDescripcion (descripcionUnidadesMed)`.
- Permanecieron 15 tablas, 14 claves foráneas, dos roles, las restricciones de
  F008 y los recuentos previos de las 15 tablas.

## Implementación

- `UnidadesMedidaDAO` ofrece alta, consulta, listado ordenado, actualización y
  eliminación con claves generadas, conexión propia o compartida y traducción
  uniforme de errores `DUPLICATE`, `NOT_FOUND` y `FOREIGN_KEY`.
- `UnidadesMedidaServlet` publica JSON en `/admin/unidades-medida`, admite
  GET/POST/PUT/DELETE, valida allowlists, ID positivo, texto obligatorio de
  máximo 45 caracteres, JSON, CSRF y errores 400/404/405/409/503.
- DELETE usa `?id=` y cabecera CSRF sin cuerpo: GlassFish/Grizzly 7.0.9 rechaza
  cuerpos DELETE antes de invocar el servlet.
- La vista administrativa carga el módulo asíncrono, estados vacío/carga/error,
  formulario crear/editar, confirmación de borrado y render mediante
  `textContent`, sin `innerHTML`.

## Pruebas

- Integración DAO real en GlassFish: claves generadas, listar/consultar,
  duplicado insensible a mayúsculas por colación, edición, inexistentes,
  conflicto FK, eliminación, conexión reutilizable y rollback. Conteo final:
  cero unidades, productos y categorías sintéticos.
- HTTP real: anónimo 302, CLIENTE 403, ADMINISTRADOR 200, lista vacía 200,
  crear 201, consultar/listar 200, duplicado 409, editar 200, inexistente 404,
  validaciones 400, método no permitido 405, FK 409, eliminar 200 y repetición
  404. La limpieza final confirmó cero registros sintéticos.
- DOM controlado: carga, creación, edición, borrado, mensajes, estado vacío,
  caracteres especiales como texto y contrato DELETE/CSRF. El navegador
  integrado no pudo abrir localhost por política empresarial; no se intentó
  eludirla y se cubrió el comportamiento con HTTP real más DOM simulado.
- `clean` y `compile-test`: `BUILD SUCCESSFUL`; 56 fuentes de producto y 17 de
  prueba compiladas. Pasaron 14 suites Java ejecutables, la prueba DOM y
  `node --check`.
- `dist`: `BUILD SUCCESSFUL`. SHA-256 del WAR final:
  `79EEB6A8F1DA8265415A1A51A2597B940FE0C253EDE7A01C0B16430DDFE01477`.
- Despliegue final correcto; `/`, `/inicio`, `/productos`, `/login` y
  `/registro` respondieron 200; CSS y JavaScript 200; rutas temporales 404;
  `ping-connection-pool CavaPool` fue satisfactorio.

## Higiene y límites

- El servlet de auditoría F009 y el ejecutor runtime fueron temporales y no
  están en fuente, WAR ni commit final.
- `build/fase9-test-classes/WEB-INF/classes/.keep` no existe y no pertenece al
  código fuente.
- No quedaron datos sintéticos, credenciales, cookies, tokens ni respaldos en
  el repositorio.
- No se hizo push, PR ni merge. La Fase 10 permanece no iniciada.

## Resultado

Los criterios de listar, crear, editar, eliminar, validar, manejar errores,
persistir, respetar permisos y evitar duplicación quedan aprobados. La Fase 9
queda cerrada en desarrollo local.
