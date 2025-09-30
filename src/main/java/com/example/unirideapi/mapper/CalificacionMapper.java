package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.CalificacionRequestDTO;
import com.example.unirideapi.dto.response.CalificacionResponseDTO;
import com.example.unirideapi.model.Calificacion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalificacionMapper {
    private final ModelMapper modelMapper;

    public CalificacionResponseDTO toDto(Calificacion calificacion) { return modelMapper.map(calificacion, CalificacionResponseDTO.class);}

    public Calificacion toEntity(CalificacionRequestDTO dto) {return modelMapper.map(dto, Calificacion.class);}
}
