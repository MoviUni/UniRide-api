package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.Estado;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record RutaResponseDTO(
        Integer idRuta,
        String origen,
        String destino,
        LocalDate fechaSalida,
        LocalTime horaSalida,
        Long tarifa,
        Integer asientosDisponibles,
        Estado estado
) {
}
