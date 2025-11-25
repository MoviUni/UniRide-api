package com.example.unirideapi.dto.request;
import lombok.Builder;
import lombok.Data;

@Builder
public record ConductorRequestDTO(
        String nombre,
        String apellido,
        String dni,
        Integer edad,
        String codigoUni,
        String carrera
) {}
