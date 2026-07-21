-- CAVA - Fase 8
-- Únicos catálogos autoritativos aprobados para autenticación.
INSERT INTO roles (codigoRol, descripcionRol) VALUES
    ('CLIENTE', 'Cliente'),
    ('ADMINISTRADOR', 'Administrador')
ON DUPLICATE KEY UPDATE descripcionRol = VALUES(descripcionRol);
