package com.example.unirideapi.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CalificacionResponseDTO(
        Integer idCalificacion,
        Integer puntaje,
        String comentario,
        LocalDateTime updatedAt,
        Integer conductorId,
        Integer pasajeroId
) {
}
