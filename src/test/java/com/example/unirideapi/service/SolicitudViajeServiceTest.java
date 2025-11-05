package com.example.unirideapi.service;

import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.SolicitudViajeMapper;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.SolicitudViaje;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.model.enums.EstadoSolicitud;
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
import java.time.LocalTime;
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
    private RutaRepository rutaRepository;

    @InjectMocks
    private SolicitudViajeServiceImpl solicitudViajeService;

    private Ruta mockRuta;

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