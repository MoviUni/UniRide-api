package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.ERol;
import lombok.Builder;

@Builder
public record UsuarioPerfilResponseDTO(
        Integer idUsuario,
        String nombre,
        String apellido,
        String email,
        ERol rol
) {}
