-- CAVA - Esquema canónico definitivo.
-- Reconstruye una instalación nueva de `cava` sin crear usuarios ni datos.
-- Falla si encuentra tablas preexistentes para no ocultar estados parciales.

CREATE DATABASE IF NOT EXISTS cava
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cava;

-- 1. TABLAS INDEPENDIENTES (sin FK)

CREATE TABLE categoriaProductos (
    idCategoriaProductos     INT AUTO_INCREMENT PRIMARY KEY,
    descripcionCategoriaProductos VARCHAR(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE ciudades (
    idCiudades      INT AUTO_INCREMENT PRIMARY KEY,
    codigoCiudad    VARCHAR(45) NOT NULL,
    nombreCiudad    VARCHAR(45) NOT NULL,
    codigoPostal    VARCHAR(45)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE tipoDocumento (
    idTipoDocumento INT AUTO_INCREMENT PRIMARY KEY,
    descripcion     VARCHAR(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE roles (
    idRoles         INT AUTO_INCREMENT PRIMARY KEY,
    codigoRol       VARCHAR(30) NOT NULL,
    descripcionRol  VARCHAR(45) NOT NULL,
    CONSTRAINT uqRolesCodigoRol UNIQUE (codigoRol)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE unidadesMedida (
    idUnidadesMedida        INT AUTO_INCREMENT PRIMARY KEY,
    descripcionUnidadesMed  VARCHAR(45) NOT NULL,
    CONSTRAINT uqUnidadesMedidaDescripcion UNIQUE (descripcionUnidadesMed)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE transportadoras (
    idTransportadoras       INT AUTO_INCREMENT PRIMARY KEY,
    nombreTransportadoras   VARCHAR(45) NOT NULL,
    nit                     VARCHAR(45) NOT NULL,
    correo                  VARCHAR(45),
    telefono                VARCHAR(45)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE estadoEnvio (
    idEstadoEnvio           INT AUTO_INCREMENT PRIMARY KEY,
    descripcionEstadoEnvio  VARCHAR(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE mediosPagos (
    idMediosPagos           INT AUTO_INCREMENT PRIMARY KEY,
    descripcionMediosPagos  VARCHAR(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. USUARIOS (depende de roles, tipoDocumento, ciudades)

CREATE TABLE usuarios (
    idUsuarios                      INT AUTO_INCREMENT PRIMARY KEY,
    nombres                         VARCHAR(45) NOT NULL,
    apellidos                       VARCHAR(45) NOT NULL,
    identificacion                  VARCHAR(45),
    correo                          VARCHAR(100) NOT NULL UNIQUE,
    direccion                       VARCHAR(45),
    telefono                        VARCHAR(45),
    clave                           VARCHAR(255) NOT NULL,
    isActivo                        TINYINT(1) NOT NULL DEFAULT 1,
    fechaNacimiento                 DATE,
    fechaVencimientoClave           DATE,
    autorizacionTratamientoDatos    TINYINT(1) NOT NULL DEFAULT 0,
    idRoles                         INT NOT NULL,
    idTipoDocumento                 INT,
    idCiudades                      INT,
    CONSTRAINT uqUsuariosTipoDocumentoIdentificacion
        UNIQUE (idTipoDocumento, identificacion),
    CONSTRAINT chkUsuariosDocumentoCompleto
        CHECK ((idTipoDocumento IS NULL) = (identificacion IS NULL)),
    CONSTRAINT fkUsuariosRoles         FOREIGN KEY (idRoles)         REFERENCES roles(idRoles) ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fkUsuariosTipoDocumento FOREIGN KEY (idTipoDocumento) REFERENCES tipoDocumento(idTipoDocumento) ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fkUsuariosCiudades      FOREIGN KEY (idCiudades)      REFERENCES ciudades(idCiudades) ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Catálogos autoritativos mínimos de autenticación (Fase 8).
INSERT INTO roles (codigoRol, descripcionRol) VALUES
    ('CLIENTE', 'Cliente'),
    ('ADMINISTRADOR', 'Administrador')
ON DUPLICATE KEY UPDATE descripcionRol = VALUES(descripcionRol);

-- 3. PRODUCTOS (depende de unidadesMedida, categoriaProductos)

CREATE TABLE productos (
    idProductos             INT AUTO_INCREMENT PRIMARY KEY,
    descripcionProductos    VARCHAR(45) NOT NULL,
    precioProductos         DECIMAL(10,2) NOT NULL,
    idUnidadesMedida        INT NOT NULL,
    idCategoriaProductos    INT NOT NULL,
    CONSTRAINT fkProductosUnidadesMedida     FOREIGN KEY (idUnidadesMedida)     REFERENCES unidadesMedida(idUnidadesMedida) ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fkProductosCategoriaProductos FOREIGN KEY (idCategoriaProductos) REFERENCES categoriaProductos(idCategoriaProductos) ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 4. INVENTARIO (depende de productos)

CREATE TABLE inventario (
    idInventario            INT AUTO_INCREMENT PRIMARY KEY,
    descripcionInventario   VARCHAR(45),
    stock                   DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    idProductos             INT NOT NULL,
    CONSTRAINT fkInventarioProductos FOREIGN KEY (idProductos) REFERENCES productos(idProductos) ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 5. PEDIDOS CABEZA (depende de usuarios)

CREATE TABLE pedidosCabeza (
    idPedidosCabeza     INT AUTO_INCREMENT PRIMARY KEY,
    numeroPedido        VARCHAR(45) NOT NULL UNIQUE,
    fechaPedido         DATETIME NOT NULL,
    descripcionPedido   VARCHAR(45),
    valorTotal          DECIMAL(10,2) NOT NULL,
    idUsuarios          INT NOT NULL,
    CONSTRAINT fkPedidosCabezaUsuarios FOREIGN KEY (idUsuarios) REFERENCES usuarios(idUsuarios) ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 6. PEDIDOS DETALLE (depende de pedidosCabeza, productos)

CREATE TABLE pedidosDetalle (
    idPedidosDetalle    INT AUTO_INCREMENT PRIMARY KEY,
    cantidadUnitaria    DECIMAL(10,2) NOT NULL,
    subtotalPed         DECIMAL(10,2) NOT NULL,
    idPedidosCabeza     INT NOT NULL,
    idProductos         INT NOT NULL,
    CONSTRAINT fkPedidosDetallePedidosCabeza FOREIGN KEY (idPedidosCabeza) REFERENCES pedidosCabeza(idPedidosCabeza) ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fkPedidosDetalleProductos      FOREIGN KEY (idProductos)     REFERENCES productos(idProductos) ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 7. PAGOS (depende de mediosPagos, pedidosCabeza)

CREATE TABLE pagos (
    idPagos             INT AUTO_INCREMENT PRIMARY KEY,
    fechaPagos          DATETIME NOT NULL,
    descripcionPagos    VARCHAR(45),
    monto               DECIMAL(10,2) NOT NULL,
    referenciaPago      VARCHAR(45) NOT NULL UNIQUE,
    comprobantePago     VARCHAR(45),
    idMediosPagos       INT NOT NULL,
    idPedidosCabeza     INT NOT NULL,
    CONSTRAINT fkPagosMediosPagos   FOREIGN KEY (idMediosPagos)   REFERENCES mediosPagos(idMediosPagos) ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fkPagosPedidosCabeza FOREIGN KEY (idPedidosCabeza) REFERENCES pedidosCabeza(idPedidosCabeza) ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 8. ENVIOS (depende de pedidosCabeza, estadoEnvio, transportadoras)

CREATE TABLE envios (
    idEnvios            INT AUTO_INCREMENT PRIMARY KEY,
    fechaEnvios         DATETIME NOT NULL,
    descripcionEnvios   VARCHAR(45),
    numeroGuia          VARCHAR(45) NOT NULL UNIQUE,
    idPedidosCabeza     INT NOT NULL,
    idEstadoEnvio       INT NOT NULL,
    idTransportadoras   INT NOT NULL,
    CONSTRAINT fkEnviosPedidosCabeza   FOREIGN KEY (idPedidosCabeza)   REFERENCES pedidosCabeza(idPedidosCabeza) ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fkEnviosEstadoEnvio     FOREIGN KEY (idEstadoEnvio)     REFERENCES estadoEnvio(idEstadoEnvio) ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fkEnviosTransportadoras FOREIGN KEY (idTransportadoras) REFERENCES transportadoras(idTransportadoras) ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
