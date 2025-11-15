package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudEstadoResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.SolicitudViajeMapper;
import com.example.unirideapi.model.*;
import com.example.unirideapi.model.enums.ERol;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import com.example.unirideapi.repository.PasajeroRepository;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.repository.SolicitudViajeRepository;
import com.example.unirideapi.service.impl.SolicitudViajeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de solicitudViaje Service")
public class SolicitudViajeServiceTest {

    @Mock
    private SolicitudViajeRepository solicitudViajeRepository;

    @Mock
    private SolicitudViajeMapper solicitudViajeMapper;

    @Mock
    private PasajeroRepository pasajeroRepository;

    @Mock
    private RutaRepository rutaRepository;

    @InjectMocks
    private SolicitudViajeServiceImpl solicitudViajeService;

    private Ruta mockRuta;

    private Ruta mockRuta1;

    private Pasajero mockPasajero;
    private Conductor mockConductor;
    private Vehiculo mockVehiculo;
    private Usuario mockUsuario;
    private Rol mockRol;
    private Rol mockRolC;
    private Usuario mockUsuarioC;

    @BeforeEach
    void setUp() {


        mockRol = createMockRol(ERol.PASAJERO,3);
        mockUsuario = createMockUsuario(1, mockRol,"user@uniride.test", "driver123");

        mockPasajero = createMockPasajero(1, "Carlos","Soto", "5 años de experiencia conduciendo autos de servicio.",
                LocalDateTime.parse("2025-09-30T09:00:00"),LocalDateTime.parse("2025-09-30T09:00:00"),
                30,
                "44444444",
                mockUsuario);

        mockRolC = createMockRol(ERol.CONDUCTOR,2);
        mockUsuarioC = createMockUsuario(2, mockRol,"driver@uniride.test", "driver123");
        mockVehiculo = createMockVehiculo(1, "Audi", "ABC-123", "Azul", "nuevo",
                true, 4, "Es auto nuevo");

        mockConductor = createMockConductor(1,
                "Carlos","Soto",
                "5 años de experiencia conduciendo autos de servicio.",
                LocalDateTime.parse("2025-09-30T09:00:00"),
                30,
                "44444444",
                mockUsuario,
                mockVehiculo);

        mockRuta1 = createMockRuta(1, LocalDate.parse("2025-09-23"), LocalTime.parse("08:30:17"),"Surquillo", "UPC Monterrico", Long.parseLong("12"), 4, EstadoRuta.PROGRAMADO, mockConductor);

    }

    private SolicitudViaje createMockSolicitud(Integer id, EstadoSolicitud estadoSolicitud, LocalTime hora, LocalDate fecha, LocalDateTime updatedAt, Ruta ruta, Pasajero pasajero){

        SolicitudViaje solicitud = new SolicitudViaje();
        solicitud.setIdSolicitudViaje(id);
        solicitud.setEstadoSolicitud(estadoSolicitud);
        solicitud.setFecha(fecha);
        solicitud.setHora(hora);
        solicitud.setUpdatedAt(updatedAt);
        solicitud.setRuta(ruta);
        solicitud.setPasajero(pasajero);
        return solicitud;
    }

    private Ruta createMockRuta(Integer id, LocalDate fechaSalida, LocalTime horaSalida,
                                String origen, String destino, Long tarifa, Integer asientosDisponibles,
                                EstadoRuta  estadoRuta, Conductor conductor) {

        Ruta ruta = new Ruta();
        ruta.setIdRuta(id);
        ruta.setEstadoRuta(estadoRuta);
        ruta.setFechaSalida(fechaSalida);
        ruta.setHoraSalida(horaSalida);
        ruta.setTarifa(tarifa);
        ruta.setAsientosDisponibles(asientosDisponibles);
        ruta.setOrigen(origen);
        ruta.setDestino(destino);
        ruta.setConductor(conductor);

        return ruta;
    }

    private Pasajero createMockPasajero(Integer id, String nombre,String apellido,String descripcion,
                                        LocalDateTime createdAt,LocalDateTime updatedAt, Integer edad, String dni,
                                        Usuario usuario) {

        Pasajero pasajero = new Pasajero();
        pasajero.setIdPasajero(id);
        pasajero.setNombre(nombre);
        pasajero.setApellido(apellido);
        pasajero.setDescripcionPasajero(descripcion);
        pasajero.setEdad(edad);
        pasajero.setDni(dni);
        pasajero.setUsuario(usuario);
        pasajero.setUpdatedAt(updatedAt);
        pasajero.setCreatedAt(createdAt);

        return pasajero;
    }

    private Conductor createMockConductor(Integer id, String nombre,String apellido,String descripcion,
                                          LocalDateTime createdAt, Integer edad, String dni,
                                          Usuario usuario, Vehiculo vehiculo) {
        //Usuario us = createMockUsuario(1,  ERol.CONDUCTOR,2, "admin@uniride.test", "driver123");
        //Vehiculo vehiculo = createMockVehiculo(1, "Audi", "ABC-123", "Azul", "nuevo",
        //        true, 4, "Es auto nuevo");

        Conductor conductor = new Conductor();
        conductor.setIdConductor(id);
        conductor.setNombre(nombre);
        conductor.setApellido(apellido);
        conductor.setDescripcionConductor(descripcion);
        conductor.setEdad(edad);
        conductor.setDni(dni);
        conductor.setUsuario(usuario);
        conductor.setVehiculo(vehiculo);
        conductor.setCreatedAt(createdAt);

        return conductor;
    }

    private Usuario createMockUsuario(Integer id,Rol rol, String email, String password){

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setRol(rol);
        usuario.setEmail(email);
        usuario.setPassword(password);

        return usuario;
    }

    private Rol createMockRol(ERol name, Integer id){
        Rol rol = new Rol();
        rol.setIdRol(id);
        rol.setName(name);
        return rol;
    }

    private Vehiculo createMockVehiculo(Integer id, String marca, String placa, String color, String modelo,
                                        Boolean soat, Integer capacidad, String descripcion){
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setIdVehiculo(id);
        vehiculo.setMarca(marca);
        vehiculo.setPlaca(placa);
        vehiculo.setColor(color);
        vehiculo.setModelo(modelo);
        vehiculo.setSoat(soat);
        vehiculo.setCapacidad(capacidad);
        vehiculo.setDescripcionVehiculo(descripcion);
        return vehiculo;
    }

    @Test
    @DisplayName("Debe crear una solicitud de viaje exitosamente con datos válidos")
    void createSolicitudViaje_ValidData_Success() {
        // Arrange
        SolicitudViajeRequestDTO request = new SolicitudViajeRequestDTO(EstadoSolicitud.PENDIENTE, LocalDate.parse("2025-09-23"), LocalTime.parse("08:30:17"),
                2, 1, LocalDate.parse("2025-09-23")
        );

        //when(accountRepository.existsByAccountNumber(accountNumber)).thenReturn(false);
        SolicitudViaje savedSolicitud = createMockSolicitud(4, EstadoSolicitud.PENDIENTE,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"),LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta1, mockPasajero);

        when(solicitudViajeRepository.save(any(SolicitudViaje.class))).thenReturn(savedSolicitud);
        Pasajero pasajero = new Pasajero();
        when(pasajeroRepository.findById(1)).thenReturn(Optional.of(pasajero));
        Ruta ruta = new Ruta();
        when(rutaRepository.findById(2L)).thenReturn(Optional.of(ruta));
        // Act
        SolicitudViajeResponseDTO response = solicitudViajeService.create(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.idSolicitudViaje()).isNotNull();
        assertThat(response.hora()).isEqualTo(LocalTime.parse("08:30:17"));
        assertThat(response.fecha()).isEqualTo(LocalDate.parse("2025-09-23"));
        assertThat(response.updatedAt()).isEqualTo(LocalDateTime.parse("2025-09-23T08:30:17"));
        assertThat(response.rutaId()).isEqualTo(mockRuta1.getIdRuta());
        verify(solicitudViajeRepository).save(any(SolicitudViaje.class));
    }

    @Test
    @DisplayName("No debe tener éxito creando una solicitud de viaje con datos no válidos")
    void createSolicitudViaje_DuplicateData_ThrowsBusinessRuleException() {

        // Arrange
        SolicitudViajeRequestDTO request2 = new SolicitudViajeRequestDTO(
                EstadoSolicitud.PENDIENTE,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),
                1,
                1,
                LocalDate.parse("2025-09-23")

        );
        SolicitudViaje savedSolicitud1 = createMockSolicitud(4, EstadoSolicitud.PENDIENTE,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"),LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta1, mockPasajero);

        Pasajero pasajero1 = mockPasajero;
        List<SolicitudViaje> lista = List.of(savedSolicitud1);
        when(solicitudViajeRepository.searchByUsuario(pasajero1.getIdPasajero())).thenReturn(lista);

        // Assert
        assertThatThrownBy(() -> solicitudViajeService.create(request2))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Un usuario no puede enviar más de una solicitud a una misma ruta");


    }
    @Test
    @DisplayName("Debe cancelar una solicitud de manera exitosa.")
    void patchSolicitudViaje_EstadoValido_Success() {
        // Arrange
        SolicitudViaje savedSolicitud = createMockSolicitud(4, EstadoSolicitud.PENDIENTE,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"), LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta1, mockPasajero);
        when(solicitudViajeRepository.findById(Long.valueOf(savedSolicitud.getIdSolicitudViaje()))).thenReturn(Optional.of(savedSolicitud));

        Ruta ruta = mockRuta1;
        when(rutaRepository.findById(Long.valueOf(savedSolicitud.getRuta().getIdRuta()))).thenReturn(Optional.of(ruta));
        when(solicitudViajeMapper.toDTO(any(SolicitudViaje.class)))
                .thenAnswer(invocation -> {
                    SolicitudViaje s = invocation.getArgument(0);
                    return new SolicitudViajeResponseDTO(
                            s.getIdSolicitudViaje(),
                            s.getFecha(),
                            s.getHora(),
                            null,
                            s.getEstadoSolicitud(),
                            s.getRuta().getIdRuta(),
                            100
                    );
                });

        // Act
        SolicitudViajeResponseDTO response = solicitudViajeService.cancelSolicitud(savedSolicitud.getIdSolicitudViaje());
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.estadoSolicitud()).isEqualTo(EstadoSolicitud.CANCELADO);
    }

    @Test
    @DisplayName("No debe tener exito cancelando la solicitud.")
    void patchSolicitudViaje_EstadoInvalido_ThrowsBusinessRuleException(){
        // Arrange
        SolicitudViaje savedSolicitud = createMockSolicitud(4, EstadoSolicitud.ACEPTADO,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"),LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta1, mockPasajero);
        when(solicitudViajeRepository.findById(Long.valueOf(savedSolicitud.getIdSolicitudViaje()))).thenReturn(Optional.of(savedSolicitud));

        Ruta ruta = mockRuta1;
        when(rutaRepository.findById(Long.valueOf(savedSolicitud.getRuta().getIdRuta()))).thenReturn(Optional.of(ruta));


        // Assert
        assertThatThrownBy(() -> solicitudViajeService.cancelSolicitud(savedSolicitud.getIdSolicitudViaje()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Solo se pueden cancelar solicitudes pendientes");

    }
    @Test
    @DisplayName("Debe mostrar un arreglo vacío de las solicitudes del pasajero (no tiene solicitudes)")
    void getEstadoSolicitudViaje_SinSolicitudes_Success() {

        // Act
        List<SolicitudViajeResponseDTO> responses = solicitudViajeService.searchByUsuario(mockPasajero.getIdPasajero());

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("Debe mostrar un arreglo con todos los estados de las solicitudes del pasajero")
    void getEstadoSolicitudViaje_ConSolicitudes_Success() {
        // Arrange
        SolicitudViajeRequestDTO request = new SolicitudViajeRequestDTO(
                EstadoSolicitud.PENDIENTE,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),
                1,
                1,
                LocalDate.parse("2025-09-23")

        );

        SolicitudViaje savedSolicitud = createMockSolicitud(4, EstadoSolicitud.PENDIENTE,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"),LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta1, mockPasajero);

        when(solicitudViajeRepository.searchByUsuario(mockPasajero.getIdPasajero())).thenReturn(Arrays.asList(savedSolicitud));
        // Act
        List<SolicitudViajeResponseDTO> responses = solicitudViajeService.searchByUsuario(savedSolicitud.getPasajero().getIdPasajero());

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isGreaterThan(0);
    }


    // -----US: 13------
    @Test
    @DisplayName("CP01: Listado de solicitudes - debe listar las solicitudes por ruta")
    void findSolicitudesByRutaId_RutaExistente_Success() {
        // DADO
        Integer idRuta = 10;

        Ruta ruta = new Ruta();
        ruta.setIdRuta(idRuta);
        ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);

        SolicitudViaje solicitudPendiente = new SolicitudViaje();
        solicitudPendiente.setIdSolicitudViaje(1);
        solicitudPendiente.setFecha(LocalDate.now());
        solicitudPendiente.setHora(LocalTime.now());
        solicitudPendiente.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitudPendiente.setRuta(ruta);

        SolicitudViaje solicitudAceptada = new SolicitudViaje();
        solicitudAceptada.setIdSolicitudViaje(2);
        solicitudAceptada.setFecha(LocalDate.now());
        solicitudAceptada.setHora(LocalTime.now());
        solicitudAceptada.setEstadoSolicitud(EstadoSolicitud.ACEPTADO);
        solicitudAceptada.setRuta(ruta);

        List<SolicitudViaje> solicitudes = List.of(solicitudPendiente, solicitudAceptada);

        SolicitudViajeResponseDTO dtoPendiente = new SolicitudViajeResponseDTO(
                1, solicitudPendiente.getFecha(), solicitudPendiente.getHora(), null,
                EstadoSolicitud.PENDIENTE, idRuta, 5
        );

        SolicitudViajeResponseDTO dtoAceptada = new SolicitudViajeResponseDTO(
                2, solicitudAceptada.getFecha(), solicitudAceptada.getHora(), null,
                EstadoSolicitud.ACEPTADO, idRuta, 6
        );

        when(rutaRepository.findById(10L)).thenReturn(Optional.of(ruta));
        when(solicitudViajeRepository.findByRutaId(10)).thenReturn(solicitudes);
        when(solicitudViajeMapper.toDTO(solicitudPendiente)).thenReturn(dtoPendiente);
        when(solicitudViajeMapper.toDTO(solicitudAceptada)).thenReturn(dtoAceptada);

        // CUANDO
        List<SolicitudViajeResponseDTO> resultado = solicitudViajeService.findSolicitudesByRutaId(idRuta);

        // ENTONCES
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).estadoSolicitud()).isEqualTo(EstadoSolicitud.PENDIENTE);
        assertThat(resultado.get(1).estadoSolicitud()).isEqualTo(EstadoSolicitud.ACEPTADO);

        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository).findByRutaId(10);
        verify(solicitudViajeMapper, times(2)).toDTO(any(SolicitudViaje.class));
    }
    @Test
    @DisplayName("CP02: Ruta no encontrada - debe lanzar excepción cuando la ruta no existe.")
    void findSolicitudesByRutaId_RutaNoEncontrada_ThrowsException() {
        // DADO
        Integer idRutaInexistente = 99;
        when(rutaRepository.findById(99L)).thenReturn(Optional.empty());

        // CUANDO / ENTONCES
        assertThatThrownBy(() -> solicitudViajeService.findSolicitudesByRutaId(idRutaInexistente))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ruta no encontrada");

        verify(rutaRepository).findById(99L);
        verifyNoInteractions(solicitudViajeRepository);
        verifyNoInteractions(solicitudViajeMapper);
    }
    // ----- US: 14-------
    @Test
    @DisplayName("CP01: Aceptar solicitud - debe aceptar una solicitud pendiente y reducir los asientos disponibles")
    void updateEstadoSolicitud_AceptarSolicitud_Success() {
        // DADO
        Ruta mockRuta = new Ruta();
        mockRuta.setIdRuta(10);
        mockRuta.setEstadoRuta(EstadoRuta.CONFIRMADO);
        mockRuta.setAsientosDisponibles(3);

        SolicitudViaje solicitud = new SolicitudViaje();
        solicitud.setIdSolicitudViaje(1);
        solicitud.setFecha(LocalDate.now());
        solicitud.setHora(LocalTime.now());
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitud.setRuta(mockRuta);

        SolicitudViajeResponseDTO expectedResponse = new SolicitudViajeResponseDTO(
                1,
                solicitud.getFecha(),
                solicitud.getHora(),
                null,
                EstadoSolicitud.ACEPTADO,
                mockRuta.getIdRuta(),
                100
        );

        when(solicitudViajeRepository.findById(1L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));
        when(solicitudViajeRepository.save(any(SolicitudViaje.class))).thenReturn(solicitud);
        when(solicitudViajeMapper.toDTO(any(SolicitudViaje.class))).thenReturn(expectedResponse);

        // CUANDO
        SolicitudViajeResponseDTO resultado = solicitudViajeService.updateEstadoSolicitud(1, EstadoSolicitud.ACEPTADO);

        // ENTONCES
        assertThat(resultado).isNotNull();
        assertThat(resultado.estadoSolicitud()).isEqualTo(EstadoSolicitud.ACEPTADO);
        assertThat(mockRuta.getAsientosDisponibles()).isEqualTo(2);

        verify(solicitudViajeRepository).findById(1L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository).save(solicitud);
    }
    @Test
    @DisplayName("CP02: Rechazar solicitud - debe rechazar una solicitud pendiente sin modificar los asientos disponibles")
    void updateEstadoSolicitud_RechazarSolicitud_Success() {
        // DADO
        Ruta mockRuta = new Ruta();
        mockRuta.setIdRuta(10);
        mockRuta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        mockRuta.setAsientosDisponibles(4);

        SolicitudViaje solicitud = new SolicitudViaje();
        solicitud.setIdSolicitudViaje(2);
        solicitud.setFecha(LocalDate.now());
        solicitud.setHora(LocalTime.now());
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitud.setRuta(mockRuta);

        SolicitudViajeResponseDTO expectedResponse = new SolicitudViajeResponseDTO(
                2,
                solicitud.getFecha(),
                solicitud.getHora(),
                null,
                EstadoSolicitud.RECHAZADO,
                mockRuta.getIdRuta(),
                200
        );

        when(solicitudViajeRepository.findById(2L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));
        when(solicitudViajeRepository.save(any(SolicitudViaje.class))).thenReturn(solicitud);
        when(solicitudViajeMapper.toDTO(any(SolicitudViaje.class))).thenReturn(expectedResponse);

        // CUANDO
        SolicitudViajeResponseDTO resultado = solicitudViajeService.updateEstadoSolicitud(2, EstadoSolicitud.RECHAZADO);

        // ENTONCES
        assertThat(resultado).isNotNull();
        assertThat(resultado.estadoSolicitud()).isEqualTo(EstadoSolicitud.RECHAZADO);
        assertThat(mockRuta.getAsientosDisponibles()).isEqualTo(4);

        verify(solicitudViajeRepository).findById(2L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository).save(solicitud);
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP03: Repetir acción - debe lanzar excepción si se intenta modificar una solicitud ya aceptada o rechazada")
    void updateEstadoSolicitud_SolicitudNoPendiente_ThrowsException() {
        // DADO
        Ruta mockRuta = new Ruta();
        mockRuta.setIdRuta(10);
        mockRuta.setEstadoRuta(EstadoRuta.CONFIRMADO);
        mockRuta.setAsientosDisponibles(2);

        SolicitudViaje solicitud = new SolicitudViaje();
        solicitud.setIdSolicitudViaje(3);
        solicitud.setFecha(LocalDate.now());
        solicitud.setHora(LocalTime.now());
        solicitud.setEstadoSolicitud(EstadoSolicitud.ACEPTADO);
        solicitud.setRuta(mockRuta);

        when(solicitudViajeRepository.findById(3L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));

        // CUANDO / ENTONCES
        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(3, EstadoSolicitud.RECHAZADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Solo se pueden aceptar o rechazar solicitudes pendientes");

        verify(solicitudViajeRepository).findById(3L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository, never()).save(any());
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP04: Ruta con estado inválido - debe lanzar excepción si la ruta no está programada ni confirmada")
    void updateEstadoSolicitud_RutaInvalida_ThrowsException() {
        // DADO QUE la ruta está cancelada
        Ruta mockRuta = new Ruta();
        mockRuta.setIdRuta(10);
        mockRuta.setEstadoRuta(EstadoRuta.CANCELADO);
        mockRuta.setAsientosDisponibles(3);

        SolicitudViaje solicitud = new SolicitudViaje();
        solicitud.setIdSolicitudViaje(4);
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitud.setRuta(mockRuta);

        when(solicitudViajeRepository.findById(4L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));

        // CUANDO / ENTONCES
        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(4, EstadoSolicitud.ACEPTADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("No se pueden aceptar o rechazar solicitudes si la ruta no está programada o confirmada");

        verify(solicitudViajeRepository).findById(4L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository, never()).save(any());
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP05: Sin asientos disponibles - debe lanzar excepción si no hay asientos disponibles para aceptar la solicitud")
    void updateEstadoSolicitud_SinAsientosDisponibles_ThrowsException() {
        // DADO QUE la ruta está programada pero no tiene asientos
        Ruta mockRuta = new Ruta();
        mockRuta.setIdRuta(10);
        mockRuta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        mockRuta.setAsientosDisponibles(0);

        SolicitudViaje solicitud = new SolicitudViaje();
        solicitud.setIdSolicitudViaje(5);
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitud.setRuta(mockRuta);

        when(solicitudViajeRepository.findById(5L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));

        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(5, EstadoSolicitud.ACEPTADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("No hay asientos disponibles para aceptar la solicitud");

        verify(solicitudViajeRepository).findById(5L);
        verify(rutaRepository).findById(10L);
        verify(rutaRepository, never()).save(any());
        verify(solicitudViajeRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP06: Solicitud no encontrada - debe lanzar excepción si la solicitud no existe")
    void updateEstadoSolicitud_SolicitudNoEncontrada_ThrowsException() {
        // DADO QUE no existe una solicitud con el ID proporcionado
        when(solicitudViajeRepository.findById(6L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(6, EstadoSolicitud.ACEPTADO)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Solicitud no encontrada");

        verify(solicitudViajeRepository).findById(6L);
        verify(rutaRepository, never()).findById(any());
        verify(solicitudViajeRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP07: Ruta no encontrada - debe lanzar excepción si la ruta asociada a la solicitud no existe")
    void updateEstadoSolicitud_RutaNoEncontrada_ThrowsException() {
        // DADO QUE existe una solicitud pendiente, pero la ruta no se encuentra
        Ruta mockRuta = new Ruta();
        mockRuta.setIdRuta(10);

        SolicitudViaje solicitud = new SolicitudViaje();
        solicitud.setIdSolicitudViaje(7);
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitud.setRuta(mockRuta);

        when(solicitudViajeRepository.findById(7L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(7, EstadoSolicitud.ACEPTADO)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ruta no encontrada");

        verify(solicitudViajeRepository).findById(7L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository, never()).save(any());
    }


}