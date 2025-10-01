package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RutaServiceImpl implements RutaService {
    private final RutaRepository rutaRepository;
    private  final RutaMapper rutaMapper;

    @Override
    public RutaResponseDTO create(RutaRequestDTO rutaRequestDTO) {
        Ruta ruta = rutaMapper.toEntity(rutaRequestDTO);
        return rutaMapper.toDTO( rutaRepository.save(ruta));
    }

    @Override
    public RutaResponseDTO searchById(Long id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ruta no existe"));
        return rutaMapper.toDTO(ruta);
    }
    @Override
    public List<RutaResponseDTO> findAll(){
        return rutaRepository.findAll().stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<RutaResponseDTO> searchByOrigen(String origen) {
        return rutaRepository.searchByOrigen(origen).stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RutaResponseDTO> searchByDestino(String destino) {
        return rutaRepository.searchByDestino(destino).stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
