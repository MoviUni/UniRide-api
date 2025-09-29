package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.CalificacionDTO;
import com.example.unirideapi.model.entity.Calificacion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalificacionMapper {
    private final ModelMapper modelMapper;

    public CalificacionDTO toDto(Calificacion calificacion) {
        return modelMapper.map(calificacion, CalificacionDTO.class);
    }

    public Calificacion toEntity(CalificacionDTO dto) {return modelMapper.map(dto, Calificacion.class);}
}
