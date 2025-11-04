package com.example.unirideapi.unit;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.model.*;
import com.example.unirideapi.model.enums.ERol;
import com.example.unirideapi.service.impl.RutaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.repository.RutaRepository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.example.unirideapi.repository.ConductorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
@DisplayName("RutaService - Pruebas unitarias de ruta service")
public class RutaServiceTest {
    @Mock
    private RutaRepository rutaRepository;

    @Mock
    private RutaMapper rutaMapper;
  
    @Mock
    private ConductorRepository conductorRepository;

    @InjectMocks
    private RutaServiceImpl rutaService;


    private Conductor mockConductor;
    private Vehiculo mockVehiculo;
    private Usuario mockUsuario;
    private Rol mockRol;

    @BeforeEach
    void setUp() {
        // mockRuta = createMockRuta(null, LocalDate.parse("2025-09-23"), LocalTime.parse("08:30:17"),"Surquillo", "UPC Monterrico", Long.parseLong("12"), 4, EstadoRuta.PROGRAMADO, 1);
        mockVehiculo = createMockVehiculo(1, "Audi", "ABC-123", "Azul", "nuevo",
                true, 4, "Es auto nuevo");

        mockRol = createMockRol(ERol.CONDUCTOR,2);
        mockUsuario = createMockUsuario(1, mockRol,"admin@uniride.test", "driver123");

        mockConductor = createMockConductor(1,
                "Carlos","Soto",
                "5 años de experiencia conduciendo autos de servicio.",
                LocalDateTime.parse("2025-09-30T09:00:00"),
                30,
                "44444444",
                mockUsuario,
                mockVehiculo);
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

    private Conductor createMockConductor(Integer id, String nombre,String apellido,String descripcion,
                                          LocalDateTime createdAt, Integer edad, String dni,
                                          Usuario usuario, Vehiculo vehiculo) {

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
    @DisplayName("Debe mostrar todas las rutas disponibles dado un filtro válido")
    void getRuta_ValidFilter_Success() {
        // Arrange

        Ruta ruta = createMockRuta(4,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),"Surquillo",
                "UPC Monterrico",
                Long.parseLong("12"),
                4,
                EstadoRuta.PROGRAMADO,
                mockConductor);

        when(rutaRepository.searchBy("UPC Monterrico", "Surquillo",
                LocalTime.parse("08:30:17"),LocalDate.parse("2025-09-23"))
        ).thenReturn(Arrays.asList(ruta));

        // Act
        List<RutaResponseDTO> responses = rutaService.searchBy("UPC Monterrico", "Surquillo",
                "08:30:17","2025-09-23");

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Debe mostrar un arreglo vacío dado un filtro no válido")
    void getRuta_InvalidFilter_Success() {
        // Arrange
        Ruta ruta = createMockRuta(4,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),"Surquillo",
                "UPC San Isidro",
                Long.parseLong("12"),
                4,
                EstadoRuta.PROGRAMADO,
                mockConductor);

        List<Ruta> empty = new ArrayList<>();
        when(rutaRepository.searchBy("UPC San Isidro", "Surquillo",
                LocalTime.parse("08:30:17"),LocalDate.parse("2025-09-23"))
        ).thenReturn(empty);

        // Act
        List<RutaResponseDTO> responses = rutaService.searchBy("UPC San Isidro", "Surquillo",
                "08:30:17","2025-09-23");

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(0);

    }
    @Test
    @DisplayName("CP01: Confirmar viaje - debe confirmar viaje antes de la hora de salida")
    void updateEstadoRuta_RutaProgramada_ConfirmarViajeExito() {
        // Dado
        Ruta ruta = new Ruta();
        ruta.setIdRuta(1);
        ruta.setOrigen("Lima");
        ruta.setDestino("Cusco");
        ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        ruta.setFechaSalida(LocalDate.now().plusDays(1));
        ruta.setHoraSalida(LocalTime.now().plusHours(4));
        ruta.setAsientosDisponibles(3);

        RutaResponseDTO expectedResponse = RutaResponseDTO.builder()
                .idRuta(1)
                .origen("Lima")
                .destino("Cusco")
                .fechaSalida(ruta.getFechaSalida())
                .horaSalida(ruta.getHoraSalida())
                .tarifa(100L)
                .asientosDisponibles(3)
                .estadoRuta(EstadoRuta.CONFIRMADO)
                .idConductor(10)
                .build();

        when(rutaRepository.findById(1L)).thenReturn(Optional.of(ruta));
        when(rutaRepository.save(any(Ruta.class))).thenReturn(ruta);
        when(rutaMapper.toDTO(ruta)).thenReturn(expectedResponse);

        // Cuando
        RutaResponseDTO response = rutaService.updateEstadoRuta(1, EstadoRuta.CONFIRMADO);

        // Entonces
        assertThat(response).isNotNull();
        assertThat(response.estadoRuta()).isEqualTo(EstadoRuta.CONFIRMADO);

        verify(rutaRepository).findById(1L);
        verify(rutaRepository).save(ruta);
    }

    @Test
    @DisplayName("CP02: Cancelar Viaje - debe cancelar viaje antes de la hora de salida")
    void updateEstadoRuta_RutaProgramada_CancelarViajeExito() {
        // Dado
        Ruta ruta = new Ruta();
        ruta.setIdRuta(2);
        ruta.setOrigen("Arequipa");
        ruta.setDestino("Puno");
        ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        ruta.setFechaSalida(LocalDate.now().plusDays(1));
        ruta.setHoraSalida(LocalTime.now().plusHours(2));
        ruta.setAsientosDisponibles(4);

        RutaResponseDTO expectedResponse = RutaResponseDTO.builder()
                .idRuta(2)
                .origen("Arequipa")
                .destino("Puno")
                .fechaSalida(ruta.getFechaSalida())
                .horaSalida(ruta.getHoraSalida())
                .tarifa(80L)
                .asientosDisponibles(4)
                .estadoRuta(EstadoRuta.CANCELADO)
                .idConductor(15)
                .build();

        when(rutaRepository.findById(2L)).thenReturn(Optional.of(ruta));
        when(rutaRepository.save(any(Ruta.class))).thenReturn(ruta);
        when(rutaMapper.toDTO(ruta)).thenReturn(expectedResponse);

        // Cuando
        RutaResponseDTO response = rutaService.updateEstadoRuta(2, EstadoRuta.CANCELADO);

        // Entonces
        assertThat(response).isNotNull();
        assertThat(response.estadoRuta()).isEqualTo(EstadoRuta.CANCELADO);

        verify(rutaRepository).findById(2L);
        verify(rutaRepository).save(ruta);
    }
    @Test
    @DisplayName("CP03: Fuera del plazo - no debe permitir cambio de estado si la ruta ya está confirmada o cancelada")
    void updateEstadoRuta_RutaConfirmada_ThrowsException() {
        // Dado
        Ruta ruta = new Ruta();
        ruta.setIdRuta(3);
        ruta.setEstadoRuta(EstadoRuta.CONFIRMADO); // o CANCELADO
        ruta.setFechaSalida(LocalDate.now().plusDays(1));
        ruta.setHoraSalida(LocalTime.now().plusHours(5));

        when(rutaRepository.findById(3L)).thenReturn(Optional.of(ruta));

        // Cuando / Entonces
        assertThatThrownBy(() ->
                rutaService.updateEstadoRuta(3, EstadoRuta.CANCELADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Solo se pueden confirmar o cancelar rutas en estado PROGRAMADO");

        verify(rutaRepository).findById(3L);
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP04: Acción no permitida - no debe permitir confirmar o cancelar si falta menos de 1 hora para la salida")
    void updateEstadoRuta_MenosDeUnaHora_ThrowsException() {
        // Dado
        Ruta ruta = new Ruta();
        ruta.setIdRuta(4);
        ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        ruta.setFechaSalida(LocalDate.now());
        ruta.setHoraSalida(LocalTime.now().plusMinutes(30));

        when(rutaRepository.findById(4L)).thenReturn(Optional.of(ruta));

        // Cuando / Entonces
        assertThatThrownBy(() ->
                rutaService.updateEstadoRuta(4, EstadoRuta.CONFIRMADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("No se puede confirmar o cancelar el viaje con menos de 1 hora de anticipación");

        verify(rutaRepository).findById(4L);
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP05: Ruta no encontrada - debe lanzar excepción si la ruta no existe")
    void updateEstadoRuta_RutaNoEncontrada_ThrowsException() {
        // Dado
        when(rutaRepository.findById(99L)).thenReturn(Optional.empty());

        // Cuando / Entonces
        assertThatThrownBy(() ->
                rutaService.updateEstadoRuta(99, EstadoRuta.CONFIRMADO)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ruta no encontrada");

        verify(rutaRepository).findById(99L);
        verify(rutaRepository, never()).save(any());

    }
}
