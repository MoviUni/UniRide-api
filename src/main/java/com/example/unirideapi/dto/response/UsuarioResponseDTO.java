package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record UsuarioResponseDTO(
        Integer idUsuario,
        String email,
        String rol
) {}
