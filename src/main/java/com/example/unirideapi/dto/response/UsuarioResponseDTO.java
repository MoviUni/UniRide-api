package com.example.unirideapi.dto.response;
import com.example.unirideapi.model.enums.ERol;

public record UsuarioResponseDTO(
        Integer idUsuario,
        String email,
        Integer idRol
) {
}
