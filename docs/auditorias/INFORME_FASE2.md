# Fase 2 — Corrección estructural aplicada

## Cambios realizados

- Los 15 DAO permanecen en `src/java/Controlador/`.
- Las 15 declaraciones `package DAO;` se cambiaron a `package Controlador;`.
- Se buscaron y corrigieron referencias `import DAO.`; no quedan coincidencias.
- `src/java/Servlets/` queda reservado para clases Java Servlet con `package Servlets;`.
- No existen JSP, HTML, CSS ni JavaScript dentro de `src/java/`.
- `database/cava.sql` quedó como único script principal canónico después de
  eliminar la copia residual, previamente respaldada y verificada byte a byte.
- Se corrigieron las referencias internas a los documentos numerados.
- Las librerías duplicadas no fueron eliminadas porque su uso debe confirmarse mediante Clean and Build e inspección del WAR.

## Validaciones estáticas

- `package DAO;`: 0 coincidencias observadas.
- `import DAO.`: 0 coincidencias observadas.
- `DriverManager`: 0 usos observados en el código Java del proyecto.
- Recursos web dentro de `src/java`: 0 archivos observados.
- Los 15 DAO declaran `package Controlador;`.
- Los 33 archivos Java declaran un paquete coherente con su ruta.

## Cierre de compilación — 17 de julio de 2026

### Entorno y comandos

- Java: 17.0.14 LTS.
- Ant: 1.10.14, incluido en NetBeans 20.
- Ant no estaba en `PATH`; se usó su ruta reproducible dentro de NetBeans.
- Comando de limpieza:
  `& 'C:\Program Files\NetBeans-20\netbeans\extide\ant\bin\ant.bat' -f build.xml clean`
- Comando de compilación:
  `& 'C:\Program Files\NetBeans-20\netbeans\extide\ant\bin\ant.bat' -f build.xml compile`

### Salida completa de `clean`

```text
Buildfile: C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build.xml

-pre-init:

-init-private:

-init-user:

-init-project:

-init-macrodef-property:

-do-init:

-post-init:

-init-check:

-init-ap-cmdline-properties:

-init-macrodef-javac-with-processors:

-init-macrodef-javac-without-processors:

-init-macrodef-javac:

-init-macrodef-test-impl:

-init-macrodef-junit-init:

-init-macrodef-junit-single:

-init-test-properties:

-init-macrodef-junit-batch:

-init-macrodef-junit:

-init-macrodef-junit-impl:

-init-macrodef-testng:

-init-macrodef-testng-impl:

-init-macrodef-test:

-init-macrodef-junit-debug:

-init-macrodef-junit-debug-batch:

-init-macrodef-junit-debug-impl:

-init-macrodef-test-debug-junit:

-init-macrodef-testng-debug:

-init-macrodef-testng-debug-impl:

-init-macrodef-test-debug-testng:

-init-macrodef-test-debug:

-init-macrodef-java:

-init-debug-args:

-init-macrodef-nbjpda:

-init-macrodef-nbjsdebug:

-init-macrodef-debug:

-init-taskdefs:

-init-ap-cmdline-supported:

-init-ap-cmdline:

init:

undeploy-clean:

deps-clean:

do-clean:
   [delete] Deleting directory C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build

check-clean:

-post-clean:

clean:

BUILD SUCCESSFUL
Total time: 3 seconds
```

### Salida completa de `compile`

```text
Buildfile: C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build.xml

-pre-init:

-init-private:

-init-user:

-init-project:

-init-macrodef-property:

-do-init:

-post-init:

-init-check:

-init-ap-cmdline-properties:

-init-macrodef-javac-with-processors:

-init-macrodef-javac-without-processors:

-init-macrodef-javac:

-init-macrodef-test-impl:

-init-macrodef-junit-init:

-init-macrodef-junit-single:

-init-test-properties:

-init-macrodef-junit-batch:

-init-macrodef-junit:

-init-macrodef-junit-impl:

-init-macrodef-testng:

-init-macrodef-testng-impl:

-init-macrodef-test:

-init-macrodef-junit-debug:

-init-macrodef-junit-debug-batch:

-init-macrodef-junit-debug-impl:

-init-macrodef-test-debug-junit:

-init-macrodef-testng-debug:

-init-macrodef-testng-debug-impl:

-init-macrodef-test-debug-testng:

-init-macrodef-test-debug:

-init-macrodef-java:

-init-debug-args:

-init-macrodef-nbjpda:

-init-macrodef-nbjsdebug:

-init-macrodef-debug:

-init-taskdefs:

-init-ap-cmdline-supported:

-init-ap-cmdline:

init:

deps-module-jar:

deps-ear-jar:

deps-jar:

-pre-pre-compile:
    [mkdir] Created dir: C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\web\WEB-INF\classes

-pre-compile:

-copy-manifest:
    [mkdir] Created dir: C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\web\META-INF
     [copy] Copying 1 file to C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\web\META-INF

-copy-persistence-xml:

-copy-webdir:
     [copy] Copying 13 files to C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\web
     [copy] Copied 4 empty directories to 1 empty directory under C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\web

library-inclusion-in-archive:

library-inclusion-in-manifest:

-do-compile:
    [mkdir] Created dir: C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\empty
    [mkdir] Created dir: C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\generated-sources\ap-source-output
    [javac] Compiling 33 source files to C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\web\WEB-INF\classes
     [copy] Copied 4 empty directories to 4 empty directories under C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava\build\web\WEB-INF\classes

-post-compile:

compile:

BUILD SUCCESSFUL
Total time: 9 seconds
```

### Resultado

- Limpieza: exitosa.
- Compilación: exitosa; 33 fuentes compilados.
- Errores: ninguno.
- Advertencias: ninguna.
- Correcciones Java necesarias: ninguna.
- No se generó ni inspeccionó el WAR, no se desplegó y no se validó la
  conexión; esas actividades pertenecen a la Fase 3 y no fueron autorizadas.

## Cambio de ubicación del proyecto

Después de las validaciones de compilación de la Fase 2, CAVA fue trasladado
desde `C:\Users\Maria Camila R\Documents\NetBeansProjects\Cava` a:

`C:\Users\Maria Camila R\Documents\Cava\Cava`

El cambio aisló el proyecto del repositorio Git accidental ubicado en el perfil
de Windows. La carpeta interna que contiene `build.xml`, `src/`, `web/`,
`database/`, `docs/`, `prompts/` y `nbproject/` es la raíz real comprobada.
El traslado no modifica el cierre técnico de la Fase 2. Las rutas anteriores
incluidas en las salidas históricas de Ant se conservan como evidencia válida
de las ejecuciones previas y no se reescriben. No se ejecutó una nueva
compilación en la ubicación actual durante la limpieza posterior.

## Cierre final de la Fase 2 — 17 de julio de 2026

### Seguridad y archivo SQL

- CAVA no tiene un repositorio Git propio. El repositorio descubierto en el
  directorio de usuario no tiene commits y considera todo CAVA como no
  rastreado, por lo que no constituía un respaldo utilizable del proyecto.
- Antes de eliminar el residual se creó la copia externa verificable
  `C:\Users\Maria Camila R\.codex\backups\CAVA\fase2-final-20260717-165412\cava residual.sql`.
- El archivo canónico y la copia residual medían 6015 bytes, tenían SHA-256
  `4297AA85AB4ED9CDBCB74743730B3B47196BFB0DA22AFDA9A3E0A8342ABCA023`
  y eran idénticos byte a byte.
- Se eliminó únicamente la copia residual. `database/cava.sql` conserva el
  mismo SHA-256 y es el único script principal canónico.
- No quedan referencias al nombre anterior ni duplicados SQL conocidos dentro
  del alcance de la Fase 2.
- Las 4 migraciones y los 33 archivos Java conservaron sus huellas previas.

### Revalidación de compilación

- Comando de limpieza:
  `& 'C:\Program Files\NetBeans-20\netbeans\extide\ant\bin\ant.bat' -f build.xml clean`
- Resultado de `clean`: `BUILD SUCCESSFUL`; tiempo informado por Ant: 2 segundos.
- Comando de compilación:
  `& 'C:\Program Files\NetBeans-20\netbeans\extide\ant\bin\ant.bat' -f build.xml compile`
- Resultado de `compile`: `BUILD SUCCESSFUL`; 33 fuentes compiladas; tiempo
  informado por Ant: 10 segundos.
- Errores: ninguno.
- Advertencias: ninguna.
- No se generó ni inspeccionó el WAR, no se desplegó y no se validaron JNDI,
  pool ni MySQL.

### Estado formal

- Fase 2: **CERRADA**.
- Fase 3: **NO INICIADA**.
- La Fase 3 solo puede comenzar con autorización explícita.

## Limpieza posterior al cierre — 17 de julio de 2026

- Se creó `.gitignore` en la raíz real con las exclusiones mínimas aprobadas.
- La copia de `FASE2_CORRECCION_ESTRUCTURAL.md` ubicada en la raíz de
  `prompts/` se eliminó después de comprobar que tenía 8442 bytes, SHA-256
  `8C91D0D3A5F7B34349DD232DEA329928A0F37A9E25040304E21220AA85616162`
  y contenido idéntico byte a byte al archivo conservado en
  `prompts/completadas/`.
- `01_CIERRE_FASE2.md` se movió a `prompts/completadas/` sin modificar sus
  2157 bytes ni su SHA-256
  `35D7B5F346F0D590642590E30E11B518E0C7F9F305592898FB631CBA3C859A02`.
- No se encontraron referencias activas a las antiguas rutas de estos prompts.
- `build/` se verificó como salida generada por Ant, sin Java, SQL, Markdown ni
  contenido único, y se eliminó por completo. Las dos copias JAR generadas que
  contenía desaparecieron con la carpeta; los cinco JAR protegidos de `lib/` y
  `web/WEB-INF/lib/` conservaron sus huellas.
- `dist/` no existía y no fue modificado. `nbproject/private/` permaneció en su
  ubicación y quedó ignorado.
- No se modificaron Java, SQL, migraciones, JAR protegidos, conexión ni
  configuración GlassFish.
- No se ejecutaron Ant, WAR, despliegue, JNDI, pool, MySQL ni la Fase 3.
- Fase 2: **CERRADA**.
- Fase 3: **NO INICIADA**.

## Pendiente fuera de la Fase 2

- Inspeccionar el WAR (Fase 3; no iniciada).
- Resolver la estrategia definitiva de librerías (Fase 3; no iniciada).
- Validar JNDI, pool y MySQL (Fase 3; no iniciada).
