package com.example.unirideapi.dto;

import com.example.unirideapi.enums.Estado;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RutaDTO {
    @NotBlank(message = "El origen es requerido")
    private String origen;

    @NotBlank(message = "El destino es requerido")
    private String destino;

    @NotNull(message = "La fecha de salida es requerida")
    @Future(message = "La fecha de salida debe ser futura")
    private LocalDate fechaSalida;

    @NotNull(message = "La hora de salida es requerida")
    private LocalTime horaSalida;

    @NotNull(message = "La tarifa es requerida")
    @Min(value = 0, message = "La tarifa debe ser mayor o igual a 0")
    private Long tarifa;

    @NotNull(message = "Los asientos disponibles son requeridos")
    @Min(value = 1, message = "Debe haber al menos un asiento disponible")
    private Integer asientosDisponibles;

    @NotNull(message = "El estado es requerido")
    private Estado estado;

    @NotNull(message = "El ID del conductor es requerido")
    private Integer conductorId;
}
