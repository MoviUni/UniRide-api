package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.LoginRequestDTO;
import com.example.unirideapi.dto.request.UsuarioRegistroRequestDTO;
import com.example.unirideapi.dto.response.AuthResponseDTO;
import com.example.unirideapi.dto.response.UsuarioPerfilResponseDTO;
import com.example.unirideapi.model.Usuario;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Pasajero;
import com.example.unirideapi.model.Rol;
import com.example.unirideapi.model.enums.ERol;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class UsuarioMapper {
    private final ModelMapper mm;

    public UsuarioMapper(ModelMapper mm) {
        this.mm = mm;
    }

    // --------------------------------------------------
    // CONFIGURACIÓN DE MAPPER
    // --------------------------------------------------
    @PostConstruct
    void config() {
        mm.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true);
    }

    // --------------------------------------------------
    // DTO registro -> ENTITY Usuario
    // --------------------------------------------------
    public Usuario toUsuarioEntity(UsuarioRegistroRequestDTO dto) {
        return mm.map(dto, Usuario.class);
    }

    // --------------------------------------------------
    // DTO login -> ENTITY Usuario
    // --------------------------------------------------
    public Usuario toUsuarioFromLogin(LoginRequestDTO dto) {
        return mm.map(dto, Usuario.class);
    }

    // --------------------------------------------------
    // ENTITY Usuario -> DTO Perfil
    // --------------------------------------------------
    public UsuarioPerfilResponseDTO toUsuarioPerfilResponseDTO(Usuario u) {
        String nombre = resolveNombre(u);
        String apellido = resolveApellido(u);
        String rolStr = resolveRol(u);

        ERol rolEnum = null;
        if (rolStr != null && !rolStr.isBlank()) {
            try {
                rolEnum = ERol.valueOf(rolStr);
            } catch (IllegalArgumentException ignored) {
                // si no matchea exactamente con el enum, lo dejamos null
            }
        }

        return new UsuarioPerfilResponseDTO(
                u.getIdUsuario(),
                nombre,        // nombre real
                apellido,      // apellido real
                u.getEmail(),  // email real
                rolEnum
        );
    }

    // --------------------------------------------------
    // ENTITY Usuario + token -> DTO AuthResponse
    // --------------------------------------------------
    public AuthResponseDTO toAuthResponseDTO(Usuario usuario, String token) {

        String nombre = resolveNombre(usuario);
        String apellido = resolveApellido(usuario);
        String rol = resolveRol(usuario);

        Integer idRol = null;
        if (usuario.getConductor() != null) {
            idRol = usuario.getConductor().getIdConductor();
        } else if (usuario.getPasajero() != null) {
            idRol = usuario.getPasajero().getIdPasajero();
        }

        return new AuthResponseDTO(token, nombre, apellido, rol, idRol);
    }

    // --------------------------------------------------
    // ACTUALIZAR PERFIL (updateUsuarioPerfil)
    // --------------------------------------------------
    public void applyPerfilUpdates(UsuarioPerfilResponseDTO source, Usuario target) {
        if (source.email() != null && !source.email().isBlank()) {
            target.setEmail(source.email());
        }

        // Si el usuario tiene pasajero asociado
        if (target.getPasajero() != null) {
            Pasajero p = target.getPasajero();
            if (source.nombre() != null && !source.nombre().isBlank()) {
                p.setNombre(source.nombre());
            }
            if (source.apellido() != null && !source.apellido().isBlank()) {
                p.setApellido(source.apellido());
            }
        }

        // Si el usuario tiene conductor asociado
        if (target.getConductor() != null) {
            Conductor c = target.getConductor();
            if (source.nombre() != null && !source.nombre().isBlank()) {
                c.setNombre(source.nombre());
            }
            if (source.apellido() != null && !source.apellido().isBlank()) {
                c.setApellido(source.apellido());
            }
        }
    }

    // --------------------------------------------------
    // MÉTODOS AUXILIARES PRIVADOS
    // --------------------------------------------------
    private String resolveNombre(Usuario u) {
        if (u.getConductor() != null && u.getConductor().getNombre() != null)
            return u.getConductor().getNombre();
        if (u.getPasajero() != null && u.getPasajero().getNombre() != null)
            return u.getPasajero().getNombre();
        return "";
    }

    private String resolveApellido(Usuario u) {
        if (u.getConductor() != null && u.getConductor().getApellido() != null)
            return u.getConductor().getApellido();
        if (u.getPasajero() != null && u.getPasajero().getApellido() != null)
            return u.getPasajero().getApellido();
        return "";
    }

    private String resolveRol(Usuario u) {
        Rol rol = u.getRol();
        if (rol == null || rol.getName() == null) return "";
        return rol.getName().toString();
    }


}
