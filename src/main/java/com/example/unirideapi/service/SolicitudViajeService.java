package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.model.enums.EstadoSolicitud;

public interface SolicitudViajeService {
    SolicitudViajeResponseDTO create(SolicitudViajeRequestDTO solicitudViajeRequestDTO);
    SolicitudViajeResponseDTO updateEstadoSolicitud(Integer idSolicitud, EstadoSolicitud nuevoEstado);
}
