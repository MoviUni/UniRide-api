package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.*;
import com.example.unirideapi.dto.response.AuthResponseDTO;
import com.example.unirideapi.dto.response.UsuarioPerfilResponseDTO;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    /** Registro de PASAJERO (crea Usuario + Pasajero asociado). */
    UsuarioPerfilResponseDTO registroPasajero(PasajeroRequestDTO registroRequestDTO);

    /** Registro de CONDUCTOR (crea Usuario + Conductor asociado).
     *  Si luego necesitas vincular vehículo, hazlo en el ServiceImpl (parámetro extra opcional). */
    AuthResponseDTO registrarConductor(RegistroConductorRequestDTO request);

    /** Autenticación por email/password. */
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);

    /** Actualiza datos visibles del perfil (nombre/apellido desde Conductor/Pasajero o Usuario). */
    UsuarioPerfilResponseDTO updateUsuarioPerfil(Integer idUsuario, UsuarioPerfilResponseDTO usuarioPerfilResponseDTO);

    /** Obtiene el perfil por ID de usuario. */
    UsuarioPerfilResponseDTO getUsuarioPerfilById(Integer idUsuario);


}

