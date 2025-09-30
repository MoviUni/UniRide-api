package com.example.unirideapi.dto.response;

public record VehiculoResponseDTO(
        Integer idVehiculo,
        String placa,
        String modelo,
        String marca,
        Integer capacidad,
        String descripcionVehiculo
) {
}
