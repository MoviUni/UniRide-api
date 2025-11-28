package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.PasajeroResponseDTO;
import com.example.unirideapi.exception.BadRequestException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.PasajeroMapper;
import com.example.unirideapi.model.Pasajero;
import com.example.unirideapi.repository.PasajeroRepository;
import com.example.unirideapi.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasajeroServiceImpl implements PasajeroService {
    private final PasajeroRepository pasajeroRepository;
    private final PasajeroMapper pasajeroMapper;

    @Transactional(readOnly = true)
    @Override
    public List<PasajeroResponseDTO>getAll() {
        List<Pasajero> pasajeros = pasajeroRepository.findAll();
        return pasajeros.stream().map(pasajeroMapper::toDTO).toList();
    }

    @Override
    public List<PasajeroResponseDTO> findAll() {
        List<Pasajero> pasajeros = pasajeroRepository.findAll();
        return pasajeros.stream().map(pasajeroMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PasajeroResponseDTO> paginate(Pageable pageable) {
        Page<Pasajero> pasajeros = pasajeroRepository.findAll(pageable);
        return pasajeros.map(pasajeroMapper::toDTO);
    }

    @Transactional
    @Override
    public PasajeroResponseDTO create(PasajeroRequestDTO pasajeroRequestDTO) {
        List<Pasajero> DniExistente = pasajeroRepository.findByDni(pasajeroRequestDTO.dni());
        if (!DniExistente.isEmpty()) {
            throw new BadRequestException("Ya existe un pasajero con el mismo DNI");
        }
        Pasajero pasajero = pasajeroMapper.toEntity(pasajeroRequestDTO);
        pasajero.setCreatedAt(LocalDateTime.now());
        pasajero = pasajeroRepository.save(pasajero);
        return pasajeroMapper.toDTO(pasajero);
    }

    @Transactional
    @Override
    public PasajeroResponseDTO update(Integer id, PasajeroRequestDTO updatePasajeroRequestDTO) {
        Pasajero pasajeroFromDb = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El pasajero con ID " + id + " no fue encontrado"));

        // Validar número de colegiatura duplicado
        List<Pasajero> existentes = pasajeroRepository.findByDni(updatePasajeroRequestDTO.dni());
        boolean existeOtro = existentes.stream()
                .anyMatch(existingPasajero -> !existingPasajero.getIdPasajero().equals(id));
        if (existeOtro) {
            throw new BadRequestException("Ya existe un pasajero con el mismo DNI");
        }
        if (updatePasajeroRequestDTO.dni() != null &&
                pasajeroRepository.existsByDniAndIdPasajeroNot(updatePasajeroRequestDTO.dni(), id)) {
            throw new BadRequestException("Ya existe un pasajero con el mismo DNI");
        }

//        if (updatePasajeroRequestDTO.userId() != null &&
//                pasajeroRepository.existsByUsuario_IdUsuarioAndIdPasajeroNot(updatePasajeroRequestDTO.userId().intValue(), id)) {
//            throw new BadRequestException("Ese usuario ya está asignado a otro pasajero");
//        }
//
//        if (updatePasajeroRequestDTO.vehiculoId() != null &&
//                pasajeroRepository.existsByVehiculo_IdVehiculoAndIdPasajeroNot(updatePasajeroRequestDTO.vehiculoId(), id)) {
//            throw new BadRequestException("Ese vehículo ya está asignado a otro pasajero");
//        }

        // Actualizar campos básicos
        pasajeroFromDb.setNombre(updatePasajeroRequestDTO.nombre());
        pasajeroFromDb.setApellido(updatePasajeroRequestDTO.apellido());
        pasajeroFromDb.setDni(updatePasajeroRequestDTO.dni());
        pasajeroFromDb.setEdad(updatePasajeroRequestDTO.edad());
        pasajeroFromDb.setUpdatedAt(LocalDateTime.now());

        // Guardar cambios
        pasajeroFromDb = pasajeroRepository.save(pasajeroFromDb);
        return pasajeroMapper.toDTO(pasajeroFromDb);
    }

    @Override
    public PasajeroResponseDTO findById(Integer id) {
        Pasajero pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El pasajero con ID "+id+" no fue encontrado"));
        return pasajeroMapper.toDTO(pasajero);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Pasajero pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El pasajero con ID "+id+" no fue encontrado"));
        pasajeroRepository.delete(pasajero);

    }
}
