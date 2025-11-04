package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConductorService {
    List<ConductorResponseDTO> getAll();
    List<ConductorResponseDTO> findAll();
    Page<ConductorResponseDTO> paginate(Pageable pageable);
    ConductorResponseDTO create(ConductorRequestDTO conductorRequestDTO);
    ConductorResponseDTO update(Integer id, ConductorRequestDTO updateConductorRequestDTO);
    ConductorResponseDTO findById(Integer id);
    void delete(Integer id);
}
