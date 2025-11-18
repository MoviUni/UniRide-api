package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record VehiculoResponseDTO(
        String placa,
        Boolean soat,
        String modelo,
        String marca,
        String color,
        Integer capacidad,
        String descripcionVehiculo,
        Integer idVehiculo,
        Integer idConductor
) {}
