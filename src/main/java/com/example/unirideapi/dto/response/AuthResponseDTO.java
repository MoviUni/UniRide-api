package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record AuthResponseDTO(
        String token,    // JWT
        String nombre,
        String apellido,
        String rol,
        Integer idUsuario,
        Integer idRol,
        Integer idConductor,
        Integer idPasajero
) {}