package com.example.unirideapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConductorDTO {
    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    private String apellido;

    @NotBlank(message = "El dni es requerido")
    private String dni;

    @NotBlank(message = "La edad es requerida")
    private Integer edad;

    @NotBlank(message = "La disponibilidad es requerida")
    private String disponibilidad;

    @Size(max = 1000, message = "La descripcion no puede exceder los 500 caracteres")
    private String descripcionConductor;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Integer usuarioId;

    @NotNull(message = "El ID del vehiculo no puede ser nulo")
    private Integer vehiculoId;
}
