# CAVA: despliegue temporal en Azure Container Apps

Esta carpeta define el runtime de prueba de CAVA. La imagen compila un WAR
nuevo desde el código de la rama y no recibe `dist/` en el contexto Docker.
El WAR conserva el contexto `/Cava` y no contiene Connector/J; el driver se
instala una única vez en el runtime de GlassFish.

## Requisitos y límites

- Java 17, GlassFish 7.0.25 y MySQL Connector/J 9.7.0.
- Connector/J 9.7 soporta JRE 8 o superior y MySQL 8.0 o posterior. La
  aplicación ya usa `com.mysql.cj.jdbc.MysqlDataSource`.
- Azure Database for MySQL Flexible Server debe usar TLS. Esta configuración
  exige `sslMode=VERIFY_IDENTITY` y un host con el nombre DNS real del servidor.
- El contenedor no publica el puerto administrativo 4848; solo 8080 queda
  disponible para el ingress de Container Apps.
- No cree ni conserve un archivo `.env`: está ignorado para evitar secretos en
  Git y en el contexto de construcción.
- El intento local de `compile-jsps` con NetBeans 20 queda registrado como
  bloqueado por `PWC6309: Illegal compilerSourceVM: 17`. No se reduce Java ni
  se declara esa comprobación aprobada. La imagen conserva Java 17, construye
  el WAR con las tareas de NetBeans y valida las JSP durante el arranque real
  de GlassFish en Container Apps.

## Construcción local

Con Docker disponible, desde la raíz del repositorio:

```powershell
docker build --file deployment/azure/Dockerfile --tag cava-test:local .
```

La imagen exige estos secretos únicamente en ejecución: `CAVA_DB_HOST`,
`CAVA_DB_PORT`, `CAVA_DB_NAME`, `CAVA_DB_USER` y `CAVA_DB_PASSWORD`. No los
escriba en comandos guardados, archivos, Dockerfile ni documentación. Use un
administrador de secretos o una sesión interactiva protegida para inyectarlos.

El arranque crea `CavaPool` y `jdbc/CavaDS`, comprueba el pool y despliega
`/Cava`. La contraseña se transforma en el alias cifrado de GlassFish dentro
del filesystem efímero del contenedor y el archivo temporal se elimina antes
del despliegue.

## Secuencia de Azure aprobada

Solo ejecutar después de confirmar que la suscripción es **Free Trial**, que
conserva el límite de gasto y que la estimación no supera USD 5 del crédito:

1. Crear `rg-cava-test` en `eastus2` y una alerta de presupuesto de USD 5.
   La alerta avisa; no detiene recursos automáticamente.
2. Crear exclusivamente el ACR permitido, el entorno y la Container App en
   Consumption, y MySQL Flexible Server B1ms solo si la oferta Free Trial lo
   cubre. No crear VM, App Service, AKS, Key Vault, red privada ni dominio.
3. Crear una base vacía mediante este orden, una sola vez:
   `database/00_create_database.sql`, `database/01_schema.sql`,
   `database/02_indexes.sql`, `database/03_seed_catalogs.sql`.
   No ejecutar `database/cava.sql` junto a esa secuencia.
4. Cargar los cinco valores de base de datos como secretos de Container Apps;
   no imprimirlos. Configurar `minReplicas=0`, `maxReplicas=1`, ingress HTTPS
   y `targetPort=8080` sin puerto administrativo.
5. Verificar `/Cava/`, `/Cava/inicio`, `/Cava/productos` y `/Cava/origen`.

## Eliminación al cerrar la prueba

Tras verificar que el grupo contiene solo recursos de prueba, eliminarlo con:

```powershell
az group delete --name rg-cava-test --yes --no-wait
```

Este comando se documenta para la aprobación posterior; no se ejecuta durante
la preparación.
