-- data-test.sql – PostgreSQL (UniRide) ✅ CORREGIDO
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
-- OJO: asegúrate que tu enum/DDL solo tenga estos 3 valores
INSERT INTO rol (name) VALUES
                           ('ADMIN'),
                           ('CONDUCTOR'),
                           ('PASAJERO');

-- ========== USUARIOS ==========
INSERT INTO usuario (email, password, id_rol) VALUES
                                                              ('admin@uniride.test',     'admin123',     (SELECT id_rol FROM rol WHERE name = 'ADMIN')),
                                                              ('conductor@uniride.test', 'driver123',    (SELECT id_rol FROM rol WHERE name = 'CONDUCTOR')),
                                                              ('pasajero@uniride.test',  'passenger123', (SELECT id_rol FROM rol WHERE name = 'PASAJERO')),
                                                              ('conductora@uniride.test','driver456',    (SELECT id_rol FROM rol WHERE name = 'CONDUCTOR'));


-- CONDUCTOR 1 (usuario 2, vehículo 1)
INSERT INTO conductor (
id_conductor,nombre,apellido,dni,edad,descripcion_conductor,created_at,updated_at,id_usuario
)
VALUES (
    1,    'Carlos',    'Soto',    '44444444',    30,
    '5 años de experiencia conduciendo autos de servicio.',    '2025-09-30 09:00:00',    '2025-09-30 09:00:00',    2),
    (2, 'Marta','Quispe','55555555',28,'Conduce con prudencia',
     '2025-09-30 10:30:00', NULL, 4);

-- VEHICULOS
INSERT INTO vehiculo
(id_vehiculo, placa, soat, modelo, marca, capacidad, descripcion_vehiculo, id_conductor)
VALUES
    (1, 'ABC-123', TRUE, 'Yaris',  'Toyota',  4, 'Sedán compacto, aire acondicionado', 1),
    (2, 'XYZ-987', TRUE, 'Accent', 'Hyundai', 4, 'Buen maletero, mantenimiento al día', 2);


-- ========== PASAJERO ==========
-- 1–1 con usuario 'pasajero@uniride.test'
INSERT INTO pasajero (
    nombre, apellido, dni, edad, descripcion_pasajero,
    created_at, updated_at, usuario_id_usuario
) VALUES (
             'Favio', 'Arroyo', '77777777', 24, 'Prefiere asiento delantero',
             '2025-09-30 10:00:00', NULL,
             (SELECT id_usuario FROM usuario WHERE email = 'pasajero@uniride.test')
         );


-- ========== SOLICITUDES ==========
-- Usa la secuencia explícitamente (columna NO es identity)
INSERT INTO solicitud_viaje (
    id_solicitud_viaje, fecha, hora, updated_at, estado_solicitud, id_ruta, id_pasajero
) VALUES
      (
          nextval('solicitud_viaje_seq'),
          '2025-10-05', '08:00:00', '2025-10-05 08:05:00', 'PENDIENTE',
          (SELECT id_ruta FROM ruta WHERE origen='Barranco' AND destino='Miraflores' LIMIT 1),
      (SELECT id_pasajero FROM pasajero WHERE dni = '77777777')
    ),
  (
    nextval('solicitud_viaje_seq'),
    '2025-10-05', '17:40:00', '2025-10-05 17:45:00', 'PENDIENTE',
    (SELECT id_ruta FROM ruta WHERE origen='Surco' AND destino='San Isidro' LIMIT 1),
    (SELECT id_pasajero FROM pasajero WHERE dni = '77777777')
  );

-- ========== PAGOS ==========
-- También usa secuencia (columna NO es identity)
INSERT INTO pago (
    id_pago, monto, fecha, hora, comision, medio_pago, estado_pago, id_solicitud_viaje
) VALUES
    (
        nextval('pago_seq'),
        8, '2025-10-05', '08:10:00', 0.50, 'YAPE', 'COMPLETADO',
        (SELECT id_solicitud_viaje FROM solicitud_viaje ORDER BY id_solicitud_viaje ASC LIMIT 1)
    ),
  (
    nextval('pago_seq'),
    10, '2025-10-05', '17:50:00', 0.60, 'TARJETA_DEBITO', 'EN_PROGRESO',
    (SELECT id_solicitud_viaje FROM solicitud_viaje ORDER BY id_solicitud_viaje DESC LIMIT 1)
  );

-- CALIFICACIONES
INSERT INTO calificacion
(id_calificacion, puntaje, comentario,                     updated_at,            id_conductor, id_pasajero)
VALUES
    (1, 5, 'Viaje cómodo y puntual',                          '2025-10-05 09:15:00', 1, 1),
    (2, 4, 'Todo bien, podría mejorar la música',             '2025-10-05 19:30:00', 1, 1);


-- Ruta extra del conductor 2
INSERT INTO ruta (
    origen, destino, fecha_salida, hora_salida,
    tarifa, asientos_disponibles, estado_ruta, id_conductor
) VALUES (
             'La Molina', 'Centro de Lima', '2025-10-06', '07:15:00',
             12, 4, 'PROGRAMADO',
             (SELECT id_conductor FROM conductor WHERE dni = '55555555')
         );
