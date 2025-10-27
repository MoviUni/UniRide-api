package com.example.unirideapi.unit;

import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.PasajeroResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PasajeroService {
    List<PasajeroResponseDTO> getAll();
    List<PasajeroResponseDTO> findAll();
    Page<PasajeroResponseDTO> paginate(Pageable pageable);
    PasajeroResponseDTO create(PasajeroRequestDTO pasajeroRequestDTO);
    PasajeroResponseDTO update(Integer id,  PasajeroRequestDTO updatePasajeroRequestDTO);
    PasajeroResponseDTO findById(Integer id);
    void delete(Integer id);
}
