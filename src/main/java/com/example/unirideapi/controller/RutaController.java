package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.service.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {
    private final RutaService rutaService;

    @PostMapping
    public ResponseEntity<RutaResponseDTO> create(@Valid @RequestBody RutaRequestDTO rutaRequestDTO) {
        return ResponseEntity.ok(rutaService.create(rutaRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<RutaResponseDTO>> findAll()
    {
        return ResponseEntity.ok(rutaService.findAll());
    }

    @GetMapping("/{rutaId}")
    public ResponseEntity<RutaResponseDTO> searchById(Long rutaId)
    {
        return ResponseEntity.ok(rutaService.searchById(rutaId));
    }

    @GetMapping("/{origen}")
    public ResponseEntity<List<RutaResponseDTO>> searchByOrigen(String origen)
    {
        return ResponseEntity.ok(rutaService.searchByOrigen(origen));
    }

    @GetMapping("/{destino}")
    public ResponseEntity<List<RutaResponseDTO>> searchByDestino(String destino)
    {
        return ResponseEntity.ok(rutaService.searchByDestino(destino));
    }
}
