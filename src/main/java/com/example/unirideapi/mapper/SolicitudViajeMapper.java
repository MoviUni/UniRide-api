package com.example.unirideapi.mapper;

import com.example.unirideapi.model.SolicitudViaje;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudViajeMapper {
    private final ModelMapper modelMapper;

    public SolicitudViajeDTO toDTO(SolicitudViaje solicitudViaje) {
        return modelMapper.map(solicitudViaje, SolicitudViajeDTO.class);
    }

    public SolicitudViaje toEntity(SolicitudViajeDTO dto) {return modelMapper.map(dto, SolicitudViaje.class);}
}
