USE cava;

-- Tabla de puntos de fidelidad: guarda cada movimiento (ganancia o redención)
-- en vez de solo un contador, para poder mostrar historial y auditar.

CREATE TABLE IF NOT EXISTS puntosUsuario (
    idPuntosUsuario     INT AUTO_INCREMENT PRIMARY KEY,
    idUsuarios          INT NOT NULL,
    idPedidosCabeza     INT NULL,
    puntos              INT NOT NULL,
    tipoMovimiento      ENUM('GANADO', 'REDIMIDO') NOT NULL,
    descripcionMovimiento VARCHAR(100),
    fechaMovimiento     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fkPuntosUsuarioUsuarios
        FOREIGN KEY (idUsuarios) REFERENCES usuarios(idUsuarios)
        ON DELETE CASCADE,
    CONSTRAINT fkPuntosUsuarioPedidosCabeza
        FOREIGN KEY (idPedidosCabeza) REFERENCES pedidosCabeza(idPedidosCabeza)
        ON DELETE SET NULL
);
