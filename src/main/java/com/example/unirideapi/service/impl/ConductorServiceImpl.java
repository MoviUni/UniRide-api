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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public ConductorResponseDTO update(Integer id, ConductorRequestDTO req) {

        // 1. Buscar conductor
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El conductor con ID " + id + " no fue encontrado"));

        // 2. Validaciones de DNI duplicado
        if (req.dni() != null) {
            boolean existeOtroConductor = conductorRepository
                    .existsByDniAndIdConductorNot(req.dni(), id);

            if (existeOtroConductor) {
                throw new BadRequestException("Ya existe un conductor registrado con ese DNI");
            }
        }

        // 3. Actualizar campos simples (solo si no son null)
        if (req.nombre() != null) conductor.setNombre(req.nombre());
        if (req.apellido() != null) conductor.setApellido(req.apellido());
        if (req.dni() != null) conductor.setDni(req.dni());
        if (req.edad() != null) conductor.setEdad(req.edad());

        // Marca fecha actualización
        conductor.setUpdatedAt(LocalDateTime.now());

        // 4. Guardar cambios
        conductor = conductorRepository.save(conductor);

        // 5. Convertir a DTO
        return conductorMapper.toDTO(conductor);
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
