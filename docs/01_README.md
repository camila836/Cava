# CAVA — Sistema web para chocolates artesanales

> **Estado:** En desarrollo  
> **Arquitectura:** Java web por capas  
> **Backend:** Java 17, Jakarta EE 10, Servlets, JSP, JDBC y DAO  
> **Base de datos:** MySQL  
> **Servidor:** Eclipse GlassFish 7.0.9  
> **IDE principal:** Apache NetBeans

---

## 1. Descripción

CAVA es una aplicación web orientada a la comercialización y administración de chocolates artesanales.

El sistema está diseñado para ofrecer dos áreas principales:

1. Una vista para clientes, desde la cual podrán consultar productos, registrarse, iniciar sesión, administrar su carrito y realizar pedidos.
2. Un panel administrativo desde el cual se gestionarán los catálogos, productos, inventario, usuarios, pedidos, pagos y envíos sin modificar directamente el código.

El proyecto se desarrolla con una arquitectura por capas para mantener separadas la presentación, el control, las reglas de negocio, la persistencia y la conexión con MySQL.

---

## 2. Objetivo general

Desarrollar una aplicación web robusta, mantenible y escalable para gestionar la venta de chocolates artesanales, conservando una estructura clara que evite duplicaciones y permita ampliar el sistema de forma controlada.

---

## 3. Objetivos específicos

- Implementar registro, inicio y cierre de sesión.
- Administrar usuarios y roles.
- Gestionar categorías y productos.
- Gestionar inventario.
- Administrar pedidos y sus detalles.
- Registrar pagos.
- Gestionar envíos y transportadoras.
- Construir un panel administrativo con operaciones CRUD.
- Construir una vista de tienda para clientes.
- Centralizar la conexión mediante JNDI y Connection Pool.
- Evitar `DriverManager` y configuraciones manuales innecesarias.
- Mantener la documentación sincronizada con el código.

---

## 4. Tecnologías oficiales

### Backend

- Java 17.
- Jakarta EE 10.
- Servlets.
- JSP.
- JDBC.
- DAO.
- JNDI.
- `DataSource`.
- Connection Pool.

### Frontend

- HTML5.
- CSS3.
- Bootstrap.
- JavaScript.

### Persistencia

- MySQL.

### Herramientas

- Apache NetBeans.
- Eclipse GlassFish 7.0.9. La carpeta local `glassfish-7.0.25` conserva un
  nombre legado y no representa la versión real informada por `asadmin`.
- Ant.
- Git.

No se deben introducir tecnologías adicionales sin evaluar primero su necesidad y su impacto sobre la arquitectura.

---

## 5. Arquitectura resumida

```text
Usuario
→ JSP / HTML
→ JavaScript
→ Servlet
→ Service, cuando se necesite
→ DAO
→ Conexion
→ ConexionPool
→ JNDI jdbc/CavaDS
→ GlassFish
→ MySQL
```

Cada capa tiene una responsabilidad única.

- Los JSP presentan.
- JavaScript gestiona interacción.
- Los Servlets reciben solicitudes.
- Los Services coordinan reglas y transacciones.
- Los DAO ejecutan SQL.
- La capa de conexión entrega conexiones.
- MySQL conserva la información.

La arquitectura completa se encuentra en `docs/03_ARCHITECTURE.md`.

---

## 6. Estado actual conocido

El proyecto fue reiniciado tomando como base los Models existentes.

Posteriormente se añadieron DAO y clases de conexión. La Fase 3 cerró su validación reproducible de compilación, WAR, despliegue, JNDI, pool y consulta real el 18 de julio de 2026. La auditoría funcional completa de los DAO pertenece a fases posteriores.

La auditoría inicial encontró:

- Models existentes.
- DAO existentes.
- Conexión centralizada mediante JNDI.
- Recursos JDBC globales administrados por GlassFish (`CavaPool` y `jdbc/CavaDS`); el WAR no contiene un descriptor app-scoped.
- Scripts SQL.
- JSP y recursos frontend.
- Ausencia de Servlets funcionales.
- JSP duplicados en una carpeta incorrecta.
- DAO ubicados físicamente en `Controlador`, mientras el paquete declarado era `DAO` (decisión ya tomada: el paquete se corrige a `Controlador` para que coincida con la ubicación física; los archivos no se mueven).
- Uso de `double` en valores que deberían migrarse a `BigDecimal`.
- Dependencias duplicadas.
- Documentación que afirmaba validaciones no demostradas.

El estado detallado y actualizado se mantiene en `docs/02_PROJECT_STATE.md`.

---

## 7. Regla principal de desarrollo

Antes de crear nuevas funcionalidades debe completarse este proceso:

```text
auditar
→ ordenar estructura
→ validar conexión
→ consolidar base de datos
→ corregir Models
→ corregir DAO
→ probar
→ documentar
→ comenzar desarrollo funcional
```

No se debe iniciar autenticación, dashboard, carrito o pedidos mientras la infraestructura base permanezca inestable.

---

## 8. Documentación oficial

| Archivo | Responsabilidad |
|---|---|
| `docs/01_README.md` | Presentación y orientación inicial. |
| `docs/03_ARCHITECTURE.md` | Fundamentos, principios y reglas por capa. |
| `docs/03_ARCHITECTURE.md` | Arquitectura de conexión y persistencia (DAO, pool, transacciones). |
| `docs/03_ARCHITECTURE.md` | Backend web, frontend, seguridad y calidad. |
| `docs/04_DATABASE.md` | Reglas y diseño de la base de datos. |
| `docs/05_ROADMAP.md` | Plan de implementación fase por fase. |
| `docs/02_PROJECT_STATE.md` | Estado real, pendientes y decisiones. |
| `docs/06_AGENTS.md` | Reglas para IA y desarrolladores. |

Cada documento debe tener una responsabilidad propia y evitar repetir información innecesariamente.

---

## 9. Estructura objetivo

```text
CAVA/
├── src/java/
│   ├── Conexion/
│   ├── Controlador/   (clases DAO; package Controlador)
│   ├── Model/
│   ├── Servlets/      (clases Servlet; package Servlets)
│   ├── Service/
│   ├── Filter/
│   ├── DTO/
│   ├── Exception/
│   └── Util/
├── web/
│   ├── css/
│   ├── js/
│   ├── img/
│   ├── admin/
│   ├── usuario/
│   └── WEB-INF/
├── database/
└── docs/
```

No todas las carpetas deben crearse desde el inicio. Se crean cuando exista código real que las necesite.

---

## 10. Flujo de trabajo

Todo cambio debe seguir este orden:

```text
analizar
→ identificar archivos afectados
→ revisar dependencias
→ implementar
→ compilar
→ desplegar
→ probar
→ actualizar documentación
```

Una tarea no se considera terminada solamente porque el código compile.

---

## 11. Criterios generales de calidad

- No duplicar clases.
- No duplicar SQL.
- No abrir conexiones desde Servlets.
- No ejecutar SQL desde JSP.
- No usar `DriverManager`.
- No almacenar contraseñas en texto plano.
- No ocultar errores críticos devolviendo únicamente `false`.
- No introducir rutas absolutas.
- No depender de configuraciones ocultas de un equipo.
- No afirmar que algo fue probado sin evidencia.
- No iniciar una fase nueva si la anterior no fue cerrada.

---

## 12. Alcance inicial

La primera versión deberá cubrir:

- autenticación;
- usuarios;
- roles;
- categorías;
- productos;
- inventario;
- pedidos;
- detalles de pedido;
- pagos;
- envíos;
- panel administrativo;
- tienda de usuario.

Favoritos, reseñas, fidelización y verificación de cuenta se tratarán como ampliaciones posteriores, salvo que el alcance sea modificado formalmente.

---

## 13. Estado del documento

Este archivo es el punto de entrada del proyecto.

Debe mantenerse breve y actualizado. Los detalles técnicos pertenecen a los documentos especializados.
