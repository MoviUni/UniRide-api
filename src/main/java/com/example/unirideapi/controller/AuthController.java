package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.request.LoginRequestDTO;
import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.request.UsuarioRegistroRequestDTO;
import com.example.unirideapi.dto.response.AuthResponseDTO;
import com.example.unirideapi.dto.response.UsuarioPerfilResponseDTO;
import com.example.unirideapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de autenticación y registro.
 * Nota: Tu app ya expone el contexto "/api/v1" (por configuración),
 * así que este controller quedará en: /api/v1/auth/...
 */
@Tag(
        name = "Auth",
        description = "Endpoints para registro de usuarios (pasajero y conductor) y login. "
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    /** Registro de PASAJERO */
    @PostMapping("/registro/pasajero")
    public ResponseEntity<UsuarioPerfilResponseDTO> registroPasajero(
            @Valid @RequestBody PasajeroRequestDTO dto) {

        UsuarioPerfilResponseDTO perfil = usuarioService.registroPasajero(dto);
        return new ResponseEntity<>(perfil, HttpStatus.CREATED);
    }

    /** Registro de CONDUCTOR */
    @PostMapping("/registro/conductor")
    public ResponseEntity<UsuarioPerfilResponseDTO> registroConductor(
            @Valid @RequestBody ConductorRequestDTO dto) {

        UsuarioPerfilResponseDTO perfil = usuarioService.registroConductor(dto);
        return new ResponseEntity<>(perfil, HttpStatus.CREATED);
    }

    /** Login con email/password */
    @GetMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO auth = usuarioService.login(dto);
        return ResponseEntity.ok(auth);
    }

}
