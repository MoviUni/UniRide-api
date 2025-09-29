package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.ConductorDTO;
import com.example.unirideapi.model.entity.Conductor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConductorMapper {
    private final ModelMapper modelMapper;

    public ConductorDTO toDTO(Conductor conductor) {
        return modelMapper.map(conductor, ConductorDTO.class);
    }
    public Conductor toEntity(ConductorDTO dto) { return modelMapper.map(dto, Conductor.class);}
}
