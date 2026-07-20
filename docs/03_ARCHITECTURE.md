# ARCHITECTURE.md — Parte 1

# Fundamentos y arquitectura general de CAVA

> **Proyecto:** CAVA — Plataforma web para chocolates artesanales  
> **Versión:** 1.0  
> **Estado:** Arquitectura objetivo; no equivale a implementación validada.

---

## 1. Propósito

Este documento define la arquitectura oficial objetivo del proyecto CAVA.

Cuando exista una diferencia entre este documento y el código:

1. se identifica la contradicción;
2. se revisa el impacto;
3. se decide si debe corregirse el código o actualizarse la arquitectura;
4. se documenta la decisión;
5. se implementa después del análisis.

No se debe modificar código automáticamente únicamente para hacerlo coincidir con la documentación.

---

## 2. Objetivos

- Separar responsabilidades.
- Evitar duplicación.
- Facilitar mantenimiento.
- Implementar módulos de manera independiente.
- Centralizar el acceso a MySQL.
- Evitar conexiones manuales.
- Facilitar despliegues en otros equipos.
- Mantener dependencias dentro del proyecto cuando sea viable.
- Permitir que una persona o IA comprenda el sistema antes de modificarlo.
- Proteger información, sesiones y permisos.

---

## 3. Principios

### Responsabilidad única

- JSP presenta.
- JavaScript gestiona interacción.
- Servlet controla solicitudes.
- Service aplica reglas.
- DAO persiste.
- Conexion entrega conexiones.
- MySQL almacena.

### Bajo acoplamiento

Los DAO conocen `Conexion.getConn()`, pero no los detalles internos del `DataSource`.

### Alta cohesión

Cada clase debe contener lógica relacionada con una única responsabilidad.

### Simplicidad

No se agregan frameworks o patrones sin necesidad.

### Portabilidad

Se minimizan configuraciones manuales externas.

### Evolución controlada

```text
entorno
→ estructura
→ conexión
→ base de datos
→ Models
→ DAO
→ infraestructura web
→ autenticación
→ administración
→ tienda
→ pruebas
```

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
- DataSource.
- Connection Pool.

### Frontend

- HTML5.
- CSS3.
- Bootstrap.
- JavaScript.

### Persistencia y servidor

- MySQL.
- Eclipse GlassFish 7.0.9.

### Herramientas

- Apache NetBeans.
- Ant.
- Git.

No se incorporarán Spring, JPA, Hibernate, CDI, Maven, Gradle u otras tecnologías sin evaluación y aprobación.

---

## 5. Arquitectura general

```text
Usuario
  │
  ▼
Navegador
  │
  ├── JSP / HTML
  ├── CSS / Bootstrap
  └── JavaScript
          │
          ▼
       Servlet
          │
          ▼
 Service, cuando se necesite
          │
          ▼
         DAO
          │
          ▼
   Conexion.getConn()
          │
          ▼
     ConexionPool
          │
          ▼
 DataSource JNDI jdbc/CavaDS
          │
          ▼
 Connection Pool GlassFish
          │
          ▼
         MySQL
```

Ninguna capa puede saltarse las responsabilidades de otra.

---

## 6. Organización objetivo

```text
src/java/
├── Conexion/
├── Controlador/   (paquete y carpeta oficial de los DAO)
├── Model/
├── Servlets/      (paquete y carpeta oficial de las clases Servlet)
├── Service/
├── Filter/
├── DTO/
├── Exception/
└── Util/
```

```text
web/
├── css/
├── js/
├── img/
├── admin/
├── usuario/
├── componentes/
└── WEB-INF/
```

Las carpetas se crean cuando exista una clase real que las necesite. Los JSP no deben almacenarse dentro de `src/java`.

Reglas de ubicación aprobadas:

- `src/java/Controlador/` con `package Controlador;`: contiene los DAO.
- `src/java/Servlets/` con `package Servlets;`: contiene exclusivamente las clases Servlet.
- `src/java/Servlets/` no debe contener JSP, HTML, CSS ni JavaScript.

---

## 7. Reglas por capa

### JSP

- presentación;
- formularios;
- componentes;
- sin SQL;
- sin conexiones;
- sin lógica sensible.

### JavaScript

- eventos;
- validaciones de experiencia;
- solicitudes HTTP;
- actualización visual;
- nunca reemplaza validación del servidor.

### Servlet

- recibe;
- valida;
- verifica sesión;
- coordina;
- responde;
- sin SQL ni conexiones.

### Service

- reglas;
- transacciones;
- coordinación de varios DAO.

### DAO

- SQL;
- parámetros;
- mapeo;
- persistencia.

### Conexión

- entrega conexiones;
- no ejecuta lógica de negocio.

### MySQL

- integridad;
- relaciones;
- tipos;
- índices;
- persistencia.

---

## 8. Models

Los Models deben contener:

- atributos;
- constructores necesarios;
- getters y setters;
- tipos compatibles;
- métodos simples propios de la entidad cuando proceda.

No deben:

- abrir conexiones;
- ejecutar SQL;
- conocer Servlets;
- generar HTML;
- contener credenciales.

El dinero y los decimales exactos usarán `BigDecimal`.

---

# ARCHITECTURE.md — Parte 2

# Arquitectura de conexión y persistencia

> Esta parte continúa el documento principal de arquitectura de CAVA.

---

## 11. Objetivo de la conexión

La conexión de CAVA debe:

- usar un pool de conexiones;
- resolverse mediante JNDI;
- centralizarse;
- evitar completamente `DriverManager`;
- impedir que los DAO conozcan credenciales o URL JDBC;
- minimizar pasos manuales en GlassFish;
- cargar el driver desde el classloader común de GlassFish cuando el pool sea global;
- permitir despliegues reproducibles;
- cerrar correctamente `Connection`, `PreparedStatement` y `ResultSet`.

La meta de portabilidad es:

```text
Clonar o copiar el proyecto
→ abrirlo en NetBeans
→ crear la base y el usuario MySQL
→ compilar
→ desplegar
```

La arquitectura aprobada usa un pool global. Por ello el driver oficial debe estar disponible en `domain1/lib`; el WAR no debe empaquetar otra copia. La provisión de recursos se ejecuta con comandos `asadmin` documentados y no mediante edición manual de `domain.xml`.

---

## 12. Razón para usar Connection Pool

Crear una conexión física a MySQL tiene un costo. Un pool conserva conexiones administradas y las reutiliza.

Beneficios:

- menor latencia;
- uso controlado de recursos;
- límite de conexiones;
- validación;
- recuperación;
- monitoreo;
- administración centralizada.

Cuando el código cierra una conexión obtenida desde un `DataSource`, normalmente la devuelve al pool. Por esta razón, cada conexión debe manejarse con `try-with-resources`.

---

## 13. Razón para usar JNDI

JNDI permite que el código solicite un recurso mediante un nombre lógico:

```text
jdbc/CavaDS
```

El código Java no necesita conocer:

- URL física;
- puerto;
- usuario;
- contraseña;
- tamaño del pool;
- propiedades específicas del servidor.

El recurso JNDI desacopla la aplicación de la configuración física de la base de datos.

---

## 14. Componentes de conectividad

### 14.1 Recursos JDBC globales

La arquitectura validada declara fuera del WAR:

- un `jdbc-connection-pool` global llamado `CavaPool`;
- `datasource-classname="com.mysql.cj.jdbc.MysqlDataSource"`;
- un recurso global `jdbc/CavaDS`;
- servidor, puerto, base y usuario administrados por GlassFish;
- la contraseña mediante el alias cifrado `cava.mysql.password`;
- validación de conexiones mediante la tabla `productos`.

`web/WEB-INF/glassfish-resources.xml` fue retirado porque creaba recursos
app-scoped y contenía una configuración anterior basada en `root`. El WAR final
no contiene ese descriptor. La provisión global debe realizarse con `asadmin` y
evidencia sanitizada, sin editar manualmente `domain.xml`.

### 14.2 `glassfish-web.xml`

Debe contener únicamente la configuración necesaria para la aplicación.

El atributo del classloader no se utiliza para resolver el driver del pool global. No se debe cambiar `delegate` por costumbre, sino únicamente cuando exista evidencia de un conflicto de classloading.

### 14.3 Driver MySQL

Ubicación oficial validada:

```text
C:\Users\Maria Camila R\Documents\glassfish\glassfish-7.0.25\glassfish\domains\domain1\lib\mysql-connector-java-8.0.12.jar
```

Requisitos:

- disponible para el classloader común de GlassFish;
- ausente del WAR;
- compatible con Java 17;
- compatible con MySQL y Eclipse GlassFish 7.0.9;
- una sola copia de ejecución;
- ubicación operativa documentada para la instalación local autorizada.

Las copias fuente heredadas en `lib/` y `WEB-INF/lib` son redundantes. El cierre intentó retirarlas, pero la ACL del entorno impidió la eliminación; se conservan respaldadas y excluidas de `build` y del WAR mediante `build.web.excludes`. No son copias de ejecución.

---

## 15. Diseño de `ConexionPool.java`

Responsabilidad única:

- localizar el `DataSource`;
- conservarlo;
- entregar conexiones.

Diseño de referencia:

```java
package Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class ConexionPool {

    private static final String JNDI_NAME = "jdbc/CavaDS";
    private static volatile DataSource dataSource;

    private ConexionPool() {
    }

    private static DataSource getDataSource() throws SQLException {
        DataSource local = dataSource;

        if (local == null) {
            synchronized (ConexionPool.class) {
                local = dataSource;

                if (local == null) {
                    try {
                        InitialContext context = new InitialContext();
                        local = (DataSource) context.lookup(JNDI_NAME);
                        dataSource = local;
                    } catch (NamingException ex) {
                        throw new SQLException(
                            "No fue posible resolver el recurso JNDI " + JNDI_NAME,
                            ex
                        );
                    }
                }
            }
        }

        return local;
    }

    public static Connection getConn() throws SQLException {
        return getDataSource().getConnection();
    }
}
```

Este código es una referencia arquitectónica. Antes de reemplazar una implementación existente se debe revisar el código real.

Reglas:

- clase final;
- constructor privado;
- nombre JNDI constante;
- `DataSource` cacheado;
- sin credenciales;
- sin URL JDBC;
- sin `DriverManager`;
- sin pruebas embebidas;
- sin imprimir datos sensibles.

---

## 16. Diseño de `Conexion.java`

`Conexion.java` funciona como fachada mínima:

```java
package Conexion;

import java.sql.Connection;
import java.sql.SQLException;

public final class Conexion {

    private Conexion() {
    }

    public static Connection getConn() throws SQLException {
        return ConexionPool.getConn();
    }
}
```

La clase no debe:

- crear pools;
- repetir el lookup;
- contener credenciales;
- usar `DriverManager`;
- ejecutar SQL;
- conservar conexiones abiertas.

Su propósito es permitir que todos los DAO dependan de una interfaz estable:

```java
Connection connection = Conexion.getConn();
```

---

## 17. Ciclo de vida de una conexión

```text
DAO solicita Connection
→ Conexion delega
→ ConexionPool obtiene el DataSource
→ DataSource entrega una Connection
→ DAO ejecuta SQL
→ try-with-resources cierra la Connection
→ la Connection vuelve al pool
```

Un DAO nunca debe guardar una conexión como atributo de instancia:

```java
private Connection connection;
```

Ese patrón puede producir:

- conexiones vencidas;
- problemas de concurrencia;
- fugas;
- uso simultáneo por varias solicitudes;
- errores difíciles de reproducir.

---

## 18. Validación real de la conexión

La conexión solo se considera validada cuando existe evidencia de:

- compilación limpia;
- WAR generado;
- driver presente una sola vez;
- despliegue exitoso;
- registro del pool;
- resolución de `jdbc/CavaDS`;
- consulta `SELECT 1`;
- operación real sobre una tabla;
- respuesta controlada con MySQL apagado;
- respuesta controlada con credenciales inválidas;
- ausencia de `DriverManager`;
- ausencia de pasos manuales ocultos.

Un mensaje escrito en consola que diga “conexión exitosa” no constituye una prueba suficiente.

---

## 19. Usuario MySQL

No debe usarse `root` como configuración definitiva.

Debe crearse un usuario específico, por ejemplo:

```text
cava_app
```

Permisos:

- acceso únicamente a la base de CAVA;
- solo operaciones necesarias;
- sin privilegios administrativos globales.

Las credenciales de desarrollo pueden estar temporalmente en el descriptor si el proyecto es académico y local, pero nunca deben reutilizarse contraseñas personales ni publicarse credenciales reales.

---

## 20. Arquitectura DAO

Cada Model persistente tendrá un DAO cuando el módulo lo necesite.

Responsabilidades:

- insertar;
- actualizar;
- eliminar o desactivar;
- consultar por ID;
- listar;
- ejecutar consultas especializadas justificadas;
- mapear filas a Models;
- participar en transacciones.

No todos los DAO tienen que exponer exactamente las mismas operaciones. Una entidad histórica, por ejemplo, podría no permitir eliminación física.

---

## 21. Patrón de métodos DAO

Ejemplo conceptual:

```java
package Controlador;

public boolean insertar(Productos producto) {
    String sql = """
        INSERT INTO productos (
            descripcionProductos,
            precioProductos,
            idUnidadesMedida,
            idCategoriaProductos
        ) VALUES (?, ?, ?, ?)
        """;

    try (
        Connection connection = Conexion.getConn();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
        statement.setString(1, producto.getDescripcionProductos());
        statement.setBigDecimal(2, producto.getPrecioProductos());
        statement.setInt(3, producto.getIdUnidadesMedida());
        statement.setInt(4, producto.getIdCategoriaProductos());

        return statement.executeUpdate() > 0;
    } catch (SQLException ex) {
        throw DataAccessException.from("insertar producto", ex);
    }
}
```

El ejemplo utiliza la convención oficial de CAVA: tabla y columnas en
`camelCase`, clase `Productos` en `PascalCase` y DAO dentro de
`package Controlador;`. Los nombres deben confirmarse siempre contra el script
SQL y el Model reales antes de copiar el método al código.

El patrón definitivo se aprobará antes de refactorizar todos los DAO.

---

## 22. Manejo de excepciones

No se recomienda ocultar todos los errores devolviendo `false`.

Este patrón:

```java
catch (SQLException ex) {
    System.err.println(ex.getMessage());
    return false;
}
```

no permite distinguir:

- operación sin cambios;
- duplicado;
- conexión caída;
- SQL inválido;
- violación de clave foránea;
- error interno.

Arquitectura objetivo:

```text
SQLException
→ excepción de persistencia
→ Service o Servlet
→ mensaje seguro
→ log técnico
```

Ejemplo:

```java
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DataAccessException from(
            String operation,
            SQLException cause
    ) {
        return new DataAccessException(
            "Error de persistencia al " + operation,
            cause
        );
    }
}
```

La migración a excepciones personalizadas debe hacerse gradualmente para evitar romper todos los DAO al mismo tiempo.

---

## 23. Logging

No deben utilizarse indiscriminadamente:

```java
System.out.println(...)
System.err.println(...)
ex.printStackTrace()
```

Los logs pueden incluir:

- operación;
- componente;
- identificador no sensible;
- `SQLState`;
- causa técnica.

Nunca deben incluir:

- contraseñas;
- hashes;
- tokens;
- identificadores de sesión;
- credenciales JDBC;
- datos financieros sensibles.

---

## 24. `PreparedStatement`

Toda entrada variable se envía mediante parámetros.

Correcto:

```java
String sql = "SELECT * FROM usuarios WHERE correo = ?";
```

Incorrecto:

```java
String sql = "SELECT * FROM usuarios WHERE correo = '" + correo + "'";
```

Los nombres dinámicos de tablas y columnas no pueden parametrizarse. Cuando sean necesarios, deben seleccionarse desde listas internas permitidas, nunca desde texto libre enviado por el usuario.

---

## 25. Mapeo de resultados

Cuando el mapeo se reutilice, debe centralizarse:

```java
private Usuario mapearUsuario(ResultSet resultSet) throws SQLException {
    Usuario usuario = new Usuario();
    usuario.setId(resultSet.getInt("id"));
    usuario.setCorreo(resultSet.getString("correo"));
    return usuario;
}
```

Se prefieren nombres de columna explícitos en lugar de posiciones numéricas.

---

## 26. Tipos decimales

Se utilizará `BigDecimal` para:

- precios;
- subtotales;
- totales;
- impuestos;
- descuentos;
- pagos;
- stock fraccionario;
- cantidades exactas.

Java:

```java
BigDecimal
```

JDBC:

```java
setBigDecimal(...)
getBigDecimal(...)
```

MySQL:

```text
DECIMAL(p, s)
```

No debe usarse `double` para dinero.

---

## 27. Transacciones

Una transacción es obligatoria cuando varias operaciones forman una única unidad.

Ejemplo:

```text
crear cabecera del pedido
→ crear detalles
→ descontar inventario
→ registrar pago
```

Si una operación falla, deben revertirse las anteriores.

Patrón:

```java
try (Connection connection = Conexion.getConn()) {
    try {
        connection.setAutoCommit(false);

        pedidoDAO.insertar(connection, pedido);
        detalleDAO.insertarTodos(connection, detalles);
        inventarioDAO.descontar(connection, detalles);

        connection.commit();
    } catch (Exception ex) {
        connection.rollback();
        throw ex;
    } finally {
        connection.setAutoCommit(true);
    }
}
```

Los DAO usados dentro de la transacción deben aceptar una conexión existente. No deben abrir una conexión distinta para cada paso.

---

## 28. Eliminación

Antes de implementar `DELETE`, debe definirse por entidad:

- eliminación física;
- desactivación;
- cambio de estado;
- conservación histórica;
- restricciones por relaciones.

Pedidos, pagos y envíos requieren trazabilidad y normalmente no deberían eliminarse físicamente.

---

## 29. Paginación

Los listados grandes deben paginarse desde SQL.

Usar:

- `LIMIT`;
- `OFFSET` o paginación por cursor;
- filtros autorizados;
- orden estable.

No se debe descargar una tabla completa para recortarla en JavaScript.

---

## 30. Consultas N+1

Debe evitarse ejecutar una consulta adicional por cada fila.

Incorrecto:

```text
listar 100 productos
→ consultar categoría 100 veces
```

Correcto:

- `JOIN`;
- carga por lote;
- consulta agrupada;
- DTO de lectura.

---

## 31. Criterios de aceptación de persistencia

La capa queda aprobada cuando:

- [ ] Compila.
- [ ] Usa una única fuente de conexión.
- [ ] Usa `PreparedStatement`.
- [ ] Cierra todos los recursos.
- [ ] Mapea correctamente.
- [ ] Respeta los tipos.
- [ ] Maneja registros inexistentes.
- [ ] Maneja duplicados.
- [ ] No oculta fallos críticos.
- [ ] Tiene pruebas reales.
- [ ] No contiene lógica de presentación.
- [ ] No contiene credenciales.
- [ ] No usa `DriverManager`.

---

# ARCHITECTURE.md — Parte 3

# Backend web, frontend, seguridad y calidad

> Esta parte continúa después de la arquitectura de conexión y persistencia.

---

## 32. Servlets

Los Servlets son controladores HTTP.

Deben:

- declarar rutas claras;
- definir métodos HTTP;
- validar entrada;
- verificar autenticación;
- verificar autorización;
- invocar Services o DAO;
- devolver una respuesta coherente;
- manejar errores.

No deben:

- contener SQL;
- abrir conexiones;
- incluir credenciales;
- generar HTML extenso;
- repetir validaciones compartidas;
- guardar datos de una solicitud en atributos de instancia.

Los Servlets son reutilizados por varias solicitudes. Los datos de cada solicitud deben permanecer en variables locales.

---

## 33. Métodos HTTP

Convención recomendada:

| Operación | Método |
|---|---|
| Consultar | GET |
| Crear | POST |
| Reemplazar o actualizar | PUT |
| Actualización parcial | PATCH |
| Eliminar o desactivar | DELETE |

Si por restricciones del proyecto los formularios JSP usan principalmente GET y POST, se documentará una convención consistente.

---

## 34. Tipos de respuesta

CAVA puede utilizar dos flujos.

### Navegación tradicional

```text
Servlet
→ request.setAttribute(...)
→ forward a JSP
```

Adecuado para páginas renderizadas por el servidor.

### Solicitudes asíncronas

```text
JavaScript fetch
→ Servlet
→ JSON
```

Adecuado para CRUD dinámicos, modales y el panel administrativo.

No todos los Servlets deben devolver JSON.

Ejemplo exitoso:

```json
{
  "ok": true,
  "message": "Producto creado correctamente",
  "data": {
    "id": 25
  }
}
```

Ejemplo de error:

```json
{
  "ok": false,
  "message": "No fue posible crear el producto",
  "errors": {
    "nombre": "El nombre es obligatorio"
  }
}
```

Nunca se debe devolver un stack trace al navegador.

---

## 35. Codificación UTF-8

Todas las solicitudes y respuestas deben usar UTF-8.

```java
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
```

Para JSON:

```text
application/json;charset=UTF-8
```

Se recomienda centralizar esta configuración mediante un filtro.

---

## 36. Validación

La validación tiene dos niveles.

### Cliente

- campos requeridos;
- formato;
- longitud;
- experiencia inmediata.

### Servidor

- obligatoriedad;
- longitud;
- formato;
- permisos;
- integridad;
- reglas de negocio;
- existencia de relaciones;
- duplicados.

El servidor siempre debe validar nuevamente.

---

## 37. DTO

Un DTO se utiliza cuando:

- un formulario no coincide con el Model;
- una respuesta combina varias entidades;
- no deben exponerse campos internos;
- se necesita una estructura específica;
- se desea separar credenciales del Model persistente.

No se deben crear DTO duplicados o vacíos sin una necesidad concreta.

---

## 38. Services

Debe crearse una clase Service cuando un flujo:

- usa varios DAO;
- requiere una transacción;
- aplica varias reglas;
- calcula totales;
- modifica inventario;
- coordina pedido, pago y envío.

Ejemplos:

```text
AuthService
ProductoService
InventarioService
PedidoService
```

Responsabilidades:

- reglas de negocio;
- coordinación;
- transacciones;
- cálculos;
- cambios de estado.

No debe contener HTML ni depender de JSP.

---

## 39. JSP

Los JSP deben concentrarse en presentación.

Se permite:

- Expression Language;
- JSTL correctamente configurado;
- etiquetas;
- inclusiones;
- condiciones simples;
- datos preparados por el Servlet.

Se debe evitar:

- scriptlets Java;
- SQL;
- conexiones;
- lógica extensa;
- duplicar encabezados y navegación;
- mostrar datos sensibles.

Los JSP administrativos protegidos pueden ubicarse bajo `WEB-INF` para evitar acceso directo.

---

## 40. JavaScript

Cada archivo debe tener un alcance claro.

Ejemplo:

```text
auth-login.js
auth-registro.js
admin-productos.js
admin-categorias.js
carrito.js
```

Reglas:

- evitar variables globales innecesarias;
- comprobar que los elementos existen;
- manejar errores de red;
- deshabilitar botones durante envíos;
- evitar solicitudes duplicadas;
- mostrar mensajes consistentes;
- no guardar datos sensibles;
- no simular persistencia definitiva en memoria;
- no usar `alert()` como mecanismo principal.

---

## 41. CSS y componentes

Debe evitarse duplicar estilos por cada JSP.

Estructura posible:

```text
css/
├── variables.css
├── base.css
├── layout.css
├── components.css
├── admin.css
└── tienda.css
```

Bootstrap debe utilizarse de forma coherente. Si finalmente no se usa, debe eliminarse de la documentación y las dependencias.

---

## 42. Autenticación

Flujo:

```text
registro
→ validación
→ hash seguro
→ persistencia
→ login
→ verificación
→ creación de sesión
→ autorización
```

La contraseña:

- nunca se almacena en texto plano;
- nunca se registra en logs;
- nunca se devuelve;
- nunca se envía mediante GET;
- nunca se compara sin un algoritmo de hash adecuado.

Debe usarse un algoritmo con sal y costo, como PBKDF2, BCrypt o Argon2, según las dependencias aprobadas.

---

## 43. Sesiones

La sesión debe guardar únicamente la información necesaria.

Ejemplo:

```text
usuarioId
rol
nombreVisible
```

No almacenar:

- contraseña;
- hash;
- tokens innecesarios;
- objetos con demasiados datos.

Al cerrar sesión:

```java
session.invalidate();
```

---

## 44. Autorización

La autorización se verifica en el servidor.

No basta con ocultar botones.

Filtros esperados:

```text
EncodingFilter
AuthenticationFilter
AdminAuthorizationFilter
```

Ejemplo:

```text
/admin/*
→ requiere sesión
→ requiere rol administrador
```

---

## 45. Protección CSRF

Las operaciones que modifican información deben protegerse contra solicitudes externas.

La estrategia puede incluir:

- token CSRF en sesión;
- token en formularios;
- cabecera en `fetch`;
- verificación centralizada.

Debe implementarse antes de considerar estable el panel administrativo.

---

## 46. Protección XSS

Todo dato generado por usuarios debe escaparse al mostrarlo.

Evitar:

```javascript
element.innerHTML = datoUsuario;
```

Preferir:

```javascript
element.textContent = datoUsuario;
```

Cuando se necesite HTML dinámico, debe existir sanitización controlada.

---

## 47. Cookies

Las cookies de sesión deben utilizar, cuando el entorno lo permita:

- `HttpOnly`;
- `Secure`;
- `SameSite`.

La configuración local puede diferir de producción, pero debe estar documentada.

---

## 48. Manejo global de errores

Clasificación recomendada:

| Situación | HTTP |
|---|---:|
| Validación inválida | 400 |
| No autenticado | 401 |
| Sin permisos | 403 |
| Recurso no encontrado | 404 |
| Conflicto o duplicado | 409 |
| Error interno | 500 |

Los mensajes deben ser comprensibles, pero no exponer SQL, rutas internas ni stack traces.

---

## 49. Estados de dominio

Los estados de pedidos, pagos y envíos deben definirse explícitamente.

Ejemplo:

```text
CREADO
CONFIRMADO
EN_PREPARACION
ENVIADO
ENTREGADO
CANCELADO
```

No deben manejarse como texto libre disperso. Las transiciones válidas deben controlarse.

---

## 50. Inventario

Toda modificación de inventario debe:

- validar existencia;
- validar cantidad;
- registrar motivo;
- mantener trazabilidad;
- participar en una transacción cuando dependa de un pedido.

No se permitirá stock negativo salvo una regla explícita.

---

## 51. Pedidos

Un pedido debe separar:

- cabecera;
- detalles;
- estado;
- pago;
- envío.

El precio usado al comprar debe conservarse históricamente. Un pedido antiguo no debe recalcularse usando el precio actual del producto.

---

## 52. Panel administrativo

Se implementará por módulos completos.

Orden recomendado:

1. Catálogos.
2. Categorías.
3. Productos.
4. Inventario.
5. Usuarios.
6. Pedidos.
7. Pagos.
8. Envíos.
9. Reportes.

Cada módulo debe completar:

```text
interfaz
→ JavaScript
→ Servlet
→ Service o DAO
→ MySQL
→ respuesta
→ actualización visual
```

No se conectarán todos los módulos simultáneamente.

---

## 53. Vista de usuario

Puede incluir:

- catálogo;
- detalle de producto;
- filtros;
- búsqueda;
- carrito;
- perfil;
- pedidos;
- historial;
- favoritos;
- reseñas.

Solo se implementarán las funciones aprobadas para la versión correspondiente.

---

## 54. Dependencias

Reglas:

- rutas relativas;
- versiones documentadas;
- una sola copia;
- necesidad comprobada;
- inclusión reproducible en el WAR.

`nbproject/private` no debe considerarse parte portable del proyecto.

Las rutas locales de un computador no pueden convertirse en dependencias permanentes.

---

## 55. Compilación y despliegue

Proceso esperado:

```text
clean
→ compile
→ package WAR
→ inspeccionar WAR
→ deploy
→ prueba mínima
```

Un proyecto que compila pero no despliega no está validado.

Un proyecto que despliega pero no conecta tampoco está validado.

---

## 56. Entornos

### Desarrollo

- MySQL local;
- logs detallados;
- datos de prueba;
- credenciales exclusivamente locales.

### Producción futura

- HTTPS;
- credenciales seguras;
- usuario MySQL restringido;
- backups;
- logs controlados;
- secretos fuera del repositorio.

---

## 57. Pruebas

### Unitarias

- validadores;
- utilidades;
- cálculos;
- reglas puras.

### Integración

- DAO con MySQL;
- JNDI;
- transacciones;
- Servlets desplegados.

### Sistema

- registro;
- login;
- permisos;
- CRUD;
- pedidos;
- navegación.

### Regresión

Se ejecutan antes de cerrar cada fase.

---

## 58. Definición de terminado

Una funcionalidad está terminada cuando:

- [ ] Cumple el alcance aprobado.
- [ ] Compila.
- [ ] Despliega.
- [ ] Persiste correctamente.
- [ ] Maneja errores.
- [ ] Valida permisos.
- [ ] No duplica código.
- [ ] No expone información sensible.
- [ ] Tiene pruebas.
- [ ] La documentación fue actualizada.
- [ ] `docs/02_PROJECT_STATE.md` refleja el resultado.

---

## 59. Reglas de evolución

Antes de modificar:

1. Leer `docs/06_AGENTS.md`.
2. Leer `docs/02_PROJECT_STATE.md`.
3. Revisar la arquitectura.
4. Revisar dependencias.
5. Identificar archivos afectados.
6. Limitar el alcance.
7. Implementar.
8. Probar.
9. Documentar.

No se debe:

- reescribir el proyecto sin necesidad;
- crear clases duplicadas;
- crear una segunda conexión;
- cambiar nombres masivamente sin análisis;
- modificar la base y los Models por separado;
- agregar tecnologías por preferencia de una IA;
- afirmar que algo fue probado cuando no se ejecutó;
- avanzar si el checkpoint anterior falla.

---

## 60. Decisiones pendientes

Deben resolverse mediante auditoría o pruebas:

- versión definitiva del driver MySQL;
- ubicación definitiva de los JAR;
- autocreación real del pool al desplegar;
- estrategia final de excepciones;
- librería de logging;
- algoritmo de hash;
- estrategia CSRF;
- uso real de Bootstrap;
- ~~convenciones SQL oficiales;~~ resuelto: camelCase confirmado contra el esquema real (ver `docs/04_DATABASE.md` §5);
- tablas incluidas en la primera versión;
- eliminación física o lógica;
- formato uniforme de respuestas;
- ubicación definitiva de JSP protegidos.

No deben presentarse como decisiones cerradas hasta tener evidencia.

---

## 61. Diagrama final

```text
┌─────────────────────────────────────────────┐
│                  Navegador                  │
│ JSP · HTML · CSS · Bootstrap · JavaScript  │
└──────────────────────┬──────────────────────┘
                       │ HTTP
                       ▼
┌─────────────────────────────────────────────┐
│             Filtros de seguridad            │
│ UTF-8 · sesión · rol · CSRF                │
└──────────────────────┬──────────────────────┘
                       ▼
┌─────────────────────────────────────────────┐
│                  Servlets                   │
│ validación · coordinación · respuesta      │
└──────────────────────┬──────────────────────┘
                       ▼
┌─────────────────────────────────────────────┐
│                  Services                   │
│ reglas · transacciones · procesos          │
└──────────────────────┬──────────────────────┘
                       ▼
┌─────────────────────────────────────────────┐
│                     DAO                     │
│ SQL · mapeo · persistencia                 │
└──────────────────────┬──────────────────────┘
                       ▼
┌─────────────────────────────────────────────┐
│       Conexion → ConexionPool → JNDI        │
│             jdbc/CavaDS · DataSource        │
└──────────────────────┬──────────────────────┘
                       ▼
┌─────────────────────────────────────────────┐
│                    MySQL                    │
│ tablas · relaciones · índices · datos      │
└─────────────────────────────────────────────┘
```

---

## 63. Persistencia DAO - cierre de Fase 6

La capa DAO usa `DAOException` como excepcion tipada y segura.
`SQLExceptionTranslator` clasifica SQLState y codigos MariaDB sin exponer SQL,
parametros, credenciales ni detalles JDBC. La capa DAO no registra ni imprime
errores; la capa web autorizada sera responsable de un unico registro tecnico
en Fase 7.

Los catalogos se pueden borrar solo cuando no tienen referencias. Usuarios se
desactivan mediante `isActivo`; productos, inventario y entidades historicas
no tienen borrado ordinario. Pedidos, detalles, pagos y envios conservan
trazabilidad y devuelven `OPERATION_NOT_ALLOWED` ante su eliminacion.

Una `Connection` creada por un DAO es responsabilidad de ese DAO. Las
sobrecargas coordinadas de pedido reciben una conexion externa sin cerrarla ni
alterar commit, rollback o `autoCommit`; el propietario controla esas
operaciones.

---

## 62. Cierre

Esta arquitectura es la base técnica objetivo de CAVA.

Debe utilizarse para:

- auditar el código;
- ordenar la estructura;
- estabilizar la conexión;
- corregir Models y DAO;
- construir Servlets;
- implementar el panel administrativo;
- desarrollar la tienda;
- validar calidad y seguridad.

La arquitectura no sustituye las pruebas ni el estado real. Toda afirmación sobre componentes terminados debe registrarse en `docs/02_PROJECT_STATE.md` y respaldarse con evidencia.
