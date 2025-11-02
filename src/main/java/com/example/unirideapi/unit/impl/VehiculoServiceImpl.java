package com.example.unirideapi.unit.impl;

import com.example.unirideapi.dto.request.VehiculoRequestDTO;
import com.example.unirideapi.dto.response.VehiculoResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Vehiculo;
import com.example.unirideapi.repository.ConductorRepository;
import com.example.unirideapi.unit.VehiculoService;
import com.example.unirideapi.mapper.VehiculoMapper;
import com.example.unirideapi.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;
    private final VehiculoMapper vehiculoMapper;

    @Override
    @Transactional
    public VehiculoResponseDTO registrarVehiculo(Integer idConductor, VehiculoRequestDTO request) {
        // Validar que la capacidad sea positiva
        if (request.capacidad() <= 0) {
            throw new BusinessRuleException("La capacidad del vehículo debe ser mayor a cero");
        }

        // Verificar que la placa no exista (duplicada)
        if (vehiculoRepository.existsByPlaca(request.placa())) {
            throw new BusinessRuleException("La placa ya está registrada");
        }

        // Buscar conductor
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));

        // Validar que no tenga ya un vehículo
        if (conductor.getVehiculo() != null) {
            throw new BusinessRuleException("El conductor ya tiene un vehículo registrado");
        }

        // Crear y asignar vehículo
        Vehiculo vehiculo = vehiculoMapper.toEntity(request);
        vehiculo.setConductor(conductor);
        vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toDTO(vehiculo);
    }
    @Override
    @Transactional
    public VehiculoResponseDTO actualizarColorVehiculo(Integer idVehiculo, String nuevoColor) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado"));

        // No debe ser vacio
        if (nuevoColor == null || nuevoColor.trim().isEmpty()) {
            throw new BusinessRuleException("El color no puede estar vacío");
        }

        vehiculo.setColor(nuevoColor);
        vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toDTO(vehiculo);
    }
    @Override
    @Transactional(readOnly = true)
    public VehiculoResponseDTO findVehiculoByConductorId(Integer idConductor) {

        //Validar que el conductor exista
        var conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));

        // Buscar vehículo asociado a ese conductor
        var vehiculo = vehiculoRepository.findByConductorId(conductor.getIdConductor())
                .orElseThrow(() -> new ResourceNotFoundException("El conductor no tiene un vehículo registrado"));

        //Devolver DTO
        return vehiculoMapper.toDTO(vehiculo);
    }
}