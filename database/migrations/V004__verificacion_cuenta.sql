-- Estado: APLAZADA PARA FASE 12. No ejecutar durante Fase 4B.
-- Dependencias funcionales pendientes: entrega, expiración y protección de token.

USE cava;

-- Tabla para verificación de cuenta por correo y/o SMS.
-- Un usuario puede tener varios tokens a lo largo del tiempo (reenvíos),
-- por eso se guarda el historial en vez de un solo campo en usuarios.

CREATE TABLE IF NOT EXISTS verificacionCuenta (
    idVerificacionCuenta    INT AUTO_INCREMENT PRIMARY KEY,
    idUsuarios              INT NOT NULL,
    tipoVerificacion        ENUM('CORREO', 'SMS') NOT NULL,
    token                   VARCHAR(10) NOT NULL,
    fechaCreacion           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fechaExpiracion         DATETIME NOT NULL,
    verificado              TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fkVerificacionCuentaUsuarios
        FOREIGN KEY (idUsuarios) REFERENCES usuarios(idUsuarios)
        ON DELETE CASCADE
);

-- Índice para buscar rápido el token vigente de un usuario
CREATE INDEX idxVerificacionCuentaUsuarioToken
    ON verificacionCuenta (idUsuarios, token);
