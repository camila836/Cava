# PROJECT_STATE.md

# Estado real del proyecto CAVA

> **Última revisión documental:** julio de 2026  
> **Regla:** registrar únicamente información comprobada.  
> **Importante:** “existe” no significa “validado”.

---

## 1. Resumen

CAVA cerró la Fase 3 de conexión y despliegue el 18 de julio de 2026.

El proyecto fue reiniciado utilizando como base los Models. Posteriormente se incorporaron DAO y clases de conexión. La cadena MariaDB → `cava` → `cava_app` → driver común → `CavaPool` → `jdbc/CavaDS` → Cava quedó validada de forma reproducible, incluidos despliegue, consulta real y prueba posterior al reinicio de GlassFish. Los CRUD completos continúan pendientes.

La Fase 4 — Consolidación de base de datos permanece pendiente de autorización. No deben iniciarse funcionalidades nuevas dentro de este cierre.

---

## 2. Entorno observado

| Componente | Estado conocido |
|---|---|
| Proyecto web NetBeans con Ant | Confirmado |
| Java 17 | Confirmado |
| Jakarta EE 10 Web | Confirmado |
| Eclipse GlassFish 7.0.9 | Confirmado por `asadmin`; la carpeta local `glassfish-7.0.25` tiene nombre legado |
| MariaDB 10.4.32 mediante XAMPP | Confirmado en puerto 3306 |
| Servlets funcionales | No encontrados |
| JSP | Existen |
| JavaScript | Existe |
| Bootstrap | Debe confirmarse uso real |

### Cambio de ubicación del proyecto

Después de las validaciones de compilación de la Fase 2, el proyecto fue movido
desde `C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava` a:

`C:\Users\Maria Camila R\Documents\Cava\Cava`

El traslado aisló CAVA del repositorio Git accidental detectado en el perfil de
Windows. La carpeta interna que contiene al mismo nivel `build.xml`, `src/`,
`web/`, `database/`, `docs/`, `prompts/` y `nbproject/` es la raíz real del
proyecto. Este cambio de ubicación no modifica el cierre técnico de la Fase 2.
Las rutas anteriores conservadas en salidas históricas de Ant son evidencia de
ejecuciones previas al traslado y no representan la ubicación actual. No se
recompiló el proyecto durante esta limpieza posterior.

---

## 3. Componentes existentes

### Models

- Existen 15 Models.
- Deben auditarse contra el esquema SQL.
- Deben revisarse tipos monetarios.
- Algunos valores usan `double` y deben evaluarse para `BigDecimal`.

### DAO

- Existen 15 DAO.
- Los archivos están físicamente en `src/java/Controlador/`.
- **Decisión aprobada:** en vez de mover los archivos a una carpeta `DAO`,
  se corrige la declaración `package DAO;` → `package Controlador;` en los
  15 archivos, para que el paquete coincida con la ubicación física real.
  `Controlador` queda como el paquete oficial de la capa DAO.
- Corrección aplicada: los 15 DAO declaran `package Controlador;`.
- Búsqueda global realizada: no quedan `package DAO;` ni `import DAO.*;`.
- Deben probarse uno por uno.
- Su manejo de errores debe revisarse.
- Confirmado: usan `Conexion.getConn()`, `PreparedStatement` y
  `try-with-resources`. Pendiente: varios usan `double`/`setDouble` para
  precios (Productos, Inventario, Pagos, PedidosCabeza, PedidosDetalle) en
  vez de `BigDecimal`.

### Conexión

- Existe una clase `Conexion`.
- Existe una clase `ConexionPool`.
- Existe referencia al recurso `jdbc/CavaDS`.
- Existe configuración de GlassFish.
- No se encontró `DriverManager`.
- Los nombres JNDI observados parecen coincidir.
- La conexión todavía no debe considerarse validada de manera reproducible.

### Frontend

- Existen JSP principales.
- **Verificado contra el ZIP real (julio 2026):** `src/java/Servlets/` está vacía. No existen copias de JSP dentro de ella. El hallazgo anterior sobre "copias duplicadas de JSP" ya no aplica y se elimina de los pendientes.
- `src/java/Servlets/` se conserva como ubicación oficial de las futuras clases Servlet, que deberán declarar `package Servlets;`.
- Existen CSS y JavaScript.
- El panel administrativo contiene o contenía datos simulados.
- Deben revisarse rutas y referencias de recursos.

### Base de datos

- Existe un script principal.
- Existen migraciones adicionales.
- No existe todavía un único estado oficial consolidado.
- `database/cava.sql` es el único script principal canónico.
- La copia residual cuyo nombre contenía un espacio antes de `.sql` fue
  eliminada el 17 de julio de 2026, después de comprobar el mismo tamaño,
  SHA-256 y contenido byte a byte que el archivo canónico.
- No quedan referencias al nombre anterior ni duplicados SQL conocidos dentro
  del alcance de la Fase 2.
- Debe definirse la primera versión del esquema.

---

## 4. Hallazgos confirmados

### Alta prioridad

- ~~DAO con paquete declarado (`DAO`) que no coincide con su carpeta física (`Controlador`).~~ **Resuelto:** los 15 archivos en `src/java/Controlador/` ya declaran `package Controlador;`.
- ~~JSP duplicados dentro de `src/java`.~~ Descartado: verificado que `src/java/Servlets/` está vacía.
- Ausencia de Servlets funcionales — confirmado, la carpeta está vacía (pendiente para Fase 7, fuera de alcance de Fase 2).
- **Confirmado:** 0 imports `import DAO.*` en todo el proyecto.
- **Confirmado:** 0 usos de `DriverManager` en el código.
- ~~Copia ambigua del script canónico.~~ **Resuelto:** la copia residual fue
  eliminada con respaldo externo verificable; `database/cava.sql` conserva su
  SHA-256 original.
- Existen 4 migraciones sueltas sin numeración ni orden (`cava_migracion_fidelidad.sql`, `cava_migracion_favoritos.sql`, `cava_migracion_verificacion.sql`, `cava_migracion_resenas.sql`) — no siguen el esquema `V00X__nombre.sql` de `docs/04_DATABASE.md` §4.
- Conexión sin validación reproducible.
- Uso de `root` sin contraseña como configuración observada.
- Driver MySQL antiguo.
- Librerías duplicadas.
- Uso de `double` para dinero o cantidades exactas.
- DAO que ocultan errores devolviendo `false`.
- Scripts SQL no consolidados.

### Prioridad media

- Revisar rutas locales de NetBeans.
- Revisar referencias a recursos inexistentes.
- Revisar uso real de Bootstrap.
- Definir estrategia de logging.
- Definir excepciones.
- Definir eliminación física o lógica.
- Revisar migraciones adicionales.

---

## 5. Estado por área

| Área | Estado |
|---|---|
| Respaldo | Copia externa verificable creada antes de eliminar el SQL residual |
| Auditoría inicial | Realizada de forma estática |
| Estructura | Fase 2 cerrada: paquetes y rutas Java corregidos, duplicado SQL eliminado y compilación validada |
| Conexión | Fase 3 cerrada; JNDI global, pool, WAR, despliegue y consulta real validados tras reinicio |
| Base de datos | Requiere consolidación |
| Models | Existen, requieren auditoría |
| DAO | Los 15 compilan; requieren auditoría funcional y pruebas de persistencia |
| Servlets | No implementados |
| Autenticación | No implementada |
| Seguridad | No implementada |
| Dashboard real | No implementado |
| Tienda conectada | No implementada |
| Pruebas | No consolidadas |
| Entrega | No iniciada |

---

## 6. Fase oficial en curso

La Fase 2 quedó formalmente **CERRADA** el 17 de julio de 2026. La copia SQL
residual fue eliminada, `database/cava.sql` quedó como único script principal
canónico, no quedan referencias al nombre anterior y la estructura no presenta
duplicados conocidos dentro del alcance de esta fase.

La Fase 3 fue autorizada el 17 de julio de 2026 y quedó formalmente **CERRADA**
el 18 de julio de 2026. Los Pasos 4A y 4B pertenecen a esta fase y quedaron
completados: recursos globales, alias cifrado, WAR limpio, despliegue, ping,
consulta real y persistencia posterior al reinicio fueron demostrados.

```text
Fase 3 — Conexión y despliegue
```

`prompts/FASE3_VALIDACION_CONEXION.md` queda conservado como instrucción
histórica completada. La evidencia detallada y el cierre autoritativo se
encuentran en `docs/auditorias/INFORME_FASE3.md` y
`docs/auditorias/evidencias/fase3/`.

---

## 7. Conexión: criterio de cierre

La conexión queda **VALIDADA** con la siguiente evidencia demostrada:

- [x] El WAR se genera.
- [x] El WAR no empaqueta el driver; el runtime usa la copia común de GlassFish.
- [x] GlassFish despliega.
- [x] `CavaPool` se registra como recurso global.
- [x] `jdbc/CavaDS` resuelve como recurso global.
- [x] `SELECT 1` y el ping funcionan.
- [x] `Conexion.getConn()` ejecuta `SELECT COUNT(*) FROM productos`.
- [x] La conexión persiste después de reiniciar MariaDB y GlassFish.
- [ ] La prueba negativa con credenciales inválidas no se repitió durante el cierre documental porque las credenciales quedaron fuera del alcance.
- [x] No hay `DriverManager`. Verificado en el código actual.
- [x] El driver común de `domain1/lib` se conserva intacto y documentado.
- [x] No se requieren pasos manuales ocultos.
- [x] Se guardaron logs sanitizados y evidencias con SHA-256.

Estado actual:

```text
FASE 3 CERRADA; FASE 4 PENDIENTE DE AUTORIZACIÓN
```

---

## 8. Base de datos: criterio de cierre

- [ ] Script inicial oficial.
- [ ] Migraciones ordenadas.
- [ ] Base creada desde cero.
- [ ] Datos iniciales reproducibles.
- [ ] Claves foráneas validadas.
- [ ] Índices revisados.
- [ ] Tipos comparados con Models.
- [ ] Dinero migrado a `DECIMAL` y `BigDecimal`.
- [ ] Matriz de correspondencia terminada.

Estado actual:

```text
PENDIENTE
```

---

## 9. DAO: criterio de cierre

- [x] 15 DAO con `package Controlador;` (coincidente con su ubicación física), sin referencias residuales a `package DAO;` ni `import DAO.*;`. Verificado.
- [x] Todos compilan. **Evidencia (17 de julio de 2026):** Ant 1.10.14 de
  NetBeans 20 ejecutó `clean` y `compile`; se compilaron 33 fuentes y ambos
  comandos terminaron con `BUILD SUCCESSFUL`, sin errores ni advertencias.
  Salida completa en `docs/auditorias/INFORME_FASE2.md`.
- [ ] Todos usan `Conexion.getConn()`.
- [ ] Todos usan `PreparedStatement`.
- [ ] Todos cierran recursos.
- [ ] Todos mapean correctamente.
- [ ] CRUD real probado.
- [ ] Errores diferenciados.
- [ ] Transacciones definidas.
- [ ] No hay SQL fuera de DAO.

Estado actual:

```text
COMPILACIÓN VALIDADA; PENDIENTE DE AUDITORÍA FUNCIONAL Y PRUEBAS
```

---

## 10. Decisiones pendientes

- Versión final del driver MySQL.
- Ubicación única de dependencias.
- Usuario MySQL de desarrollo.
- Estrategia de excepciones.
- Librería de logging.
- Algoritmo de hash.
- Estrategia CSRF.
- Uso de Services.
- ~~Convención SQL definitiva.~~ Resuelto: camelCase confirmado contra el esquema real (ver `docs/04_DATABASE.md` §5).
- Política de eliminación.
- Tablas de la primera versión.
- Formato estándar de JSON.
- Ubicación de JSP protegidos.

---

## 11. Historial

### Auditoría inicial

Se revisó estáticamente el proyecto entregado y se identificaron problemas de estructura, conexión pendiente de prueba, tipos monetarios, dependencias duplicadas y falta de Servlets.

### Documentación base

Se creó una arquitectura objetivo y un plan de fases.

### Cierre de compilación de Fase 2 — 17 de julio de 2026

- `clean`: `BUILD SUCCESSFUL`.
- `compile`: 33 fuentes compilados; `BUILD SUCCESSFUL`.
- Errores: ninguno.
- Advertencias: ninguna.
- No fue necesario modificar Java.
- La evidencia completa quedó registrada en
  `docs/auditorias/INFORME_FASE2.md`.
- Se documentó la copia SQL residual sin modificarla.

### Cierre final de la Fase 2 — 17 de julio de 2026

- Se creó un respaldo externo verificable antes de eliminar la copia SQL
  residual.
- Se comprobó que ambos SQL medían 6015 bytes, compartían SHA-256
  `4297AA85AB4ED9CDBCB74743730B3B47196BFB0DA22AFDA9A3E0A8342ABCA023`
  y eran idénticos byte a byte.
- Se eliminó únicamente la copia residual; `database/cava.sql` conserva el
  mismo SHA-256 y queda como único script principal canónico.
- La búsqueda global terminó sin referencias al nombre anterior.
- Las 4 migraciones y los 33 archivos Java conservaron sus huellas previas.
- `clean`: `BUILD SUCCESSFUL`.
- `compile`: 33 fuentes compiladas; `BUILD SUCCESSFUL`.
- Errores: ninguno.
- Advertencias: ninguna.
- Fase 2: **CERRADA**.
- Fase 3: **NO INICIADA**; requiere autorización explícita.

### Limpieza posterior al cierre — 17 de julio de 2026

- Se creó `.gitignore` en la raíz real con reglas para `build/`, `dist/`,
  `nbproject/private/`, archivos `.class` y logs.
- Los prompts de Fase 2 quedaron archivados exclusivamente en
  `prompts/completadas/`, sin modificar su contenido funcional.
- La búsqueda global no encontró referencias activas a las antiguas rutas de
  los prompts en la raíz.
- `build/` fue verificada como salida generada por Ant: contenía 33 clases
  compiladas, un manifiesto y 13 copias idénticas de archivos de `web/`, sin
  Java, SQL, Markdown ni contenido único. La carpeta fue eliminada por completo
  y queda ignorada para futuras compilaciones.
- No se ejecutaron `clean`, `compile`, generación de WAR, despliegue ni pruebas
  de conexión.
- La Fase 2 continúa **CERRADA** y la Fase 3 continúa **NO INICIADA**.

### Inicio de la Fase 3 — Paso 1 — 17 de julio de 2026

- La versión oficial del servidor fue confirmada mediante `asadmin` como
  Eclipse GlassFish **7.0.9**. El segmento `glassfish-7.0.25` de la ruta de
  instalación es únicamente un nombre legado.
- `domain1` está activo y los puertos 4848, 8080 y 8181 están escuchando bajo
  el mismo proceso Java.
- Se completó la auditoría previa estática de conexión, JNDI, dependencias y
  configuración, sin generar WAR ni modificar Java, JAR o el dominio.
- Se detectó una copia manual de Connector/J en `domain1/lib`; permanece sin
  cambios y debe resolverse de forma controlada antes de considerar portable el
  despliegue.
- El dominio activo no tiene registrados actualmente `CavaPool` ni
  `jdbc/CavaDS`, y la aplicación registrada apunta a una carpeta `build/web/`
  que ya no existe.
- La evidencia completa quedó registrada en
  `docs/auditorias/INFORME_FASE3.md`.
- La Fase 2 permanece **CERRADA** y la Fase 3 queda **EN PROGRESO**.

---

## 12. Regla de actualización

Cada tarea cerrada debe actualizar:

- estado;
- evidencia;
- archivos modificados;
- pruebas;
- pendientes;
- siguiente paso.

Nunca se debe marcar un componente como terminado únicamente porque existe un archivo con ese nombre.
