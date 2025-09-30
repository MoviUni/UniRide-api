package com.example.unirideapi.mapper;

import com.example.unirideapi.model.Pasajero;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasajeroMapper {
    private final ModelMapper modelMapper;

    public PasajeroDTO toDTO(Pasajero pasajero) {
        return modelMapper.map(pasajero, PasajeroDTO.class);
    }

    public Pasajero toEntity(PasajeroDTO dto) { return modelMapper.map(dto, Pasajero.class);}
}
