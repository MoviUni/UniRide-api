package com.example.unirideapi.unit;

import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.service.impl.RutaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de Viaje Service")

public class RutaServiceTest {
    @Mock
    private RutaRepository rutaRepository;
    @Mock
    private RutaMapper rutaMapper;
    @InjectMocks
    private RutaServiceImpl rutaService;

    // ------US: 15 -------------
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
