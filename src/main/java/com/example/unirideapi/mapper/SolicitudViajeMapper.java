package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.SolicitudViaje;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudViajeMapper {
    private final ModelMapper modelMapper;

    public SolicitudViajeResponseDTO toDTO(SolicitudViaje solicitudViaje) {
        return new SolicitudViajeResponseDTO(
                solicitudViaje.getIdSolicitudViaje(),
                solicitudViaje.getFecha(),
                solicitudViaje.getHora(),
                solicitudViaje.getUpdatedAt(),
                solicitudViaje.getEstadoSolicitud(),
                solicitudViaje.getRuta().getIdRuta(),
                solicitudViaje.getPasajero().getIdPasajero()
        );
    }

    public SolicitudViaje toEntity(SolicitudViajeRequestDTO dto) {
        return modelMapper.map(dto, SolicitudViaje.class);
    }
}
