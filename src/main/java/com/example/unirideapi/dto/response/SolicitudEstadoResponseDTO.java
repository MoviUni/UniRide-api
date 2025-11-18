package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record SolicitudEstadoResponseDTO(
        Integer idSolicitudViaje,
        Integer pasajeroId,
        Integer rutaId,
        EstadoSolicitud estadoSolicitud
) {}