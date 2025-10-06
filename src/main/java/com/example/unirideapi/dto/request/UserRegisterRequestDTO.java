package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.Rol;
import lombok.Builder;

@Builder
public record UserRegisterRequestDTO(
        //Datos son comunes
        String email,
        String password,

        Rol roleType,
        String nombre,
        String apellido,

        //Datos pasajero
        String descripcionPasajero,

        //Datos conductor
        String descripcionConductor
) {}
