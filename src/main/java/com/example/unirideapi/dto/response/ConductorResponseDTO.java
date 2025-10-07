package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record ConductorResponseDTO(
        Integer idConductor,
        String nombre,
        String apellido,
        Integer edad,
        String descripcionConductor,
        String disponibilidad
) {}
