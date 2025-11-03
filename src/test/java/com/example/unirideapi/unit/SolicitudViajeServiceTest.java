package com.example.unirideapi.unit;

import com.example.unirideapi.dto.request.SolicitudEstadoRequestDTO;
import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;

import com.example.unirideapi.dto.response.SolicitudEstadoResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;

import com.example.unirideapi.model.*;
import com.example.unirideapi.model.enums.ERol;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import com.example.unirideapi.repository.PasajeroRepository;
import com.example.unirideapi.repository.SolicitudViajeRepository;

import com.example.unirideapi.service.impl.SolicitudViajeServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.example.unirideapi.model.enums.EstadoRuta;

import com.example.unirideapi.repository.RutaRepository;
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
@DisplayName("RutaService - Pruebas unitarias de ruta service")
public class SolicitudViajeServiceTest {
    @Mock
    private SolicitudViajeRepository solicitudViajeRepository;

    @Mock
    private PasajeroRepository pasajeroRepository;

    @Mock
    private RutaRepository rutaRepository;

    @InjectMocks
    private SolicitudViajeServiceImpl solicitudViajeService;

    private Ruta mockRuta;
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

        mockPasajero = createMockPasajero(1,
                "Carlos","Soto",
                "5 años de experiencia conduciendo autos de servicio.",
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

        mockRuta = createMockRuta(null, LocalDate.parse("2025-09-23"), LocalTime.parse("08:30:17"),"Surquillo", "UPC Monterrico", Long.parseLong("12"), 4, EstadoRuta.PROGRAMADO, mockConductor);
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
        SolicitudViajeRequestDTO request = new SolicitudViajeRequestDTO(
                EstadoSolicitud.PENDIENTE,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),
                2,
                1,
                LocalDate.parse("2025-09-23")

        );

        //when(accountRepository.existsByAccountNumber(accountNumber)).thenReturn(false);
        SolicitudViaje savedSolicitud = createMockSolicitud(4, EstadoSolicitud.PENDIENTE,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"),LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta, mockPasajero);

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
        assertThat(response.rutaId()).isEqualTo(mockRuta.getIdRuta());

        //verify(rutaRepository).existsRutasByIdRuta();
        verify(solicitudViajeRepository).save(any(SolicitudViaje.class));
    }

    @Test
    @DisplayName("No debe tener éxito creando una solicitud de viaje con datos no válidos")
    void createSolicitudViaje_DuplicateData_Conflict() {

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
                mockRuta, mockPasajero);

        when(solicitudViajeRepository.save(any(SolicitudViaje.class))).thenReturn(savedSolicitud1);
        Pasajero pasajero1 = new Pasajero();
        when(pasajeroRepository.findById(1)).thenReturn(Optional.of(pasajero1));
        Ruta ruta2 = new Ruta();
        when(rutaRepository.findById(1L)).thenReturn(Optional.of(ruta2));

        when(solicitudViajeRepository.existDuplicate(1, 1)).thenReturn(true);

        // Assert
        assertThatThrownBy(() -> solicitudViajeService.create(request2))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Un usuario no puede enviar más de una solicitud a una misma ruta");

        verify(solicitudViajeRepository, never()).save(any(SolicitudViaje.class));
    }
    @Test
    @DisplayName("Debe cancelar una solicitud de manera exitosa.")
    void patchSolicitudViaje_CambiarEstado_Success(){
        // Arrange
        SolicitudViaje savedSolicitud = createMockSolicitud(4, EstadoSolicitud.PENDIENTE,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"),LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta, mockPasajero);

        SolicitudViaje patchedSolicitud = createMockSolicitud(4, EstadoSolicitud.CANCELADO,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"),LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta, mockPasajero);

        Integer state = 0;
        when(solicitudViajeRepository.actualizarEstadoSolicitud(savedSolicitud.getIdSolicitudViaje(), EstadoSolicitud.CANCELADO)).thenReturn(1);

        // Assert
        assertThat(solicitudViajeRepository.findById(Long.valueOf(patchedSolicitud.getIdSolicitudViaje())).get().getEstadoSolicitud()).isEqualTo(EstadoSolicitud.CANCELADO);

    }

    @Test
    @DisplayName("No debe tener exito cancelando la solicitud.")
    void patchSolicitudViaje_CambiarEstado_Conflict(){

    }
    @Test
    @DisplayName("Debe mostrar un arreglo vacío de las solicitudes del pasajero (no tiene solicitudes)")
    void getSolicitudViaje_SinSolicitudes_Success() {

        // Act
        List<SolicitudEstadoResponseDTO> responses = solicitudViajeService.searchByUsuario(mockPasajero.getIdPasajero());

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("Debe mostrar un arreglo con todos los estados de las solicitudes del pasajero")
    void getSolicitudViaje_ConSolicitudes_Success() {
        // Arrange
        SolicitudViajeRequestDTO request = new SolicitudViajeRequestDTO(
                EstadoSolicitud.PENDIENTE,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),
                1,
                1,
                LocalDate.parse("2025-09-23")

        );

        // Se crea una mockSolicitud
        SolicitudViaje savedSolicitud = createMockSolicitud(4, EstadoSolicitud.PENDIENTE,
                LocalTime.parse("08:30:17"), LocalDate.parse("2025-09-23"),LocalDateTime.parse("2025-09-23T08:30:17"),
                mockRuta, mockPasajero);
        when(solicitudViajeRepository.save(any(SolicitudViaje.class))).thenReturn(savedSolicitud);

        when(pasajeroRepository.findById(1)).thenReturn(Optional.of(mockPasajero));

        when(rutaRepository.findById(1L)).thenReturn(Optional.of(mockRuta));

        when(solicitudViajeRepository.searchByUsuario(mockPasajero.getIdPasajero())).thenReturn(Arrays.asList(savedSolicitud));
        // Act
        // guardar solicitud
        SolicitudViajeResponseDTO response = solicitudViajeService.create(request);


        // buscar estados de las solicitudes (deberia aparecer la solicitud que acabo de crear)
        List<SolicitudEstadoResponseDTO> responses = solicitudViajeService.searchByUsuario(savedSolicitud.getPasajero().getIdPasajero());

        // Assert
        assertThat(response).isNotNull();
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isGreaterThan(0);
    }


}