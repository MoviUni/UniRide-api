package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.EstadoRuta;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record RutaCardResponseDTO(
        Integer idRuta,
        String origen,
        String destino,
        LocalDate fechaSalida,
        LocalTime horaSalida,
        Long tarifa,
        Integer asientosDisponibles,
        String nombreConductor,
        String apellidoConductor,
        String vehiculoColor,
        String vehiculoPlaca,
        String vehiculoModelo

) {}
