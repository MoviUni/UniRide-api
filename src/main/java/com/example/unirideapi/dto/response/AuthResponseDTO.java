package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record AuthResponseDTO(
        String token,    // JWT
        String nombre,
        String apellido,
        String rol,
        Integer idRol
) {}