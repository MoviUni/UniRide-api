package com.example.unirideapi.dto;

import com.example.unirideapi.enums.Estado;

import java.time.LocalDate;
import java.time.LocalTime;

public class VisualizarRuta {
    private Integer idRuta;
    private String origen;
    private String destino;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;
    private Long tarifa;
    private Integer asientosDisponibles;
    private Estado estado;
    private Integer conductorId;
}
