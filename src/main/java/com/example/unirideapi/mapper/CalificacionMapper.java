package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.CalificacionRequestDTO;
import com.example.unirideapi.dto.response.CalificacionResponseDTO;
import com.example.unirideapi.model.Calificacion;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Pasajero;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalificacionMapper {
    private final ModelMapper modelMapper;

//    public CalificacionResponseDTO toDto(Calificacion calificacion) { return modelMapper.map(calificacion, CalificacionResponseDTO.class);}
//
//    public Calificacion toEntity(CalificacionRequestDTO dto) {return modelMapper.map(dto, Calificacion.class);}


    // Convierte entidad a DTO
    public CalificacionResponseDTO toDto(Calificacion calificacion) {
        if (calificacion == null) return null;

        return new CalificacionResponseDTO(
                calificacion.getIdCalificacion(),
                calificacion.getPuntaje(),
                calificacion.getComentario(),
                calificacion.getUpdatedAt(),
                calificacion.getConductor() != null ? calificacion.getConductor().getIdConductor() : null,
                calificacion.getPasajero() != null ? calificacion.getPasajero().getIdPasajero() : null
        );
    }

    // Convierte DTO a entidad
    public Calificacion toEntity(CalificacionRequestDTO dto) {
        if (dto == null) return null;

        Calificacion calificacion = new Calificacion();
        calificacion.setPuntaje(dto.puntaje());
        calificacion.setComentario(dto.comentario());

        // Asignar referencias por ID
        if (dto.conductor() != null) {
            Conductor conductor = new Conductor();
            conductor.setIdConductor(dto.conductor());
            calificacion.setConductor(conductor);
        }

        if (dto.pasajero() != null) {
            Pasajero pasajero = new Pasajero();
            pasajero.setIdPasajero(dto.pasajero());
            calificacion.setPasajero(pasajero);
        }

        return calificacion;
    }
}
