# Cierre de Fase 2 — Compilación y consolidación documental

Antes de empezar, lee (en orden): `docs/01_README.md` → `docs/02_PROJECT_STATE.md` → `docs/06_AGENTS.md`.

No has sido autorizado a avanzar a Fase 3 (`prompts/FASE3_VALIDACION_CONEXION.md`). Tu única tarea es cerrar la Fase 2.

## Qué falta (confirmado en `docs/02_PROJECT_STATE.md` §5 y §9)

La corrección estructural ya se aplicó: los 15 DAO declaran `package Controlador;`, no hay imports `DAO.*`, no hay `DriverManager`, el script se llama `cava.sql`. Lo único que falta es la prueba de compilación.

## Tarea única

1. Ejecuta **Clean and Build** sobre el proyecto (`ant -f build.xml clean`, luego `ant -f build.xml compile` o el mecanismo reproducible disponible en tu entorno; si tienes NetBeans, hazlo desde ahí).
2. Registra el resultado exacto: comando usado, salida completa, errores y advertencias.
3. Si falla, corrige **únicamente** errores de paquete, import o ruta relacionados con el cambio `DAO` → `Controlador`. No toques lógica, SQL, Models ni JSP.
4. Si no puedes ejecutar la compilación en tu entorno (falta NetBeans/Ant/GlassFish), dilo explícitamente y entrega los pasos exactos para que se ejecute manualmente. No asumas ni afirmes que compiló sin evidencia.

## Actualiza `docs/02_PROJECT_STATE.md`

- §5 "Estructura": cambia `Corregida; pendiente Clean and Build` por el resultado real.
- §9 "DAO: criterio de cierre": marca `[x] Todos compilan` solo si tienes evidencia real; si no, deja `[ ]` y anota por qué.
- §6 "Próxima fase oficial": si todo cierra, indica que la próxima fase es Fase 3 (conexión); si no, indica qué falta.

## Prohibido en esta tarea

- Iniciar Fase 3 (conexión, JNDI, pool, GlassFish).
- Modificar Models, SQL, JSP o clases de conexión.
- Eliminar librerías duplicadas (`mysql-connector`, `jstl`) — eso es Fase 3.
- Afirmar un resultado sin comando y salida que lo respalden.

## Entrega

Informe corto: comando ejecutado, resultado, archivos modificados (si hubo), y confirmación de que `docs/02_PROJECT_STATE.md` quedó actualizado sin contradicciones. Detente ahí y espera autorización para Fase 3.
