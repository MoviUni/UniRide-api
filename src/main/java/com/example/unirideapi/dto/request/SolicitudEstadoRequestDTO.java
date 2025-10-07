package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.enums.EstadoSolicitud;

public record SolicitudEstadoRequestDTO(
        EstadoSolicitud estado
) {}
