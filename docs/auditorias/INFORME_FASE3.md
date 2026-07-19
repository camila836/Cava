# Informe de Fase 3 — Conexión y despliegue

## Estado

**FASE 3 CERRADA — PASOS 4A Y 4B COMPLETADOS**

- Inicio documentado: 17 de julio de 2026.
- Cierre técnico y funcional: 18 de julio de 2026.
- Los apartados 1 a 22 conservan cronológicamente diagnósticos e intentos anteriores; sus estados `AUSENTE`, `NO INICIADO` o `DETENIDO` son históricos y no describen el estado vigente.
- El apartado 23 es la referencia autoritativa del estado final.

## 1. Precondición del servidor

La instalación registrada está ubicada en:

```text
C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish
```

El nombre `glassfish-7.0.25` es legado y no representa la versión instalada.
La versión oficial reportada por `asadmin version --local` es:

```text
Eclipse GlassFish 7.0.9
```

El comando autorizado para iniciar `domain1` informó que el puerto 4848 ya
estaba ocupado. La comprobación posterior confirmó que no se trataba de otro
servidor: `domain1` ya estaba activo desde esa misma instalación. Un único
proceso Java escucha en:

- 4848: administración.
- 8080: HTTP.
- 8181: HTTPS.

`asadmin list-domains` confirmó `domain1 running`.

## 2. Entorno observado

| Componente | Evidencia |
|---|---|
| Java | 17.0.14 LTS, HotSpot 64 bits |
| Ant | 1.10.14 incluido con NetBeans 20 |
| GlassFish | Eclipse GlassFish 7.0.9 |
| Dominio | `domain1` activo |
| MySQL | Puerto 3306 escuchando mediante XAMPP |

No se ejecutó todavía una consulta SQL; que MySQL escuche en el puerto 3306 no
equivale a validar credenciales, esquema ni persistencia.

## 3. Archivos revisados

- `src/java/Conexion/Conexion.java`
- `src/java/Conexion/ConexionPool.java`
- `src/java/Pruebas/PruebaConexion.java`
- `web/WEB-INF/glassfish-resources.xml`
- `web/WEB-INF/glassfish-web.xml`
- `nbproject/project.properties`
- `nbproject/project.xml`
- `nbproject/private/private.properties`, solo como configuración local
- JAR presentes en `lib/`, `web/WEB-INF/lib/` y `domain1/lib/`

No existe `web/WEB-INF/web.xml`. La aplicación depende de anotaciones y de los
descriptores específicos de GlassFish presentes en `WEB-INF`.

## 4. Diseño de conexión

`Conexion.getConn()` delega exclusivamente en `ConexionPool.obtener()`.
`ConexionPool` obtiene un `DataSource` mediante JNDI con el nombre
`jdbc/CavaDS` y solicita la conexión al pool. No se encontraron llamadas a
`DriverManager` ni credenciales incrustadas en el código Java.

El descriptor `glassfish-resources.xml` declara:

- pool: `CavaPool`;
- recurso JNDI: `jdbc/CavaDS`;
- clase: `com.mysql.cj.jdbc.MysqlDataSource`;
- tipo: `javax.sql.DataSource`;
- servidor: `localhost`;
- puerto: `3306`;
- base de datos: `cava`;
- usuario configurado: `root`;
- contraseña: Valor sensible presente y omitido;
- SSL: desactivado;
- zona horaria: `America/Bogota`.

La contraseña no se reproduce ni se caracteriza en el informe.

`PruebaConexion.java` está vacío y no constituye evidencia de conexión.

## 5. Dependencias y Connector/J

Se encontraron estas copias relevantes:

| Ubicación | Artefacto | Tamaño | Resultado |
|---|---|---:|---|
| `lib/` | Connector/J 8.0.12 | 2.020.431 bytes | Idéntico |
| `web/WEB-INF/lib/` | Connector/J 8.0.12 | 2.020.431 bytes | Idéntico |
| `domain1/lib/` | Connector/J 8.0.12 | 2.020.431 bytes | Idéntico |
| `Cava.war!/WEB-INF/lib/` | Connector/J 8.0.12 | 2.020.431 bytes | Idéntico |

Las cuatro copias comparten el SHA-256:

```text
8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2
```

También existen copias idénticas de la implementación JSTL 3.0.1 en `lib/` y
`web/WEB-INF/lib/`. El API JSTL 3.0.0 aparece en `lib/`.

`javac.classpath` está vacío y `project.xml` no declara bibliotecas web.
Connector/J llega al WAR por su presencia física en `web/WEB-INF/lib/`, porque
el target de NetBeans copia todo `web/` a `build/web/` y luego empaqueta esa
salida. La copia raíz de `lib/` no está referenciada por la compilación ni por el
empaquetado actual.

## 6. Búsquedas globales

- No hay uso de `DriverManager` en fuentes Java.
- Los DAO usan de manera consistente `Conexion.getConn()`; se observaron 96
  ocurrencias contando llamadas y comentarios en los 15 DAO.
- `jdbc/CavaDS` y `CavaPool` son consistentes entre el código y el descriptor.
- No se encontró automatización del proyecto que copie el driver a
  `domain1/lib`.
- No se encontraron rutas locales absolutas dentro de las clases de conexión ni
  del descriptor JDBC.
- Las rutas locales en `nbproject/private/private.properties` corresponden al
  registro privado del servidor en NetBeans y están ignoradas por Git.

## 7. Estado observado del dominio

Las consultas de solo lectura a GlassFish mostraron:

- pools registrados: `__TimerPool`, `DerbyPool` y `SamplePool`;
- recursos JDBC registrados: `jdbc/sample`, `jdbc/__TimerPool` y
  `jdbc/__default`;
- `CavaPool`: no registrado;
- `jdbc/CavaDS`: no registrado;
- aplicación `Cava`: registrada como aplicación web;
- recursos con alcance de aplicación asociados al despliegue residual:
  `java:app/CavaPool` y `java:app/jdbc/CavaDS`;
- ubicación registrada de `Cava`:
  `C:/Users/Maria Camila R/Documents/Cava/Cava/build/web/`.

La carpeta `build/web/` no existe actualmente, por lo que ese registro es
residual y no demuestra un despliegue válido del estado actual del proyecto.

## 8. Hallazgos y riesgos

### Alto — instalación del driver en el dominio no reproducible

Existe una copia de Connector/J en `domain1/lib` y GlassFish la reconoce mediante
`list-libraries`. Para la arquitectura global seleccionada en el Paso 3 esta
ubicación es necesaria, pero su instalación actual no está automatizada ni
documentada como operación reproducible. No se modificó ni eliminó.

### Alto — estado híbrido de recursos JNDI

El descriptor del proyecto crea recursos con alcance de aplicación, visibles
como `java:app/CavaPool` y `java:app/jdbc/CavaDS`, pero los recursos globales
aprobados `CavaPool` y `jdbc/CavaDS` no están registrados en `domain1`. La
resolución JNDI no puede considerarse validada.

### Alto — despliegue registrado obsoleto

La aplicación `Cava` apunta a una salida expandida que ya no existe. Debe
generarse e inspeccionarse el WAR y realizarse un despliegue limpio en los pasos
posteriores.

### Medio — duplicación dentro del proyecto

Connector/J y la implementación JSTL aparecen tanto en `lib/` como en
`web/WEB-INF/lib/`. El Paso 2 confirmó que solo las copias situadas físicamente
en `web/WEB-INF/lib/` entran al WAR.

## 9. Cambios realizados

- Se corrigió la versión documental a Eclipse GlassFish 7.0.9.
- Se aclaró que `glassfish-7.0.25` es solo un nombre de carpeta legado.
- Se actualizó el estado a **Fase 3 en progreso**.
- Se creó este informe de auditoría.
- Se ejecutaron `clean` y `dist` con Ant de NetBeans 20 y se inspeccionó el WAR.
- Se creó un respaldo externo verificable de las tres copias físicas del driver
  y del WAR.

No se modificaron fuentes Java, JAR, SQL, configuración del dominio ni recursos
JDBC. No se ejecutó despliegue ni consulta SQL.

## 10. Pendientes para continuar

1. Obtener autorización antes de aplicar cualquier eliminación, reubicación o
   cambio de referencia del driver.
2. Incorporar un procedimiento reproducible para instalar/verificar Connector/J
   como biblioteca común del dominio, sin rutas absolutas versionadas.
3. Mantener `domain1/lib` mientras la arquitectura A sea la decisión aprobada.
4. Reconciliar en el Paso 4 el descriptor de recursos de aplicación con los
   recursos globales aprobados, sin crear nombres duplicados.
5. Desplegar el WAR válido y confirmar el registro y la resolución de
   `CavaPool` y `jdbc/CavaDS`.
6. Ejecutar `SELECT 1`, una operación real mediante DAO y las pruebas de error
   definidas para la fase.

Hasta completar esos puntos, la conexión sigue **PENDIENTE DE VALIDACIÓN** y la
Fase 3 permanece **EN PROGRESO**.

## 11. Paso 2 — Generación e inspección del WAR

Se ejecutó el target oficial `dist` después de `clean` mediante Ant 1.10.14 de
NetBeans 20. El resultado fue `BUILD SUCCESSFUL`, con 33 fuentes compiladas.

```text
C:\Users\Maria Camila R\Documents\Cava\Cava\dist\Cava.war
```

- Tamaño: 8.349.670 bytes.
- SHA-256:
  `228BAE832AF097B6F0BF0DA72DCD3ABD64C10C4CB4891EB5483E04E1928E2712`.
- Entradas totales: 62.
- JAR en `WEB-INF/lib`: JSTL 3.0.1 y Connector/J 8.0.12.
- Copias reales de Connector/J dentro del WAR: una.
- No existen clases MySQL desempaquetadas adicionalmente en el WAR.

## 12. Paso 3 — Verificación de las copias del driver

Las cuatro fuentes se leyeron como flujos binarios y se compararon directamente,
incluida la entrada comprimida dentro del WAR. Todas son idénticas byte a byte:

| Copia | Tamaño | SHA-256 |
|---|---:|---|
| `lib/mysql-connector-java-8.0.12.jar` | 2.020.431 | `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2` |
| `web/WEB-INF/lib/mysql-connector-java-8.0.12.jar` | 2.020.431 | `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2` |
| `domain1/lib/mysql-connector-java-8.0.12.jar` | 2.020.431 | `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2` |
| `Cava.war!/WEB-INF/lib/mysql-connector-java-8.0.12.jar` | 2.020.431 | `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2` |

## 13. Respaldo externo

Antes de autorizar cualquier cambio se creó el respaldo:

```text
C:\Users\Maria Camila R\CAVA_Backups\Fase3_Driver_Paso3_20260717-203142
```

| Archivo respaldado | Tamaño | SHA-256 |
|---|---:|---|
| `project-lib_mysql-connector-java-8.0.12.jar` | 2.020.431 | `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2` |
| `web-WEB-INF-lib_mysql-connector-java-8.0.12.jar` | 2.020.431 | `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2` |
| `domain1-lib_mysql-connector-java-8.0.12.jar` | 2.020.431 | `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2` |
| `dist_Cava.war` | 8.349.670 | `228BAE832AF097B6F0BF0DA72DCD3ABD64C10C4CB4891EB5483E04E1928E2712` |

El primer destino propuesto bajo `Documents/CAVA_Backups` no pudo crearse porque
la carpeta `Documents` está marcada como solo lectura en el entorno. No dejó
archivos parciales. El respaldo válido quedó fuera del proyecto y de GlassFish,
en la ruta indicada arriba.

## 14. Arquitectura evaluada y decisión

### Evidencia del proyecto y del dominio

- `ConexionPool` usa `InitialContext.lookup("jdbc/CavaDS")` y no usa
  `DriverManager`.
- `glassfish-resources.xml` está dentro de `WEB-INF`; según GlassFish, esa
  ubicación define recursos con alcance de aplicación.
- `list-applications --resources` confirma que el despliegue residual creó
  `java:app/CavaPool` y `java:app/jdbc/CavaDS`.
- `list-jdbc-connection-pools` y `list-jdbc-resources` confirman que no existen
  globalmente `CavaPool` ni `jdbc/CavaDS`.
- `list-libraries` confirma que Connector/J está disponible en el classloader
  común del dominio.
- El WAR contiene una copia del driver en `WEB-INF/lib`.

La documentación oficial de GlassFish establece que un
[`glassfish-resources.xml` dentro de `WEB-INF` crea recursos de aplicación](https://glassfish.org/docs/7.1.1/application-deployment-guide.html)
y que esos nombres reciben alcance `java:app` o `java:module`. También establece
que `domain-dir/lib` pertenece al classloader común. El `asadmin help
add-library` de la instalación 7.0.9 confirma localmente que `--type common`
instala en `domain-dir/lib` y requiere reiniciar el DAS en esta versión.

### Estado actual

El proyecto está en un estado híbrido:

- tiene recursos de aplicación empaquetados y un driver dentro del WAR;
- tiene además el driver en el classloader común;
- no tiene todavía los recursos globales aprobados.

La presencia de recursos `java:app/*` no demuestra que el lookup corto actual
resuelva ni que la conexión funcione. Esas pruebas pertenecen al Paso 4.

### Decisión técnica

Se selecciona la **arquitectura A — pool global administrado por GlassFish** como
arquitectura objetivo, porque la decisión técnica aprobada exige `CavaPool` y
`jdbc/CavaDS` como recursos del dominio y el código actual usa el nombre lógico
global `jdbc/CavaDS`.

Consecuencias:

- la copia de Connector/J en `domain1/lib` se conserva;
- la copia empaquetada en el WAR será innecesaria cuando los recursos globales
  estén registrados y probados;
- la copia raíz `lib/` puede actuar como fuente controlada para instalar el
  driver, pero hoy no está referenciada por Ant;
- los recursos `java:app/*` del descriptor empaquetado deben reconciliarse en el
  Paso 4 para evitar dos arquitecturas simultáneas;
- la conexión continúa pendiente de validación.

Esta decisión sustituye la hipótesis del Paso 1 que trataba `domain1/lib` como
necesariamente residual: para la arquitectura A la ubicación es correcta; el
problema real es que su instalación todavía es manual y no reproducible.

## 15. Cambio mínimo propuesto y reversión

### Cambio propuesto — no ejecutado

1. Mantener `lib/mysql-connector-java-8.0.12.jar` como fuente controlada del
   driver.
2. Añadir a `build.xml` un target explícito, fuera de `clean` y `dist`, que use
   `asadmin add-library --type common` para instalar esa fuente en el dominio.
3. Resolver `asadmin` y el dominio mediante propiedades externas de NetBeans o
   parámetros requeridos; no versionar rutas absolutas ni datos privados.
4. Hacer que el target falle de forma clara si faltan propiedades, valide el
   SHA-256 esperado y documente el reinicio obligatorio de GlassFish 7.0.9.
5. No ejecutar automáticamente el target durante compilación o despliegue.
6. No retirar todavía el driver de `WEB-INF/lib`: su retirada solo será segura
   al coordinarla con el registro global y la prueba de despliegue del Paso 4.

No existe una referencia rota, absoluta o privada al driver en
`nbproject/project.properties`; por ello no se propone modificar ese archivo.
`lib.dir` apunta a `web/WEB-INF/lib`, pero no hay bibliotecas declaradas y
`javac.classpath` está vacío. El driver entra al WAR como contenido web físico,
no como dependencia formal de NetBeans.

### Reversión prevista

- Antes de cualquier cambio se conservará el respaldo externo ya verificado.
- Si una futura instalación reproducible altera el driver del dominio, se
  detendrá `domain1`, se restaurará
  `domain1-lib_mysql-connector-java-8.0.12.jar` desde el respaldo y se reiniciará
  el dominio.
- En Windows, `asadmin remove-library` declara no estar soportado; una retirada
  futura deberá hacerse con el dominio detenido y mediante una operación de
  archivo explícita y recuperable.
- Si la transición a recursos globales falla, se conservará o restaurará el WAR
  respaldado y no se eliminará la copia empaquetada hasta recuperar el estado
  anterior.

No se ha eliminado, movido ni reemplazado ningún JAR. Tampoco se han creado los
recursos globales. Se requiere autorización explícita antes de implementar el
cambio propuesto.

## 16. Paso 4A — Diagnóstico y plan de transición a recursos globales

### 16.1 Identificación y alcance

- Fecha y hora: 17 de julio de 2026, 21:00:37, zona
  `America/Bogota`.
- Raíz comprobada:
  `C:\Users\Maria Camila R\Documents\Cava\Cava`.
- Instalación comprobada:
  `C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish`.
- Versión real: Eclipse GlassFish 7.0.9, commit
  `5860e42c2edeb9f3b46428fcf9dab95790a9a3f0`.
- El nombre `glassfish-7.0.25` sigue siendo únicamente un nombre legado.
- Alcance ejecutado: consultas, solicitudes HTTP sin acciones de negocio,
  lectura de logs, respaldo externo y planificación.

La raíz contiene al mismo nivel `build.xml`, `src/`, `web/`, `database/`,
`docs/`, `prompts/` y `nbproject/`.

### 16.2 Barrera inicial de seguridad

El único `asadmin` utilizado fue:

```text
C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat
```

Resultados:

- `domain1 running`;
- PID del DAS: 16948;
- ejecutable: `C:\Program Files\Java\jdk-17\bin\java.exe`;
- `com.sun.aas.installRoot` y `com.sun.aas.instanceRoot` apuntan a la instalación
  y al `domain1` autorizados;
- 4848, 8080 y 8181 escuchan bajo el mismo PID 16948.

No se inició, detuvo ni reinició el dominio.

### 16.3 Consultas ejecutadas

Se utilizaron, sin mutación, los siguientes mecanismos:

- `asadmin version --local`;
- `asadmin list-domains`;
- `asadmin list-applications --long`;
- `asadmin list-applications --resources`;
- `asadmin get` únicamente sobre propiedades no sensibles de `Cava`;
- `asadmin list-jdbc-connection-pools`;
- `asadmin list-jdbc-resources`;
- `asadmin list-libraries`;
- `asadmin list-password-aliases`;
- `asadmin get` sobre niveles de monitoreo;
- `asadmin get --monitor=true` para Cava y el contenedor web;
- `Get-NetTCPConnection` y `Get-CimInstance`;
- solicitudes `HEAD` con `curl.exe` sobre HTTP y HTTPS;
- lectura de `server.log` sin truncarlo ni rotarlo;
- lectura de código, descriptores y configuración de NetBeans;
- `Get-FileHash -Algorithm SHA256`.

No se ejecutó `ping-connection-pool`.

### 16.4 Estado del despliegue residual

| Propiedad | Resultado |
|---|---|
| Nombre | `Cava` |
| Tipo | web |
| Estado | habilitada |
| Referencia del servidor | habilitada |
| Contexto registrado | `/Cava` |
| Servidor virtual | `server` |
| Tipo de despliegue | directorio expandido |
| Ubicación | `C:/Users/Maria Camila R/Documents/Cava/Cava/build/web/` |

`build/web/` existe actualmente, contiene 47 archivos y 8.340.789 bytes. Fue
recreada por la construcción del Paso 2 el 17 de julio a las 18:31. Esto corrige
la observación previa de que la ruta no existía, pero no reactiva por sí solo el
despliegue que falló al iniciar el dominio.

El arranque actual del dominio registró a las 17:57:52:

```text
Application previously deployed is not at its original location any more
```

La carpeta se recreó después de ese arranque y no se ejecutó despliegue,
redespliegue ni recarga. Aunque GlassFish conserva `Cava` como habilitada, las
siguientes rutas devolvieron 404 tanto por HTTP como por HTTPS:

- `/Cava`;
- `/Cava/`;
- `/Cava/Index.jsp`;
- `/Cava/InicioSesion.jsp`.

Por tanto, **Cava está registrada y habilitada, pero no responde como aplicación
desplegada en el estado actual**.

Los logs históricos muestran que una versión anterior se desplegó y creó el
pool `java:app/CavaPool`. También registran un
`ClassNotFoundException: com.mysql.cj.jdbc.MysqlDataSource` el 16 de julio antes
de que existiera la biblioteca común. Ese error histórico no prueba el estado
actual del driver y no sustituye un ping.

### 16.5 Recursos JDBC

Recursos globales observados:

- pools: `__TimerPool`, `DerbyPool`, `SamplePool`;
- recursos: `jdbc/sample`, `jdbc/__TimerPool`, `jdbc/__default`;
- `CavaPool` global: ausente;
- `jdbc/CavaDS` global: ausente.

Recursos asociados a la aplicación `Cava`:

- `java:app/CavaPool` — `JdbcConnectionPool`;
- `java:app/jdbc/CavaDS` — `JdbcResource`.

La indentación producida por `list-applications --resources` demuestra que
ambos pertenecen a `Cava`. No se observaron otras aplicaciones registradas.

Configuración funcional no sensible del descriptor:

| Propiedad | Valor |
|---|---|
| Pool declarado | `CavaPool` |
| DataSource | `com.mysql.cj.jdbc.MysqlDataSource` |
| Tipo | `javax.sql.DataSource` |
| Recurso | `jdbc/CavaDS` |
| Servidor | `localhost` |
| Puerto | `3306` |
| Base | `cava` |
| Usuario | `root` |
| Contraseña | Valor sensible presente y omitido |
| SSL | `false` |
| Zona horaria | `America/Bogota` |
| Validación | requerida, método `auto-commit` |
| Tamaño máximo | 32 |

La ubicación `web/WEB-INF/glassfish-resources.xml` hace que GlassFish interprete
estas definiciones como recursos con alcance de aplicación y les añada el
prefijo `java:app`.

### 16.6 Driver JDBC

Estado diferenciado:

1. **Presente físicamente:** sí, en
   `domain1/lib/mysql-connector-java-8.0.12.jar`.
2. **Reconocido como biblioteca común:** sí; `list-libraries` devuelve
   `mysql-connector-java-8.0.12.jar`.
3. **Cargable por GlassFish:** no fue posible confirmarlo sin crear o probar un
   pool. En el inicio actual no se encontró un nuevo `ClassNotFoundException`,
   pero tampoco se intentó instanciar el DataSource.
4. **Probado mediante `CavaPool`:** no; el pool global aún no existe y el ping
   está prohibido en Paso 4A.

El JAR conserva:

- tamaño: 2.020.431 bytes;
- SHA-256:
  `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2`.

No se movió, reemplazó ni eliminó.

### 16.7 Código y descriptores

- `ConexionPool` crea un `InitialContext`, ejecuta
  `lookup("jdbc/CavaDS")`, convierte el resultado a `DataSource` y lo cachea.
- `Conexion.getConn()` delega únicamente en `ConexionPool.obtener()`.
- No hay otro lookup de `DataSource` ni uso de `DriverManager` en Java.
- El nombre corto `jdbc/CavaDS` es compatible con el recurso JDBC global
  aprobado por GlassFish.
- `web.xml` no existe; por tanto, no existe `resource-ref` estándar.
- `glassfish-web.xml` existe, configura `delegate="false"` y
  `keepgenerated="true"`, pero no mapea recursos.
- `glassfish-resources.xml` contiene el pool y el recurso descritos arriba; su
  ubicación explica los recursos `java:app`.
- `build.xml` solo importa `nbproject/build-impl.xml` y no contiene un target
  personalizado de aprovisionamiento.
- `javac.classpath` está vacío.
- `lib.dir=${web.docbase.dir}/WEB-INF/lib`.
- `project.xml` tiene vacíos `web-module-libraries` y
  `web-module-additional-libraries`.
- No existe una referencia rota, absoluta o privada al driver y no se propone
  modificar `nbproject/`.

Para la arquitectura global, el cambio mínimo será impedir que
`glassfish-resources.xml` permanezca dentro de `WEB-INF`, conservar su definición
como plantilla externa no desplegable y aprovisionar los recursos globales de
forma explícita.

### 16.8 Sesiones, solicitudes, conexiones y dependencias

Todos los niveles de monitoreo de GlassFish están en `OFF`, incluidos
`web-container`, `http-service` y `jdbc-connection-pool`. Las consultas de
monitoreo respondieron `No monitoring data to report`.

Conclusiones con el grado de certeza correcto:

- solicitudes HTTP activas: no se encontraron conexiones TCP activas en una
  instantánea posterior a las pruebas; no se confirmó su inexistencia continua;
- conexiones JDBC de Cava: no se encontraron conexiones del PID 16948 hacia
  3306; no fue posible confirmar que nunca existan conexiones en memoria;
- las dos conexiones TCP observadas contra MySQL pertenecían a
  `MySQLWorkbench.exe`, PID 3084, no a GlassFish;
- sesiones HTTP: no fue posible comprobarlas porque el monitoreo está apagado;
- recursos `java:app`: pertenecen exclusivamente a Cava; no se encontró otra
  aplicación registrada que dependa de ellos;
- procesos que usan el despliegue: el DAS mantiene el registro, pero Cava
  devuelve 404 y no se encontró actividad HTTP en la instantánea;
- dependencias externas no instrumentadas: no fue posible descartarlas por
  completo.

Retirar un despliegue no elimina los datos permanentes de MySQL, pero puede
cerrar conexiones y perder sesiones en memoria. Las sesiones en memoria no
pueden recuperarse desde los respaldos de archivos. La retirada futura requiere
una ventana controlada y aceptación explícita de ese riesgo.

### 16.9 Respaldo obligatorio

Ruta externa al proyecto y al dominio:

```text
C:\Users\Maria Camila R\CAVA_Backups\Fase3_Paso4A_20260717-210037
```

| Original | Respaldo | Bytes | SHA-256 | Igualdad |
|---|---|---:|---|---|
| `domain1/config/domain.xml` | `domain1-config_domain.xml` | 35.573 | `980DCB40E4C1E6E34683EB75D53A2FE374FE5028FA7A37E7FA218D9B4714BB71` | confirmada |
| `web/WEB-INF/glassfish-resources.xml` | `project-web-WEB-INF_glassfish-resources.xml` | 987 | `20981CDD4816D9743CE3115535EFE0F1D6EF25738342351AB86E48FD31A2AF00` | confirmada |
| `web/WEB-INF/glassfish-web.xml` | `project-web-WEB-INF_glassfish-web.xml` | 1.193 | `7C1F551F6D1E003731F0512891B91A263011E0F3EDB114B1E8430D5605285C7D` | confirmada |
| `dist/Cava.war` | `project-dist_Cava.war` | 8.349.670 | `228BAE832AF097B6F0BF0DA72DCD3ABD64C10C4CB4891EB5483E04E1928E2712` | confirmada |

`web/WEB-INF/web.xml` no existe y por ello no se respaldó. El contenido de
`domain.xml` no fue copiado al informe.

### 16.10 Alternativas para `glassfish-resources.xml`

#### Alternativa 1 — recomendada

Retirar la definición de `web/WEB-INF` después de un ping global exitoso y
convertirla en una plantilla sin credenciales reales, ubicada en:

```text
provisioning/glassfish/glassfish-resources-global.template.xml
```

La ruta se mantiene fuera de `web/` y fuera de `setup/`. No se recomienda
`setup/` porque `resource.dir=setup` puede ser consumido por el flujo de
despliegue de NetBeans. La plantilla usará la referencia no secreta
`${ALIAS=cava.mysql.password}` y no se ejecutará automáticamente mediante
`clean`, `compile`, `dist` o `deploy`.

Ventajas:

- conserva una especificación reproducible de `CavaPool` y `jdbc/CavaDS`;
- evita recrear recursos `java:app`;
- no versiona contraseñas ni rutas personales;
- separa construcción de aprovisionamiento.

#### Alternativa 2 — no recomendada

Eliminar el descriptor desplegable y sustituirlo solo por documentación externa.
Evita los recursos `java:app`, pero pierde una plantilla procesable y aumenta el
riesgo de divergencia entre documentación y configuración real.

### 16.11 Plan exacto del futuro Paso 4B — no ejecutado

Todos los comandos siguientes son propuestas. Cada comando debe ejecutarse y
validarse de forma individual; no deben encadenarse.

#### Etapa A — preparación

1. Repetir la comprobación de ausencia global:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' list-jdbc-connection-pools
```

Resultado esperado: no aparece `CavaPool`. Si aparece, detenerse y consultar su
configuración no sensible; no sobrescribir.

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' list-jdbc-resources
```

Resultado esperado: no aparece `jdbc/CavaDS`. Si aparece, detenerse y comparar
su pool y destino.

2. Confirmar la biblioteca común:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' list-libraries
```

Resultado esperado: `mysql-connector-java-8.0.12.jar`. Si falta o su archivo no
conserva el SHA-256 aprobado, detenerse sin crear el pool.

3. Confirmar que el alias reservado no existe:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' list-password-aliases
```

Si `cava.mysql.password` ya existe, no actualizarlo ni reemplazarlo; detenerse y
determinar su procedencia.

4. Crear el alias de contraseña de forma interactiva:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' create-password-alias cava.mysql.password
```

El comando solicitará el secreto sin mostrarlo y lo almacenará cifrado. No debe
ponerse el valor en la línea de comandos, XML versionable, informe o script. Se
validará con `list-password-aliases`. Si falla, detenerse sin crear el pool.

Reversión del alias, solo si fue creado en esta transición y ningún recurso lo
usa:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' delete-password-alias cava.mysql.password
```

#### Etapa B — crear y probar recursos globales

5. Crear `CavaPool`:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' create-jdbc-connection-pool --target server --datasourceclassname com.mysql.cj.jdbc.MysqlDataSource --restype javax.sql.DataSource --isconnectvalidatereq true --validationmethod auto-commit --maxpoolsize 32 --property 'serverName=localhost:portNumber=3306:databaseName=cava:user=root:password=${ALIAS=cava.mysql.password}:useSSL=false:serverTimezone=America/Bogota' CavaPool
```

Resultado esperado: creación exitosa. Validar inmediatamente con
`list-jdbc-connection-pools`. Si el nombre aparece pese a un resultado de error,
tratarlo como creación parcial y no continuar.

Reversión de creación parcial del pool, únicamente si `jdbc/CavaDS` no existe:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' delete-jdbc-connection-pool CavaPool
```

6. Crear el recurso global:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' create-jdbc-resource --target server --connectionpoolid CavaPool jdbc/CavaDS
```

Resultado esperado: creación exitosa. Validar con `list-jdbc-resources`. Si
falla o queda una creación parcial, no tocar Cava.

Reversión del recurso:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' delete-jdbc-resource jdbc/CavaDS
```

Después de validar su ausencia puede eliminarse `CavaPool`, si fue creado por la
misma transición.

7. Probar el pool:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' ping-connection-pool CavaPool
```

Resultado esperado: ping exitoso. Validar además que el log no registre
`ClassNotFoundException`, error de autenticación ni error de red. Ante cualquier
fallo:

- conservar logs;
- no modificar la aplicación;
- no retirar recursos `java:app`;
- no modificar el descriptor;
- revertir recurso, pool y alias solo si fueron creados en esta transición y se
  decide volver al estado inicial.

Los recursos globales pueden coexistir temporalmente con los `java:app` porque
sus alcances son diferentes.

#### Etapa C — eliminar la configuración híbrida del artefacto

8. Solo tras ping exitoso, aplicar un parche único y revisable que:

- elimine `web/WEB-INF/glassfish-resources.xml` del artefacto desplegable;
- cree
  `provisioning/glassfish/glassfish-resources-global.template.xml`;
- conserve la configuración técnica no sensible;
- use `${ALIAS=cava.mysql.password}`;
- no contenga contraseña real, ruta personal ni ejecución automática.

No se propone `Move-Item`, `Remove-Item` ni edición mediante comandos de shell.
La operación de archivos se realizará mediante un parche explícito. Antes del
parche se recalculará el hash del original y se comparará con el respaldo.

Reversión: aplicar el parche inverso o restaurar
`project-web-WEB-INF_glassfish-resources.xml` desde el respaldo únicamente si el
hash del archivo de destino es el esperado. Esta reversión del proyecto no
requiere restaurar `domain.xml`.

#### Etapa D — construcción e inspección

9. Limpiar con Ant de NetBeans 20:

```powershell
& 'C:\Program Files\NetBeans-20\netbeans\extide\ant\bin\ant.bat' -f 'C:\Users\Maria Camila R\Documents\Cava\Cava\build.xml' clean
```

Detenerse si no termina en `BUILD SUCCESSFUL`.

10. Generar el WAR:

```powershell
& 'C:\Program Files\NetBeans-20\netbeans\extide\ant\bin\ant.bat' -f 'C:\Users\Maria Camila R\Documents\Cava\Cava\build.xml' dist
```

Detenerse si falla o si no crea exactamente
`C:\Users\Maria Camila R\Documents\Cava\Cava\dist\Cava.war`.

11. Listar el contenido:

```powershell
& 'C:\Program Files\Java\jdk-17\bin\jar.exe' tf 'C:\Users\Maria Camila R\Documents\Cava\Cava\dist\Cava.war'
```

Validar antes de continuar:

- no aparece `WEB-INF/glassfish-resources.xml`;
- no hay otra copia de ese descriptor;
- permanece exactamente un
  `WEB-INF/lib/mysql-connector-java-8.0.12.jar`;
- no se modificaron los demás archivos fuera de lo previsto.

Ante un WAR incorrecto, no desplegar. Revertir el parche, reconstruir y comparar
con `project-dist_Cava.war`. El driver de `WEB-INF/lib` permanece protegido.

#### Etapa E — sustituir el despliegue residual

12. Revalidar nombre, contexto y ubicación:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' list-applications --long
```

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' get applications.application.Cava.location
```

Detenerse si el nombre ya no es exactamente `Cava`, la aplicación desapareció o
su ubicación cambió inesperadamente.

13. Antes de retirar, exigir ventana de mantenimiento y aceptación del riesgo de
pérdida de sesiones, porque no pueden medirse ni respaldarse.

14. Retirar el despliegue residual:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' undeploy Cava
```

Validar con `list-applications`. Si falla, no usar `--force` ni continuar.

15. Desplegar el WAR limpio:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' deploy --target server --name Cava --contextroot /Cava 'C:\Users\Maria Camila R\Documents\Cava\Cava\dist\Cava.war'
```

Resultado esperado: `Cava` habilitada desde el WAR y contexto `/Cava`. Validar
con `list-applications --long`, consultas `get` y solicitudes HTTP.

Si el despliegue nuevo falla, no modificar recursos globales de forma impulsiva.
Reversión inmediata del servicio mediante el WAR respaldado:

```powershell
& 'C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat' deploy --target server --name Cava --contextroot /Cava 'C:\Users\Maria Camila R\CAVA_Backups\Fase3_Paso4A_20260717-210037\project-dist_Cava.war'
```

El WAR respaldado recreará los recursos `java:app` anteriores. Esta reversión no
recupera sesiones en memoria ya perdidas.

#### Etapa F — validación completa

16. Confirmar:

- `Cava` habilitada y con respuestas HTTP esperadas;
- ausencia de `java:app/CavaPool` y `java:app/jdbc/CavaDS` mediante
  `list-applications --resources`;
- presencia global de `CavaPool` y `jdbc/CavaDS`;
- nuevo ping exitoso;
- logs sin errores relevantes;
- ausencia de `DriverManager`.

17. Validar desde el contenedor:

- resolución de `jdbc/CavaDS`;
- `ConexionPool.obtener()`;
- `Conexion.getConn()`;
- `SELECT 1`;
- una consulta DAO real de solo lectura.

Actualmente no existe un Servlet ni prueba ejecutable que exponga ese diagnóstico
desde Cava y `PruebaConexion.java` está vacío. Antes de implementar un mecanismo
temporal de diagnóstico se requerirá autorización específica, con retirada del
mecanismo al finalizar.

18. Separar las pruebas negativas:

- MySQL apagado;
- credenciales inválidas.

No se ejecutarán dentro del Paso 4B sin autorización adicional, porque requieren
alterar estado externo o credenciales.

### 16.12 Condiciones generales de detención

Detener el Paso 4B si ocurre cualquiera de estas condiciones:

- versión, dominio, PID o instalación distintos;
- `CavaPool` o `jdbc/CavaDS` globales aparecen antes de crearlos;
- falta el driver común o cambia su SHA-256;
- el alias reservado ya existe sin procedencia confirmada;
- falla la creación o validación de alias, pool o recurso;
- falla el ping;
- el WAR contiene recursos `java:app` o una cantidad inesperada de drivers;
- cambia el nombre, contexto o ubicación de la aplicación;
- no existe una ventana aceptada para pérdida de sesiones;
- falla el `undeploy` o el nuevo despliegue;
- aparecen errores de classloading, JNDI, autenticación o red;
- cualquier comando requiere un secreto en texto visible.

### 16.13 Matriz de reversión

| Falla | Reversión prevista |
|---|---|
| Pool parcial | Confirmar ausencia de recurso y ejecutar `delete-jdbc-connection-pool CavaPool` |
| Recurso parcial | `delete-jdbc-resource jdbc/CavaDS`, validar, luego evaluar retirar el pool |
| Alias creado sin uso | `delete-password-alias cava.mysql.password` |
| Descriptor incorrecto | Parche inverso o restauración desde respaldo después de comparar hashes |
| WAR incorrecto | No desplegar; restaurar descriptor y reconstruir; conservar WAR respaldado |
| Despliegue residual retirado | Desplegar `project-dist_Cava.war` respaldado |
| Nuevo despliegue fallido | No usar `--force`; restaurar WAR respaldado y conservar logs |
| JNDI fallido | Mantener datos intactos; restaurar aplicación anterior y revisar alcance/nombre |
| Conexión fallida | No tocar MySQL ni JAR; conservar recursos para diagnóstico o revertirlos en orden recurso → pool → alias |
| Sesiones perdidas | No existe restauración desde archivos; prevenir mediante ventana y aceptación previa |

`domain.xml` es un respaldo de último recurso. No debe restaurarse con GlassFish
activo. Si alguna reversión excepcional exige restaurarlo, se deberá detener el
dominio, comparar el archivo vigente y obtener autorización específica antes de
copiarlo. Ninguna reversión elimina bases de datos, altera datos de negocio o
borra respaldos.

### 16.14 Acciones explícitamente no ejecutadas

Durante el Paso 4A no se ejecutó ninguna de estas acciones:

- iniciar, detener o reiniciar GlassFish;
- habilitar, deshabilitar, retirar, desplegar o redesplegar Cava;
- crear, modificar, eliminar o hacer ping a pools JDBC;
- crear, modificar o eliminar recursos JDBC;
- crear o modificar alias de contraseña;
- modificar `domain.xml`;
- modificar descriptores de despliegue;
- modificar Java, JSP, SQL, JavaScript o CSS;
- modificar `build.xml` o `nbproject/`;
- ejecutar `clean`, `compile` o `dist`;
- iniciar, detener o modificar MySQL;
- ejecutar SQL, `SELECT 1` o DAO;
- mover, reemplazar o eliminar JAR;
- crear targets Ant, scripts, commits, repositorios o respaldos Git.

El único archivo del proyecto modificado en Paso 4A es este informe. Fuera del
proyecto se creó exclusivamente el respaldo descrito en 16.9.

### 16.15 Estado al cierre

- Fase 2: **CERRADA**.
- Fase 3: **EN PROGRESO**.
- Paso 3: **COMPLETADO**.
- Paso 4A: **COMPLETADO únicamente como diagnóstico y planificación**.
- Paso 4B: **NO INICIADO**.
- Conexión: **PENDIENTE DE VALIDACIÓN**.

## 17. Paso 4B-1 — Intento controlado de infraestructura JDBC global

### 17.1 Alcance autorizado

Se autorizó exclusivamente revalidar el entorno, crear de forma interactiva el
alias `cava.mysql.password`, crear `CavaPool` y `jdbc/CavaDS`, ejecutar
`ping-connection-pool CavaPool` y documentar el resultado. No se autorizó
modificar ni redesplegar Cava, editar descriptores, compilar, generar otro WAR,
ejecutar SQL o avanzar al Paso 4B-2.

### 17.2 Precondiciones comprobadas

- `asadmin` utilizado:
  `C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\bin\asadmin.bat`.
- Versión local: Eclipse GlassFish 7.0.9, commit
  `5860e42c2edeb9f3b46428fcf9dab95790a9a3f0`.
- `domain1`: activo desde la instalación autorizada, PID 16948.
- Driver común: `domain1/lib/mysql-connector-java-8.0.12.jar`, 2.020.431
  bytes, SHA-256
  `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2`.
- `list-libraries`: reconoce `mysql-connector-java-8.0.12.jar`.
- Alias `cava.mysql.password`: ausente antes del intento.
- `CavaPool` global: ausente antes del intento.
- `jdbc/CavaDS` global: ausente antes del intento.
- MySQL: puerto 3306 escuchando.
- Recursos de aplicación conservados:
  `java:app/CavaPool` y `java:app/jdbc/CavaDS`.

La configuración no sensible contrastada antes del intento fue:

- datasource classname: `com.mysql.cj.jdbc.MysqlDataSource`;
- resource type: `javax.sql.DataSource`;
- servidor: `localhost`;
- puerto: `3306`;
- base de datos: `cava`;
- usuario configurado: `root`;
- validación requerida mediante `auto-commit`;
- tamaño máximo del pool: 32;
- SSL desactivado;
- zona horaria: `America/Bogota`.

### 17.3 Intento de creación del alias

Comando ejecutado de forma individual:

```text
asadmin create-password-alias cava.mysql.password
```

La entrada se realizó mediante los dos prompts interactivos oficiales de
GlassFish. El valor sensible no se mostró, no se incluyó en el comando y no se
almacenó en la documentación ni en archivos temporales.

GlassFish no creó el alias. El log nuevo de la operación registró a las
21:56:36:

```text
java.lang.IllegalArgumentException: Empty key
```

La evidencia permite clasificar el fallo: GlassFish 7.0.9 no aceptó el valor
vacío como clave del alias cifrado. La verificación posterior mediante
`list-password-aliases` terminó correctamente y respondió `Nothing to list`.
Por tanto, `cava.mysql.password` continúa ausente.

### 17.4 Barrera de detención y estado posterior

De acuerdo con la regla de detenerse ante un error de alias:

- no se creó `CavaPool`;
- no se creó `jdbc/CavaDS`;
- no se ejecutó `ping-connection-pool`;
- no se intentaron combinaciones de credenciales;
- no fue necesaria ninguna reversión, porque no quedó una creación parcial;
- los pools globales continúan siendo `__TimerPool`, `DerbyPool` y
  `SamplePool`;
- los recursos JDBC globales continúan siendo `jdbc/sample`,
  `jdbc/__TimerPool` y `jdbc/__default`;
- `java:app/CavaPool` y `java:app/jdbc/CavaDS` permanecen intactos;
- Cava permanece registrada sin retiro, despliegue ni redespliegue.

### 17.5 Acciones expresamente no ejecutadas

No se modificaron `glassfish-resources.xml`, `glassfish-web.xml`, `web.xml`,
Java, SQL, JAR, `build.xml`, `nbproject/`, el WAR ni MySQL. No se ejecutaron
`clean`, `compile`, `dist`, `SELECT 1`, DAO, pruebas negativas, reinicios,
detenciones, despliegues ni el Paso 4B-2.

### 17.6 Estado al cierre del intento

- Paso 4B-1: **BLOQUEADO antes de crear el pool**.
- Causa comprobada: GlassFish 7.0.9 rechazó la entrada vacía del alias con
  `IllegalArgumentException: Empty key`.
- `cava.mysql.password`: **AUSENTE**.
- `CavaPool` global: **NO CREADO**.
- `jdbc/CavaDS` global: **NO CREADO**.
- Ping: **NO EJECUTADO**.
- Despliegue de Cava: **SIN MODIFICAR**.
- Conexión desde la aplicación: **PENDIENTE**.
- Paso 4B-2: **NO INICIADO**.

Para reanudar Paso 4B-1 bajo la arquitectura aprobada será necesario disponer
de una contraseña MySQL no vacía que pueda almacenarse mediante el alias
cifrado. Cambiar esa credencial requiere una autorización separada.

## 18. Paso 4B-1 — Usuario exclusivo, fallo de sintaxis y reversión

### 18.1 Identificación actual del servidor

Las comprobaciones se repitieron contra el sistema activo antes de modificar:

- proceso del puerto 3306: `C:\xampp8\mysql\bin\mysqld.exe`, PID 16460;
- proceso padre: `C:\xampp8\xampp-control.exe`;
- servicio de Windows asociado: ninguno;
- producto SQL: MariaDB 10.4.32, distribución binaria de `mariadb.org`;
- esquema `cava`: presente, con 15 tablas observadas mediante metadatos;
- `localhost` y `127.0.0.1` resolvieron efectivamente como
  `root@localhost`;
- `old_passwords=OFF` y `mysql_native_password` activo;
- logs general, lento y binario: desactivados;
- las cuentas `root` para `127.0.0.1`, `::1` y `localhost` permanecían sin
  contraseña y no fueron modificadas.

El aviso `Warning - not supported` de MySQL Workbench corresponde a que el
servidor es MariaDB y Workbench no lo trata como una versión soportada de
MySQL. No constituye por sí mismo evidencia de daño en el esquema `cava`.

### 18.2 Compatibilidad comprobada

Se cargó de forma diagnóstica, sin modificar el proyecto, la clase
`com.mysql.cj.jdbc.MysqlDataSource` desde el JAR común aprobado:

- Connector/J: 8.0.12;
- clase compilada para Java 8 y cargada mediante Java 17.0.14;
- conexión TCP a `localhost:3306/cava`: exitosa;
- producto reportado por JDBC: `5.5.5-10.4.32-MariaDB`;
- usuario efectivo de la prueba: `root@localhost`.

Esta prueba confirmó compatibilidad operativa del driver, el DataSource y el
protocolo para continuar con la infraestructura aprobada. No se ejecutó SQL de
negocio, `SELECT 1` ni un DAO.

### 18.3 Usuario de aplicación creado y validado

Antes de crear la cuenta se confirmó que `cava_app` no existía. La contraseña
fue introducida personalmente mediante una ventana local con entrada oculta,
confirmación y validación de complejidad. No se incluyó en argumentos, archivos,
historial, código, descriptores, respaldos ni documentación.

Se creó exclusivamente:

```text
cava_app@localhost
```

Mecanismo de autenticación: `mysql_native_password`.

Los 15 DAO actuales contienen operaciones `SELECT`, `INSERT`, `UPDATE` y
`DELETE`, sin DDL. Por ello se concedieron únicamente esos cuatro privilegios
sobre `cava.*`, sin `GRANT OPTION` y sin privilegios globales o acceso a otros
esquemas.

Una primera consulta de diagnóstico devolvió accidentalmente en la salida el
hash de autenticación de la cuenta. La contraseña en texto claro no se mostró,
pero el hash se trató como sensible: se detuvo el procedimiento y se rotó la
contraseña de `cava_app` mediante otra entrada interactiva, invalidando el hash
anterior. El valor nuevo y su hash no se mostraron ni documentaron.

La autenticación de la credencial rotada se comprobó mediante una sesión TCP
real:

```text
usuario: cava_app
host: localhost
esquema: cava
estado: Sleep
```

No se ejecutaron consultas sobre tablas ni se modificaron datos. La sesión de
verificación fue cerrada después de observarla desde `information_schema`.

### 18.4 Alias creado

El alias `cava.mysql.password` se creó exitosamente mediante el prompt
interactivo oficial de GlassFish y se verificó por nombre con
`list-password-aliases`. El valor no fue mostrado ni almacenado en la
documentación.

### 18.5 Fallo al crear el pool

Se intentó crear `CavaPool` con:

- datasource: `com.mysql.cj.jdbc.MysqlDataSource`;
- resource type: `javax.sql.DataSource`;
- servidor: `localhost`;
- puerto: 3306;
- base: `cava`;
- usuario: `cava_app`;
- contraseña: referencia al alias cifrado, valor sensible omitido;
- SSL desactivado;
- zona horaria: `America/Bogota`;
- validación: `auto-commit`;
- máximo: 32.

El comando terminó con código 1 antes de crear el pool. Error exacto no
sensible:

```text
Invalid property syntax, "=" in value: password=${ALIAS=cava.mysql.password}
```

`list-jdbc-connection-pools` confirmó que `CavaPool` no quedó creado y
`list-jdbc-resources` confirmó que `jdbc/CavaDS` tampoco existía. No se ejecutó
ping.

La ayuda local de `create-jdbc-connection-pool` explica que un signo igual
dentro del valor de `--property` requiere dos barras inversas de escape. Una
futura repetición deberá proteger la referencia para que el argumento contenga:

```text
${ALIAS\\=cava.mysql.password}
```

Esta corrección quedó diagnosticada, pero no se ejecutó después del fallo.

### 18.6 Reversión controlada

Antes de revertir se confirmó:

- `CavaPool` ausente;
- `jdbc/CavaDS` ausente;
- alias presente y creado durante este procedimiento;
- `cava_app@localhost` presente y creado durante este procedimiento;
- cero sesiones activas de `cava_app`.

Se revirtió en orden seguro:

1. `delete-password-alias cava.mysql.password`: exitoso.
2. `DROP USER 'cava_app'@'localhost'`: exitoso.

Estado verificado después de la reversión:

- `cava_app`: **AUSENTE**;
- `cava.mysql.password`: **AUSENTE**;
- `CavaPool` global: **AUSENTE**;
- `jdbc/CavaDS` global: **AUSENTE**;
- cuentas `root`: **SIN MODIFICAR**;
- `java:app/CavaPool`: **INTACTO**;
- `java:app/jdbc/CavaDS`: **INTACTO**;
- despliegue de Cava: **SIN MODIFICAR**.

### 18.7 Estado al cierre

- Paso 4B-1: **REVERTIDO Y BLOQUEADO antes del pool**.
- Causa: sintaxis sin escape del signo igual interno de la referencia al alias.
- Ping: **NO EJECUTADO**.
- Conexión desde la aplicación: **PENDIENTE**.
- Paso 4B-2: **NO INICIADO**.

No se modificaron datos ni estructura de `cava`, cuentas `root`, WAR,
descriptores, Java, JAR, `build.xml` o `nbproject/`. No se compiló, reconstruyó,
desplegó ni redesplegó Cava.

## 19. Paso 4B-1 — Segundo intento autorizado y revertido

### 19.1 Precondiciones revalidadas

Antes del segundo intento se comprobó nuevamente:

- `cava_app`: ausente;
- `cava.mysql.password`: ausente;
- `CavaPool` global: ausente;
- `jdbc/CavaDS` global: ausente;
- logs general, lento y binario de MariaDB: desactivados;
- cuentas `root` para `127.0.0.1`, `::1` y `localhost`: intactas;
- `java:app/CavaPool` y `java:app/jdbc/CavaDS`: intactos;
- GlassFish: Eclipse GlassFish 7.0.9, `domain1` activo;
- driver común: SHA-256
  `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2`;
- WAR y descriptores protegidos: sin cambios frente al respaldo de Paso 4A.

### 19.2 Escape revalidado antes de crear

La ayuda local de `create-jdbc-connection-pool` mostró el ejemplo:

```text
connectionAttributes=\;create\\=true
```

y explicó literalmente que se usan dos barras inversas para distinguir un
signo igual dentro de un valor. Esta comprobación se hizo nuevamente antes de
modificar y no se basó únicamente en el intento anterior.

### 19.3 Usuario y acceso

`cava_app@localhost` se recreó con la última contraseña elegida, introducida de
forma oculta y confirmada personalmente. La contraseña y su hash no se
mostraron ni almacenaron en argumentos, archivos, historial o documentación.

Se asignaron exclusivamente `SELECT`, `INSERT`, `UPDATE` y `DELETE` sobre
`cava.*`, sin `GRANT OPTION` ni privilegios globales. La autenticación y el
acceso se comprobaron mediante una sesión TCP observada desde MariaDB:

```text
usuario: cava_app
host: localhost
esquema: cava
estado: Sleep
```

La sesión se cerró sin ejecutar consultas sobre tablas.

### 19.4 Alias

`cava.mysql.password` se creó mediante el prompt interactivo oficial de
GlassFish y se verificó por nombre. El valor no se mostró ni documentó.

### 19.5 Segundo fallo de creación del pool

Se omitió la opción obsoleta `--target` y se repitió
`create-jdbc-connection-pool` con la referencia del alias entregada con dos
barras inversas dentro del valor de `--property`.

El comando terminó con código 1. Error exacto no sensible:

```text
Invalid property syntax, "=" in value: password=${ALIAS\\=cava.mysql.password}
```

La salida demuestra que, en esta ruta de ejecución de `asadmin.bat` sobre
PowerShell y GlassFish 7.0.9, las dos barras llegaron literalmente al
analizador remoto y no protegieron el signo igual interno. `CavaPool` no fue
creado, `jdbc/CavaDS` no fue creado y no se ejecutó ping.

No se probó otra cantidad de barras ni otra sintaxis por ensayo y error.

### 19.6 Reversión del segundo intento

Antes de revertir se confirmó que no existían pool ni recurso global, el alias
y la cuenta pertenecían a este segundo intento y había cero sesiones de
`cava_app`.

Reversión ejecutada:

1. `delete-password-alias cava.mysql.password`: exitosa.
2. `DROP USER 'cava_app'@'localhost'`: exitosa.

Estado final verificado:

- `cava_app`: **AUSENTE**;
- `cava.mysql.password`: **AUSENTE**;
- `CavaPool` global: **AUSENTE**;
- `jdbc/CavaDS` global: **AUSENTE**;
- cuentas `root`: **SIN MODIFICAR**;
- recursos `java:app`: **INTACTOS**;
- despliegue y artefactos de Cava: **SIN MODIFICAR**.

### 19.7 Alternativa oficial identificada — no ejecutada

La documentación oficial de GlassFish muestra que un alias puede asignarse a
una propiedad mediante `asadmin set`, pasando el operando completo entre
comillas, por ejemplo:

```text
asadmin set "...password=${ALIAS=cava.mysql.password}"
```

También documenta que las propiedades de un pool existente pueden modificarse
con `get` y `set`. Esto permite evitar el analizador de la opción compuesta
`--property` que falló en los dos intentos:

1. crear `CavaPool` sin la propiedad `password`;
2. asignar después exclusivamente
   `resources.jdbc-connection-pool.CavaPool.property.password` mediante
   `asadmin set` y la referencia al alias;
3. verificar la propiedad sin mostrar su valor antes de crear el recurso o
   ejecutar ping.

Referencias oficiales consultadas:

- Oracle GlassFish Server Security Guide, administración de aliases;
- Oracle GlassFish Administration Guide, actualización de propiedades JDBC
  mediante `get` y `set`;
- ayuda local de Eclipse GlassFish 7.0.9.

Esta alternativa no se ejecutó porque el segundo intento ya había fallado y la
regla de seguridad exigía detenerse y revertir.

### 19.8 Estado al cierre

- Paso 4B-1: **REVERTIDO Y BLOQUEADO antes del pool**.
- Causa: incompatibilidad práctica entre el escape documentado de `=` y el
  análisis remoto de `--property` en la ejecución actual sobre Windows.
- Ping: **NO EJECUTADO**.
- Conexión desde la aplicación: **PENDIENTE**.
- Paso 4B-2: **NO INICIADO**.

## 20. Paso 4B-1 — Tercer intento autorizado, detenido antes de crear la cuenta

### 20.1 Alcance autorizado

Se autorizó un tercer intento exclusivo del Paso 4B-1 con la estrategia oficial
en dos operaciones: crear `CavaPool` sin la propiedad `password` y asignar
después `resources.jdbc-connection-pool.CavaPool.property.password` mediante
`asadmin set`, usando como operando citado la referencia al alias.

La autorización exigió detenerse ante cualquier error y revertir únicamente los
objetos creados durante este intento. No se autorizó modificar `root`, esquema,
datos, WAR, descriptores, recursos `java:app` ni ningún componente posterior al
Paso 4B-1.

### 20.2 Precondiciones revalidadas

Antes de modificar se comprobó:

- servidor activo: MariaDB 10.4.32 de XAMPP, puerto 3306;
- GlassFish: `domain1` activo;
- `cava_app@localhost`: ausente;
- `cava.mysql.password`: ausente;
- `CavaPool` global: ausente;
- `jdbc/CavaDS` global: ausente;
- logs general, lento y binario de MariaDB: desactivados;
- cuentas `root` para `127.0.0.1`, `::1` y `localhost`: intactas;
- `java:app/CavaPool` y `java:app/jdbc/CavaDS`: presentes e intactos;
- WAR y descriptores protegidos: idénticos al respaldo aprobado.

Hashes SHA-256 revalidados:

- `web/WEB-INF/glassfish-resources.xml`:
  `20981CDD4816D9743CE3115535EFE0F1D6EF25738342351AB86E48FD31A2AF00`;
- `web/WEB-INF/glassfish-web.xml`:
  `7C1F551F6D1E003731F0512891B91A263011E0F3EDB114B1E8430D5605285C7D`;
- `dist/Cava.war`:
  `228BAE832AF097B6F0BF0DA72DCD3ABD64C10C4CB4891EB5483E04E1928E2712`.

### 20.3 Intento seguro de crear `cava_app@localhost`

Se abrió una ventana local independiente para introducir y confirmar la
contraseña de forma oculta. La contraseña no se recibió por chat, no se mostró
y no se incorporó a argumentos de procesos, archivos, historial, scripts,
respaldos ni documentación. Tampoco se calculó, mostró o almacenó su hash.

La ventana informó `OPERACIÓN FALLIDA`. En cumplimiento de la regla de
detención, no se realizó ningún reintento y no se inició la creación del alias,
del pool, del recurso ni el ping.

La comprobación inmediata de MariaDB produjo estos resultados no sensibles:

```text
cava_app_count=0
cava_app_sessions=0
```

Por tanto, el fallo ocurrió antes de que la cuenta quedara creada y no hubo un
objeto MySQL que revertir.

### 20.4 Límite del diagnóstico

El cliente interactivo descartó deliberadamente la salida interna de error para
evitar que una respuesta del servidor pudiera reproducir parte de la sentencia
que contenía la credencial. Los logs general, lento y binario estaban
desactivados y `mysql_error.log` no registró el fallo. En consecuencia, el
código SQLSTATE y el texto exacto de MariaDB no quedaron almacenados y no son
recuperables después del cierre de la ventana.

Una comprobación posterior de solo lectura confirmó
`strict_password_validation=ON`. Este dato no prueba por sí solo que la política
de contraseña haya causado el fallo, por lo que no se atribuye una causa sin
evidencia suficiente.

### 20.5 Estado final verificado

- `cava_app@localhost`: **AUSENTE**;
- `cava.mysql.password`: **AUSENTE**;
- `CavaPool` global: **AUSENTE**;
- `jdbc/CavaDS` global: **AUSENTE**;
- cuentas `root`: **SIN MODIFICAR**;
- esquema y datos de `cava`: **SIN MODIFICAR**;
- recursos `java:app`: **INTACTOS**;
- WAR y descriptores: **SIN MODIFICAR**;
- ping: **NO EJECUTADO**;
- Paso 4B-2: **NO INICIADO**.

No fue necesaria una operación de reversión porque ninguno de los cuatro
objetos autorizados llegó a crearse. La estrategia de `asadmin set` no fue
alcanzada y permanece sin ejecutar.

## 21. Diagnóstico autorizado del fallo del tercer intento

### 21.1 Alcance y método

Se realizó exclusivamente un diagnóstico de solo lectura. No se solicitó
ninguna contraseña y no se creó, modificó ni eliminó ninguna cuenta, alias,
pool o recurso JDBC. Se compararon el registro operativo del procedimiento que
sí creó `cava_app@localhost`, el procedimiento de rotación posterior y el código
de la ventana empleada en el tercer intento.

### 21.2 Cliente y servidor confirmados

El tercer intento no utilizó MySQL Workbench. Utilizó directamente:

```text
C:\xampp8\mysql\bin\mysql.exe
Ver 15.1 Distrib 10.4.32-MariaDB, Win64 (AMD64)
FileVersion/ProductVersion: 10.4.32.0
SHA-256: A60D3CBC24984E3C1C5DEE3726ABC685EC6FFF58BCE3FBD763F9261C652FB73D
```

El proceso servidor activo continuó siendo:

```text
C:\xampp8\mysql\bin\mysqld.exe
MariaDB 10.4.32, mariadb.org binary distribution
configuración: C:\xampp8\mysql\bin\my.ini
```

Por tanto, cambiar la versión de Workbench no afectaría el canal que falló.

### 21.3 Diferencias entre el procedimiento exitoso y el tercero

#### Procedimiento que sí creó la cuenta

La ventana exitosa:

1. comprobó dentro de la propia ventana que `cava_app` estaba ausente y que los
   logs general, lento y binario estaban apagados;
2. solicitó y confirmó la contraseña de forma oculta;
3. comprobó localmente longitud mínima de 16 caracteres, mayúscula, minúscula,
   número y símbolo;
4. calculó en memoria el hash compatible con `mysql_native_password`;
5. envió por separado una operación equivalente a:

   ```text
   CREATE USER 'cava_app'@'localhost' IDENTIFIED BY PASSWORD '[HASH OMITIDO]';
   ```

6. después de confirmar la creación, ejecutó el `GRANT` mínimo en otra llamada;
7. si el `GRANT` fallaba, tenía una reversión explícita de la cuenta;
8. la rotación preventiva posterior también utilizó por separado
   `ALTER USER ... IDENTIFIED BY PASSWORD '[HASH OMITIDO]'`.

El servidor recibió únicamente una cadena hash ASCII; no recibió la contraseña
como literal SQL. Este mecanismo no debe repetirse bajo la restricción actual
que prohíbe calcular o conservar el hash.

#### Tercer intento fallido

La tercera ventana:

1. solo comprobó que las dos entradas ocultas coincidieran;
2. convirtió temporalmente la contraseña a texto en memoria;
3. escapó barra inversa y comilla simple;
4. construyó una sola entrada con dos sentencias, equivalente a:

   ```text
   CREATE USER 'cava_app'@'localhost' IDENTIFIED BY '[VALOR OMITIDO]';
   GRANT SELECT, INSERT, UPDATE, DELETE ON cava.* TO 'cava_app'@'localhost';
   ```

5. inició el cliente con `--default-character-set=utf8mb4`, pero no fijó la
   propiedad .NET `StandardInputEncoding`;
6. descartó por completo `stderr` antes de extraer código, SQLSTATE o mensaje.

La medición no destructiva del mismo estilo de proceso confirmó:

```text
PowerShell Console.InputEncoding: ibm850
mysql.exe StandardInput.Encoding: ibm850
caracteres declarados al cliente: utf8mb4
```

Además existieron diferencias secundarias que no explican por sí solas el
fallo: `localhost` frente a `127.0.0.1`, opciones `--raw`/`--skip-column-names`
frente a `--silent` y precondición interna frente a precondición externa. Ambas
rutas TCP resolvieron anteriormente como `root@localhost`.

### 21.4 Política y plugins de contraseña

El estado actual de solo lectura es:

```text
strict_password_validation=ON
old_passwords=OFF
sql_mode=NO_ZERO_IN_DATE,NO_ZERO_DATE,NO_ENGINE_SUBSTITUTION
```

`information_schema.PLUGINS` no contiene `simple_password_check`,
`cracklib_password_check` ni `password_reuse_check`; tampoco hay un plugin de
tipo de validación de contraseña. `mysql.plugin` no registró plugins dinámicos.
Solo se observaron los mecanismos de autenticación estáticos
`mysql_native_password` y `mysql_old_password`.

Según la documentación oficial de MariaDB, `strict_password_validation` no
tiene efecto si no hay un plugin de validación cargado. En consecuencia, no hay
evidencia de que una política activa de complejidad haya rechazado la contraseña
del tercer intento. No se desactivó ni modificó esta variable.

Referencia oficial:

- https://mariadb.com/docs/server/reference/product-development/plugin-development/password-validation

### 21.5 Causa probable

La causa más probable es la diferencia de codificación y serialización del
secreto en el tercer intento: se envió un literal de contraseña por un
`StandardInput` IBM850 mientras el cliente interpretaba la conexión como
`utf8mb4`. Si la entrada contenía cualquier carácter cuya representación no
fuera idéntica en ASCII, el flujo podía llegar alterado o como una secuencia no
válida y hacer fallar `CREATE USER` antes de crear la cuenta.

La ruta exitosa evitó completamente este riesgo porque transmitió un hash ASCII
calculado en memoria. También evitó que caracteres especiales del valor
participaran en el análisis del literal SQL.

Esta conclusión es una **causa probable respaldada**, no una causa demostrada:
si la contraseña estaba compuesta exclusivamente por caracteres ASCII, la
diferencia de codificación no bastaría para explicar el fallo. El SQLSTATE y el
mensaje originales fueron descartados por la tercera ventana, por lo que la
evidencia disponible no permite identificar de manera única otra causa. No se
solicitó ni caracterizó la contraseña para completar este diagnóstico.

### 21.6 Diseño de la ventana segura mejorada — no ejecutado

El siguiente intento deberá usar este orden exacto:

1. Revalidar dentro de la ventana la ausencia de `cava_app` y el estado apagado
   de los logs sensibles.
2. Solicitar y confirmar la contraseña con `Read-Host -AsSecureString`.
3. Mantener `IDENTIFIED BY` para que MariaDB reciba el texto y pueda aplicar
   cualquier política vigente; no calcular ni utilizar un hash.
4. Fijar explícitamente
   `StandardInputEncoding = [System.Text.UTF8Encoding]::new($false)` antes
   de iniciar `mysql.exe`, en correspondencia con
   `--default-character-set=utf8mb4`.
5. Escapar la contraseña únicamente en memoria conforme al modo SQL verificado;
   no incluirla en argumentos, archivos, historial ni salida.
6. Ejecutar `CREATE USER` como única sentencia y comprobar su código de salida
   antes de ejecutar el `GRANT`.
7. Capturar `stderr` solo en memoria y extraer inmediatamente mediante una
   expresión estricta:
   - código numérico después de `ERROR`;
   - SQLSTATE de cinco caracteres entre paréntesis;
   - primera línea del mensaje, con límite de longitud.
8. Sanitizar antes de mostrar o conservar:
   - reemplazar coincidencias exactas del texto y su forma escapada por
     `[SECRETO OMITIDO]`;
   - reemplazar cualquier fragmento entre comillas por `[FRAGMENTO OMITIDO]`;
   - eliminar cualquier texto que empiece por `CREATE USER`, `ALTER USER` o
     `GRANT`;
   - no conservar `stdout`, `stderr`, sentencia ni variables intermedias sin
     sanear.
9. Mostrar únicamente un registro con esta forma:

   ```text
   client_exit=<número>
   error_code=<número o NO_DISPONIBLE>
   sqlstate=<cinco caracteres o NO_DISPONIBLE>
   message=<texto sanitizado o NO_DISPONIBLE>
   ```

10. Borrar de memoria las cadenas de contraseña, literal SQL y salida cruda en
    bloques `finally`, liberar los `SecureString` y ejecutar la recolección de
    memoria pendiente.
11. Si `CREATE USER` falla, detenerse sin ejecutar `GRANT`.
12. Si `CREATE USER` funciona y `GRANT` falla, comprobar que la cuenta pertenece
    al intento y revertir únicamente esa cuenta.

Este diseño no se guardó como script y no fue ejecutado.

### 21.7 Estado final del diagnóstico

- `cava_app@localhost`: **AUSENTE**;
- `cava.mysql.password`: **AUSENTE**;
- `CavaPool` global: **AUSENTE**;
- `jdbc/CavaDS` global: **AUSENTE**;
- contraseña solicitada durante el diagnóstico: **NO**;
- `strict_password_validation`: **ON, SIN MODIFICAR**;
- cuentas, privilegios, esquema y datos: **SIN MODIFICAR**;
- GlassFish, WAR, descriptores y recursos `java:app`: **SIN MODIFICAR**;
- ventana mejorada: **DISEÑADA, NO EJECUTADA**;
- Paso 4B-1: **NO REANUDADO**;
- Paso 4B-2: **NO INICIADO**.

## 22. Recuperación controlada de MariaDB e intento interactivo no completado

### 22.1 Diagnóstico del puerto 3306

Al iniciar esta etapa no existía un proceso `mysqld.exe` y el puerto 3306 no
tenía un listener. El log de MariaDB mostraba arranques anteriores con
recuperación automática de Aria e InnoDB completada, pero no contenía después
una entrada de `Normal shutdown`, error de enlace, corrupción, aborto del motor
o caída registrada por Windows.

La evidencia permite clasificar la pérdida del listener como una terminación
abrupta externa al cierre normal de MariaDB. No existe evidencia de que el
comando cliente `mysql.exe --password` ejecutara una orden de apagado; la
coincidencia temporal con la prueba y su limpieza impide identificar con
certeza el proceso externo que terminó la instancia. No se encontró evidencia
de un fallo interno del motor ni se ejecutó ninguna reparación.

### 22.2 Inicio mediante XAMPP

Se utilizó exclusivamente el mecanismo existente:

```text
C:\xampp8\mysql_start.bat
```

Este mecanismo inició una sola instancia con:

```text
ejecutable: C:\xampp8\mysql\bin\mysqld.exe
argumentos: --defaults-file=mysql\bin\my.ini --standalone
PID: 5152
basedir: C:/xampp8/mysql
datadir: C:\xampp8\mysql\data\
puerto: 3306
versión: 10.4.32-MariaDB
```

El log terminó en `ready for connections`. Dos comprobaciones separadas de
`mysqladmin ping` respondieron `mysqld is alive`; después de la interacción no
completada, otra comprobación confirmó una sola instancia, el mismo PID, un
listener y un tiempo de actividad creciente.

No se creó un servicio, no se inició una segunda instancia y no se borró,
reparó, reemplazó ni reinicializó ningún archivo de datos.

### 22.3 Precondiciones posteriores al inicio

Antes de abrir la consola interactiva se confirmó:

- `cava_app@localhost`: ausente;
- sesiones de `cava_app`: cero;
- logs general, lento y binario: desactivados;
- `strict_password_validation`: `ON`, sin modificar;
- cuentas `root`: intactas;
- esquema `cava`: presente;
- `cava.mysql.password`: ausente;
- `CavaPool` global: ausente;
- `jdbc/CavaDS` global: ausente;
- una sola instancia de MariaDB y un solo listener en 3306.

Hashes SHA-256 protegidos revalidados:

- `web/WEB-INF/glassfish-resources.xml`:
  `20981CDD4816D9743CE3115535EFE0F1D6EF25738342351AB86E48FD31A2AF00`;
- `web/WEB-INF/glassfish-web.xml`:
  `7C1F551F6D1E003731F0512891B91A263011E0F3EDB114B1E8430D5605285C7D`;
- `dist/Cava.war`:
  `228BAE832AF097B6F0BF0DA72DCD3ABD64C10C4CB4891EB5483E04E1928E2712`;
- driver común de GlassFish:
  `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2`.

### 22.4 Consola segura no completada

Se abrió una consola interactiva visible, con límite de 120 segundos para cada
cliente de autenticación. El procedimiento estaba diseñado para separar
`CREATE USER`, comprobación de existencia, `GRANT`, `SHOW GRANTS` sanitizado,
autenticación positiva, prueba negativa y confirmación posterior.

La persona usuaria informó que no pudo completar la interacción. La consola se
cerró y no quedó disponible un código, SQLSTATE o mensaje sanitizado con el que
atribuir una causa específica. La comprobación inmediata confirmó:

```text
cava_app_count=0
cava_app_sessions=0
procesos mysql.exe pendientes=0
```

Por tanto, `CREATE USER` no dejó una cuenta creada y no fue necesaria ninguna
reversión. No se ejecutaron `GRANT`, `SHOW GRANTS`, autenticación como
`cava_app`, `SELECT 1`, lectura de una tabla ni prueba negativa.

### 22.5 Estado final

- MariaDB: **ACTIVO Y ESTABLE**, una instancia, PID 5152;
- puerto 3306: **ESCUCHANDO**;
- `cava_app@localhost`: **AUSENTE**;
- `cava.mysql.password`: **AUSENTE**;
- `CavaPool` global: **AUSENTE**;
- `jdbc/CavaDS` global: **AUSENTE**;
- contraseñas o hashes expuestos: **NINGUNO**;
- procesos cliente pendientes: **NINGUNO**;
- `root`, políticas, esquema, datos, GlassFish, WAR, descriptores y recursos
  `java:app`: **SIN MODIFICAR**;
- Paso 4B-1: **DETENIDO antes de crear la cuenta**;
- Paso 4B-2: **NO INICIADO**.

## 23. Cierre definitivo de la Fase 3 — 18 de julio de 2026

### 23.1 Aclaración de nomenclatura

Los Pasos 4A y 4B pertenecen a la **Fase 3 — Conexión y despliegue**. Este cierre no inicia la Fase 4 — Consolidación de base de datos. No se modificaron esquema, tablas, datos, migraciones, Models, DAO, Servlets, JSP, frontend, autenticación ni la ruta `/Cava/`.

### 23.2 Estado final de la cadena

La cadena validada es:

```text
MariaDB -> cava -> cava_app -> Connector/J común -> CavaPool global -> jdbc/CavaDS global -> Cava
```

- MariaDB 10.4.32 permanece activo en el puerto 3306.
- La base real es `cava` y conserva 15 tablas.
- `cava_app@localhost` existe con contraseña y privilegios mínimos `SELECT`, `INSERT`, `UPDATE` y `DELETE` sobre `cava.*`.
- No existen privilegios globales ni cuentas remotas equivalentes.
- GlassFish usa el alias cifrado `cava.mysql.password`; no se documenta su contenido.
- `CavaPool` y `jdbc/CavaDS` son recursos globales.
- `ConexionPool` conserva el lookup exacto `jdbc/CavaDS`.
- `web/WEB-INF/glassfish-resources.xml` fue retirado para eliminar la configuración app-scoped anterior basada en `root`.

### 23.3 Auditoría del driver

La copia oficial de ejecución es:

```text
C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\domains\domain1\lib\mysql-connector-java-8.0.12.jar
```

Mide 2020431 bytes y conserva SHA-256 `8EFE96BB34C38DB4C2AF61AECEF6B5D67E12B011845F61C383A26FBB5F6492F2`.

Las copias heredadas en `Cava/lib/` y `Cava/web/WEB-INF/lib/` son byte a byte idénticas y redundantes. La eliminación fue intentada con respaldo verificado, pero Windows negó `DELETE` y la modificación de ACL al grupo de ejecución. Los archivos quedaron intactos; no se forzaron permisos. `nbproject/project.properties` usa `build.web.excludes` para impedir que entren en `build` o en el WAR. El código no importa `com.mysql.*`, `javac.classpath` está vacío y no existe `DriverManager`.

### 23.4 Compilación y WAR

- `clean`: `BUILD SUCCESSFUL`, código 0.
- `compile`: 33 fuentes, `BUILD SUCCESSFUL`, código 0.
- `dist`: `BUILD SUCCESSFUL`, código 0.
- WAR final: `dist/Cava.war`.
- Tamaño: 6327952 bytes.
- SHA-256 final: `48014EA9EF42106419A3A9735EC87083C3EFF317A2A0ED7EE355E50CEB088A2B`.
- Contiene `glassfish-web.xml`, `Conexion.class`, `ConexionPool.class` y las cuatro JSP actuales.
- No contiene `glassfish-resources.xml`, Connector/J, sondas, temporales, logs ni credenciales.

### 23.5 Despliegue y prueba funcional

- Cava fue desplegada y habilitada como aplicación web.
- `ping-connection-pool CavaPool`: exitoso.
- `/Cava/Index.jsp`: HTTP 200.
- `/Cava/Admin.jsp`: HTTP 200.
- Una sonda temporal en la copia desplegada ejecutó mediante `Conexion.getConn()`:

  ```sql
  SELECT COUNT(*) FROM productos;
  ```

- Resultado: `CAVA_DB_OK productos=0`.
- La sonda fue eliminada; se repitieron `clean`, `compile`, `dist` y el redespliegue. No quedan sondas en fuente, build, WAR ni dominio y su URL responde 404.

### 23.6 Prueba posterior al reinicio

- PID GlassFish anterior: 5792.
- PID GlassFish posterior: 3380.
- `restart-domain domain1`: código 0.
- MariaDB conservó PID 7748 y no fue reiniciado ni modificado durante este cierre.
- Cava continuó desplegada.
- Ping posterior: exitoso.
- Index.jsp y Admin.jsp posteriores: HTTP 200.
- Consulta posterior mediante `Conexion.getConn()`: `CAVA_DB_OK productos=0`.

### 23.7 Logs y evidencia

El intervalo `2026-07-18T21:21:28.957-05:00` a `2026-07-18T21:23:55.063-05:00` no contiene errores de SQL, autenticación, driver, classloader, CavaPool ni `jdbc/CavaDS`. Se registró un incidente no JDBC al desregistrar JMX/RMI porque `Camila.lan` no resolvió durante la detención; el reinicio concluyó correctamente. Un `PWC6117` corresponde a la verificación deliberada del 404 de la sonda eliminada.

Evidencias sanitizadas:

- `docs/auditorias/evidencias/fase3/01_precondiciones.txt`;
- `02_compilacion.txt`;
- `03_inventario_war.txt`;
- `04_despliegue_y_pruebas.txt`;
- `05_pruebas_post_reinicio.txt`;
- `06_revision_logs_sanitizada.txt`;
- `07_busqueda_global.txt`;
- `SHA256SUMS.txt`.

### 23.8 Git y estado formal

`git rev-parse --show-toplevel` devuelve `C:/Users/Maria Camila R`, un repositorio padre accidental que no corresponde exactamente a Cava. No se ejecutaron `git status` global, `git add`, commit, tag ni push. El checkpoint queda no aplicable; no se inicializó un repositorio nuevo.

Estado formal final:

```text
FASE 3 CERRADA; FASE 4 PENDIENTE DE AUTORIZACIÓN
```

Pendientes fuera del alcance: `/Cava/` continúa sin `welcome-file` y responde 404; la resolución local de `Camila.lan` para JMX/RMI requiere revisión separada. Ninguno afecta la cadena JDBC validada.
