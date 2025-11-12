package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudEstadoResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.SolicitudViajeMapper;
import com.example.unirideapi.model.Pasajero;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.SolicitudViaje;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import com.example.unirideapi.repository.PasajeroRepository;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.repository.SolicitudViajeRepository;
import com.example.unirideapi.service.SolicitudViajeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudViajeServiceImpl implements SolicitudViajeService {
    private final SolicitudViajeRepository solicitudViajeRepository;
    private final SolicitudViajeMapper solicitudViajeMapper;
    private final RutaRepository rutaRepository;
    private final PasajeroRepository pasajeroRepository;

    @Override
    public SolicitudViajeResponseDTO create(SolicitudViajeRequestDTO solicitudViajeRequestDTO){
        // Regla de negocio 10

        List<SolicitudViaje> lista = solicitudViajeRepository.searchByUsuario(solicitudViajeRequestDTO.pasajeroId());

        for (SolicitudViaje sol : lista){
            Ruta rut = sol.getRuta();
            if (rut == null) {
                continue;
            }
            if (Objects.equals(rut.getIdRuta(), solicitudViajeRequestDTO.rutaId())){
                // Se encontró duplicado, entonces no se agrega
                throw new BusinessRuleException("Un usuario no puede enviar más de una solicitud a una misma ruta");
            }
        }

        Pasajero pasajero = pasajeroRepository.findById(solicitudViajeRequestDTO.pasajeroId())
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado"));
        Ruta ruta1 = rutaRepository.findById(Long.valueOf(solicitudViajeRequestDTO.rutaId()))
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        SolicitudViaje solicitudViaje = SolicitudViaje.builder()
                .estadoSolicitud(solicitudViajeRequestDTO.estadoSolicitud())
                .fecha(solicitudViajeRequestDTO.fecha())
                .hora(solicitudViajeRequestDTO.hora())
                .pasajero(pasajero)
                .updatedAt(solicitudViajeRequestDTO.updatedAt().atStartOfDay())
                .ruta(ruta1)
                .build();

        return toResponse(solicitudViajeRepository.save(solicitudViaje));
    }


    @Override
    public List<SolicitudEstadoResponseDTO> searchByUsuario(Integer idUsuario) {
        return solicitudViajeRepository.searchByUsuario(idUsuario).stream()
                .map(this::toEstadoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudViajeResponseDTO> findSolicitudesByRutaId(Integer idRuta) {
        var ruta = rutaRepository.findById(Long.valueOf(idRuta))
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        return solicitudViajeRepository.findByRutaId(ruta.getIdRuta())
                .stream()
                .map(solicitudViajeMapper::toDTO)
                .toList();
    }
    @Override
    @Transactional
    public SolicitudViajeResponseDTO updateEstadoSolicitud(Integer idSolicitud, EstadoSolicitud nuevoEstado) {
        var solicitud = solicitudViajeRepository.findById((long)idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        var ruta = rutaRepository.findById(Long.valueOf(solicitud.getRuta().getIdRuta()))
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        // Regla 1:Solo solicitudes en estado PENDIENTE pueden cambiar
        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new BusinessRuleException("Solo se pueden aceptar o rechazar solicitudes pendientes");
        }

        //Regla 2:La ruta debe estar PROGRAMADA o CONFIRMADA
        if (ruta.getEstadoRuta() != EstadoRuta.PROGRAMADO && ruta.getEstadoRuta() != EstadoRuta.CONFIRMADO) {
            throw new BusinessRuleException("No se pueden aceptar o rechazar solicitudes si la ruta no está programada o confirmada");
        }

        // Regla 3:Verificar disponibilidad de asientos si se va a aceptar
        if (nuevoEstado == EstadoSolicitud.ACEPTADO) {
            if (ruta.getAsientosDisponibles() <= 0) {
                throw new BusinessRuleException("No hay asientos disponibles para aceptar la solicitud");
            }
            // Restar un asiento disponible
            ruta.setAsientosDisponibles(ruta.getAsientosDisponibles() - 1);
            rutaRepository.save(ruta);
        }

        //Aplicar el nuevo estado
        solicitud.setEstadoSolicitud(nuevoEstado);
        solicitudViajeRepository.save(solicitud);

        return solicitudViajeMapper.toDTO(solicitud);
    }
    @Override
    @Transactional
    public SolicitudViajeResponseDTO cancelSolicitud(Integer idSolicitud) {
        EstadoSolicitud estadoCancelado = EstadoSolicitud.CANCELADO;
        var solicitud = solicitudViajeRepository.findById((long)idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        var ruta = rutaRepository.findById(Long.valueOf(solicitud.getRuta().getIdRuta()))
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        // Regla 1:Solo solicitudes en estado PENDIENTE pueden cambiar
        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new BusinessRuleException("Solo se pueden cancelar solicitudes pendientes");
        }

        //Regla 2:La ruta debe estar PROGRAMADA o CONFIRMADA
        if (ruta.getEstadoRuta() != EstadoRuta.PROGRAMADO && ruta.getEstadoRuta() != EstadoRuta.CONFIRMADO) {
            throw new BusinessRuleException("No se pueden cancelar solicitudes si la ruta no está programada o confirmada");
        }


        //Aplicar el nuevo estado
        solicitud.setEstadoSolicitud(estadoCancelado);
        solicitudViajeRepository.save(solicitud);

        return solicitudViajeMapper.toDTO(solicitud);
    }

    @Override
    @Transactional
    public SolicitudViajeResponseDTO cancelSolicitud(Integer idSolicitud) {
        EstadoSolicitud estadoCancelado = EstadoSolicitud.CANCELADO;
        var solicitud = solicitudViajeRepository.findById((long)idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        var ruta = rutaRepository.findById(Long.valueOf(solicitud.getRuta().getIdRuta()))
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        // Regla 1:Solo solicitudes en estado PENDIENTE pueden cambiar
        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new BusinessRuleException("Solo se pueden cancelar solicitudes pendientes");
        }

        //Regla 2:La ruta debe estar PROGRAMADA o CONFIRMADA
        if (ruta.getEstadoRuta() != EstadoRuta.PROGRAMADO && ruta.getEstadoRuta() != EstadoRuta.CONFIRMADO) {
            throw new BusinessRuleException("No se pueden cancelar solicitudes si la ruta no está programada o confirmada");
        }


        //Aplicar el nuevo estado
        solicitud.setEstadoSolicitud(estadoCancelado);
        solicitudViajeRepository.save(solicitud);

        return solicitudViajeMapper.toDTO(solicitud);
    }

    private SolicitudEstadoResponseDTO toEstadoResponse(SolicitudViaje solicitudViaje) {
        return SolicitudEstadoResponseDTO.builder()
                .idSolicitudViaje(solicitudViaje.getIdSolicitudViaje())
                .estadoSolicitud(solicitudViaje.getEstadoSolicitud())
                .pasajeroId(solicitudViaje.getPasajero().getIdPasajero())
                .rutaId(solicitudViaje.getRuta().getIdRuta())
                .build();
    }

    private SolicitudViajeResponseDTO toResponse(SolicitudViaje solicitudViaje) {
        return SolicitudViajeResponseDTO.builder()
                .idSolicitudViaje(solicitudViaje.getIdSolicitudViaje())
                .estadoSolicitud(solicitudViaje.getEstadoSolicitud())
                .fecha(solicitudViaje.getFecha())
                .hora(solicitudViaje.getHora())
                .pasajeroId(solicitudViaje.getPasajero().getIdPasajero())
                .rutaId(solicitudViaje.getRuta().getIdRuta())
                .updatedAt(solicitudViaje.getUpdatedAt())
                .build();
    }
}
