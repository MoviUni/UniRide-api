package com.example.unirideapi.dto.request;

public record PasajeroRequestDTO(
        String nombre,
        String apellido,
        String dni,
        Integer edad,
        String descripcionPasajero,
        Long userId
) {
}
