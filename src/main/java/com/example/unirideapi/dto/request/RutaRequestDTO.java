package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.enums.Estado;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record RutaRequestDTO(
        String origen,
        String destino,
        LocalDate fechaSalida,
        LocalTime horaSalida,
        Float tarifa,
        Integer asientosDisponibles,
        Estado estado,
        Integer conductorId
) {
}
