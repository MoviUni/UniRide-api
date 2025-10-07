package com.example.unirideapi.dto.request;

import lombok.Builder;

@Builder
public record PasajeroRequestDTO(
        String nombre,
        String apellido,
        String dni,
        Integer edad,
        String descripcionPasajero,
        Long userId
) {}
