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

    public SolicitudViajeResponseDTO toDTO(SolicitudViaje solicitudViaje) {
        return modelMapper.map(solicitudViaje, SolicitudViajeResponseDTO.class);
    }

    public SolicitudViaje toEntity(SolicitudViajeRequestDTO dto) {return modelMapper.map(dto, SolicitudViaje.class);}
}
