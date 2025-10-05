package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;

public interface SolicitudViajeService {
    SolicitudViajeResponseDTO create(SolicitudViajeRequestDTO solicitudViajeRequestDTO);
}
