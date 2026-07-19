-- Estado: APLAZADA PARA FASE 12. No ejecutar durante Fase 4B.
-- Dependencias funcionales pendientes: política de eliminación y capa Java.

USE cava;

-- Tabla de favoritos/wishlist: relación muchos-a-muchos entre usuarios y productos.

CREATE TABLE IF NOT EXISTS favoritos (
    idFavoritos     INT AUTO_INCREMENT PRIMARY KEY,
    idUsuarios      INT NOT NULL,
    idProductos     INT NOT NULL,
    fechaAgregado   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fkFavoritosUsuarios
        FOREIGN KEY (idUsuarios) REFERENCES usuarios(idUsuarios)
        ON DELETE CASCADE,
    CONSTRAINT fkFavoritosProductos
        FOREIGN KEY (idProductos) REFERENCES productos(idProductos)
        ON DELETE CASCADE,
    CONSTRAINT uqFavoritosUsuarioProducto
        UNIQUE (idUsuarios, idProductos)
);
