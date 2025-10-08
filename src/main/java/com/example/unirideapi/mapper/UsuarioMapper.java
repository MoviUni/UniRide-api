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

    /** Registro -> Usuario (solo campos básicos del usuario) */
    public Usuario toUsuarioEntity(UsuarioRegistroRequestDTO dto) {
        // Mapea email y password; nombre/apellido los podrías guardar
        // en Usuario si tu entidad los tiene, o crear luego Conductor/Pasajero.
        return mm.map(dto, Usuario.class);
    }

    /** Login -> "Usuario" mínimo para comparar credenciales (email, password) */
    public Usuario toUsuarioFromLogin(LoginRequestDTO dto) {
        return mm.map(dto, Usuario.class);
    }

    /** Usuario -> Perfil (elige nombre/apellido desde Conductor/Pasajero si existen) */
    public UsuarioPerfilResponseDTO toUsuarioPerfilResponseDTO(Usuario u) {
        String nombre = resolveNombre(u);
        String apellido = resolveApellido(u);
        String rol = resolveRol(u);
        return new UsuarioPerfilResponseDTO(
                u.getIdUsuario(),
                u.getEmail(),
                nombre,
                apellido,
                ERol.valueOf(rol)
        );
    }

    /** Usuario + token -> AuthResponse */
    public AuthResponseDTO toAuthResponseDTO(Usuario u, String token) {
        String nombre = resolveNombre(u);
        String apellido = resolveApellido(u);
        String rol = resolveRol(u);
        return new AuthResponseDTO(token, nombre, apellido, rol);
    }

    // ----------------- helpers -----------------

    private String resolveNombre(Usuario u) {
        Conductor c = safeConductor(u);
        if (c != null && c.getNombre() != null) return c.getNombre();

        Pasajero p = safePasajero(u);
        if (p != null && p.getNombre() != null) return p.getNombre();

        // fallback si Usuario también tiene nombre
        try { var m = u.getClass().getMethod("getNombre");
            Object v = m.invoke(u);
            if (v != null) return v.toString(); } catch (Exception ignored) {}
        return "";
    }

    private String resolveApellido(Usuario u) {
        Conductor c = safeConductor(u);
        if (c != null && c.getApellido() != null) return c.getApellido();

        Pasajero p = safePasajero(u);
        if (p != null && p.getApellido() != null) return p.getApellido();

        // fallback si Usuario también tiene apellido
        try { var m = u.getClass().getMethod("getApellido");
            Object v = m.invoke(u);
            if (v != null) return v.toString(); } catch (Exception ignored) {}
        return "";
    }

    private String resolveRol(Usuario u) {
        Rol rol = u.getRol();
        if (rol == null) return "";
        // Ajusta según tu tipo: Enum directo, o campo name (Enum) dentro de Rol.
        // Ejemplos:
        // return rol.getName().name();      // si Rol{ ERole name; }
        // return rol.getName().toString();  // si es Enum simple
        // return rol.getNombre();           // si guardas String
        try {
            var m = rol.getClass().getMethod("getName");
            Object name = m.invoke(rol);
            return name != null ? name.toString() : "";
        } catch (Exception e) {
            return rol.toString();
        }
    }

    private Conductor safeConductor(Usuario u) {
        try { return (Conductor) u.getClass().getMethod("getConductor").invoke(u); }
        catch (Exception e) { return null; }
    }

    private Pasajero safePasajero(Usuario u) {
        try { return (Pasajero) u.getClass().getMethod("getPasajero").invoke(u); }
        catch (Exception e) { return null; }
    }
}

