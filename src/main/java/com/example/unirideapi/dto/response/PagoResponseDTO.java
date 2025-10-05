package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.EstadoPago;
import com.example.unirideapi.model.enums.MedioPago;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record PagoResponseDTO(
        Integer idPago,
        Float monto,
        LocalDate fecha,
        LocalTime hora,
        Float comision,
        MedioPago medioPago,
        EstadoPago estadoPago,
        Integer solicitudViajeId
) {
}
