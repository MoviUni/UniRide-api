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
                                                  ('admin@uniride.test',      'admin123',     (SELECT id_rol FROM rol WHERE name = 'ADMIN')),
                                                  ('conductor@uniride.test',  'driver123',    (SELECT id_rol FROM rol WHERE name = 'CONDUCTOR')),
                                                  ('pasajero@uniride.test',   'passenger123', (SELECT id_rol FROM rol WHERE name = 'PASAJERO')),
                                                  ('conductora@uniride.test', 'driver456',    (SELECT id_rol FROM rol WHERE name = 'CONDUCTOR')),
                                                  ('conductor3@uniride.test',   'driver789', (SELECT id_rol FROM rol WHERE name = 'CONDUCTOR'));

-- ========== CONDUCTORES ==========
INSERT INTO conductor (
    nombre, apellido, dni, edad, descripcion_conductor, created_at, updated_at, id_usuario
) VALUES
      ('Carlos', 'Soto', '44444444', 30, '5 años de experiencia conduciendo autos de servicio.', '2025-09-30 09:00:00', '2025-09-30 09:00:00',
       (SELECT id_usuario FROM usuario WHERE email='conductor@uniride.test')),
      ('Marta', 'Quispe', '55555555', 28, 'Conduce con prudencia y respeto por las normas de tránsito.', '2025-09-30 10:30:00', '2025-09-30 10:30:00',
       (SELECT id_usuario FROM usuario WHERE email='conductora@uniride.test')),
      ('David', 'Atencio', '22222222', 21, '3 años de experiencia conduciendo.', '2025-09-30 09:00:00', '2025-09-30 09:00:00',
       (SELECT id_usuario FROM usuario WHERE email='conductor3@uniride.test'));

-- ========== VEHICULOS ==========
-- INSERT INTO vehiculo (
--     placa, soat, modelo, color, marca, capacidad, descripcion_vehiculo, id_vehiculo
-- ) VALUES
--       ('ABC-123', TRUE, 'Yaris', 'Rojo', 'Toyota', 4, 'Sedán compacto, aire acondicionado y GPS.', (SELECT id_conductor FROM conductor WHERE dni='44444444')),
--       ('XYZ-987', TRUE, 'Accent', 'Azul', 'Hyundai', 4, 'Buen maletero, mantenimiento al día y asientos cómodos.', (SELECT id_conductor FROM conductor WHERE dni='55555555'));

-- ========== PASAJEROS ==========
INSERT INTO pasajero (
    nombre, apellido, dni, edad, descripcion_pasajero, created_at, updated_at, usuario_id_usuario
) VALUES
    ('Favio', 'Arroyo', '77777777', 24, 'Prefiere asiento delantero y viajes tranquilos.', '2025-09-30 10:00:00', '2025-09-30 10:00:00',
     (SELECT id_usuario FROM usuario WHERE email = 'pasajero@uniride.test'));

-- ========== RUTAS ==========
INSERT INTO ruta (
    origen, destino, fecha_salida, hora_salida, tarifa, asientos_disponibles, estado, id_conductor --estado_ruta
) VALUES
      ('Barranco', 'Miraflores', '2025-10-05', '08:00:00', 8.00, 3, 'PROGRAMADO',
       (SELECT id_conductor FROM conductor WHERE dni='44444444')),
      ('Surco', 'San Isidro', '2025-10-05', '17:30:00', 10.00, 2, 'PROGRAMADO',
       (SELECT id_conductor FROM conductor WHERE dni='44444444')),
      ('La Molina', 'Centro de Lima', '2025-10-06', '07:15:00', 12.00, 4, 'PROGRAMADO',
       (SELECT id_conductor FROM conductor WHERE dni='55555555')),
      ('La Molina', 'UPC-Monterrico', '2025-12-11', '07:15:00', 12.00, 4, 'PROGRAMADO',
       (SELECT id_conductor FROM conductor WHERE dni='22222222'));

-- ========== SOLICITUDES ==========
INSERT INTO solicitud_viaje (
    fecha, hora, updated_at, estado, id_ruta, id_pasajero --estado_solicitud
) VALUES
      ('2025-10-05', '08:00:00', '2025-10-05 08:05:00', 'PENDIENTE',
       (SELECT id_ruta FROM ruta WHERE origen='Barranco' AND destino='Miraflores'),
       (SELECT id_pasajero FROM pasajero WHERE dni='77777777')),
      ('2025-10-05', '17:40:00', '2025-10-05 17:45:00', 'ACEPTADO',
       (SELECT id_ruta FROM ruta WHERE origen='Surco' AND destino='San Isidro'),
       (SELECT id_pasajero FROM pasajero WHERE dni='77777777'));

-- ========== PAGOS ==========
INSERT INTO pago (
    monto, fecha, hora, comision, medio_pago, estado, id_solicitud_viaje --estado_pago
) VALUES
      (8.00, '2025-10-05', '08:10:00', 0.50, 'YAPE', 'COMPLETADO',
       (SELECT id_solicitud_viaje FROM solicitud_viaje WHERE estado='PENDIENTE')),
      (10.00, '2025-10-05', '17:50:00', 0.60, 'TARJETA_DEBITO', 'EN_PROGRESO',
       (SELECT id_solicitud_viaje FROM solicitud_viaje WHERE estado='ACEPTADO'));

-- ========== CALIFICACIONES ==========
INSERT INTO calificacion (puntaje, comentario, updated_at, id_conductor, id_pasajero)
VALUES
    (5, 'Viaje cómodo y puntual, muy amable el conductor.', '2025-10-05 09:15:00',
     (SELECT id_conductor FROM conductor WHERE dni='44444444'),
     (SELECT id_pasajero FROM pasajero WHERE dni='77777777')),
    (4, 'Todo bien, aunque podría mejorar la música.', '2025-10-05 19:30:00',
     (SELECT id_conductor FROM conductor WHERE dni='44444444'),
     (SELECT id_pasajero FROM pasajero WHERE dni='77777777'));