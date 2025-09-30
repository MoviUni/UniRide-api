package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.PagoRequestDTO;
import com.example.unirideapi.dto.response.PagoResponseDTO;
import com.example.unirideapi.model.Pago;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagoMapper {
    private final ModelMapper modelMapper;

    public PagoResponseDTO toEntity(Pago pago) { return modelMapper.map(pago, PagoResponseDTO.class);}

    public Pago toEntity(PagoRequestDTO dto) {return modelMapper.map(dto, Pago.class);}
}
