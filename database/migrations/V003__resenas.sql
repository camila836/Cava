-- Estado: APLAZADA PARA FASE 12. No ejecutar durante Fase 4B.
-- Dependencias funcionales pendientes: compra verificada y moderación.

USE cava;

-- Tabla de reseñas: un usuario puede calificar un producto una sola vez
-- (se recomienda validar en el DAO que haya comprado el producto antes de permitir la reseña).

CREATE TABLE IF NOT EXISTS resenas (
    idResenas       INT AUTO_INCREMENT PRIMARY KEY,
    idUsuarios      INT NOT NULL,
    idProductos     INT NOT NULL,
    calificacion    TINYINT NOT NULL,
    comentario      VARCHAR(500),
    fechaResena     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fkResenasUsuarios
        FOREIGN KEY (idUsuarios) REFERENCES usuarios(idUsuarios)
        ON DELETE CASCADE,
    CONSTRAINT fkResenasProductos
        FOREIGN KEY (idProductos) REFERENCES productos(idProductos)
        ON DELETE CASCADE,
    CONSTRAINT uqResenasUsuarioProducto
        UNIQUE (idUsuarios, idProductos),
    CONSTRAINT chkResenasCalificacion
        CHECK (calificacion BETWEEN 1 AND 5)
);
