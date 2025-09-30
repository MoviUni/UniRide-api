package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.Estado;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record SolicitudViajeResponseDTO(
        Integer idSolicitudViaje,
        LocalDate fecha,
        LocalTime hora,
        LocalDateTime updatedAt,
        Estado estado,
        Integer rutaId,
        Integer pasajeroId
) {

}
