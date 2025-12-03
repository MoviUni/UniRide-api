-- data-test.sql – PostgreSQL (UniRide) ✅ CORREGIDO Y COMPLETO

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

-- ========== ROLES ==========
INSERT INTO rol (name) VALUES
                           ('ADMIN'),
                           ('CONDUCTOR'),
                           ('PASAJERO');

-- ========== USUARIOS ==========
INSERT INTO usuario (email, password, id_rol) VALUES
                                                  ('admin@uniride.test',      '$2a$10$QF0HXpfN/PT.E18bFxY8AOzFmfc3WW4.4dcDHDIPuCl0L5xM2b/CO',     (SELECT id_rol FROM rol WHERE name = 'ADMIN')), --contra: admin123
                                                  ('conductor@uniride.test',  '$2a$10$5yzScqF/9Xi2agkmPj//Lu2IkqqS6A3peLYNBr.u/pIauUV.2dgJO',    (SELECT id_rol FROM rol WHERE name = 'CONDUCTOR')), -- driver123
                                                  ('pasajero@uniride.test',   '$2a$10$L7OOSfUlkchHzjmB4ry/K.QU4JqkKnl1/9smYP2URLog.NlCXTYiO', (SELECT id_rol FROM rol WHERE name = 'PASAJERO')), --passenger123
                                                  ('conductora@uniride.test', 'driver456',    (SELECT id_rol FROM rol WHERE name = 'CONDUCTOR')),
                                                  ('conductor3@uniride.test',   'driver789', (SELECT id_rol FROM rol WHERE name = 'CONDUCTOR'));

-- ========== CONDUCTORES ==========
INSERT INTO conductor (
    nombre, apellido, dni, edad, codigo_uni, created_at, updated_at, id_usuario
) VALUES
      ('Carlos', 'Soto', '44444444', 30, 'U202218883','2025-09-30 09:00:00', '2025-09-30 09:00:00',
       (SELECT id_usuario FROM usuario WHERE email='conductor@uniride.test')),
      ('Marta', 'Quispe', '55555555', 28,  'U202119884','2025-09-30 10:30:00', '2025-09-30 10:30:00',
       (SELECT id_usuario FROM usuario WHERE email='conductora@uniride.test')),
      ('David', 'Atencio', '22222222', 21,'U202215552', '2025-09-30 09:00:00', '2025-09-30 09:00:00',
       (SELECT id_usuario FROM usuario WHERE email='conductor3@uniride.test'));

-- ========== VEHICULOS ==========
INSERT INTO vehiculo (
    placa, soat, modelo, color, marca, capacidad, id_conductor
) VALUES
      ('ABC-123', TRUE, 'Yaris', 'Rojo', 'Toyota', 4, (SELECT id_conductor FROM conductor WHERE dni='44444444')),
      ('AYC-123', TRUE, 'Yaris', 'Rojo', 'Toyota', 4, (SELECT id_conductor FROM conductor WHERE dni='22222222')),
      ('XYZ-987', TRUE, 'Accent', 'Azul', 'Hyundai', 4, (SELECT id_conductor FROM conductor WHERE dni='55555555'));


-- ========== PASAJEROS ==========
INSERT INTO pasajero (
    nombre, apellido, dni, edad, descripcion_pasajero, created_at, updated_at, usuario_id_usuario, carrera, codigo_uni
) VALUES
    ('Favio', 'Arroyo', '77777777', 24, 'Prefiere asiento delantero y viajes tranquilos.', '2025-09-30 10:00:00', '2025-09-30 10:00:00',
     (SELECT id_usuario FROM usuario WHERE email = 'pasajero@uniride.test'), 'Ciencias de la computación','U202216669');

-- ========== RUTAS ==========
INSERT INTO ruta (
    origen, destino, fecha_salida, hora_salida, tarifa, asientos_disponibles, estado_ruta, id_conductor
) VALUES
      ('Barranco', 'Miraflores', '2025-12-05', '12:55:00', 8.00, 3, 'CONFIRMADO',
       (SELECT id_conductor FROM conductor WHERE dni='44444444')),
      ('Surco', 'San Isidro', '2025-12-10', '14:00:00', 10.00, 4, 'PROGRAMADO',
       (SELECT id_conductor FROM conductor WHERE dni='44444444')),
      ('La Molina', 'Centro de Lima', '2025-10-06', '07:15:00', 12.00, 4, 'PROGRAMADO',
       (SELECT id_conductor FROM conductor WHERE dni='55555555')),
      ('La Molina', 'UPC-Monterrico', '2025-12-11', '07:15:00', 12.00, 4, 'PROGRAMADO',
       (SELECT id_conductor FROM conductor WHERE dni='22222222')),
      ('San Miguel', 'UPC-Monterrico', '2025-11-11', '07:15:00', 12.00, 4, 'FINALIZADO',
       (SELECT id_conductor FROM conductor WHERE dni='44444444'));


-- ========== SOLICITUDES ==========
INSERT INTO solicitud_viaje (
    fecha, hora, updated_at, estado_solicitud, id_ruta, id_pasajero
) VALUES
      ('2025-10-05', '08:00:00', '2025-10-05 08:05:00', 'ACEPTADO',
       (SELECT id_ruta FROM ruta WHERE origen='Barranco' AND destino='Miraflores'),
       (SELECT id_pasajero FROM pasajero WHERE dni='77777777' LIMIT 1)),
      ('2025-10-05', '17:40:00', '2025-10-05 17:45:00', 'PENDIENTE',
       (SELECT id_ruta FROM ruta WHERE origen='Surco' AND destino='San Isidro'),
       (SELECT id_pasajero FROM pasajero WHERE dni='77777777' LIMIT 1));

INSERT INTO solicitud_viaje (fecha, hora, updated_at, estado_solicitud, id_ruta, id_pasajero)
VALUES
    ('2025-11-11', '07:15:00', '2025-11-11 07:15:00', 'FINALIZADO',
     (SELECT id_ruta FROM ruta WHERE origen='San Miguel' AND destino='UPC-Monterrico' LIMIT 1),
     (SELECT id_pasajero FROM pasajero WHERE dni='77777777' LIMIT 1));


-- ========== PAGOS ==========
INSERT INTO pago (
    monto, fecha, hora, comision, medio_pago, estado_pago, id_solicitud_viaje
) VALUES
      (10.00, '2025-10-05', '08:10:00', 0.50, 'YAPE', 'COMPLETADO',
       (SELECT id_solicitud_viaje FROM solicitud_viaje WHERE estado_solicitud='PENDIENTE' LIMIT 1)),
      (8.00, '2025-10-05', '17:50:00', 0.60, 'TARJETA_DEBITO', 'EN_PROGRESO',
       (SELECT id_solicitud_viaje FROM solicitud_viaje WHERE estado_solicitud='ACEPTADO' LIMIT 1));

-- ========== CALIFICACIONES ==========
INSERT INTO calificacion (puntaje, comentario, updated_at, id_conductor, id_pasajero)
VALUES
    (5, 'Viaje cómodo y puntual, muy amable el conductor.', '2025-10-05 09:15:00',
     (SELECT id_conductor FROM conductor WHERE dni='44444444'),
     (SELECT id_pasajero FROM pasajero WHERE dni='77777777')),
    (4, 'Todo bien, aunque podría mejorar la música.', '2025-10-05 19:30:00',
     (SELECT id_conductor FROM conductor WHERE dni='44444444'),
     (SELECT id_pasajero FROM pasajero WHERE dni='77777777'));