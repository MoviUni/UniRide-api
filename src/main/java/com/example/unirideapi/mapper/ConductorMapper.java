package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Usuario;
import com.example.unirideapi.model.Vehiculo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ConductorMapper {

    private final ModelMapper mm;

    public ConductorMapper(ModelMapper mm) {
        this.mm = mm;
        // Puedes dejarlo así por si lo usas en otros mappers
        this.mm.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
    }

    /** Request -> Entity (CREATE básico, sin relaciones) */
    public Conductor toEntity(ConductorRequestDTO d) {
        if (d == null) return null;
        Conductor c = new Conductor();
        c.setNombre(d.nombre());
        c.setApellido(d.apellido());
        c.setDni(d.dni());
        c.setEdad(d.edad());
        c.setDescripcionConductor(d.descripcionConductor());
        // userId / vehiculoId se resuelven en el service (ver helper abajo)
        return c;
    }

    /** Request -> Entity (UPDATE sobre existente, ignora nulls) */
    public void updateEntity(ConductorRequestDTO d, Conductor c) {
        if (d == null || c == null) return;
        if (d.nombre() != null) c.setNombre(d.nombre());
        if (d.apellido() != null) c.setApellido(d.apellido());
        if (d.dni() != null) c.setDni(d.dni());
        if (d.edad() != null) c.setEdad(d.edad());
        if (d.descripcionConductor() != null) c.setDescripcionConductor(d.descripcionConductor());
        // Las relaciones (userId / vehiculoId) también se aplican en el service
    }

    /** Helper opcional: adjunta relaciones ya cargadas por el service */
    public void attachRelations(Conductor c, Usuario u, Vehiculo v) {
        if (c == null) return;
        if (u != null) c.setUsuario(u);
        if (v != null) c.setVehiculo(v);
    }

    /** Entity -> Response (manual; compatible con record o clase sin ctor vacío) */
    public ConductorResponseDTO toDTO(Conductor e) {
        if (e == null) return null;
        return new ConductorResponseDTO(
                e.getIdConductor(),
                e.getNombre(),
                e.getApellido(),
                e.getEdad(),
                e.getDescripcionConductor()
        );
    }
}


