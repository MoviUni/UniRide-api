package com.example.unirideapi.controller;

import com.example.unirideapi.dto.response.PerfilUsuarioResponseDTO;
import com.example.unirideapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService userService;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<PerfilUsuarioResponseDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long userId,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        userService.changePassword(userId, currentPassword, newPassword);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}
