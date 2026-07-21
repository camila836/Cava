# Instalación de la base de datos CAVA

## Estado

`cava.sql` es la fuente canónica y autocontenida del esquema definitivo de la
primera versión. Crea `cava`, sus 15 tablas y únicamente los roles
autoritativos `CLIENTE` y `ADMINISTRADOR`; no crea usuarios ni credenciales.

Los scripts `00` a `03` son el instalador modular equivalente para operaciones
que necesitan separar creación de base, estructura, índices y catálogos. No se
debe ejecutar `cava.sql` junto con `01_schema.sql` en la misma base: ambos
describen intencionalmente las mismas 15 tablas y fallan ante instalaciones
parciales para no ocultar divergencias.

## Orden obligatorio

1. `00_create_database.sql` — crea la base `cava` con `utf8mb4` y
   `utf8mb4_unicode_ci`.
2. Seleccionar explícitamente `cava` en el cliente.
3. `01_schema.sql` — crea las 15 tablas, PK, FK, UNIQUE, tipos y defaults.
4. `02_indexes.sql` — reserva los índices de negocio; actualmente no ejecuta
   DDL porque no existen índices adicionales autorizados.
5. `03_seed_catalogs.sql` — inserta idempotentemente `CLIENTE` y
   `ADMINISTRADOR` por `codigoRol`; no contiene otros catálogos ni usuarios.

Todas las tablas fijan explícitamente `ENGINE=InnoDB`, `utf8mb4` y
`utf8mb4_unicode_ci`. Las 14 relaciones usan `ON UPDATE RESTRICT` y
`ON DELETE RESTRICT`; no existen cascadas destructivas en esta versión.

Ejemplo conceptual, sin credenciales:

```powershell
mysql [opciones-locales-seguras] < database\00_create_database.sql
mysql [opciones-locales-seguras] --database=cava < database\01_schema.sql
mysql [opciones-locales-seguras] --database=cava < database\02_indexes.sql
mysql [opciones-locales-seguras] --database=cava < database\03_seed_catalogs.sql
```

`cava.sql` y `01_schema.sql` deben ejecutarse sobre una base nueva y vacía.
Fallan si detectan tablas existentes para no ocultar instalaciones parciales.
`00` sí es idempotente: `CREATE DATABASE IF NOT EXISTS` es seguro para su único
objetivo.

## Validación aislada

La instalación debe probarse primero sobre una base temporal única. Para ello,
se define `@cava_database_name` antes de cargar `00_create_database.sql` en la
misma sesión; después se ejecutan `01` a `03` seleccionando esa base. La
validación debe comparar `information_schema`, verificar 15 tablas, exactamente
dos roles autorizados y cero usuarios. Las semillas deben ejecutarse dos veces
sin alterar el conteo.

No se debe ejecutar DDL o DML sobre la base real `cava` sin respaldo,
validación temporal satisfactoria y autorización explícita.

## Actualización de Fase 8

`migrations/F008__autenticacion_base.sql` actualiza una instalación existente:
añade `roles.codigoRol`, habilita los campos opcionales de perfil, sustituye la
unicidad individual de identificación por
`uqUsuariosTipoDocumentoIdentificacion`, añade
`chkUsuariosDocumentoCompleto` y crea únicamente los dos roles autorizados.
Debe ejecutarse con respaldo previo y validación aislada.

## Migraciones futuras

Las migraciones `V001`–`V004` siguen reservadas para Fase 12 y no fueron
editadas, renumeradas ni ejecutadas durante Fase 8:

1. `V001__favoritos.sql`.
2. `V002__puntos_usuario.sql`.
3. `V003__resenas.sql`.
4. `V004__verificacion_cuenta.sql`.
