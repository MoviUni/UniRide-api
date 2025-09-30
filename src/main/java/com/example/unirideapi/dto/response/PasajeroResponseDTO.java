package com.example.unirideapi.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasajeroResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String dni,
        String descripcion,
        Integer edad
) {
}
