package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;

import java.util.List;


public interface RutaService {
    RutaResponseDTO create(RutaRequestDTO rutaRequestDTO);
    RutaResponseDTO searchById(Long id);
    List<RutaResponseDTO> searchByOrigen(String origen);
    List<RutaResponseDTO> searchByDestino(String destino);
    List<RutaResponseDTO> findAll();
}
