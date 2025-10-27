package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.LoginRequestDTO;
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
import com.example.unirideapi.security.TokenProvider;       // <-- adapta el paquete si cambió
import com.example.unirideapi.security.UserPrincipal;    // <-- adapta el paquete si cambió
import com.example.unirideapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ConductorRepository conductorRepository;
    private final PasajeroRepository pasajeroRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager; // si no usas AM, puedes validar manualmente
    private final TokenProvider tokenProvider;                 // si usas otro servicio JWT, reemplaza aquí
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioPerfilResponseDTO registroPasajero(UsuarioRegistroRequestDTO registroRequestDTO) {
        return registerUsuarioWithRol(registroRequestDTO, ERol.PASAJERO);
    }

    @Override
    public UsuarioPerfilResponseDTO registroConductor(UsuarioRegistroRequestDTO registroRequestDTO) {
        return registerUsuarioWithRol(registroRequestDTO, ERol.CONDUCTOR);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email, loginRequestDTO.password)
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Usuario usuario = userPrincipal.getUsuario();

        String token = tokenProvider.createAccessToken(authentication);

        String nombre = null;
        String apellido = null;

        if (usuario.getPasajero() != null) {
            nombre = usuario.getPasajero().getNombre();
            apellido = usuario.getPasajero().getApellido();
        } else if (usuario.getConductor() != null) {
            nombre = usuario.getConductor().getNombre();
            apellido = usuario.getConductor().getApellido();
        }

        return new AuthResponseDTO(token, usuario.getIdUsuario(), nombre, apellido);
    }

    @Override
    public UsuarioPerfilResponseDTO updateUsuarioPerfil(Integer idUsuario, UsuarioPerfilResponseDTO usuarioPerfilResponseDTO) {
        return null;
    }

    @Override
    public UsuarioPerfilResponseDTO getUsuarioPerfilById(Integer idUsuario) {
        return null;
    }

    private UsuarioPerfilResponseDTO registerUsuarioWithRol(UsuarioRegistroRequestDTO registroRequestDTO, ERol rolEnum) {

        boolean existsByEmail = usuarioRepository.existsByEmail(registroRequestDTO.email);
        boolean existsAsPasajero = pasajeroRepository.existsByNombreAndApellido(
                registroRequestDTO.nombre, registroRequestDTO.apellido);
        boolean existsAsConductor = conductorRepository.existsByNombreAndApellido(
                registroRequestDTO.nombre, registroRequestDTO.apellido);

        if (existsByEmail) {
            throw new IllegalArgumentException("El email ya están registrados");
        }

        if (existsAsPasajero || existsAsConductor) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre y apellido");
        }

        Rol rol = rolRepository.findByName(rolEnum)
                .orElseThrow(() -> new RoleNotFoundException("Error: Rol no encontrado: "));

        Usuario usuario = usuarioMapper.toUsuarioEntity(registroRequestDTO);
        usuario.setPassword(passwordEncoder.encode(registroRequestDTO.password));
        usuario.setRol(rol);

        if (rolEnum == ERol.PASAJERO) {
            Pasajero pasajero = new Pasajero();
            pasajero.setNombre(registroRequestDTO.nombre);
            pasajero.setApellido(registroRequestDTO.apellido);
            pasajero.setUsuario(usuario);
            usuario.setPasajero(pasajero);
        } else if (rolEnum == ERol.CONDUCTOR) {
            Conductor conductor = new Conductor();
            conductor.setNombre(registroRequestDTO.nombre);
            conductor.setApellido(registroRequestDTO.apellido);
            conductor.setUsuario(usuario);
            usuario.setConductor(conductor);
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);

        return usuarioMapper.toUsuarioPerfilResponseDTO(savedUsuario);
    }
}

