package com.example.unirideapi.dto.request;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Pasajero;
import lombok.Builder;

@Builder
public record CalificacionRequestDTO(
        Integer puntaje,
        String comentario,
        Integer conductor,
        Integer pasajero
) {
}
