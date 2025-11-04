package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.request.LoginRequestDTO;
import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.AuthResponseDTO;
import com.example.unirideapi.dto.response.UsuarioPerfilResponseDTO;
import com.example.unirideapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Registrar pasajero",
            description = "Crea un nuevo usuario de tipo pasajero en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pasajero registrado correctamente",
                    content = @Content(schema = @Schema(implementation = UsuarioPerfilResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente")
    })
    @PostMapping("/registro/pasajero")
    public ResponseEntity<UsuarioPerfilResponseDTO> registroPasajero(
            @Valid @RequestBody PasajeroRequestDTO dto) {

        UsuarioPerfilResponseDTO perfil = usuarioService.registroPasajero(dto);
        return new ResponseEntity<>(perfil, HttpStatus.CREATED);
    }

    /** Registro de CONDUCTOR */
    @Operation(
            summary = "Registrar conductor",
            description = "Crea un nuevo usuario de tipo conductor en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conductor registrado correctamente",
                    content = @Content(schema = @Schema(implementation = UsuarioPerfilResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente")
    })
    @PostMapping("/registro/conductor")
    public ResponseEntity<UsuarioPerfilResponseDTO> registroConductor(
            @Valid @RequestBody ConductorRequestDTO dto) {

        UsuarioPerfilResponseDTO perfil = usuarioService.registroConductor(dto);
        return new ResponseEntity<>(perfil, HttpStatus.CREATED);
    }

    /** Login con email/password */
    @Operation(
            summary = "Login de usuario",
            description = "Permite a un usuario iniciar sesión con su email y contraseña."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de login inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o token inválido"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO auth = usuarioService.login(dto);
        return ResponseEntity.ok(auth);
    }

}
