package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.enums.EstadoPago;
import com.example.unirideapi.model.enums.MedioPago;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record PagoRequestDTO(
        Float monto,
        EstadoPago estadoPago,
        Integer solicitudViajeId,
        MedioPago medioPago,
        Float comision,
        LocalDate fecha,
        LocalTime hora
) {

}
