package com.example.unirideapi.dto.request;
import lombok.Builder;

@Builder
public record UsuarioRequestDTO(
        String email,
        String password,
        Integer rolId
) {
}
