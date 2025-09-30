package com.example.unirideapi.dto.request;
import lombok.Builder;

@Builder
public record ConductorRequestDTO(
        String nombre,
        String apellido,
        String dni,
        Integer edad,
        String descripciononductor,
        String disponibilidad,
        Integer userId,
        Integer vehiculoId
) { }
