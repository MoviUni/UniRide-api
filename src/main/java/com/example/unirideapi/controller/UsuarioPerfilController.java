package com.example.unirideapi.api;

import com.example.unirideapi.dto.response.UsuarioPerfilResponseDTO;
import com.example.unirideapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario/perfil")
@RequiredArgsConstructor
//@PreAuthorize("hasAnyRole('CONDUCTOR','PASAJERO','ADMIN')") // ajusta roles si hace falta
public class UsuarioPerfilController {

    private final UsuarioService usuarioService;

    /** Actualizar perfil por ID de usuario */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioPerfilResponseDTO> updatePerfil(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioPerfilResponseDTO usuarioPerfilDTO
    ) {
        UsuarioPerfilResponseDTO updated = usuarioService.updateUsuarioPerfil(id, usuarioPerfilDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    /** Obtener perfil por ID de usuario */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPerfilResponseDTO> getPerfilById(@PathVariable Integer id) {
        UsuarioPerfilResponseDTO perfil = usuarioService.getUsuarioPerfilById(id);
        return (perfil == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(perfil);
    }
}

