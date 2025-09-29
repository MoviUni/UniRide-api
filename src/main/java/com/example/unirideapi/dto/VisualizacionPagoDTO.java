package com.example.unirideapi.dto;

import com.example.unirideapi.enums.Estado;
import com.example.unirideapi.enums.MedioPago;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VisualizacionPagoDTO {
    private Integer idPago;
    private Float monto;
    private LocalDate fecha;
    private LocalTime hora;
    private Float comision;
    private MedioPago medioPago;
    private Estado estado;
    private Integer solicitudViajeId;
}
