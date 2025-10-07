package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record UsuarioRegistroRequestDTO(

        //Datos comunes
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        @NotNull(message = "El rol es obligatorio")
        Rol roleType,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        //Datos pasajero
        String descripcionPasajero,

        //Datos conductor
        String descripcionConductor,
        String disponibilidad
) {}
