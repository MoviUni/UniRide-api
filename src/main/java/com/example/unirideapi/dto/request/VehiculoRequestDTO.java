package com.example.unirideapi.dto.request;
import lombok.Builder;

@Builder
public record VehiculoRequestDTO(
        String placa,
        Boolean soat,
        String modelo,
        String marca,
        String color,
        Integer capacidad,
        String descripcionVehiculo
) { }

