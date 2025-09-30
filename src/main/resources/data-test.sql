

INSERT INTO usuario (email, password, updated_at, id_rol)
VALUES
    ('admin@example.com', 'admin123', NOW(), 7),
    ('conductor1@example.com', 'pass123', NOW(), 8),
    ('conductor2@example.com', 'pass456', NOW(), 9),
    ('pasajero1@example.com', 'pass789', NOW(), 9),
    ('pasajero2@example.com', 'pass101', NOW(), 7);

INSERT INTO conductor (nombre, apellido, dni, edad, disponibilidad, descripcion_conductor, created_at, updated_at, usuario_id_vehiculo, id_usuario)
VALUES
    ('Juan', 'López', '12345678', 30, 'Disponible', 'Conductor con experiencia en viajes interurbanos', NOW(), NULL, 1, 1),
    ('María', 'García', '87654321', 28, 'Disponible', 'Conduce principalmente en horarios nocturnos', NOW(), NULL, 2, 2),
    ('Andrés', 'Rodríguez', '11223344', 35, 'No disponible', 'Conductor con 10 años de experiencia en transporte universitario', NOW(), NULL, 3, 3);


-- Datos de prueba para Ruta
INSERT INTO Ruta (origen, destino, fecha_salida, hora_salida, tarifa, asientos_disponibles, estado, id_conductor)
VALUES
    ('Monterrico', 'San Isidro', '2025-10-05', '08:30:00', 15.00, 3, 'PENDIENTE', 1),
    ('Monterrico', 'Miraflores', '2025-10-06', '07:45:00', 20.00, 2, 'COMPLETADO', 1),
    ('Monterrico', 'San Miguel', '2025-10-07', '09:00:00', 12.00, 4, 'CANCELADO', 2),
    ('San Miguel', 'Miraflores', '2025-10-08', '06:15:00', 25.00, 1, 'EN_PROGRESO', 3),
    ('San Miguel', 'San Isidro', '2025-10-09', '17:45:00', 18.00, 5, 'COMPLETADO', 2);

INSERT INTO Vehiculo (placa, soat, modelo, marca, capacidad, descripcionVehiculo)
VALUES
    ('ABC123', true, 'Corolla', 'Toyota', 4, 'Vehículo en buen estado, perfecto para viajes urbanos'),
    ('XYZ789', true, 'Elantra', 'Hyundai', 4, 'Vehículo cómodo y espacioso, ideal para trayectos largos'),
    ('LMN456', false, 'Rio', 'Kia', 4, 'Vehículo compacto, económico en consumo de combustible');

