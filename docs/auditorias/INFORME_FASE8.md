# Informe de Fase 8 — Autenticación y seguridad

## Alcance cerrado

Se implementaron registro mínimo, validación, correo único, PBKDF2, login,
sesión, logout, renovación de sesión, CSRF y autorización administrativa. No se
inició perfil, facturación, envío, DIVIPOLA, recuperación, recordarme, API ni
Fase 9.

## Base de datos

- Respaldo previo externo: `CAVA_Backups/Fase8_20260720-211514/cava_pre_fase8.sql`.
- Tamaño: 15.824 bytes; SHA-256:
  `0A77AE8EA48EFEB83EB8D0063AE8FBC19E2BD57E0D38AA43AD4B1C9881A90FEC`.
- Migración: `database/migrations/F008__autenticacion_base.sql`.
- Se validaron restauración del respaldo, migración temporal, instalación
  nueva, reejecución de semillas, columnas e índices equivalentes.
- `roles.codigoRol` es obligatorio y único; roles finales: `CLIENTE` y
  `ADMINISTRADOR`.
- Documento y ciudad son opcionales; el UNIQUE compuesto y
  `chkUsuariosDocumentoCompleto` fueron almacenados y aplicados por MariaDB.
- F008 se aplicó a `cava`: 15 tablas, dos roles y cero usuarios al cierre.
- V001–V004 no fueron editadas ni ejecutadas.

## Autenticación y sesiones

- `GET/POST /registro`, formulario bajo `WEB-INF`, validación autoritativa y
  Post/Redirect/Get.
- El servidor rechaza parámetros de rol y resuelve `CLIENTE` por `codigoRol`.
- Contraseña de 15–128 caracteres, sin normalización, derivada con
  PBKDF2WithHmacSHA256, 600.000 iteraciones, salt aleatorio de 16 bytes y clave
  de 256 bits. El formato autocontenido cabe en `VARCHAR(255)`.
- `GET/POST /login` usa mensaje idéntico para inexistente, inactivo o clave
  incorrecta y ejecuta PBKDF2 ficticio cuando el correo no existe.
- La sesión guarda solo ID, nombre, `codigoRol` e instante; el login ejecuta
  `changeSessionId()` y rota CSRF.
- Timeout 30 minutos; cookie `HttpOnly`, `SameSite=Lax`, sin `Secure` en HTTP
  local y seguimiento exclusivamente por cookie.
- `POST /logout` exige CSRF e invalida la sesión. `GET /logout` responde 405.
- `/admin` se sirve desde `WEB-INF` y requiere `ADMINISTRADOR`; anónimo va a
  login y CLIENTE recibe 403.

## Medición PBKDF2

JDK de GlassFish: 17.0.14. Tras dos calentamientos se midieron cinco rondas:

- generación: 1.631,88–2.757,33 ms;
- verificación válida: 1.700,77–2.767,33 ms;
- rechazo inválido: 1.420,50–2.084,31 ms.

Se conservaron 600.000 iteraciones. Los tiempos son aceptados para desarrollo
local y deben revalidarse en el hardware de publicación sin debilitar el
algoritmo.

## Pruebas

- `clean`, `compile`, `compile-test` y `dist`: `BUILD SUCCESSFUL`.
- Suites unitarias: PBKDF2, validación, contratos web, login neutro, cambio de
  sesión, registro, logout, autorización y regresiones de Fases 5–7.
- HTTP real: registro 200→`/login`, login 200→`/inicio`, cambio de JSESSIONID,
  CLIENTE→admin 403, logout→`/inicio`, admin posterior→`/login`, CSRF inválido
  400, GET logout 405, ruta inexistente 404 y CSS 200.
- Dos usuarios sintéticos fueron CLIENTE, usaron formato PBKDF2 y campos de
  perfil NULL; se eliminaron selectivamente y el conteo final fue cero.
- El WAR se desplegó correctamente y `CavaPool` continuó operativo.

## Promoción administrativa futura

La promoción se realizará solo localmente sobre una cuenta real existente y
con privilegios administrativos. Debe iniciar transacción, resolver el rol con
`SELECT idRoles FROM roles WHERE codigoRol='ADMINISTRADOR'`, actualizar la
cuenta identificada por un correo introducido localmente, verificar el
`codigoRol` mediante JOIN y confirmar. No se modifica la contraseña, no se usa
un ID fijo y no existe endpoint web para cambiar roles. No se ejecutó ninguna
promoción en la base real durante esta fase.

## Riesgo aceptado y pendientes

La autenticación todavía no limita intentos fallidos. Existe riesgo de fuerza
bruta mientras CAVA permanezca sin rate limiting. Este control debe
implementarse y probarse antes de cualquier exposición pública o paso a
producción.

El riesgo se acepta temporalmente solo para desarrollo local. Recuperación,
MFA, correo de verificación, perfil, facturación, envío y catálogo DIVIPOLA
permanecen fuera de alcance.
