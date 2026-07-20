-- CAVA - Fase 4B
-- Crea únicamente la base de datos con la codificación oficial.
--
-- Uso normal: el nombre predeterminado es `cava`.
-- Validación aislada: definir @cava_database_name antes de ejecutar este
-- archivo dentro de la misma sesión del cliente MariaDB.

SET @cava_database_name = COALESCE(NULLIF(@cava_database_name, ''), 'cava');
SET @cava_database_identifier = CONCAT(
    '`',
    REPLACE(@cava_database_name, '`', '``'),
    '`'
);
SET @cava_create_database_sql = CONCAT(
    'CREATE DATABASE IF NOT EXISTS ',
    @cava_database_identifier,
    ' CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci'
);

PREPARE cava_create_database_statement FROM @cava_create_database_sql;
EXECUTE cava_create_database_statement;
DEALLOCATE PREPARE cava_create_database_statement;

SET @cava_database_identifier = NULL;
SET @cava_create_database_sql = NULL;
