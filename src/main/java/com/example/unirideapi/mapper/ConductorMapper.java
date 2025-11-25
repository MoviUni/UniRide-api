package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.request.VehiculoRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.dto.response.VehiculoResponseDTO;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Vehiculo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConductorMapper {

    private final ModelMapper modelMapper;

    public Conductor toEntity(ConductorRequestDTO dto) {
        Conductor conductor = new Conductor();
        conductor.setNombre(dto.nombre());
        conductor.setApellido(dto.apellido());
        conductor.setEdad(dto.edad());
        conductor.setDni(dto.dni());
        conductor.setCarrera(dto.carrera());
        conductor.setCodigoUni(dto.codigoUni());
        return conductor;
    }


    public ConductorResponseDTO toDTO(Conductor conductor) {
        return new ConductorResponseDTO(
                conductor.getIdConductor(),
                conductor.getNombre(),
                conductor.getApellido(),
                conductor.getEdad(),
                conductor.getCarrera(),
                conductor.getCodigoUni()
        );
    }
}