package com.example.unirideapi.unit.impl;

import com.example.unirideapi.dto.request.CalificacionRequestDTO;
import com.example.unirideapi.dto.response.CalificacionResponseDTO;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.CalificacionMapper;
import com.example.unirideapi.model.Calificacion;
import com.example.unirideapi.repository.CalificacionRepository;
import com.example.unirideapi.unit.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalificacionServiceImpl implements CalificacionService {
    private final CalificacionRepository calificacionRepository;
    private final CalificacionMapper calificacionMapper;

    @Transactional
    @Override
    public CalificacionResponseDTO createCalificacion(CalificacionRequestDTO calificacionRequestDTO) {
        Calificacion calificacion = calificacionMapper.toEntity(calificacionRequestDTO);
        calificacion.setUpdatedAt(LocalDateTime.now());
        calificacion = calificacionRepository.save(calificacion);
        return calificacionMapper.toDto(calificacion);

    }

    @Override
    public CalificacionResponseDTO findCalificacionById(Integer id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La calificación con ID "+id+" no fue encontrado"));
        return calificacionMapper.toDto(calificacion);
    }

    @Override
    public List<CalificacionResponseDTO> findCalificacionByPasajeroId(Integer idPasajero) {
        List<Calificacion> calificaciones = calificacionRepository.findCalificacionByPasajero_IdPasajero(idPasajero);
        return calificaciones.stream()
                .map(calificacionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalificacionResponseDTO> findCalificacionByConductorId(Integer idConductor) {
        List<Calificacion> calificaciones = calificacionRepository.findCalificacionByConductor_IdConductor(idConductor);
        return calificaciones.stream()
                .map(calificacionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CalificacionResponseDTO updateCalificacion(Integer id, CalificacionRequestDTO calificacionRequestDTO) {
        Calificacion calificacionFromDb = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La calificación con ID " + id + " no fue encontrada"));

        if (calificacionRequestDTO.puntaje() != null) {
            calificacionFromDb.setPuntaje(calificacionRequestDTO.puntaje());
        }
        if (calificacionRequestDTO.comentario() != null) {
            calificacionFromDb.setComentario(calificacionRequestDTO.comentario());
        }

        calificacionFromDb.setUpdatedAt(LocalDateTime.now());
        calificacionFromDb = calificacionRepository.save(calificacionFromDb);
        return calificacionMapper.toDto(calificacionFromDb);
    }

    @Transactional
    @Override
    public void deleteCalificacion(Integer id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La calificacion con ID " + id + " no fue encontrado"));
        calificacionRepository.delete(calificacion);

    }
}
