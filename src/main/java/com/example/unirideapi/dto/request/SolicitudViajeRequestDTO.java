package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.enums.EstadoSolicitud;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record SolicitudViajeRequestDTO(
        EstadoSolicitud estadoSolicitud,
        LocalDate fecha,
        LocalTime hora,
        Integer rutaId,
        Integer pasajeroId,
        LocalDate updatedAt
) {
}
