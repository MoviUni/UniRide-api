// src/main/java/com/example/unirideapi/dto/request/RegistroConductorRequestDTO.java

package com.example.unirideapi.dto.request;

public record RegistroConductorRequestDTO(
        String email,
        String password,
        ConductorRequestDTO conductor,
        VehiculoRequestDTO vehiculo
) {}
