package com.example.unirideapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VisualizarConductorDTO {
    private Integer idConductor;
    private String nombre;
    private String apellido;
    private Integer edad;
    private String disponibilidad;
    private String descripcionConductor;
    private Integer vehiculoId;
}
