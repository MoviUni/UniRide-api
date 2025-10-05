package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.enums.EstadoSolicitud;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record SolicitudViajeRequestDTO(
        LocalDate fecha,
        LocalTime hora,
        EstadoSolicitud estadoSolicitud,
        Integer rutaId,
        Integer pasajeroId
) {
}
