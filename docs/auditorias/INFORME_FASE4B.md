# Informe de Fase 4B — Consolidación reproducible de base de datos

## Estado

**FASE 4B CERRADA — FASE 4 CERRADA**

Fecha de cierre: 19 de julio de 2026.

## 1. Alcance ejecutado

La Fase 4B separó el instalador de la primera versión de CAVA en creación de
base, esquema, índices adicionales y catálogos iniciales. También ordenó las
cuatro migraciones futuras sin ejecutarlas y validó la instalación desde cero
en una base temporal independiente.

No se modificaron la base real `cava`, Java, Models, DAO, Servlets, JSP,
GlassFish, `my.ini`, usuarios, contraseñas o privilegios. No se inició la Fase
5, la Fase 12 ni ninguna funcionalidad posterior.

## 2. Precondiciones y respaldo

- Rama: `feature/fase-4-db`, únicamente local.
- Punto de partida: `29ba6bb8829b1524f1c5692e97fd0dec560f546c`.
- `main` y `origin/main`: `596273b3ca87def8fcfdace7faddbad4cfdeb417`.
- MariaDB: una instancia XAMPP 10.4.32, puerto 3306, directorio de datos
  `C:\xampp8\mysql\data\`.

Se reutilizó, sin modificar ni recrear, el respaldo obligatorio:

| Propiedad | Valor |
|---|---|
| Ruta | `C:\Users\Maria Camila R\CAVA_Backups\Fase4B\cava_pre_fase4b_20260719-183800.sql` |
| Tamaño | 15.644 bytes |
| SHA-256 | `C487FFDE4DC607C5EDD230EF0CACDCE9BE8AD0FD26DE739A833C624C33333208` |
| Verificación | Archivo no vacío; `CREATE DATABASE`, `USE cava` y 15 `CREATE TABLE` reconocidos |

El respaldo permanece fuera del repositorio y no contiene material auxiliar
con credenciales.

## 3. Autenticación administrativa limitada

La cuenta local utilizada fue `root@localhost`, exclusivamente mediante el
cliente de la instalación `C:\xampp8\mysql\bin\mysql.exe`. Una consulta previa
confirmó que la conexión local autorizada funciona sin contraseña. No se
supuso ese estado antes de probarlo y no se solicitó, mostró, almacenó o pasó
ninguna contraseña en argumentos.

La cuenta administrativa se utilizó únicamente para crear, consultar y
eliminar la base temporal. No se ejecutaron `GRANT`, `REVOKE`, operaciones de
usuarios ni cambios de autenticación.

## 4. Archivos consolidados

| Archivo | Función |
|---|---|
| `database/00_create_database.sql` | Crea una base con `utf8mb4` y `utf8mb4_unicode_ci`; admite un nombre temporal mediante variable de sesión y usa `cava` como valor predeterminado |
| `database/01_schema.sql` | Crea las 15 tablas de la primera versión, PK, FK, UNIQUE, tipos y defaults |
| `database/02_indexes.sql` | Documenta que no existen índices de negocio autorizados; PK, UNIQUE e índices FK nacen en `01` |
| `database/03_seed_catalogs.sql` | Reserva las semillas sin ejecutar DML porque no hay valores empresariales autorizados |
| `database/README.md` | Define orden, compatibilidad, validación aislada y migraciones excluidas |

`database/cava.sql` permanece intacto como instantánea de compatibilidad. No
debe ejecutarse junto con `01_schema.sql`; ambos contienen las mismas 15
definiciones. La comparación estática normalizada confirmó 15 bloques
`CREATE TABLE` idénticos.

Las migraciones existentes se ordenaron sin cambiar su función:

- `database/migrations/V001__favoritos.sql`;
- `database/migrations/V002__puntos_usuario.sql`;
- `database/migrations/V003__resenas.sql`;
- `database/migrations/V004__verificacion_cuenta.sql`.

Sus encabezados y `database/migrations/README.md` las marcan como aplazadas
para Fase 12. No forman parte del orden de instalación `00`–`03` y no fueron
ejecutadas.

## 5. Base temporal y scripts ejecutados

Nombre exacto:

```text
cava_fase4b_tmp_20260719_184956
```

Antes de crearla se listaron las bases existentes y se confirmó que el nombre
no existía, no era `cava` y no coincidía con ninguna base preexistente.

Resultados:

| Ejecución | Resultado |
|---|---|
| `00_create_database.sql`, primera ejecución | Correcta |
| `00_create_database.sql`, segunda ejecución | Correcta; creación idempotente |
| `01_schema.sql` | Correcta; 15 tablas creadas desde cero |
| `02_indexes.sql`, dos ejecuciones | Correctas; sin DDL adicional |
| `03_seed_catalogs.sql`, dos ejecuciones | Correctas; sin DML ni duplicados |

`01_schema.sql` falla deliberadamente si una tabla ya existe para no ocultar
una instalación parcial. La idempotencia se aplica únicamente donde es segura:
creación de base y scripts actualmente vacíos de índices/semillas.

## 6. Comparación estructural

Se compararon `cava` y la base temporal mediante 203 filas canónicas de
metadatos por esquema: charset, colación, tablas, motores, columnas, tipos,
nulabilidad, defaults, restricciones, columnas de claves, reglas FK e índices.

| Validación | Resultado |
|---|---:|
| Firmas SHA-256 de metadatos | Ambas `8CDD7728CD82593EBC4DA7ACB75A43204C2AC4784CE2FAF66E385F3C90145850` |
| Tablas | 15 |
| Tablas InnoDB | 15 |
| Claves primarias | 15 |
| Claves foráneas | 14 |
| Restricciones UNIQUE | 5 |
| Entradas de índice | 34 |
| Columnas `DECIMAL(10,2)` | 6 |
| Otros DECIMAL | 0 |
| Reglas FK | 14 con `UPDATE RESTRICT` y `DELETE RESTRICT` |
| Filas en la base temporal | 0 |
| Filas en `cava` | 0 |

La firma temporal coincide con el esquema real documentado en Fase 4A. La
comparación estática confirmó además que `database/cava.sql` y
`database/01_schema.sql` describen las mismas 15 tablas. Los scripts `00`–`03`
no contienen `DROP DATABASE`, `USE cava` ni referencias fijas `cava.`.

## 7. Semillas

No se encontraron valores autoritativos para roles, tipos de documento,
ciudades, categorías, unidades, estados de envío, medios de pago o
transportadoras. Por tanto, `03_seed_catalogs.sql` no contiene `INSERT`.

Se ejecutó dos veces en la base temporal: ambas ejecuciones fueron correctas y
el conteo final permaneció en 0. Las semillas continúan pendientes de una
decisión funcional; no se inventaron datos.

## 8. Limpieza controlada

Antes de eliminar la base temporal se volvió a listar el servidor y se
confirmó carácter por carácter que el objetivo:

- era `cava_fase4b_tmp_20260719_184956`;
- empezaba por `cava_fase4b_tmp_`;
- existía exactamente una vez;
- no era `cava` ni una base preexistente.

Se ejecutó únicamente:

```sql
DROP DATABASE cava_fase4b_tmp_20260719_184956;
```

La comprobación posterior confirmó su desaparición. La lista de bases volvió
exactamente a: `cava`, `information_schema`, `mysql`, `performance_schema`,
`phpmyadmin`, `test` y `thea`.

## 9. Estado de la base real

`cava` no recibió DDL ni DML. Antes, durante y después de la validación conservó
sus 15 tablas; el conteo exacto final fue 0 en las 15. No se ejecutaron los
scripts consolidados ni las migraciones sobre la base real porque su estructura
ya coincide con el estado aprobado.

## 10. Cambios Java y deudas aplazadas

No se modificó Java. La Fase 4A asignó a fases posteriores:

- `double`/`setDouble`/`getDouble` frente a `DECIMAL(10,2)`: Fase 5;
- 36 consultas `SELECT *`: Fase 6;
- borrado físico y manejo de errores mediante valores vacíos: Fase 6 y decisión
  de política de eliminación;
- favoritos, fidelidad, reseñas y verificación: Fase 12.

## 11. Reversión

Los cambios de esta fase son archivos versionados. Su reversión consiste en
revertir el commit local de Fase 4B; no es necesario restaurar MariaDB porque
`cava` no fue modificada. El respaldo verificado deberá conservarse intacto
para cualquier cambio futuro sobre la base real.

## 12. Cierre

```text
FASE 4B CERRADA; FASE 4 CERRADA; FASE 5 NO INICIADA
```
