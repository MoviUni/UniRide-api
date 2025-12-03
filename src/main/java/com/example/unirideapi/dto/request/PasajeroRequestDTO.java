package com.example.unirideapi.dto.request;

import lombok.Builder;

@Builder
public record PasajeroRequestDTO(
        String nombre,
        String apellido,
        String email,  //NEW
        String password, //NEW
        String dni,
        Integer edad,
        String codigoUni,
        String carrera
) {}
