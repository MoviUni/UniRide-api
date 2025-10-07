package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.exception.BadRequestException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.ConductorMapper;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.repository.ConductorRepository;
import com.example.unirideapi.service.ConductorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConductorServiceImpl implements ConductorService {
    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ConductorResponseDTO>getAll() {
        List<Conductor> conductores = conductorRepository.findAll();
        return conductores.stream().map(conductorMapper::toDTO).toList();
    }

    @Override
    public List<ConductorResponseDTO> findAll() {
        List<Conductor> conductores = conductorRepository.findAll();
        return conductores.stream().map(conductorMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ConductorResponseDTO> paginate(Pageable pageable) {
        Page<Conductor> conductores = conductorRepository.findAll(pageable);
        return conductores.map(conductorMapper::toDTO);
    }

    @Transactional
    @Override
    public ConductorResponseDTO create(ConductorRequestDTO conductorRequestDTO) {
        List<Conductor> DniExistente = conductorRepository.findByDni(conductorRequestDTO.dni());
        if (!DniExistente.isEmpty()) {
            throw new BadRequestException("Ya existe un conductor con el mismo DNI");
        }
        Conductor conductor = conductorMapper.toEntity(conductorRequestDTO);
        conductor.setCreatedAt(LocalDateTime.now());
        conductor = conductorRepository.save(conductor);
        return conductorMapper.toDTO(conductor);
    }

    @Transactional
    @Override
    public ConductorResponseDTO update(Integer id, ConductorRequestDTO updateConductorRequestDTO) {
        Conductor conductorFromDb = conductorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + id + " no fue encontrado"));

        // Validar número de colegiatura duplicado
        List<Conductor> existentes = conductorRepository.findByDni(updateConductorRequestDTO.dni());
        boolean existeOtro = existentes.stream()
                .anyMatch(existingConductor -> !existingConductor.getIdConductor().equals(id));
        if (existeOtro) {
            throw new BadRequestException("Ya existe un conductor con el mismo DNI");
        }

        // Actualizar campos básicos
        conductorFromDb.setNombre(updateConductorRequestDTO.nombre());
        conductorFromDb.setApellido(updateConductorRequestDTO.apellido());
        conductorFromDb.setDni(updateConductorRequestDTO.dni());
        conductorFromDb.setEdad(updateConductorRequestDTO.edad());
        conductorFromDb.setDescripcionConductor(updateConductorRequestDTO.descripcionConductor());
        conductorFromDb.setUpdatedAt(LocalDateTime.now());

        // Guardar cambios
        conductorFromDb = conductorRepository.save(conductorFromDb);
        return conductorMapper.toDTO(conductorFromDb);
    }

    @Override
    public ConductorResponseDTO findById(Integer id) {
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID "+id+" no fue encontrado"));
        return conductorMapper.toDTO(conductor);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID "+id+" no fue encontrado"));
        conductorRepository.delete(conductor);

    }
}
