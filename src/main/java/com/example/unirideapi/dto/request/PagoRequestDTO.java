package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.enums.EstadoPago;
import lombok.Builder;

@Builder
public record PagoRequestDTO(
        Float monto,
        EstadoPago estadoPago,
        Integer solicitudViajeId
) {

}
