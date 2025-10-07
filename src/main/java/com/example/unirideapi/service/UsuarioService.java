package com.example.unirideapi.service;

import com.example.unirideapi.dto.response.PerfilUsuarioResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.model.Usuario;
import com.example.unirideapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public PerfilUsuarioResponseDTO getProfile(Long userId) {
        var user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return new PerfilUsuarioResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getRole().getName().name()
        );
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        var user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessRuleException("La contraseña actual no es válida");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(user);

        // Enviar correo de confirmación
        //emailService.sendPasswordChangedEmail(user.getEmail());
    }

}
