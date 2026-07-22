-- CAVA - Fase 9: unicidad de unidades de medida.
-- Precondicion: ejecutar con la base cava seleccionada y respaldo validado.
-- La colacion utf8mb4_unicode_ci aplica la comparacion autoritativa de MariaDB.

SET @f009IndiceExistente := (
    SELECT COUNT(*)
    FROM (
        SELECT INDEX_NAME
        FROM information_schema.STATISTICS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'unidadesMedida'
          AND NON_UNIQUE = 0
        GROUP BY INDEX_NAME
        HAVING COUNT(*) = 1
           AND MAX(COLUMN_NAME) = 'descripcionUnidadesMed'
    ) indices
);

SET @f009Duplicados := (
    SELECT COUNT(*)
    FROM (
        SELECT descripcionUnidadesMed
        FROM unidadesMedida
        GROUP BY descripcionUnidadesMed
        HAVING COUNT(*) > 1
    ) duplicados
);

SET @f009ValidarIndice := IF(
    @f009IndiceExistente = 0,
    'SELECT 1',
    'SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''F009 ya aplicada: existe UNIQUE para unidadesMedida.descripcionUnidadesMed'''
);
PREPARE f009ValidarIndice FROM @f009ValidarIndice;
EXECUTE f009ValidarIndice;
DEALLOCATE PREPARE f009ValidarIndice;

SET @f009ValidarDatos := IF(
    @f009Duplicados = 0,
    'SELECT 1',
    'SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''F009 bloqueada: existen unidades de medida duplicadas'''
);
PREPARE f009ValidarDatos FROM @f009ValidarDatos;
EXECUTE f009ValidarDatos;
DEALLOCATE PREPARE f009ValidarDatos;

ALTER TABLE unidadesMedida
    ADD CONSTRAINT uqUnidadesMedidaDescripcion
        UNIQUE (descripcionUnidadesMed);
