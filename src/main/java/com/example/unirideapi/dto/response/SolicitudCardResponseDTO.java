package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.EstadoSolicitud;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record SolicitudCardResponseDTO(
    Integer idSolicitudViaje,
    String origen,
    String destino,
    LocalDate fechaSalida,
    LocalTime horaSalida,
    Long tarifa,
    Integer asientosDisponibles,
    EstadoSolicitud estadoSolicitud,
    String nombreConductor,
    String apellidoConductor,
    Integer idRuta
){}
