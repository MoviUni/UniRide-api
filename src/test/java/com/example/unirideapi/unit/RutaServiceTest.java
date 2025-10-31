package com.example.unirideapi.unit;


import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.mapper.ConductorMapper;
import com.example.unirideapi.model.*;
import com.example.unirideapi.model.enums.ERol;
import com.example.unirideapi.service.impl.ConductorServiceImpl;
import com.example.unirideapi.service.impl.RutaServiceImpl;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.unirideapi.exception.ResourceNotFoundException;

import com.example.unirideapi.model.enums.EstadoRuta;

import com.example.unirideapi.repository.ConductorRepository;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.service.RutaService;

import org.springframework.http.MediaType;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RutaService - Pruebas unitarias de ruta service")
public class RutaServiceTest {
    @Mock
    private RutaRepository rutaRepository;

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
    @DisplayName("Debe mostrar un arreglo vacío dado un filtro inválido")
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
}
