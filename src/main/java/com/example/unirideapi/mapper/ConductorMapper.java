package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.model.Conductor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ConductorMapper {

    private final ModelMapper mm;

    public ConductorMapper(ModelMapper mm) {
        this.mm = mm;
        this.mm.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true); // útil para updates parciales
    }

    /** Request -> Entity (CREATE) */
    public Conductor toEntity(ConductorRequestDTO dto) {
        return mm.map(dto, Conductor.class);
    }

    /** Request -> Entity (UPDATE sobre existente) */
    public void updateEntity(ConductorRequestDTO dto, Conductor entity) {
        mm.map(dto, entity); // respeta SkipNullEnabled
    }

    /** Entity -> Response */
    public ConductorResponseDTO toDTO(Conductor entity) {
        return mm.map(entity, ConductorResponseDTO.class);
    }
}


