package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.enums.Estado;
import lombok.Builder;

@Builder
public record PagoRequestDTO(
        Float monto,
        Estado estado,
        Integer solicitudViajeId
) {

}
