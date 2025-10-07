package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RutaServiceImpl implements RutaService {

    private final RutaRepository rutaRepository;
    private final RutaMapper rutaMapper;

    @Transactional
    @Override
    public RutaResponseDTO updateEstadoRuta(Integer idRuta, EstadoRuta nuevoEstado) {
        var ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        //Regla 1: Solo rutas PROGRAMADAS pueden cambiar a CONFIRMADO o CANCELADO
        if (ruta.getEstadoRuta() != EstadoRuta.PROGRAMADO) {
            throw new BusinessRuleException("Solo se pueden confirmar o cancelar rutas en estado PROGRAMADO");
        }

        //Regla 2: No puede confirmarse ni cancelarse si falta menos de 1 hora
        LocalDateTime fechaHoraSalida = LocalDateTime.of(ruta.getFechaSalida(), ruta.getHoraSalida());
        LocalDateTime ahora = LocalDateTime.now();

        if (Duration.between(ahora, fechaHoraSalida).toMinutes() < 60) {
            throw new BusinessRuleException("No se puede confirmar o cancelar el viaje con menos de 1 hora de anticipación");
        }
        // Actualizar estado
        ruta.setEstadoRuta(nuevoEstado);
        rutaRepository.save(ruta);

        return rutaMapper.toDTO(ruta);
    }

}
