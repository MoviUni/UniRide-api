package com.example.unirideapi.unit;

import com.example.unirideapi.dto.request.CalificacionRequestDTO;
import com.example.unirideapi.dto.response.CalificacionResponseDTO;

import java.util.List;

public interface CalificacionService {
    CalificacionResponseDTO createCalificacion(CalificacionRequestDTO calificacionRequestDTO);
    CalificacionResponseDTO findCalificacionById(Integer id);
    List<CalificacionResponseDTO> findCalificacionByConductorId(Integer idConductor);
    List<CalificacionResponseDTO> findCalificacionByPasajeroId(Integer idPasajero);
    CalificacionResponseDTO updateCalificacion(Integer id, CalificacionRequestDTO calificacionRequestDTO);
    void deleteCalificacion(Integer id);

}
