package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.Estado;

import java.time.LocalDate;
import java.time.LocalTime;

public record RutaResponseDTO(
        Integer idRuta,
        String origen,
        String destino,
        LocalDate fechaSalida,
        LocalTime horaSalida,
        Long tarifa,
        Integer asientosDisponibles,
        Estado estado,
        Integer conductorId
) {
}
