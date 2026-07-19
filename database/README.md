# Instalación de la base de datos CAVA

## Estado

Los scripts `00` a `03` constituyen el instalador consolidado de la primera
versión. Crean las 15 tablas confirmadas en la Fase 4A y no incluyen datos
empresariales inventados.

`cava.sql` se conserva temporalmente como instantánea de compatibilidad del
esquema anterior a la separación. No debe ejecutarse junto con `01_schema.sql`
porque ambos describen las mismas 15 tablas. El instalador consolidado es la
fuente preferida para instalaciones nuevas.

## Orden obligatorio

1. `00_create_database.sql` — crea la base `cava` con `utf8mb4` y
   `utf8mb4_unicode_ci`.
2. Seleccionar explícitamente `cava` en el cliente.
3. `01_schema.sql` — crea las 15 tablas, PK, FK, UNIQUE, tipos y defaults.
4. `02_indexes.sql` — reserva los índices de negocio; actualmente no ejecuta
   DDL porque no existen índices adicionales autorizados.
5. `03_seed_catalogs.sql` — reserva las semillas; actualmente no ejecuta DML
   porque no existen valores empresariales autorizados.

Ejemplo conceptual, sin credenciales:

```powershell
mysql [opciones-locales-seguras] < database\00_create_database.sql
mysql [opciones-locales-seguras] --database=cava < database\01_schema.sql
mysql [opciones-locales-seguras] --database=cava < database\02_indexes.sql
mysql [opciones-locales-seguras] --database=cava < database\03_seed_catalogs.sql
```

`01_schema.sql` debe ejecutarse sobre una base nueva y vacía. Falla si detecta
tablas existentes para no ocultar instalaciones parciales. `00` sí es
idempotente: `CREATE DATABASE IF NOT EXISTS` es seguro para su único objetivo.

## Validación aislada

La instalación debe probarse primero sobre una base temporal única. Para ello,
se define `@cava_database_name` antes de cargar `00_create_database.sql` en la
misma sesión; después se ejecutan `01` a `03` seleccionando esa base. La
validación debe comparar `information_schema`, verificar 15 tablas y confirmar
0 filas cuando no haya semillas autorizadas.

No se debe ejecutar DDL o DML sobre la base real `cava` sin respaldo,
validación temporal satisfactoria y autorización explícita.

## Migraciones futuras

`migrations/` contiene propuestas ordenadas para Fase 12. No pertenecen a la
instalación de la primera versión y no deben ejecutarse durante Fase 4B:

1. `V001__favoritos.sql`.
2. `V002__puntos_usuario.sql`.
3. `V003__resenas.sql`.
4. `V004__verificacion_cuenta.sql`.
