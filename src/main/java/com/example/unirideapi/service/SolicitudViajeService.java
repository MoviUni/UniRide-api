package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.model.enums.EstadoSolicitud;

import java.util.List;

public interface SolicitudViajeService {

    SolicitudViajeResponseDTO create(SolicitudViajeRequestDTO solicitudViajeRequestDTO);
    SolicitudViajeResponseDTO updateEstadoSolicitud(Integer idSolicitud, EstadoSolicitud nuevoEstado);
    List<SolicitudViajeResponseDTO> searchByUsuario(Integer idUsuario);

    List<SolicitudViajeResponseDTO> findSolicitudesByRutaId(Integer idRuta);

}
