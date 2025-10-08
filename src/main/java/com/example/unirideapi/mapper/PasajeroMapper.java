package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.PasajeroResponseDTO;
import com.example.unirideapi.model.Pasajero;
import com.example.unirideapi.model.Usuario;
import com.example.unirideapi.model.Vehiculo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class PasajeroMapper {

    private final ModelMapper mm;

    public PasajeroMapper(ModelMapper mm) {
        this.mm = mm;
        // Puedes dejarlo así por si lo usas en otros mappers
        this.mm.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
    }

    /** Request -> Entity (CREATE básico, sin relaciones) */
    public Pasajero toEntity(PasajeroRequestDTO d) {
        if (d == null) return null;
        Pasajero c = new Pasajero();
        c.setNombre(d.nombre());
        c.setApellido(d.apellido());
        c.setDni(d.dni());
        c.setEdad(d.edad());
        c.setDescripcionPasajero(d.descripcionPasajero());
        // userId / vehiculoId se resuelven en el service (ver helper abajo)
        return c;
    }

    /** Request -> Entity (UPDATE sobre existente, ignora nulls) */
    public void updateEntity(PasajeroRequestDTO d, Pasajero c) {
        if (d == null || c == null) return;
        if (d.nombre() != null) c.setNombre(d.nombre());
        if (d.apellido() != null) c.setApellido(d.apellido());
        if (d.dni() != null) c.setDni(d.dni());
        if (d.edad() != null) c.setEdad(d.edad());
        if (d.descripcionPasajero() != null) c.setDescripcionPasajero(d.descripcionPasajero());
        // Las relaciones (userId / vehiculoId) también se aplican en el service
    }

    /** Helper opcional: adjunta relaciones ya cargadas por el service */
    public void attachRelations(Pasajero c, Usuario u) {
        if (c == null) return;
        if (u != null) c.setUsuario(u);
    }

    /** Entity -> Response (manual; compatible con record o clase sin ctor vacío) */
    public PasajeroResponseDTO toDTO(Pasajero e) {
        if (e == null) return null;
        return new PasajeroResponseDTO(
                e.getIdPasajero().longValue(),
                e.getNombre(),
                e.getApellido(),
                e.getDni(),
                e.getDescripcionPasajero(),
                e.getEdad()
        );
    }
}
