package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record PasajeroResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String dni,
        String descripcion,
        Integer edad
) {}
