package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.LoginRequestDTO;
import com.example.unirideapi.dto.request.UsuarioRegistroRequestDTO;
import com.example.unirideapi.dto.response.AuthResponseDTO;
import com.example.unirideapi.dto.response.UsuarioPerfilResponseDTO;
import com.example.unirideapi.exception.ResourceNotFoundException;
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
import com.example.unirideapi.security.UserPrincipal;      // <-- adapta el paquete si cambió
import com.example.unirideapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    // ===========================
    // REGISTROS
    // ===========================

    @Transactional
    @Override
    public UsuarioPerfilResponseDTO registroPasajero(UsuarioRegistroRequestDTO dto) {
        validarDuplicadosBasicos(dto);

        Rol rol = rolRepository.findByName(ERol.PASAJERO)
                .orElseThrow(() -> new ResourceNotFoundException("Rol PASAJERO no encontrado"));

        Usuario usuario = usuarioMapper.toUsuarioEntity(dto);
        usuario.setRol(rol);
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario = usuarioRepository.save(usuario);

        Pasajero pasajero = new Pasajero();
        pasajero.setUsuario(usuario);
        pasajero.setNombre(dto.nombre());
        pasajero.setApellido(dto.apellido());
        pasajero.setCreatedAt(LocalDateTime.now());
        pasajeroRepository.save(pasajero);

        usuario.setPasajero(pasajero); // mantener relación poblada en memoria
        return usuarioMapper.toUsuarioPerfilResponseDTO(usuario);
    }

    @Transactional
    @Override
    public UsuarioPerfilResponseDTO registroConductor(UsuarioRegistroRequestDTO dto) {
        validarDuplicadosBasicos(dto);

        Rol rol = rolRepository.findByName(ERol.CONDUCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Rol CONDUCTOR no encontrado"));

        Usuario usuario = usuarioMapper.toUsuarioEntity(dto);
        usuario.setRol(rol);
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario = usuarioRepository.save(usuario);

        Conductor conductor = new Conductor();
        conductor.setUsuario(usuario);
        conductor.setNombre(dto.nombre());
        conductor.setApellido(dto.apellido());
        conductor.setCreatedAt(LocalDateTime.now());
        conductorRepository.save(conductor);

        usuario.setConductor(conductor);
        return usuarioMapper.toUsuarioPerfilResponseDTO(usuario);
    }

    // ===========================
    // LOGIN
    // ===========================

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // Autenticación con AuthenticationManager (como en tu otro proyecto)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password())
        );

        // Principal -> Usuario
        Usuario usuario;
        if (authentication.getPrincipal() instanceof UserPrincipal up) {
            usuario = up.getUsuario();
        } else {
            // Fallback por si tu principal no expone el Usuario directamente
            usuario = usuarioRepository.findByEmail(loginRequestDTO.email())
                    .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));
        }

        String token = tokenProvider.createAccessToken(authentication);
        return usuarioMapper.toAuthResponseDTO(usuario, token);
    }

    // ===========================
    // PERFIL
    // ===========================

    @Transactional
    @Override
    public UsuarioPerfilResponseDTO updateUsuarioPerfil(Integer idUsuario, UsuarioPerfilResponseDTO perfil) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Actualizar nombre/apellido en la entidad “dueña” de esos campos (Conductor o Pasajero)
        if (usuario.getConductor() != null) {
            Conductor c = usuario.getConductor();
            if (perfil.nombre() != null) c.setNombre(perfil.nombre());
            if (perfil.apellido() != null) c.setApellido(perfil.apellido());
        } else if (usuario.getPasajero() != null) {
            Pasajero p = usuario.getPasajero();
            if (perfil.nombre() != null) p.setNombre(perfil.nombre());
            if (perfil.apellido() != null) p.setApellido(perfil.apellido());
        } else {
            // Si también guardas nombre/apellido en Usuario, descomenta esto:
            // if (perfil.nombre() != null) usuario.setNombre(perfil.nombre());
            // if (perfil.apellido() != null) usuario.setApellido(perfil.apellido());
        }

        if (perfil.email() != null) usuario.setEmail(perfil.email());

        return usuarioMapper.toUsuarioPerfilResponseDTO(usuario);
    }

    @Override
    public UsuarioPerfilResponseDTO getUsuarioPerfilById(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return usuarioMapper.toUsuarioPerfilResponseDTO(usuario);
    }

    // ===========================
    // GOOGLE LOGIN/REGISTER
    // ===========================

  //  @Transactional
   // @Override
   // public AuthResponseDTO loginOrRegisterGoogle(UsuarioRegistroRequestDTO dto) {
     //   Optional<Usuario> opt = usuarioRepository.findByEmail(dto.email());

    //    Usuario usuario = opt.orElseGet(() -> {
            // Alta rápida como PASAJERO por defecto
    //        Rol rol = rolRepository.findByName(ERol.PASAJERO)
      //              .orElseThrow(() -> new ResourceNotFoundException("Rol PASAJERO no encontrado"));

            // Password aleatoria (no usada para Google)
      //      Usuario nuevo = usuarioMapper.toUsuarioEntity(dto);
       //     nuevo.setRol(rol);
        //    nuevo.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
       //     Usuario saved = usuarioRepository.save(nuevo);

        //    Pasajero p = new Pasajero();
       //     p.setUsuario(saved);
        //    p.setNombre(dto.nombre());
        //    p.setApellido(dto.apellido());
            // Si no tienes DNI desde Google, genera un placeholder
            // (mejor opcional y no-único si tu esquema lo permite)
        //    if (p.getDni() == null) {
       //         p.setDni("GOOGLE-" + UUID.randomUUID());
        //    }
       //     p.setCreatedAt(LocalDateTime.now());
         //   pasajeroRepository.save(p);

       //     saved.setPasajero(p);
       //     return saved;
    //    });

        // Autenticación sintética para emitir token (coincide con tu TokenProvider)
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
 //               new com.example.unirideapi.security.UserPrincipal(   // ajusta si tu UserPrincipal difiere
  //                      usuario.getIdUsuario(),
   //                     usuario.getEmail(),
    //                    usuario.getPassword(),
     //                   usuario.getAuthorities(), // o construye authorities desde el rol
      //                  usuario
        //        ),
       //         null,
        //        usuario.getAuthorities()
     //   );

     //   String token = tokenProvider.createAccessToken(authentication);
   //     return usuarioMapper.toAuthResponseDTO(usuario, token);
 //   }

    // ===========================
    // HELPERS
    // ===========================

    private void validarDuplicadosBasicos(UsuarioRegistroRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        // Si quieres validar nombre+apellido para evitar duplicados entre Conductor/Pasajero,
        // puedes agregar métodos exists... en tus repositorios y chequear aquí.
    }
}

