package com.example.unirideapi.dto;

import com.example.unirideapi.enums.Estado;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class SolicitudViajeDTO {
    @NotNull(message = "La fecha de la solicitud es requerida")
    private LocalDate fecha;

    @NotNull(message = "La hora de la solicitud es requerida")
    private LocalTime hora;

    @NotNull(message = "El estado de la solicitud es requerido")
    private Estado estado;

    @NotNull(message = "El ID de la ruta es requerido")
    private Integer rutaId;

    @NotNull(message = "El ID del pasajero es requerido")
    private Integer pasajeroId;
}
