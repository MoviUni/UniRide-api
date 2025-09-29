package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.PagoDTO;
import com.example.unirideapi.dto.RutaDTO;
import com.example.unirideapi.model.entity.Ruta;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RutaMapper {
    private final ModelMapper modelMapper;

    public RutaDTO toDTO(Ruta ruta) {
        return modelMapper.map(ruta, RutaDTO.class);
    }

    public Ruta toEntity(RutaDTO dto) {return modelMapper.map(dto, Ruta.class);}
}
