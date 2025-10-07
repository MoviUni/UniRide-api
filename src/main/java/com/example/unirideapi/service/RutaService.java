package com.example.unirideapi.service;

import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.model.enums.Estado;
import org.springframework.transaction.annotation.Transactional;

public interface RutaService {
    RutaResponseDTO updateEstadoRuta(Integer idRuta, Estado nuevoEstado);

}
