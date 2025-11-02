package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.request.LoginRequestDTO;
import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.request.UsuarioRegistroRequestDTO;
import com.example.unirideapi.dto.response.AuthResponseDTO;
import com.example.unirideapi.dto.response.UsuarioPerfilResponseDTO;
import com.example.unirideapi.exception.RoleNotFoundException;
import com.example.unirideapi.mapper.UsuarioMapper;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Pasajero;
import com.example.unirideapi.model.Rol;
import com.example.unirideapi.model.Usuario;
import com.example.unirideapi.model.enums.ERol;
import com.example.unirideapi.repository.ConductorRepository;
import com.example.unirideapi.repository.PasajeroRepository;
import com.example.unirideapi.repository.RolRepository;
import com.example.unirideapi.repository.UsuarioRepository;
import com.example.unirideapi.security.TokenProvider;
import com.example.unirideapi.security.UserPrincipal;
import com.example.unirideapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ConductorRepository conductorRepository;
    private final PasajeroRepository pasajeroRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UsuarioMapper usuarioMapper;

    // --------------------------------------------------
    // Registro de PASAJERO
    // --------------------------------------------------
//    @Override
//    public UsuarioPerfilResponseDTO registroPasajero(UsuarioRegistroRequestDTO registroRequestDTO) {
//        return registerUsuarioWithRol(registroRequestDTO, ERol.PASAJERO);
//    }
    @Override
    public UsuarioPerfilResponseDTO registroPasajero(PasajeroRequestDTO dto) {
        Rol rol = rolRepository.findByName(ERol.PASAJERO)
                .orElseThrow(() -> new RoleNotFoundException("Rol no encontrado"));

        // 🔍 Validar duplicados
        if (pasajeroRepository.existsByDni(dto.dni())) {
            throw new IllegalArgumentException("Ya existe un pasajero con el DNI: " + dto.dni());
        }

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.email());
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setRol(rol);

        Pasajero pasajero = new Pasajero();
        pasajero.setNombre(dto.nombre());
        pasajero.setApellido(dto.apellido());
        pasajero.setDni(dto.dni());
        pasajero.setEdad(dto.edad());
        pasajero.setDescripcionPasajero(dto.descripcionPasajero());
        pasajero.setUsuario(usuario);

        LocalDateTime now = LocalDateTime.now();
        pasajero.setCreatedAt(now);
        pasajero.setUpdatedAt(now);

        // Relación bidireccional
        usuario.setPasajero(pasajero);

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toUsuarioPerfilResponseDTO(saved);
    }



    // --------------------------------------------------
    // Registro de CONDUCTOR
    // --------------------------------------------------
//    @Override
//    public UsuarioPerfilResponseDTO registroConductor(UsuarioRegistroRequestDTO registroRequestDTO) {
//        return registerUsuarioWithRol(registroRequestDTO, ERol.CONDUCTOR);
//    }


    @Override
    public UsuarioPerfilResponseDTO registroConductor(ConductorRequestDTO dto) {
        Rol rol = rolRepository.findByName(ERol.CONDUCTOR)
                .orElseThrow(() -> new RoleNotFoundException("Rol no encontrado"));

        // 🔍 Validar duplicados
        if (conductorRepository.existsByDni(dto.dni())) {
            throw new IllegalArgumentException("Ya existe un conductor con el DNI: " + dto.dni());
        }

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.email());
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setRol(rol);

        Conductor conductor = new Conductor();
        conductor.setNombre(dto.nombre());
        conductor.setApellido(dto.apellido());
        conductor.setDni(dto.dni());
        conductor.setEdad(dto.edad());
        conductor.setDescripcionConductor(dto.descripcionConductor());
        conductor.setUsuario(usuario);

        LocalDateTime now = LocalDateTime.now();
        conductor.setCreatedAt(now);
        conductor.setUpdatedAt(now);


        // Relación bidireccional
        usuario.setConductor(conductor);

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toUsuarioPerfilResponseDTO(saved);
    }



    // --------------------------------------------------
    // Login
    // Usa AuthenticationManager para validar credenciales,
    // genera token y devuelve AuthResponseDTO
    // --------------------------------------------------
    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {

        // OJO: LoginRequestDTO es record, por eso usamos email() / password()
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.password()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Usuario usuario = userPrincipal.getUsuario();

        // Ajusta si tu TokenProvider tiene otra firma distinta
        String token = tokenProvider.createAccessToken(authentication);

        return usuarioMapper.toAuthResponseDTO(usuario, token);
    }

    // --------------------------------------------------
    // Update perfil
    // Nota: usamos UsuarioPerfilResponseDTO como "request" aquí.
    // --------------------------------------------------
    @Override
    public UsuarioPerfilResponseDTO updateUsuarioPerfil(Integer idUsuario,
                                                        UsuarioPerfilResponseDTO perfilRequestDTO) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Aplica cambios permitidos (email, nombre, apellido)
        usuarioMapper.applyPerfilUpdates(perfilRequestDTO, usuario);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return usuarioMapper.toUsuarioPerfilResponseDTO(usuario);
    }

    // --------------------------------------------------
    // Obtener perfil por ID
    // --------------------------------------------------
    @Override
    public UsuarioPerfilResponseDTO getUsuarioPerfilById(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return usuarioMapper.toUsuarioPerfilResponseDTO(usuario);
    }

    // --------------------------------------------------
    // Lógica privada de registro con rol
    // --------------------------------------------------
    private UsuarioPerfilResponseDTO registerUsuarioWithRol(UsuarioRegistroRequestDTO registroRequestDTO,
                                                            ERol rolEnum) {

        // Como UsuarioRegistroRequestDTO es record:
        // usamos .email(), .nombre(), .apellido(), .password()
        boolean existsByEmail = usuarioRepository.existsByEmail(registroRequestDTO.email());
        boolean existsAsPasajero = pasajeroRepository.existsByNombreAndApellido(
                registroRequestDTO.nombre(),
                registroRequestDTO.apellido()
        );
        boolean existsAsConductor = conductorRepository.existsByNombreAndApellido(
                registroRequestDTO.nombre(),
                registroRequestDTO.apellido()
        );

        if (existsByEmail) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        if (existsAsPasajero || existsAsConductor) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre y apellido");
        }

        // Obtener rol de la BD
        Rol rol = rolRepository.findByName(rolEnum)
                .orElseThrow(() -> new RoleNotFoundException("Error: Rol no encontrado: " + rolEnum));

        // Mapear DTO -> entidad Usuario
        Usuario usuario = usuarioMapper.toUsuarioEntity(registroRequestDTO);

        // Encriptar la contraseña del usuario ANTES de guardar
        // (ya no mutamos el DTO porque es record)
        usuario.setPassword(
                //passwordEncoder.encode(usuario.getPassword())
                passwordEncoder.encode(registroRequestDTO.password())
        );

        // Asociar rol
        usuario.setRol(rol);

        // Crear relación hijo según rol
        if (rolEnum == ERol.PASAJERO) {
            Pasajero pasajero = new Pasajero();
            pasajero.setNombre(registroRequestDTO.nombre());
            pasajero.setApellido(registroRequestDTO.apellido());
            pasajero.setUsuario(usuario);
            usuario.setPasajero(pasajero);

        } else if (rolEnum == ERol.CONDUCTOR) {
            Conductor conductor = new Conductor();
            conductor.setNombre(registroRequestDTO.nombre());
            conductor.setApellido(registroRequestDTO.apellido());
            conductor.setUsuario(usuario);
            usuario.setConductor(conductor);
        }

        // Guardar usuario (debería hacer cascade a pasajero/conductor si está bien mapeado)
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Devolver perfil del nuevo usuario
        return usuarioMapper.toUsuarioPerfilResponseDTO(savedUsuario);
    }
}
