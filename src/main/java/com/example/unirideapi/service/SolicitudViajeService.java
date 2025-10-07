package com.example.unirideapi.service;

import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.model.enums.EstadoSolicitud;

import java.util.List;

public interface SolicitudViajeService {
    List<SolicitudViajeResponseDTO> findSolicitudesByRutaId(Integer idRuta);

    SolicitudViajeResponseDTO updateEstadoSolicitud(Integer idSolicitud, EstadoSolicitud nuevoEstado);
}
