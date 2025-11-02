package com.example.unirideapi.unit;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudEstadoResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.model.enums.EstadoSolicitud;

import java.util.List;

public interface SolicitudViajeService {

    SolicitudViajeResponseDTO create(SolicitudViajeRequestDTO solicitudViajeRequestDTO);
    SolicitudViajeResponseDTO updateEstadoSolicitud(Integer idSolicitud, EstadoSolicitud nuevoEstado);
    List<SolicitudEstadoResponseDTO> searchByUsuario(Integer idUsuario);

    List<SolicitudViajeResponseDTO> findSolicitudesByRutaId(Integer idRuta);

}
