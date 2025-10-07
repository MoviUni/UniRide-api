package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.LoginRequestDTO;
import com.example.unirideapi.dto.request.UsuarioRegistroRequestDTO;
import com.example.unirideapi.dto.response.LoginResponseDTO;
import com.example.unirideapi.dto.response.UsuarioRegistroResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Pasajero;
import com.example.unirideapi.model.Usuario;
import com.example.unirideapi.model.enums.ERol;
import com.example.unirideapi.repository.ConductorRepository;
import com.example.unirideapi.repository.PasajeroRepository;
import com.example.unirideapi.repository.RolRepository;
import com.example.unirideapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ConductorRepository conductorRepository;
    private final PasajeroRepository pasajeroRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioRegistroResponseDTO register(UsuarioRegistroRequestDTO dto) {
        // Regla: no permitir emails duplicados
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessRuleException("El email ya está registrado");
        }

        // Buscar rol por enum
        var role = rolRepository.findByName(dto.roleType().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        // Crear usuario base
        var user = Usuario.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(role)
                .build();

        var savedUser = usuarioRepository.save(user);

        Long profileId = null;
        String profileType = null;

        // Crear perfil según el rol
        if (role.getName() == ERol.CONDUCTOR) {
            var conductor = Conductor.builder()
                    .nombre(dto.nombre())
                    .apellido(dto.apellido())
                    .edad(dto.edad())
                    .descripcionConductor(dto.descripcionConductor())
                    .disponibilidad(dto.disponibilidad())
                    .usuario(user)
                    .build();

            var savedConductor = conductorRepository.save(conductor);
            profileId = savedConductor.getIdConductor();
            profileType = ERol.CONDUCTOR.name();

        } else if (role.getName() == ERol.PASAJERO) {
            var pasajero = Pasajero.builder()
                    .nombre(dto.nombre())
                    .apellido(dto.apellido().trim())
                    .dni(dto.dni())
                    .edad(dto.edad())
                    .descripcionPasajero(dto.descripcionPasajero())
                    .usuario(user)
                    .build();
            var savedPasajero = pasajeroRepository.save(pasajero);
            profileId = savedPasajero.getIdPasajero();
            profileType = ERol.PASAJERO.name();

        } else if (role.getName() == ERol.ADMIN) {
            // Un admin no requiere perfil adicional
            profileType = ERol.ADMIN.name();
        }

        // Respuesta
        return UsuarioRegistroResponseDTO.builder()
                .usuarioId(savedUser.getId())
                .email(savedUser.getEmail())
                .Rol(role.getName().name()) // ✅ correcto
                .perfilId(profileId)
                .tipoPerfil(profileType)
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .build();

    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        var user = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Validar credenciales
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BusinessRuleException("Credenciales inválidas");
        }

        // TODO: aquí generar JWT real; por ahora token simulado
        String fakeToken = "demo-token";

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .token(fakeToken)
                .build();

    }
}
