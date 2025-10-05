package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.model.SolicitudViaje;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudViajeMapper {
    private final ModelMapper modelMapper;

    public SolicitudViajeResponseDTO toDTO(SolicitudViaje entity) {
        return new SolicitudViajeResponseDTO(
                entity.getIdSolicitudViaje(),
                entity.getFecha(),
                entity.getHora(),
                entity.getEstado(),
                entity.getPasajero().getNombre(),
                entity.getPasajero().getApellido(),
                entity.getPasajero().getDescripcionPasajero()
        );
    }

    public SolicitudViaje toEntity(SolicitudViajeRequestDTO dto) {return modelMapper.map(dto, SolicitudViaje.class);}
}
