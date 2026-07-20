# ROADMAP.md

# Plan maestro de implementación de CAVA

> **Regla:** no iniciar una fase nueva hasta cerrar los criterios de aceptación de la fase anterior.

---

## 1. Propósito

Este documento define el orden oficial de trabajo.

No describe el estado real. El estado se registra en `docs/02_PROJECT_STATE.md`.

Cada fase debe producir:

- análisis;
- cambios limitados;
- pruebas;
- evidencia;
- documentación actualizada;
- decisión de cierre.

---

# Fase 0 — Respaldo y control de cambios

## Objetivo

Proteger el estado original antes de cualquier corrección.

## Actividades

- Conservar el ZIP original.
- Crear repositorio Git.
- Crear una rama de estabilización.
- Agregar `.gitignore`.
- Registrar versiones del entorno.
- No agregar funcionalidades.

## Entregables

- respaldo;
- repositorio;
- inventario inicial.

## Criterios de aceptación

- [ ] Existe copia intacta.
- [ ] Git está configurado.
- [ ] Los cambios pueden compararse.
- [ ] No se perdió ningún archivo.

---

# Fase 1 — Auditoría estructural

## Objetivo

Comprender el proyecto real antes de modificarlo.

## Actividades

- Obtener árbol de archivos.
- Identificar tipo de proyecto.
- Revisar Java, Jakarta EE y GlassFish.
- Inventariar Models.
- Inventariar DAO.
- Inventariar conexión.
- Buscar Servlets.
- Revisar JSP.
- Revisar CSS y JavaScript.
- Revisar scripts SQL.
- Revisar dependencias.
- Buscar duplicados.
- Buscar rutas absolutas.
- Buscar `DriverManager`.
- Compilar sin corregir.
- Registrar errores reales.

## Entregables

- informe de auditoría;
- lista priorizada;
- mapa de dependencias.

## Criterios de aceptación

- [ ] Todos los archivos fueron inventariados.
- [ ] Los hallazgos tienen evidencia.
- [ ] Se distingue entre confirmado y supuesto.
- [ ] Existe orden de corrección.

---

# Fase 2 — Corrección de estructura

## Objetivo

Alinear carpetas, paquetes y recursos sin cambiar funcionalidad.

## Actividades

- Corregir `package DAO;` → `package Controlador;` en los 15 DAO existentes en `src/java/Controlador/` (no se mueven archivos; se conserva esa carpeta como ubicación oficial).
- Actualizar cualquier `import DAO.*;` en el resto del proyecto para que apunte a `Controlador.*`.
- Eliminar los JSP duplicados de `src/java/Servlets`; conservar esa carpeta para las futuras clases Java Servlet con `package Servlets;`.
- Confirmar que no existen archivos Java fuera de su paquete.
- Renombrar scripts ambiguos.
- Unificar librerías.
- Corregir documentación obsoleta.
- Ejecutar compilación limpia.

## Criterios de aceptación

- [ ] Paquetes coinciden con carpetas.
- [ ] No hay JSP dentro de `src/java`.
- [ ] No hay archivos duplicados innecesarios.
- [ ] Las dependencias usan rutas relativas.
- [ ] El proyecto compila.

---

# Fase 3 — Conexión y despliegue

**Estado: CERRADA el 18 de julio de 2026.** Los Pasos 4A y 4B pertenecen a esta fase. La evidencia está en `docs/auditorias/INFORME_FASE3.md` y `docs/auditorias/evidencias/fase3/`.

## Objetivo

Demostrar que la conexión JNDI y el pool funcionan sin configuraciones manuales ocultas.

## Actividades

- Revisar `ConexionPool`.
- Revisar `Conexion`.
- Confirmar retiro de `glassfish-resources.xml` app-scoped.
- Revisar `glassfish-web.xml`.
- Elegir versión del driver.
- Dejar una sola copia de ejecución del driver en el classloader común; excluirla del WAR.
- Verificar contenido del WAR.
- Crear usuario MySQL exclusivo.
- Desplegar en GlassFish.
- Ejecutar ping.
- Ejecutar `SELECT 1`.
- Probar una consulta real.
- Probar MySQL apagado.
- Probar credenciales inválidas.
- Revisar logs.
- Confirmar ausencia de `DriverManager`.

## Criterios de aceptación

- [x] `jdbc/CavaDS` resuelve como recurso global.
- [x] `CavaPool` existe como pool global.
- [x] El driver carga desde `domain1/lib` y no se empaqueta en el WAR.
- [x] El WAR se despliega.
- [x] `SELECT 1` y el ping funcionan.
- [x] Una consulta real mediante `Conexion.getConn()` funciona.
- [x] La conexión se recupera y persiste tras reinicios.
- [x] El driver común permanece intacto.
- [x] No hay pasos manuales ocultos.

---

# Fase 4 — Consolidación de base de datos

**Estado: CERRADA el 19 de julio de 2026.** Auditoría en
`docs/auditorias/INFORME_FASE4A.md`; implementación y validación temporal en
`docs/auditorias/INFORME_FASE4B.md`.

## Objetivo

Definir un esquema reproducible y coherente.

## Actividades

- Definir tablas de la primera versión.
- Consolidar script inicial.
- Ordenar migraciones.
- Separar datos iniciales.
- Revisar claves.
- Revisar índices.
- Revisar tipos.
- Revisar eliminación.
- Crear base desde cero.
- Comparar con Models.

## Criterios de aceptación

- [x] La base se crea desde cero.
- [x] Las 15 entidades principales están claras.
- [x] Las migraciones tienen orden y permanecen aplazadas.
- [x] No hay scripts contradictorios.
- [x] Los tipos SQL están consolidados; los ajustes de precisión Java quedan
  formalmente asignados a la Fase 5.

---

# Fase 5 — Corrección de Models

**Estado: CERRADA el 19 de julio de 2026.** La auditoría, implementación y
pruebas están documentadas en `docs/auditorias/INFORME_FASE5.md`.

## Objetivo

Alinear Java con MySQL.

## Actividades

- Construir matriz de correspondencia.
- Migrar dinero a `BigDecimal`.
- Revisar cantidades.
- Revisar fechas.
- Revisar booleanos.
- Revisar nombres.
- Revisar constructores.
- Mantener compatibilidad.

## Criterios de aceptación

- [x] Cada atributo tiene columna o un alias Java documentado.
- [x] Los tipos coinciden.
- [x] No hay `double` monetario ni decimal exacto en Java.
- [x] El proyecto compila.
- [x] Los cambios están documentados.

---

# Fase 6 — Auditoría y corrección de DAO

## Objetivo

Validar la capa de persistencia entidad por entidad.

## Orden sugerido

1. Roles.
2. TipoDocumento.
3. Ciudades.
4. UnidadesMedida.
5. CategoriaProductos.
6. EstadoEnvio.
7. MediosPagos.
8. Transportadoras.
9. Usuarios.
10. Productos.
11. Inventario.
12. PedidosCabeza.
13. PedidosDetalle.
14. Pagos.
15. Envios.

## Actividades por DAO

- Validar tabla.
- Validar columnas.
- Validar tipos.
- Validar SQL.
- Usar `PreparedStatement`.
- Usar `try-with-resources`.
- Revisar mapeo.
- Revisar excepciones.
- Probar CRUD real.
- Revisar transacciones.

## Criterios de aceptación

- [x] Los 15 DAO compilan.
- [x] Todos usan `Conexion.getConn()`.
- [ ] No hay SQL duplicado sin razón.
- [x] No hay fugas.
- [x] Los CRUD fueron probados.
- [x] Los errores se distinguen.

**Cierre 20 de julio de 2026:** Fase 6 cerrada con 15 DAO corregidos,
pruebas unitarias e integradas por `jdbc/CavaDS`, rollback confirmado y sin
cambios de esquema. Evidencia: `docs/auditorias/INFORME_FASE6B.md`.

---

# Fase 7 — Infraestructura web

## Objetivo

Crear la base común para Servlets.

## Actividades

- Crear las clases Servlet en `src/java/Servlets/` con `package Servlets;`.
- Definir rutas.
- Crear filtro UTF-8.
- Crear manejo de errores.
- Definir formato JSON.
- Definir navegación tradicional.
- Crear filtros de sesión.
- Crear autorización por rol.
- Definir DTO.
- Definir Services necesarios.
- Configurar logging.

## Criterios de aceptación

- [ ] Convenciones definidas.
- [ ] Filtros compilan.
- [ ] Errores no exponen información.
- [ ] Las rutas son coherentes.
- [ ] Existe una prueba de integración.

---

# Fase 8 — Autenticación y seguridad

## Objetivo

Implementar registro, login, sesión y permisos.

## Actividades

- Registro.
- Validación.
- Correo único.
- Hash seguro.
- Login.
- Sesión.
- Logout.
- Renovación de sesión.
- Protección de rutas.
- Rol admin.
- Mensajes seguros.
- CSRF.
- Pruebas.

## Criterios de aceptación

- [ ] Registro funciona.
- [ ] Contraseña no se guarda en texto plano.
- [ ] Login funciona.
- [ ] Logout invalida sesión.
- [ ] Usuario no autorizado es bloqueado.
- [ ] Admin está protegido.
- [ ] No se filtra información sensible.

---

# Fase 9 — Primer CRUD vertical

## Objetivo

Validar el patrón completo con una entidad simple.

## Módulo sugerido

Unidades de medida o categorías.

## Flujo

```text
JSP admin
→ JavaScript
→ Servlet
→ Service o DAO
→ MySQL
→ JSON
→ actualización visual
```

## Criterios de aceptación

- [ ] Listar.
- [ ] Crear.
- [ ] Editar.
- [ ] Eliminar o desactivar.
- [ ] Validar.
- [ ] Manejar errores.
- [ ] Persistir.
- [ ] Respetar permisos.
- [ ] No duplicar código.

---

# Fase 10 — Panel administrativo

## Objetivo

Extender el patrón aprobado.

## Orden

1. Catálogos.
2. Categorías.
3. Productos.
4. Inventario.
5. Usuarios.
6. Pedidos.
7. Pagos.
8. Envíos.
9. Reportes básicos.

Cada módulo se cierra antes de iniciar el siguiente.

---

# Fase 11 — Tienda de usuario

## Objetivo

Construir la experiencia del cliente.

## Actividades

- Catálogo.
- Detalle.
- Búsqueda.
- Filtros.
- Carrito.
- Perfil.
- Creación de pedido.
- Historial.
- Estado del pedido.

## Criterios de aceptación

- [ ] Navegación sin errores.
- [ ] Catálogo usa datos reales.
- [ ] Carrito funciona.
- [ ] Pedido usa transacción.
- [ ] Inventario se actualiza.
- [ ] Precios históricos se conservan.

---

# Fase 12 — Funciones adicionales

## Posibles módulos

- favoritos;
- reseñas;
- puntos;
- verificación de cuenta.

Solo se implementan después de cerrar la primera versión.

---

# Fase 13 — Calidad

## Actividades

- Pruebas unitarias.
- Pruebas de integración.
- Pruebas de sistema.
- Seguridad.
- Inyección SQL.
- XSS.
- CSRF.
- Sesiones.
- Accesibilidad.
- Rendimiento básico.
- Logs.
- Navegadores.
- Responsive.

---

# Fase 14 — Entrega

## Actividades

- Compilación limpia.
- WAR reproducible.
- Script SQL final.
- Datos iniciales.
- Guía de instalación.
- Guía de despliegue.
- Manual técnico.
- Manual de administrador.
- Manual de usuario.
- Checklist de entorno limpio.

## Criterios de aceptación

- [ ] Proyecto instalable desde cero.
- [ ] Sin rutas absolutas.
- [ ] Sin archivos externos ocultos.
- [ ] Documentación coherente.
- [ ] Checklist 100 % aprobado.
