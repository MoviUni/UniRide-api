package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.PasajeroResponseDTO;
import com.example.unirideapi.model.Pasajero;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasajeroMapper {
    private final ModelMapper modelMapper;

    public PasajeroResponseDTO toDTO(Pasajero pasajero) {
        return modelMapper.map(pasajero, PasajeroResponseDTO.class);
    }

    public Pasajero toEntity(PasajeroRequestDTO dto) {
        return modelMapper.map(dto, Pasajero.class);
    }
}
