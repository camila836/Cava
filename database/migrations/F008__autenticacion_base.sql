-- CAVA - Fase 8: actualización controlada de autenticación base.
-- Precondición: ejecutar con la base cava seleccionada y respaldo validado.

ALTER TABLE roles
    ADD COLUMN codigoRol VARCHAR(30) NOT NULL AFTER idRoles,
    ADD CONSTRAINT uqRolesCodigoRol UNIQUE (codigoRol);

ALTER TABLE usuarios
    MODIFY identificacion VARCHAR(45) NULL,
    MODIFY idTipoDocumento INT NULL,
    MODIFY idCiudades INT NULL;

SET @indiceIdentificacion := (
    SELECT INDEX_NAME
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'usuarios'
      AND COLUMN_NAME = 'identificacion'
      AND NON_UNIQUE = 0
    GROUP BY INDEX_NAME
    HAVING COUNT(*) = 1
    LIMIT 1
);

SET @sqlEliminarIndice := IF(
    @indiceIdentificacion IS NULL,
    'SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''No existe el UNIQUE individual esperado para usuarios.identificacion''',
    CONCAT('ALTER TABLE usuarios DROP INDEX `', REPLACE(@indiceIdentificacion, '`', '``'), '`')
);
PREPARE eliminarIndice FROM @sqlEliminarIndice;
EXECUTE eliminarIndice;
DEALLOCATE PREPARE eliminarIndice;

ALTER TABLE usuarios
    ADD CONSTRAINT uqUsuariosTipoDocumentoIdentificacion
        UNIQUE (idTipoDocumento, identificacion),
    ADD CONSTRAINT chkUsuariosDocumentoCompleto
        CHECK ((idTipoDocumento IS NULL) = (identificacion IS NULL));

INSERT INTO roles (codigoRol, descripcionRol) VALUES
    ('CLIENTE', 'Cliente'),
    ('ADMINISTRADOR', 'Administrador')
ON DUPLICATE KEY UPDATE descripcionRol = VALUES(descripcionRol);
