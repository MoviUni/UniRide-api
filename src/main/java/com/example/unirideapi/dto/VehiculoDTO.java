package com.example.unirideapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VehiculoDTO {
    @NotBlank(message = "La placa es requerida")
    private String placa;

    @NotNull(message = "El SOAT es requerido")
    private Boolean soat;

    @NotBlank(message = "El modelo es requerido")
    private String modelo;

    @NotBlank(message = "La marca es requerida")
    private String marca;

    @NotNull(message = "La capacidad es requerida")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidad;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcionVehiculo;
}
