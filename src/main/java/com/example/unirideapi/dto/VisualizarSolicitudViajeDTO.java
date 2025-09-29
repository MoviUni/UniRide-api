package com.example.unirideapi.dto;

import com.example.unirideapi.enums.Estado;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class VisualizarSolicitudViajeDTO {
    private Integer idSolicitudViaje;
    private LocalDate fecha;
    private LocalTime hora;
    private LocalDateTime updatedAt;
    private Estado estado;
    private Integer rutaId;
    private Integer pasajeroId;
}
