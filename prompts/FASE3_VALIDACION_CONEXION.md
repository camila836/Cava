# Fase 3 — Validación de compilación, WAR y conexión JNDI

> **Estado: COMPLETADA el 18 de julio de 2026.** Este archivo se conserva como instrucción histórica. El cierre autoritativo está en `docs/auditorias/INFORME_FASE3.md`. La arquitectura final usa recursos JDBC globales, driver común en `domain1/lib` y WAR sin copia del Connector/J.

## Objetivo

Validar el proyecto actualizado sin implementar funcionalidades nuevas. La fase debe demostrar que el proyecto compila, despliega y conecta mediante `CavaPool` → `jdbc/CavaDS` → MariaDB sin `DriverManager`, con el driver oficial en el classloader común de GlassFish y sin una copia redundante en el WAR.

## Alcance obligatorio

1. Lee los seis documentos de `docs/` en el orden numerado.
2. Ejecuta **Clean and Build** desde NetBeans o el mecanismo reproducible disponible.
3. Si falla, corrige únicamente errores de paquetes, imports, rutas relativas o dependencias.
4. Inspecciona el WAR y lista el contenido de `WEB-INF/lib`.
5. Determina qué JAR usa NetBeans para compilar y cuál entra en el WAR.
6. No elimines una librería sin evidencia de que es redundante.
7. Revisa `Conexion.java`, `ConexionPool.java`, la ausencia del descriptor app-scoped `glassfish-resources.xml` y `glassfish-web.xml`.
8. Confirma que el pool se llama `CavaPool` y el recurso `jdbc/CavaDS`.
9. Despliega en GlassFish 7.
10. Prueba `SELECT 1` y una consulta real mediante un mecanismo temporal de diagnóstico.
11. Prueba MySQL apagado y credenciales inválidas.
12. Elimina cualquier endpoint temporal de diagnóstico después de las pruebas.
13. No implementes login, registro, dashboard, CRUD ni nuevos Servlets funcionales.

## Prohibiciones

- No usar `DriverManager`.
- No empaquetar el Connector/J en el WAR; conservar intacta la copia común ya validada en `domain1/lib`.
- No editar `domain.xml`.
- No usar rutas absolutas.
- No cambiar Models ni SQL funcional.
- No actualizar el driver sin justificar compatibilidad y registrar evidencia.
- No afirmar que la conexión funciona sin desplegar y ejecutar una consulta.

## Evidencia obligatoria

Entrega:

- resultado de Clean and Build;
- ruta y nombre del WAR;
- contenido de `WEB-INF/lib`;
- logs relevantes de despliegue;
- resultado del lookup JNDI;
- resultado de `SELECT 1`;
- resultado de una consulta real;
- resultado con MySQL apagado;
- resultado con credenciales inválidas;
- búsqueda global de `DriverManager`;
- archivos modificados;
- pendientes y riesgos.

## Criterio de cierre

La fase solo se marca como completada cuando compila, despliega, JNDI resuelve, la consulta real funciona y no existen pasos manuales ocultos. Si algo no puede probarse, deja la fase como **PENDIENTE** y explica exactamente qué falta.
