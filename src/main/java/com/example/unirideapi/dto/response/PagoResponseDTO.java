package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.Estado;
import com.example.unirideapi.model.enums.MedioPago;

import java.time.LocalDate;
import java.time.LocalTime;

public record PagoResponseDTO(
        Integer idPago,
        Float monto,
        LocalDate fecha,
        LocalTime hora,
        Float comision,
        MedioPago medioPago,
        Estado estado,
        Integer solicitudViajeId
) {
}
