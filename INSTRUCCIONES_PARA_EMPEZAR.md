# Cómo empezar con el proyecto CAVA actualizado

## 1. Conserva el proyecto anterior

No reemplaces inmediatamente tu copia original. Guarda ambos ZIP:

- proyecto original;
- proyecto actualizado.

## 2. Descomprime el ZIP actualizado

Extrae la carpeta `Cava` en una ubicación sencilla, por ejemplo:

```text
Documentos/Proyectos/Cava
```

Evita rutas demasiado largas o carpetas sincronizadas mientras realizas las primeras pruebas.

## 3. Abre el proyecto en NetBeans

1. Inicia Apache NetBeans.
2. Selecciona **File → Open Project**.
3. Elige la carpeta `Cava` que contiene `build.xml` y `nbproject`.
4. Confirma que NetBeans asocie Java 17 y GlassFish 7.

## 4. Revisa la documentación

Lee en este orden:

1. `docs/01_README.md`.
2. `docs/02_PROJECT_STATE.md`.
3. `docs/03_ARCHITECTURE.md`.
4. `docs/04_DATABASE.md`.
5. `docs/05_ROADMAP.md`.
6. `docs/06_AGENTS.md`.

## 5. Ejecuta Clean and Build

En NetBeans:

1. Haz clic derecho sobre el proyecto.
2. Selecciona **Clean and Build**.
3. No ejecutes todavía nuevas funcionalidades.
4. Guarda el resultado completo de la ventana Output.

La compilación estática de las clases Java ya fue validada. El build Ant fuera de NetBeans no pudo completarse porque el entorno externo no tiene configurada la tarea `CopyLibs` propia de NetBeans. Esto debe comprobarse desde el IDE.

## 6. Usa el prompt inicial

Abre:

```text
prompts/00_PROMPT_INICIAL.md
```

Copia todo su contenido y úsalo como primer mensaje para Codex, Claude o la IA que tenga acceso al proyecto completo.

Ese prompt ordena ejecutar únicamente:

```text
prompts/FASE3_VALIDACION_CONEXION.md
```

## 7. No avances si Clean and Build falla

Si aparece un error:

- entrega a la IA el texto completo del Output;
- permite corregir únicamente paquetes, rutas relativas o dependencias;
- no permitas crear login, Servlets funcionales, dashboard o CRUD.

## 8. Resultado esperado de la siguiente fase

La Fase 3 debe demostrar:

- WAR generado;
- contenido de `WEB-INF/lib` identificado;
- `CavaPool` registrado;
- `jdbc/CavaDS` resuelto;
- consulta `SELECT 1` exitosa;
- una consulta real exitosa;
- errores controlados con MySQL apagado y credenciales inválidas;
- ausencia de `DriverManager`;
- ausencia de cambios manuales en `domain1/lib` o `domain.xml`.
