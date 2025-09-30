package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.model.Conductor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConductorMapper {
    private final ModelMapper modelMapper;

    public ConductorResponseDTO toDTO(Conductor conductor) { return modelMapper.map(conductor, ConductorResponseDTO.class);
    }
    public Conductor toEntity(ConductorRequestDTO dto) { return modelMapper.map(dto, Conductor.class);}
}
