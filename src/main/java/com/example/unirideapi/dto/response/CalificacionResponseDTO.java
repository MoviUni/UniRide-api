package com.example.unirideapi.dto.response;

import java.time.LocalDateTime;

public record CalificacionResponseDTO(
        Integer idCalificacion,
        Integer puntaje,
        String comentario,
        LocalDateTime updatedAt,
        Integer conductorId,
        Integer pasajeroId
) {
}
