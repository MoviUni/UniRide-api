package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.model.Ruta;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RutaMapper {
    private final ModelMapper modelMapper;

    public RutaResponseDTO toDTO(Ruta entity) {
        return new RutaResponseDTO(
                entity.getIdRuta(),
                entity.getOrigen(),
                entity.getDestino(),
                entity.getFechaSalida(),
                entity.getHoraSalida(),
                entity.getTarifa(),
                entity.getAsientosDisponibles(),
                entity.getEstadoRuta(),
                entity.getConductor() != null ? entity.getConductor().getIdConductor() : null
        );
    }

    public Ruta toEntity(RutaRequestDTO dto) {return modelMapper.map(dto, Ruta.class);}

    public RutaFrecuenteResponseDTO toRutaFrecuenteDTO(Object[] obj) {
        return RutaFrecuenteResponseDTO.builder()
                .origen((String) obj[0])
                .destino((String) obj[1])
                .horaSalida(((java.sql.Time) obj[2]).toLocalTime())
                .tarifa(((Number) obj[3]).longValue())
                .frecuencia(((Number) obj[4]).longValue())
                .build();
    }
}
