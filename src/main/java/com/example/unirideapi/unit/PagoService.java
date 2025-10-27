package com.example.unirideapi.unit;

import com.example.unirideapi.dto.request.PagoRequestDTO;
import com.example.unirideapi.dto.response.PagoResponseDTO;

public interface PagoService {
    PagoResponseDTO create(PagoRequestDTO solicitudViajeRequestDTO);
}
