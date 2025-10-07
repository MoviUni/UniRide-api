package com.example.unirideapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UsuarioRequestDTO(
        @NotBlank(message = "El email es obligatorio")
        @Size(min = 1, max = 50, message = "El email no puede exceder los 50 caracteres")
        String email,

        String password,
        Integer rolId
) {}
