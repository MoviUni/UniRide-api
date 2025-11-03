package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.PagoRequestDTO;
import com.example.unirideapi.dto.response.PagoResponseDTO;

public interface PagoService {
    PagoResponseDTO create(PagoRequestDTO solicitudViajeRequestDTO);
}
