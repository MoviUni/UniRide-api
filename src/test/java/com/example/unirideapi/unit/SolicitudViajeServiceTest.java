package com.example.unirideapi.unit;

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

    @BeforeEach
    void setUp() {
        mockRuta=createMockRuta(10,EstadoRuta.PROGRAMADO);
    }
    private Ruta createMockRuta(Integer idRuta, EstadoRuta estado) {
        Ruta ruta=new Ruta();
        ruta.setIdRuta(idRuta);
        ruta.setEstadoRuta(estado);
        return ruta;
    }
    private SolicitudViaje createMockSolicitud(Integer id, EstadoSolicitud estado, Ruta ruta) {
        SolicitudViaje solicitud= new SolicitudViaje();
        solicitud.setIdSolicitudViaje(id);
        solicitud.setFecha(LocalDate.now());
        solicitud.setHora(LocalTime.now());
        solicitud.setEstadoSolicitud(estado);
        solicitud.setRuta(ruta);
        return solicitud;
    }
    private SolicitudViajeResponseDTO createMockDTO(SolicitudViaje solicitudViaje, Integer idPasajero) {
        return new SolicitudViajeResponseDTO(
          solicitudViaje.getIdSolicitudViaje(),
          solicitudViaje.getFecha(),
          solicitudViaje.getHora(),
          null,
          solicitudViaje.getEstadoSolicitud(),
          solicitudViaje.getRuta().getIdRuta(),
          idPasajero
        );
    }
    // -----US: 13------
    @Test
    @DisplayName("CP01: Listado de solicitudes - debe listar las solicitudes por ruta")
    void findSolicitudesByRutaId_RutaExistente_Success() {
        // Arrange
        Integer idRuta=10;
        SolicitudViaje solicitudPendiente=createMockSolicitud(1,EstadoSolicitud.PENDIENTE,mockRuta);
        SolicitudViaje solicitudAceptada= createMockSolicitud(2, EstadoSolicitud.ACEPTADO, mockRuta);

        List<SolicitudViaje> solicitudes = List.of(solicitudPendiente,solicitudAceptada);

        when(rutaRepository.findById((long)idRuta)).thenReturn(Optional.of(mockRuta));
        when(solicitudViajeRepository.findByRutaId(idRuta)).thenReturn(solicitudes);;

        SolicitudViajeResponseDTO dtoPendiente=createMockDTO(solicitudPendiente, 5);
        SolicitudViajeResponseDTO dtoAceptada=createMockDTO(solicitudAceptada, 6);
        when(solicitudViajeMapper.toDTO(solicitudPendiente)).thenReturn(dtoPendiente);
        when(solicitudViajeMapper.toDTO(solicitudAceptada)).thenReturn(dtoAceptada);
        // Act
        List<SolicitudViajeResponseDTO> resultado = solicitudViajeService.findSolicitudesByRutaId(idRuta);
        // Assert
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).estadoSolicitud()).isEqualTo(EstadoSolicitud.PENDIENTE);
        assertThat(resultado.get(1).estadoSolicitud()).isEqualTo(EstadoSolicitud.ACEPTADO);

        // Verificar
        verify(rutaRepository).findById((long) idRuta);
        verify(solicitudViajeRepository).findByRutaId(idRuta);
        verify(solicitudViajeMapper, times(2)).toDTO(any(SolicitudViaje.class));
    }
    @Test
    @DisplayName("CP02: Ruta no encontrada - debe lanzar excepción cuando la ruta no existe.")
    void findSolicitudesByRutaId_RutaNoEncontrada_ThrowsException() {
        Integer idRutaInexistente = 99;

        when(rutaRepository.findById((long) idRutaInexistente)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> solicitudViajeService.findSolicitudesByRutaId(idRutaInexistente))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ruta no encontrada");

        verify(rutaRepository).findById((long) idRutaInexistente);
        verifyNoInteractions(solicitudViajeRepository);
        verifyNoInteractions(solicitudViajeMapper);
    }
    // ----- US: 14-------
    @Test
    @DisplayName("CP01: Aceptar solicitud - debe aceptar una solicitud pendiente y reducir los asientos disponibles")
    void updateEstadoSolicitud_AceptarSolicitud_Success() {
        mockRuta.setEstadoRuta(EstadoRuta.CONFIRMADO);
        mockRuta.setAsientosDisponibles(3);

        SolicitudViaje solicitud = createMockSolicitud(1, EstadoSolicitud.PENDIENTE, mockRuta);

        when(solicitudViajeRepository.findById(1L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));

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

        // CUANDO
        SolicitudViajeResponseDTO resultado = solicitudViajeService.updateEstadoSolicitud(1, EstadoSolicitud.ACEPTADO);

        // ENTONCES
        assertThat(resultado.estadoSolicitud()).isEqualTo(EstadoSolicitud.ACEPTADO);
        assertThat(mockRuta.getAsientosDisponibles()).isEqualTo(2);
    }
    @Test
    @DisplayName("CP02: Rechazar solicitud - debe rechazar una solicitud pendiente sin modificar los asientos disponibles")
    void updateEstadoSolicitud_RechazarSolicitud_Success() {
        // DADO QUE existe una solicitud pendiente y una ruta programada con asientos disponibles
        mockRuta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        mockRuta.setAsientosDisponibles(4);

        SolicitudViaje solicitud = createMockSolicitud(2, EstadoSolicitud.PENDIENTE, mockRuta);

        when(solicitudViajeRepository.findById(2L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));

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
                            200
                    );
                });

        // CUANDO el conductor rechaza la solicitud
        SolicitudViajeResponseDTO resultado = solicitudViajeService.updateEstadoSolicitud(2, EstadoSolicitud.RECHAZADO);

        // ENTONCES la solicitud cambia a estado RECHAZADO y los asientos no se modifican
        assertThat(resultado).isNotNull();
        assertThat(resultado.estadoSolicitud()).isEqualTo(EstadoSolicitud.RECHAZADO);
        assertThat(mockRuta.getAsientosDisponibles()).isEqualTo(4);

        // Verificaciones
        verify(solicitudViajeRepository).findById(2L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository).save(solicitud);
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP03: Repetir acción - debe lanzar excepción si se intenta modificar una solicitud ya aceptada o rechazada")
    void updateEstadoSolicitud_SolicitudNoPendiente_ThrowsException() {
        // DADO QUE la solicitud ya fue aceptada
        mockRuta.setEstadoRuta(EstadoRuta.CONFIRMADO);
        mockRuta.setAsientosDisponibles(2);
        SolicitudViaje solicitud = createMockSolicitud(3, EstadoSolicitud.ACEPTADO, mockRuta);

        when(solicitudViajeRepository.findById(3L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));

        // CUANDO el conductor intenta volver a aceptar o rechazar la solicitud
        // ENTONCES se lanza una excepción de regla de negocio
        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(3, EstadoSolicitud.RECHAZADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Solo se pueden aceptar o rechazar solicitudes pendientes");

        // Verificaciones
        verify(solicitudViajeRepository).findById(3L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository, never()).save(any());
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP04: Ruta con estado inválido - debe lanzar excepción si la ruta no está programada ni confirmada")
    void updateEstadoSolicitud_RutaInvalida_ThrowsException() {
        // DADO QUE la ruta está cancelada
        mockRuta.setEstadoRuta(EstadoRuta.CANCELADO);
        mockRuta.setAsientosDisponibles(3);

        SolicitudViaje solicitud = createMockSolicitud(4, EstadoSolicitud.PENDIENTE, mockRuta);

        when(solicitudViajeRepository.findById(4L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));

        // CUANDO el conductor intenta aceptar o rechazar la solicitud
        // ENTONCES el sistema lanza una excepción por estado inválido de la ruta
        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(4, EstadoSolicitud.ACEPTADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("No se pueden aceptar o rechazar solicitudes si la ruta no está programada o confirmada");

        // Verificaciones
        verify(solicitudViajeRepository).findById(4L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository, never()).save(any());
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP05: Sin asientos disponibles - debe lanzar excepción si no hay asientos disponibles para aceptar la solicitud")
    void updateEstadoSolicitud_SinAsientosDisponibles_ThrowsException() {
        // DADO QUE la ruta está programada pero no tiene asientos disponibles
        mockRuta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        mockRuta.setAsientosDisponibles(0);

        SolicitudViaje solicitud = createMockSolicitud(5, EstadoSolicitud.PENDIENTE, mockRuta);

        when(solicitudViajeRepository.findById(5L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.of(mockRuta));

        // CUANDO el conductor intenta aceptar la solicitud
        // ENTONCES el sistema lanza una excepción indicando que no hay asientos
        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(5, EstadoSolicitud.ACEPTADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("No hay asientos disponibles para aceptar la solicitud");

        // Verificaciones
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

        // CUANDO el conductor intenta aceptar o rechazar la solicitud
        // ENTONCES el sistema lanza una excepción de recurso no encontrado
        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(6, EstadoSolicitud.ACEPTADO)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Solicitud no encontrada");

        // Verificaciones
        verify(solicitudViajeRepository).findById(6L);
        verify(rutaRepository, never()).findById(any());
        verify(solicitudViajeRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP07: Ruta no encontrada - debe lanzar excepción si la ruta asociada a la solicitud no existe")
    void updateEstadoSolicitud_RutaNoEncontrada_ThrowsException() {
        // DADO QUE existe una solicitud pendiente, pero la ruta no se encuentra
        SolicitudViaje solicitud = createMockSolicitud(7, EstadoSolicitud.PENDIENTE, mockRuta);

        when(solicitudViajeRepository.findById(7L)).thenReturn(Optional.of(solicitud));
        when(rutaRepository.findById(10L)).thenReturn(Optional.empty());

        // CUANDO / ENTONCES
        assertThatThrownBy(() ->
                solicitudViajeService.updateEstadoSolicitud(7, EstadoSolicitud.ACEPTADO)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ruta no encontrada");

        // Verificaciones
        verify(solicitudViajeRepository).findById(7L);
        verify(rutaRepository).findById(10L);
        verify(solicitudViajeRepository, never()).save(any());
    }


}

