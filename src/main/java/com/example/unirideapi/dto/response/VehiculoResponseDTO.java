package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record VehiculoResponseDTO(
        Integer idVehiculo,
        String placa,
        String modelo,
        String marca,
        Integer capacidad,
        String descripcionVehiculo
) {
}
