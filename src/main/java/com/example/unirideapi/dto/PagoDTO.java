package com.example.unirideapi.dto;

import com.example.unirideapi.enums.Estado;
import com.example.unirideapi.enums.MedioPago;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PagoDTO {
    @NotNull(message = "El monto no puede ser nulo")
    @Min(value = 0, message = "El monto debe ser mayor o igual a 0")
    private Float monto;

    @NotNull(message = "El medio de pago es requerido")
    private MedioPago medioPago;

    @NotNull(message = "El estado del pago es requerido")
    private Estado estado;

    @NotNull(message = "El ID de la solicitud de viaje no puede ser nulo")
    private Integer solicitudViajeId;
}
