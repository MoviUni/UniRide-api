package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.VehiculoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehiculoMapper {
    private final ModelMapper modelMapper;

    public VehiculoDTO toDTO(VehiculoMapper vehiculo) {
        return modelMapper.map(vehiculo, VehiculoDTO.class);
    }

    public VehiculoMapper toEntity(VehiculoDTO dto) { return modelMapper.map(dto, VehiculoMapper.class);}
}
