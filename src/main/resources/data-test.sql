-- data-test.sql – PostgreSQL (UniRide)
TRUNCATE TABLE
    calificacion,
    pago,
    solicitud_viaje,
    ruta,
    conductor,
    pasajero,
    vehiculo,
    usuario,
    rol
RESTART IDENTITY CASCADE;

-- ROLES
INSERT INTO rol (id_rol, name) VALUES
                                   (1, 'ADMIN'),
                                   (2, 'CONDUCTOR'),
                                   (3, 'PASAJERO');

-- USUARIOS
INSERT INTO usuario (id_usuario, email, password, id_rol) VALUES
                                                              (1, 'admin@uniride.test',     'admin123', 1),
                                                              (2, 'conductor@uniride.test', 'driver123', 2),
                                                              (3, 'pasajero@uniride.test',  'passenger123', 3),
                                                              (4, 'conductora@uniride.test','driver456', 2);

-- VEHICULOS
INSERT INTO vehiculo
(id_vehiculo, placa, soat, modelo, marca, capacidad, descripcion_vehiculo)
VALUES
    (1, 'ABC-123', TRUE, 'Yaris',  'Toyota',  4, 'Sedán compacto, aire acondicionado'),
    (2, 'XYZ-987', TRUE, 'Accent', 'Hyundai', 4, 'Buen maletero, mantenimiento al día');

-- PASAJERO (1–1 con usuario 3)
INSERT INTO pasajero
(id_pasajero, nombre, apellido, dni, edad, descripcion_pasajero, usuario_id_usuario)
VALUES
    (1, 'Favio', 'Arroyo', '77777777', 24, 'Prefiere asiento delantero', 3);

-- CONDUCTOR 1 (usuario 2, vehículo 1)
INSERT INTO conductor
(id_conductor, nombre, apellido, dni, edad, disponibilidad,
 descripcion_conductor, usuario_id_vehiculo, id_usuario)
VALUES
    (1, 'Carlos', 'Soto', '44444444', 30, 'Lunes-Viernes',
     '5 años de experiencia',1, 2);

-- RUTAS del conductor 1
INSERT INTO ruta
(id_ruta, origen, destino, fecha_salida, hora_salida,
 tarifa,  asientos_disponibles, estado_ruta, id_conductor)
VALUES
    (1, 'Barranco',  'Miraflores', '2025-10-05', '08:30:00',
     8.50, 3, 'PENDIENTE', 1),
    (2, 'Surco',     'San Isidro', '2025-10-05', '18:00:00',
     10.00, 2, 'PENDIENTE', 1);

-- SOLICITUDES (ruta 1/2, pasajero 1)
INSERT INTO solicitud_viaje
(id_solicitud_viaje, fecha,       hora,       updated_at,            estado_solicitud,
 id_ruta, id_pasajero)
VALUES
    (1, '2025-10-05', '08:00:00', '2025-10-05 08:05:00', 'ACEPTADO',  1, 1),
    (2, '2025-10-05', '17:40:00', '2025-10-05 17:45:00', 'RECHAZADO', 2, 1);

-- PAGOS de esas solicitudes
INSERT INTO pago
(id_pago, monto, fecha,       hora,       comision, medio_pago,       estado_pago,   id_solicitud_viaje)
VALUES
    (1,  8.50, '2025-10-05', '08:10:00', 0.50, 'YAPE',            'COMPLETADO', 1),
    (2, 10.00, '2025-10-05', '17:50:00', 0.60, 'TARJETA_DEBITO',  'EN_PROGRESO', 2);

-- CALIFICACIONES
INSERT INTO calificacion
(id_calificacion, puntaje, comentario,                     updated_at,            id_conductor, id_pasajero)
VALUES
    (1, 5, 'Viaje cómodo y puntual',                          '2025-10-05 09:15:00', 1, 1),
    (2, 4, 'Todo bien, podría mejorar la música',             '2025-10-05 19:30:00', 1, 1);

-- CONDUCTOR 2 (usuario 4, vehículo 2)  <-- clave: usuario distinto para no violar UNIQUE
INSERT INTO conductor
(id_conductor, nombre, apellido, dni, edad, disponibilidad,
 descripcion_conductor, created_at,            updated_at,
 usuario_id_vehiculo, id_usuario)
VALUES
    (2, 'Marta', 'Quispe', '55555555', 28, 'Fines de semana',
     'Conduce con prudencia', '2025-09-30 10:30:00', NULL,
     2, 4);

-- Ruta extra del conductor 2
INSERT INTO ruta
(id_ruta, origen, destino, fecha_salida, hora_salida,
 tarifa, asientos_disponibles, estado_ruta, id_conductor)
VALUES
    (3, 'La Molina', 'Centro de Lima', '2025-10-06', '07:15:00',
     12.00, 4, 'PENDIENTE', 2);
