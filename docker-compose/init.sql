-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS computecDB;
USE computecDB;

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS clientes (
                                        rut VARCHAR(20) PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    comuna VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL
    );

-- Tabla de equipos
CREATE TABLE IF NOT EXISTS equipos (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       modelo VARCHAR(100) NOT NULL,
    cpu VARCHAR(100) NOT NULL,
    disco_duro INT NOT NULL,
    ram INT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    tipo ENUM('Desktop', 'Laptop') NOT NULL
    );

-- Tabla de desktops
CREATE TABLE IF NOT EXISTS desktops (
                                        equipo_id INT PRIMARY KEY,
                                        potencia_fuente INT NOT NULL,
                                        factor_forma VARCHAR(50) NOT NULL,
    FOREIGN KEY (equipo_id) REFERENCES equipos(id) ON DELETE CASCADE
    );

-- Tabla de laptops
CREATE TABLE IF NOT EXISTS laptops (
                                       equipo_id INT PRIMARY KEY,
                                       tamano_pantalla DECIMAL(4, 2) NOT NULL,
    es_touch BOOLEAN NOT NULL,
    puertos_usb INT NOT NULL,
    FOREIGN KEY (equipo_id) REFERENCES equipos(id) ON DELETE CASCADE
    );

-- Tabla de ventas
CREATE TABLE IF NOT EXISTS ventas (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      cliente_rut VARCHAR(20) NOT NULL,
    equipo_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    precio_final DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (cliente_rut) REFERENCES clientes(rut),
    FOREIGN KEY (equipo_id) REFERENCES equipos(id)
    );

-- Insertar algunos datos de prueba

-- Clientes
INSERT INTO clientes (rut, nombre_completo, direccion, comuna, correo_electronico, telefono) VALUES
                                                                                                 ('12345678-9', 'Juan Pérez', 'Calle 123', 'Santiago', 'juan@email.com', '912345678'),
                                                                                                 ('98765432-1', 'María López', 'Avenida 456', 'Viña del Mar', 'maria@email.com', '987654321');

-- Equipos
INSERT INTO equipos (modelo, cpu, disco_duro, ram, precio, tipo) VALUES
                                                                     ('PC Gamer X1', 'Intel i7', 1000, 16, 1200000, 'Desktop'),
                                                                     ('Laptop Pro Y2', 'AMD Ryzen 7', 512, 8, 800000, 'Laptop');

-- Desktops
INSERT INTO desktops (equipo_id, potencia_fuente, factor_forma) VALUES
    (1, 650, 'ATX');

-- Laptops
INSERT INTO laptops (equipo_id, tamano_pantalla, es_touch, puertos_usb) VALUES
    (2, 15.6, false, 3);

-- Ventas
INSERT INTO ventas (cliente_rut, equipo_id, fecha_hora, precio_final) VALUES
                                                                          ('12345678-9', 1, NOW(), 1150000),
                                                                          ('98765432-1', 2, NOW(), 780000);