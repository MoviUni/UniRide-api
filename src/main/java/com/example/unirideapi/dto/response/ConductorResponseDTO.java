package com.example.unirideapi.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
public record ConductorResponseDTO(
        Integer idConductor,
        String nombre,
        String apellido,
        Integer edad,
        String descripcionConductor
) {
}
