package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.PasajeroResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.model.Pasajero;
import com.example.unirideapi.repository.PasajeroRepository;
//import com.example.unirideapi.repository.UserBookHistoryRepository;
import com.example.unirideapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasajeroService {
    private final PasajeroRepository pasajeroRepository;
    private final UsuarioRepository usuarioRepository;
    //private final UserBookHistoryRepository historyRepository;

    @Transactional
    public PasajeroResponseDTO create(PasajeroRequestDTO dto) {
        var user = usuarioRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Regla: un usuario no puede tener más de un perfil de lector
        if (pasajeroRepository.existsByUserId(dto.userId())) {
            throw new BusinessRuleException("El usuario ya tiene un perfil de pasajero");
        }

        var reader = Pasajero.builder()
                .nombre(dto.nombre().trim())
                .apellido(dto.apellido().trim())
                .dni(dto.dni())
                .edad(dto.edad())
                .descripcionPasajero(dto.descripcionPasajero())
                .usuario(user)
                .build();

        return toResponse(pasajeroRepository.save(reader));
    }

    @Transactional(readOnly = true)
    public List<PasajeroResponseDTO> findAll() {
        return pasajeroRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PasajeroResponseDTO findById(Long id) {
        return pasajeroRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado"));
    }

    @Transactional
    public void delete(Long id) {
        var reader = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lector no encontrado"));

//        // Regla: no se puede eliminar un lector con historial de interacciones
//        if (!historyRepository.findByReaderId(id).isEmpty()) {
//            throw new BusinessRuleException("No se puede eliminar el lector porque tiene historial de interacciones");
//        }

        pasajeroRepository.delete(reader);
    }

    private PasajeroResponseDTO toResponse(Pasajero pasajero) {
        return PasajeroResponseDTO.builder()
                .id(pasajero.getIdPasajero())
                .nombre(pasajero.getNombre())
                .apellido(pasajero.getApellido())
                .dni(pasajero.getDni())
                .descripcion(pasajero.getDescripcionPasajero())
                .userId(pasajero.getUsuario().getId())
                .build();
    }
}
