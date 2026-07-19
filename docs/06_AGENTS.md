# AGENTS.md

# Reglas para trabajar sobre CAVA

> Este archivo aplica a Codex, ChatGPT, Claude, desarrolladores y revisores.

---

## 1. Objetivo

Garantizar que toda modificación se haga de forma ordenada, verificable y coherente con la arquitectura.

La prioridad no es escribir código rápidamente. La prioridad es conservar una base estable y evitar errores, duplicaciones y cambios innecesarios.

---

## 2. Documentos obligatorios

Antes de modificar código se debe leer, en este orden:

1. `docs/01_README.md`.
2. `docs/02_PROJECT_STATE.md`.
3. `docs/03_ARCHITECTURE.md`.
4. `docs/04_DATABASE.md`.
5. `docs/05_ROADMAP.md`.
6. `docs/06_AGENTS.md`.

Prioridad documental:

```text
estado real comprobado
→ decisiones aprobadas
→ arquitectura
→ roadmap
→ instrucciones de tarea
```

Si existe contradicción, se debe reportar antes de implementar.

---

## 3. Regla de estado

Nunca afirmar que algo:

- funciona;
- está validado;
- compila;
- despliega;
- conecta;
- es seguro;
- está terminado;

sin haberlo comprobado.

Debe distinguirse entre:

- existe;
- compila;
- despliega;
- fue probado;
- fue aprobado.

---

## 4. Antes de escribir código

Realizar:

1. Identificar el objetivo.
2. Leer archivos relacionados.
3. Revisar dependencias.
4. Buscar código existente.
5. Buscar clases o métodos duplicados.
6. Revisar esquema SQL.
7. Revisar Models.
8. Revisar DAO.
9. Enumerar archivos que se modificarán.
10. Limitar el alcance.

Si el usuario solicita auditoría primero, no se debe modificar nada durante esa etapa.

---

## 5. Alcance

No ampliar una tarea sin autorización.

Si se solicita corregir conexión:

- no implementar login;
- no modificar JSP;
- no rediseñar la base completa;
- no agregar frameworks.

Los problemas fuera del alcance deben registrarse como pendientes.

---

## 6. Código duplicado

Antes de crear una clase, método, utilidad, consulta o componente:

- buscar si ya existe;
- revisar si puede reutilizarse;
- revisar si debe corregirse;
- evitar crear una segunda versión.

Está prohibido crear:

- `Conexion2`;
- `ConexionNueva`;
- `UsuariosDAOFinal`;
- `ProductoDAOBackup`;
- Servlets duplicados;
- JSP duplicados;
- utilidades con la misma responsabilidad.

---

## 7. Arquitectura

> Detalle completo del flujo y reglas por capa: `docs/03_ARCHITECTURE.md` §5-7.

Flujo permitido:

```text
JSP / JavaScript
→ Servlet
→ Service, cuando se necesite
→ DAO
→ Conexion
→ ConexionPool
→ JNDI
→ MySQL
```

Prohibiciones (no negociables, sin excepción):

- SQL en JSP o Servlet.
- Conexión abierta en JSP o Servlet.
- Acceso a MySQL desde JavaScript.
- Lógica de presentación en DAO.
- Credenciales en código Java.
- `DriverManager`.

---

## 8. Conexión

> Diseño completo (`ConexionPool`, `Conexion`, `glassfish-resources.xml`,
> ciclo de vida, usuario MySQL, criterios de validación): `docs/03_ARCHITECTURE.md` §11-19.

Checklist de reglas duras antes de tocar cualquier archivo de conexión:

- No crear otra conexión (`Conexion2`, `ConexionNueva`, etc.).
- No copiar el driver a `domain1/lib`.
- No modificar `domain.xml`.
- No usar rutas absolutas.
- No registrar el pool manualmente sin documentarlo.
- No almacenar una `Connection` como atributo de un DAO.
- No afirmar que el pool funciona sin desplegar y probar.

La meta es minimizar pasos externos.

---

## 9. DAO

> Patrón de métodos, manejo de excepciones y transacciones:
> `docs/03_ARCHITECTURE.md` §20-31.

**Ubicación oficial (decisión aprobada):** `src/java/Controlador/`,
`package Controlador;`. No existe una carpeta ni paquete separado llamado
`DAO` — el término "DAO" en toda la documentación se refiere al patrón de
acceso a datos, no a un nombre de paquete literal.

Checklist de reglas duras:

- Usar `Conexion.getConn()`, `PreparedStatement`, `try-with-resources`.
- Mapear columnas explícitas, respetar tipos (ver `docs/04_DATABASE.md` §5-6).
- Separar SQL de negocio.
- No agregar métodos no solicitados sin necesidad comprobada.

Antes de modificar un DAO: revisar el Model, la tabla, columnas, claves,
usos y pruebas existentes.

---

## 10. Models

> Reglas de diseño completas: `docs/03_ARCHITECTURE.md` §8.

No modificar Models de forma automática. Antes de cambiar un atributo:
revisar SQL, todos los DAO, JSP, JavaScript, serialización y constructores.

Dinero y decimales exactos deben migrarse a `BigDecimal`.

---

## 11. Base de datos

No inventar nombres de tablas o columnas.

Usar únicamente el esquema oficial.

No editar una migración ya aplicada en un entorno compartido. Crear una nueva migración.

No usar eliminación en cascada sin revisar trazabilidad.

---

## 12. Servlets

**Ubicación oficial:** `src/java/Servlets/`, con declaración
`package Servlets;`. Esta carpeta contiene exclusivamente clases Java Servlet.
No debe contener JSP, HTML, CSS ni JavaScript.

Los Servlets deben:

- usar variables locales;
- validar servidor;
- verificar sesión;
- verificar rol;
- manejar UTF-8;
- devolver estados HTTP coherentes;
- no exponer stack traces.

No guardar información de una solicitud en atributos de instancia.

---

## 13. Seguridad

Obligatorio:

- contraseñas con hash seguro;
- `PreparedStatement`;
- validación del servidor;
- autorización por rol;
- sesiones controladas;
- protección CSRF;
- escape de datos;
- mensajes seguros;
- logs sin secretos.

Nunca registrar:

- contraseñas;
- hashes;
- tokens;
- cookies;
- credenciales;
- datos financieros sensibles.

---

## 14. Frontend

No asumir que un ID o clase existe. Revisar el JSP real.

No usar datos simulados como si fueran persistencia final.

No usar `alert()` como mecanismo principal.

No duplicar CSS o JavaScript por página si puede reutilizarse.

---

## 15. Dependencias

- Usar rutas relativas.
- Evitar copias duplicadas.
- Documentar versiones.
- No depender de `nbproject/private`.
- No añadir librerías sin justificar.
- No migrar a Maven o Gradle sin aprobación.

---

## 16. Refactorización

No hacer refactorizaciones masivas junto con una corrección funcional.

Separar:

```text
cambio estructural
→ prueba
→ cambio funcional
→ prueba
```

Cada cambio debe poder revisarse y revertirse.

---

## 17. Pruebas

Después de modificar:

1. Compilar.
2. Construir WAR.
3. Desplegar cuando proceda.
4. Ejecutar prueba mínima.
5. Probar error.
6. Revisar logs.
7. Confirmar que no se rompió otro flujo.
8. Documentar evidencia.

Si no se puede ejecutar una prueba, debe indicarse explícitamente.

---

## 18. Errores

No ocultar un error crítico devolviendo siempre `false`.

No mostrar detalles técnicos al usuario.

Separar:

- mensaje para usuario;
- excepción técnica;
- log.

No usar `printStackTrace()` como solución definitiva.

---

## 19. Documentación

Después de un cambio aprobado actualizar:

- `docs/02_PROJECT_STATE.md`;
- `docs/05_ROADMAP.md`, si cambia una fase;
- `docs/04_DATABASE.md`, si cambia SQL;
- `docs/03_ARCHITECTURE.md`, si cambia una decisión técnica;
- comentarios técnicos necesarios.

No duplicar la misma explicación en todos los archivos.

---

## 20. Respuesta de una IA

Antes de implementar, la IA debe indicar:

- qué encontró;
- qué archivos afectan;
- qué riesgo existe;
- qué modificará;
- qué no modificará;
- cómo probará.

Después debe entregar:

- archivos modificados;
- resumen de cambios;
- pruebas ejecutadas;
- resultados;
- riesgos;
- pendientes;
- estado de la fase.

---

## 21. Prohibiciones

- No inventar evidencia.
- No afirmar que una prueba pasó sin ejecutarla.
- No borrar código sin revisar usos.
- No cambiar nombres masivamente sin mapa de impacto.
- No saltar fases.
- No introducir frameworks por preferencia personal.
- No mezclar varias funcionalidades en una tarea.
- No reemplazar código estable sin necesidad.
- No crear archivos externos ocultos.
- No tocar la instalación de GlassFish salvo necesidad demostrada.
- No usar credenciales personales.
- No continuar silenciosamente ante una contradicción importante.

---

## 22. Criterio de finalización

Una tarea termina cuando:

- [ ] El alcance fue cumplido.
- [ ] El código compila.
- [ ] La prueba correspondiente fue ejecutada.
- [ ] Los errores fueron revisados.
- [ ] No se añadió duplicación.
- [ ] La arquitectura se respeta.
- [ ] La documentación se actualizó.
- [ ] El estado real quedó registrado.
