package com.example.unirideapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CalificacionDTO {
    @NotNull(message = "El puntaje no puede ser nulo")
    @Min(value = 1, message = "El puntaje mínimo es 1")
    @Max(value = 5, message = "El puntaje máximo es 5")
    private Integer puntaje;

    @Size(max = 500, message = "La descripcion no puede exceder los 500 caracteres")
    private String comentario;

    @NotNull(message = "El ID del conductor no puede ser nulo")
    private Integer conductorId;

    @NotNull(message = "El ID del pasajero no puede ser nulo")
    private Integer pasajeroId;
}
