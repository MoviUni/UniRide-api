package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;

import java.util.List;


public interface RutaService {
    RutaResponseDTO create(RutaRequestDTO rutaRequestDTO);
    RutaResponseDTO searchById(Long id);
    public List<RutaResponseDTO> searchByOrigen(String origen);
    public List<RutaResponseDTO> searchByDestino(String destino);
    public List<RutaResponseDTO> findAll();
}
