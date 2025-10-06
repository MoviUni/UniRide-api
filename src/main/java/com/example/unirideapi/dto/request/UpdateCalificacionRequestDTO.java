package com.example.unirideapi.dto.request;

import lombok.Builder;

@Builder
public record UpdateCalificacionRequestDTO(
        Integer puntaje,
        String comentario
) {
}
