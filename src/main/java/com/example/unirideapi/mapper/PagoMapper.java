package com.example.unirideapi.mapper;

import com.example.unirideapi.model.Pago;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagoMapper {
    private final ModelMapper modelMapper;

    public PagoDTO toEntity(Pago pago) {
        return modelMapper.map(pago, PagoDTO.class);
    }

    public Pago toEntity(PagoDTO dto) {return modelMapper.map(dto, Pago.class);}
}
