package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.mapper.SolicitudViajeMapper;
import com.example.unirideapi.model.Pasajero;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.SolicitudViaje;
import com.example.unirideapi.repository.PasajeroRepository;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.repository.SolicitudViajeRepository;
import com.example.unirideapi.service.SolicitudViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SolicitudViajeServiceImpl implements SolicitudViajeService {
    private final SolicitudViajeRepository solicitudViajeRepository;
    private final SolicitudViajeMapper solicitudViajeMapper;
    private final RutaRepository rutaRepository;
    private final PasajeroRepository pasajeroRepository;

    @Override
    public SolicitudViajeResponseDTO create(SolicitudViajeRequestDTO solicitudViajeRequestDTO){

        Pasajero pasajero = pasajeroRepository.findById((long)solicitudViajeRequestDTO.pasajeroId())
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado")).getPasajero();
        Ruta ruta = rutaRepository.findById((long)solicitudViajeRequestDTO.rutaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado"));

        var solicitudViaje = SolicitudViaje.builder()
                .estadoSolicitud(solicitudViajeRequestDTO.estadoSolicitud())
                .fecha(solicitudViajeRequestDTO.fecha())
                .hora(solicitudViajeRequestDTO.hora())
                .pasajero(pasajero)
                .ruta(ruta)
                .updatedAt(solicitudViajeRequestDTO.updatedAt().atStartOfDay())
                .build();

        return toResponse(solicitudViajeRepository.save(solicitudViaje));
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
