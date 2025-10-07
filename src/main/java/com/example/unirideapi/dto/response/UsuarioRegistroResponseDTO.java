package com.example.unirideapi.dto.response;

import lombok.Builder;

@Builder
public record UsuarioRegistroResponseDTO(

        // Datos del usuario creado
        Long usuarioId,
        String email,
        String Rol,

        //Datos del perfil creado
        Long perfilId,
        String tipoPerfil,
        String nombre,
        String apellido
) {}
