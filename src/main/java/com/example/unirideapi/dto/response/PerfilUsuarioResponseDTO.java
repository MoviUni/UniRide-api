package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record PerfilUsuarioResponseDTO(
        Long idUsuario,
        String email,
        String rol
) {}
