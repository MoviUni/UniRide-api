package com.example.unirideapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VisualizarCalificaciónDTO {
    private Integer idCalificacion;
    private Integer puntaje;
    private String comentario;
    private Integer conductorId;
    private Integer pasajeroId;
}
