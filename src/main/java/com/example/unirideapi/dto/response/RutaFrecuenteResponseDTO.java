package com.example.unirideapi.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record RutaFrecuenteResponseDTO(
        String origen,
        String destino,
        LocalDate fechaSalida,
        LocalTime horaSalida,
        Long tarifa,
        Long frecuencia
) {
}
