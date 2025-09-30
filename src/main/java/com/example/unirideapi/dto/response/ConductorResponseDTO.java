package com.example.unirideapi.dto.response;

public record ConductorResponseDTO(
        Integer idConductor,
        String nombre,
        String apellido,
        Integer edad,
        String descripcionConductor
) {
}
