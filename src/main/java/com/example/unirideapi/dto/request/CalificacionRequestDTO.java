package com.example.unirideapi.dto.request;
import lombok.Builder;

@Builder
public record CalificacionRequestDTO(
        Integer puntaje,
        String comentario,
        Integer conductorId,
        Integer pasajeroId
) {
}
