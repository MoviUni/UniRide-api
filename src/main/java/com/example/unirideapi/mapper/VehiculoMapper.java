package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.VehiculoRequestDTO;
import com.example.unirideapi.dto.response.VehiculoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehiculoMapper {
    private final ModelMapper modelMapper;

    public VehiculoResponseDTO toDTO(VehiculoMapper vehiculo) {
        return modelMapper.map(vehiculo, VehiculoResponseDTO.class);
    }

    public VehiculoMapper toEntity(VehiculoRequestDTO dto) { return modelMapper.map(dto, VehiculoMapper.class);}
}
