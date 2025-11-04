package com.example.unirideapi.unit;

import com.example.unirideapi.dto.request.VehiculoRequestDTO;
import com.example.unirideapi.dto.response.VehiculoResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.VehiculoMapper;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Vehiculo;
import com.example.unirideapi.repository.ConductorRepository;
import com.example.unirideapi.repository.VehiculoRepository;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.example.unirideapi.unit.impl.VehiculoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de Vehiculo Service")
public class VehiculoServiceTest {
    @Mock
    VehiculoRepository vehiculoRepository;
    @Mock
    VehiculoMapper vehiculoMapper;
    @Mock
    ConductorRepository conductorRepository;
    @InjectMocks
    VehiculoServiceImpl vehiculoService;

    //--------US: 16---------------
    @Test
    @DisplayName("CP01: Registro exitoso - debe registrar correctamente un vehículo válido")
    void rregistrarVehiculo_VehiculoValido_RegistroExitoso() {
        // Dado que soy un conductor que no tiene vehículo registrado
        Conductor conductor = new Conductor();
        conductor.setIdConductor(1);
        conductor.setVehiculo(null);

        VehiculoRequestDTO request = VehiculoRequestDTO.builder()
                .placa("ABC-123")
                .soat(true)
                .modelo("Corolla")
                .marca("Toyota")
                .color("Rojo")
                .capacidad(4)
                .descripcionVehiculo("Sedán en buen estado")
                .idConductor(1)
                .build();

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca("ABC-123");
        vehiculo.setMarca("Toyota");
        vehiculo.setModelo("Corolla");
        vehiculo.setCapacidad(4);
        vehiculo.setConductor(conductor);

        VehiculoResponseDTO responseDTO = VehiculoResponseDTO.builder()
                .placa("ABC-123")
                .soat(true)
                .modelo("Corolla")
                .marca("Toyota")
                .color("Rojo")
                .capacidad(4)
                .descripcionVehiculo("Sedán en buen estado")
                .build();

        // Y proporciono los datos válidos del vehículo
        when(vehiculoRepository.existsByPlaca("ABC-123")).thenReturn(false);
        when(conductorRepository.findById(1)).thenReturn(Optional.of(conductor));
        when(vehiculoMapper.toEntity(request)).thenReturn(vehiculo);
        when(vehiculoRepository.save(vehiculo)).thenReturn(vehiculo);
        when(vehiculoMapper.toDTO(vehiculo)).thenReturn(responseDTO);

        // Cuando envío el formulario de registro del vehículo
        VehiculoResponseDTO resultado = vehiculoService.registrarVehiculo(1, request);

        // Entonces el sistema registra el vehículo correctamente
        assertThat(resultado).isNotNull();
        assertThat(resultado.placa()).isEqualTo("ABC-123");
        assertThat(resultado.marca()).isEqualTo("Toyota");
        assertThat(resultado.modelo()).isEqualTo("Corolla");
        assertThat(resultado.capacidad()).isEqualTo(4);
        assertThat(vehiculo.getConductor()).isEqualTo(conductor);

        verify(vehiculoRepository).save(vehiculo);

    }
    @Test
    @DisplayName("CP02: Registrar otro vehículo - debe rechazar registro si el conductor ya tiene un vehículo")
    void registrarVehiculo_ConductorConVehiculoExistente_LanzaExcepcion() {
        // DADO QUE soy un conductor que ya tiene un vehículo registrado
        Conductor conductor = new Conductor();
        conductor.setIdConductor(1);
        conductor.setVehiculo(new Vehiculo()); // ya tiene uno

        VehiculoRequestDTO request = VehiculoRequestDTO.builder()
                .placa("XYZ-999")
                .soat(true)
                .modelo("Civic")
                .marca("Honda")
                .color("Negro")
                .capacidad(4)
                .descripcionVehiculo("Auto deportivo")
                .idConductor(1)
                .build();

        when(conductorRepository.findById(1)).thenReturn(Optional.of(conductor));

        // CUANDO/ENTONCES
        assertThatThrownBy(() ->
                vehiculoService.registrarVehiculo(1, request)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("El conductor ya tiene un vehículo registrado");

        // Verificaciones: no se guarda nada
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("CP03: Placa duplicada — debe lanzar excepción si la placa ya está registrada")
    void registrarVehiculo_PlacaDuplicada() {
        // DADO QUE existe otro vehículo registrado con la misma placa
        VehiculoRequestDTO request = VehiculoRequestDTO.builder()
                .placa("ABC-123")
                .soat(true)
                .modelo("Yaris")
                .marca("Toyota")
                .color("Azul")
                .capacidad(4)
                .descripcionVehiculo("Auto compacto")
                .idConductor(2)
                .build();

        when(vehiculoRepository.existsByPlaca("ABC-123")).thenReturn(true);

        // CUANDO/ ENTONCES
        assertThatThrownBy(() ->
                vehiculoService.registrarVehiculo(2, request)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("La placa ya está registrada");

        // Verificaciones
        verify(conductorRepository, never()).findById(any());
        verify(vehiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("CP04: Capacidad inválida — debe lanzar excepción si la capacidad es menor o igual a cero")
    void registrarVehiculo_CapacidadInvalida_LanzaExcepcion() {
        // DADO QUE estoy registrando mi vehículo con una capacidad inválida
        VehiculoRequestDTO request = VehiculoRequestDTO.builder()
                .placa("DEF-456")
                .soat(true)
                .modelo("Model 3")
                .marca("Tesla")
                .color("Blanco")
                .capacidad(0) // Capacidad inválida
                .descripcionVehiculo("Vehículo eléctrico")
                .idConductor(3)
                .build();

        // CUANDO/ENTONCES
        assertThatThrownBy(() ->
                vehiculoService.registrarVehiculo(3, request)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("La capacidad del vehículo debe ser mayor a cero");

        // Verificaciones: no se consulta repositorio ni guarda nada
        verify(vehiculoRepository, never()).existsByPlaca(any());
        verify(conductorRepository, never()).findById(any());
    }

    @Test
    @DisplayName("CP05: Conductor no encontrado — debe lanzar excepción si el conductor no existe")
    void registrarVehiculo_ConductorNoEncontrado_LanzaExcepcion() {
        // DADO QUE intento registrar un vehículo con un ID de conductor inexistente
        VehiculoRequestDTO request = VehiculoRequestDTO.builder()
                .placa("GHI-789")
                .soat(true)
                .modelo("Hilux")
                .marca("Toyota")
                .color("Gris")
                .capacidad(5)
                .descripcionVehiculo("Camioneta 4x4")
                .idConductor(99) // ID inexistente
                .build();

        when(vehiculoRepository.existsByPlaca("GHI-789")).thenReturn(false);
        when(conductorRepository.findById(99)).thenReturn(Optional.empty());

        // CUANDO/ENTONCES
        assertThatThrownBy(() ->
                vehiculoService.registrarVehiculo(99, request)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Conductor no encontrado");

        // Verificaciones: no se guarda vehículo
        verify(vehiculoRepository, never()).save(any());
    }

}