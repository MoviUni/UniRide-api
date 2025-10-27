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

    @PostConstruct
    void config() {
        mm.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true);
    }

    // --------------------------------------------------
    // REQUEST registro -> ENTITY Usuario
    // --------------------------------------------------
    public Usuario toUsuarioEntity(UsuarioRegistroRequestDTO dto) {
        return mm.map(dto, Usuario.class);
    }

    // --------------------------------------------------
    // REQUEST login -> ENTITY Usuario mínimo
    // --------------------------------------------------
    public Usuario toUsuarioFromLogin(LoginRequestDTO dto) {
        return mm.map(dto, Usuario.class);
    }

    // --------------------------------------------------
    // ENTITY Usuario -> RESPONSE Perfil
    // (usa nombre/apellido de Pasajero/Conductor)
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
                u.getEmail(),
                nombre,
                apellido,
                rolEnum
        );
    }

    // --------------------------------------------------
    // ENTITY Usuario + token -> RESPONSE Auth
    // (esto es lo que devolvemos en login)
    // --------------------------------------------------
    public AuthResponseDTO toAuthResponseDTO(Usuario u, String token) {
        String nombre = resolveNombre(u);
        String apellido = resolveApellido(u);
        String rol = resolveRol(u);

        // Esta versión asume que AuthResponseDTO tiene
        // un constructor (String token, String nombre, String apellido, String rol)
        // como en tu código original.
        return new AuthResponseDTO(token, nombre, apellido, rol);
    }

    // --------------------------------------------------
    // ACTUALIZAR PERFIL SOBRE UNA ENTIDAD EXISTENTE
    //
    // Esto lo usa updateUsuarioPerfil() en el service.
    // Copia datos editables desde el DTO hacia la entidad y
    // sus relaciones (Pasajero o Conductor).
    // --------------------------------------------------
    public void applyPerfilUpdates(UsuarioPerfilResponseDTO source, Usuario target) {

        // Actualizamos email si mandaron uno nuevo
        if (source.email() != null && !source.email().isBlank()) {
            target.setEmail(source.email());
        }

        // Si el usuario es Pasajero, actualizamos nombre/apellido ahí
        Pasajero p = safePasajero(target);
        if (p != null) {
            if (source.nombre() != null && !source.nombre().isBlank()) {
                p.setNombre(source.nombre());
            }
            if (source.apellido() != null && !source.apellido().isBlank()) {
                p.setApellido(source.apellido());
            }
        }

        // Si el usuario es Conductor, actualizamos nombre/apellido ahí
        Conductor c = safeConductor(target);
        if (c != null) {
            if (source.nombre() != null && !source.nombre().isBlank()) {
                c.setNombre(source.nombre());
            }
            if (source.apellido() != null && !source.apellido().isBlank()) {
                c.setApellido(source.apellido());
            }
        }

        // No tocamos el rol aquí a propósito
    }


    // ----------------- helpers privados -----------------

    private String resolveNombre(Usuario u) {
        Conductor c = safeConductor(u);
        if (c != null && c.getNombre() != null) return c.getNombre();

        Pasajero p = safePasajero(u);
        if (p != null && p.getNombre() != null) return p.getNombre();

        // fallback si Usuario también tiene getNombre()
        try {
            var m = u.getClass().getMethod("getNombre");
            Object v = m.invoke(u);
            if (v != null) return v.toString();
        } catch (Exception ignored) {}
        return "";
    }

    private String resolveApellido(Usuario u) {
        Conductor c = safeConductor(u);
        if (c != null && c.getApellido() != null) return c.getApellido();

        Pasajero p = safePasajero(u);
        if (p != null && p.getApellido() != null) return p.getApellido();

        // fallback si Usuario también tiene getApellido()
        try {
            var m = u.getClass().getMethod("getApellido");
            Object v = m.invoke(u);
            if (v != null) return v.toString();
        } catch (Exception ignored) {}
        return "";
    }

    private String resolveRol(Usuario u) {
        Rol rol = u.getRol();
        if (rol == null) return "";

        try {
            var m = rol.getClass().getMethod("getName");
            Object name = m.invoke(rol);
            return name != null ? name.toString() : "";
        } catch (Exception e) {
            return rol.toString();
        }
    }

    private Conductor safeConductor(Usuario u) {
        try {
            return (Conductor) u.getClass().getMethod("getConductor").invoke(u);
        } catch (Exception e) {
            return null;
        }
    }

    private Pasajero safePasajero(Usuario u) {
        try {
            return (Pasajero) u.getClass().getMethod("getPasajero").invoke(u);
        } catch (Exception e) {
            return null;
        }
    }
}
