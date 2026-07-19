# Fase de corrección estructural del proyecto CAVA

Antes de realizar cambios:

1. Lee completamente `AGENTS.md`, `PROJECT_STATE.md`, `ARCHITECTURE.md`, `DATABASE.md` y `ROADMAP.md`.
2. Revisa la estructura real del proyecto.
3. No asumas que la documentación representa el estado implementado.
4. No avances a conexión, Servlets, autenticación, dashboard ni nuevas funcionalidades.
5. Realiza únicamente las correcciones indicadas en este prompt.
6. Conserva una copia o utiliza Git para que todos los cambios puedan revertirse.

# Contexto

CAVA es un proyecto web de NetBeans con:

* Java 17.
* Jakarta EE 10.
* GlassFish 7.
* MySQL.
* Ant.
* JSP.
* Servlets.
* DAO.
* JNDI y Connection Pool.

Convenciones oficiales del proyecto:

* Clases Java: `PascalCase`.
* Métodos: `camelCase`.
* Variables: `camelCase`.
* Atributos: `camelCase`.
* Tablas MySQL: `camelCase`.
* Columnas MySQL: `camelCase`.
* DAO ubicados en `src/java/Controlador/`.
* DAO declarados con `package Controlador;`.
* Servlets ubicados en `src/java/Servlets/`.
* Servlets declarados con `package Servlets;`.
* Models ubicados en el paquete que actualmente utiliza el proyecto. No cambiar su paquete durante esta fase.
* La carpeta `Servlets` es exclusivamente para clases Java Servlet.
* Los JSP deben permanecer dentro de `web/`.

# Objetivo

Corregir únicamente la estructura y los paquetes actuales del proyecto para dejarlo preparado para compilar y, posteriormente, validar la conexión.

No crear nuevas funcionalidades.

# 1. Corregir los DAO

Revisa los siguientes archivos ubicados en:

```text
src/java/Controlador/
```

Archivos esperados:

```text
CategoriaProductosDAO.java
CiudadesDAO.java
EnviosDAO.java
EstadoEnvioDAO.java
InventarioDAO.java
MediosPagosDAO.java
PagosDAO.java
PedidosCabezaDAO.java
PedidosDetalleDAO.java
ProductosDAO.java
RolesDAO.java
TipoDocumentoDAO.java
TransportadorasDAO.java
UnidadesMedidaDAO.java
UsuariosDAO.java
```

En cada DAO, cambia:

```java
package DAO;
```

por:

```java
package Controlador;
```

No cambies el nombre de las clases DAO.

No muevas los DAO a una carpeta llamada `DAO`.

La ubicación oficial es:

```text
src/java/Controlador/
```

y el paquete oficial es:

```java
package Controlador;
```

# 2. Corregir imports

Busca en todo el proyecto imports como:

```java
import DAO.ProductosDAO;
import DAO.UsuariosDAO;
import DAO.*;
```

Cámbialos por sus equivalentes:

```java
import Controlador.ProductosDAO;
import Controlador.UsuariosDAO;
import Controlador.*;
```

No cambies imports que no pertenezcan a los DAO.

Después realiza una búsqueda global para confirmar que ya no exista:

```text
package DAO;
import DAO.
```

**Nota (verificado julio 2026):** una auditoría previa sobre el ZIP real ya confirmó 0 coincidencias de `import DAO.*` en todo el proyecto. Solo las 15 declaraciones `package DAO;` requieren corrección. Aun así, ejecuta la búsqueda global para confirmarlo sobre el estado actual del código antes de dar la fase por cerrada.

# 3. Revisar la carpeta Servlets

La ubicación oficial de los futuros Servlets es:

```text
src/java/Servlets/
```

Las clases ubicadas allí deben declarar:

```java
package Servlets;
```

Durante esta fase:

* No crees Servlets nuevos.
* No implementes login.
* No implementes registro.
* No implementes el administrador.
* No agregues `HttpServlet` vacíos.
* Conserva la carpeta `Servlets`.
* Confirma que no existan JSP, HTML, CSS o JavaScript dentro de ella.

**Nota (verificado julio 2026):** una auditoría previa sobre el ZIP real confirmó que `src/java/Servlets/` está vacía. No existen copias de `Admin.jsp`, `Index.jsp`, `InicioSesion.jsp` ni `RegistrarUsuario.jsp` dentro de ella. No es necesario buscar ni eliminar copias; basta con confirmar que la carpeta sigue vacía y reservada para futuras clases Servlet.

No elimines la carpeta `Servlets`.

# 4. Confirmar el script SQL canónico

Verifica que el único script principal sea:

```text
database/cava.sql
```

Busca referencias internas a copias con nombres alternativos y actualízalas al
nombre canónico.

No modifiques el contenido SQL en esta fase.

# 5. Revisar convenciones

Confirma que la documentación y los ejemplos respeten:

```text
Clases Java: PascalCase
Métodos Java: camelCase
Variables Java: camelCase
Atributos Java: camelCase
Tablas MySQL: camelCase
Columnas MySQL: camelCase
DAO: package Controlador
Servlets: package Servlets
```

Corrige ejemplos residuales en `snake_case`, pero no renombres tablas o columnas reales sin comparar primero el script SQL y el código.

No inventes nombres de columnas.

# 6. Revisar librerías sin borrar todavía

Identifica copias duplicadas de JAR en:

```text
lib/
web/WEB-INF/lib/
```

Revisa especialmente:

```text
mysql-connector
jakarta.servlet.jsp.jstl
jakarta.servlet.jsp.jstl-api
```

Antes de eliminar cualquier JAR:

1. Revisa `nbproject/project.properties`.
2. Determina qué copia utiliza NetBeans para compilar.
3. Determina qué copia entra en el WAR.
4. Genera el WAR si el entorno lo permite.
5. Inspecciona `WEB-INF/lib` dentro del WAR.

No copies dependencias a:

```text
domain1/lib
```

No modifiques:

```text
domain.xml
```

No elimines librerías si no puedes demostrar cuál copia es redundante.

Si no puedes decidirlo con seguridad, documenta el hallazgo y deja los archivos sin cambios.

# 7. Compilación

Después de hacer las correcciones:

1. Ejecuta Clean and Build.
2. Registra todos los errores.
3. Corrige únicamente errores relacionados con paquetes, imports, rutas o dependencias de esta fase.
4. No corrijas errores funcionales fuera del alcance.
5. No crees código nuevo para ocultar errores.

Si el entorno no puede ejecutar las tareas Ant internas de NetBeans, indícalo claramente y entrega los pasos para compilar desde NetBeans.

# 8. Validaciones obligatorias

Realiza búsquedas globales y confirma:

```text
package DAO;
```

Resultado esperado:

```text
0 coincidencias
```

Busca:

```text
import DAO.
```

Resultado esperado:

```text
0 coincidencias
```

Busca archivos `.jsp` dentro de:

```text
src/java/
```

Resultado esperado:

```text
0 archivos
```

Busca:

```text
DriverManager
```

Resultado esperado:

```text
0 usos en el código de CAVA
```

Confirma:

```text
src/java/Controlador/
```

contiene los 15 DAO.

Confirma:

```text
src/java/Servlets/
```

está reservada para clases Servlet (vacía o solo con clases Servlet reales).

Confirma que el script se llama:

```text
database/cava.sql
```

# Archivos que no debes modificar funcionalmente

No modifiques el contenido funcional de:

* Models.
* Consultas SQL de los DAO.
* Clases de conexión.
* JSP.
* JavaScript.
* CSS.
* `glassfish-resources.xml`.
* `glassfish-web.xml`.
* Scripts SQL.
* Configuración de autenticación.
* Configuración de seguridad.

Solo se permiten cambios de paquetes, imports, ubicación de duplicados, nombre del archivo SQL y correcciones documentales relacionadas.

# Informe final obligatorio

Entrega un informe con:

## Archivos modificados

Lista exacta de rutas.

## Cambios realizados

Para cada archivo, explica brevemente qué se corrigió.

## Búsquedas globales

Incluye el número de coincidencias encontradas para:

```text
package DAO;
import DAO.
DriverManager
```

## Estructura final

Muestra:

```text
src/java/
├── Conexion/
├── Controlador/
├── Modelo o Model/
├── Servlets/
└── Pruebas/
```

Utiliza el nombre real del paquete de Models sin cambiarlo.

## Compilación

Indica:

* comando o acción utilizada;
* resultado;
* errores;
* advertencias.

## Librerías

Indica:

* archivos duplicados;
* cuál utiliza NetBeans;
* cuál entra en el WAR;
* si eliminaste alguno;
* evidencia de la decisión.

## Pendientes

Registra cualquier problema fuera del alcance sin corregirlo.

# Criterio de finalización

Esta fase se considera terminada únicamente cuando:

* Los 15 DAO están en `src/java/Controlador/`.
* Los 15 DAO declaran `package Controlador;`.
* No existen imports `DAO.*`.
* No existen JSP dentro de `src/java`.
* La carpeta `Servlets` se conserva para clases Java Servlet.
* El script se llama `cava.sql`.
* No existe `DriverManager`.
* El proyecto compila o se entrega evidencia precisa del impedimento externo.
* No se implementaron funcionalidades adicionales.

No avances a la prueba de conexión.

Cuando termines, detente y entrega el informe. Espera la siguiente instrucción.
