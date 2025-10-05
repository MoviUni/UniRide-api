package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.SolicitudViajeMapper;
import com.example.unirideapi.model.enums.Estado;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.repository.SolicitudRepository;
import com.example.unirideapi.service.SolicitudViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudViajeImpl implements SolicitudViajeService {
    private final SolicitudRepository solicitudRepository;
    private final RutaRepository rutaRepository;
    private final SolicitudViajeMapper solicitudViajeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudViajeResponseDTO> findSolicitudesByRutaId(Integer idRuta) {
        var ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        return solicitudRepository.findByRutaId(ruta.getIdRuta())
                .stream()
                .map(solicitudViajeMapper::toDTO)
                .toList();
    }
    @Override
    @Transactional
    public SolicitudViajeResponseDTO updateEstadoSolicitud(Integer idSolicitud, Estado nuevoEstado) {
        var solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        var ruta = rutaRepository.findById(solicitud.getRuta().getIdRuta())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        // Regla 1:Solo solicitudes en estado PENDIENTE pueden cambiar
        if (solicitud.getEstado() != Estado.PENDIENTE) {
            throw new BusinessRuleException("Solo se pueden aceptar o rechazar solicitudes pendientes");
        }

        //Regla 2:La ruta debe estar PROGRAMADA o CONFIRMADA
        if (ruta.getEstado() != Estado.PROGRAMADO && ruta.getEstado() != Estado.CONFIRMADO) {
            throw new BusinessRuleException("No se pueden aceptar o rechazar solicitudes si la ruta no está programada o confirmada");
        }

        // Regla 3:Verificar disponibilidad de asientos si se va a aceptar
        if (nuevoEstado == Estado.ACEPTADO) {
            if (ruta.getAsientosDisponibles() <= 0) {
                throw new BusinessRuleException("No hay asientos disponibles para aceptar la solicitud");
            }
            // Restar un asiento disponible
            ruta.setAsientosDisponibles(ruta.getAsientosDisponibles() - 1);
            rutaRepository.save(ruta);
        }

        //Aplicar el nuevo estado
        solicitud.setEstado(nuevoEstado);
        solicitudRepository.save(solicitud);

        return solicitudViajeMapper.toDTO(solicitud);
    }
}
