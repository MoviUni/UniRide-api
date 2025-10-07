package com.example.unirideapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ConductorRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 255, message = "El nombre no debe exceder los 255 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 255, message = "El nombre no debe exceder los 255 caracteres")
        String apellido,

        @NotNull(message = "El DNI es obligatorio")
        @NotBlank(message = "El DNI es obligatorio")
        @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 caracteres")
        String dni,

        Integer edad,
        String descripciononductor,
        String disponibilidad,

        @NotNull(message = "El id de usuario es obligatorio")
        Long userId,

        Integer vehiculoId
) { }
