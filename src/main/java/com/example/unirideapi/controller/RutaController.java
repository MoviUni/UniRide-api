package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.service.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<RutaResponseDTO> searchById(@PathVariable Long rutaId)
    {
        return ResponseEntity.ok(rutaService.searchById(rutaId));
    }

    @GetMapping("/origen")
    public ResponseEntity<List<RutaResponseDTO>> searchByOrigen(@RequestParam String origen)
    {
        return ResponseEntity.ok(rutaService.searchByOrigen(origen));
    }

    @GetMapping("/destino")
    public ResponseEntity<List<RutaResponseDTO>> searchByDestino(@RequestParam String destino)
    {
        return ResponseEntity.ok(rutaService.searchByDestino(destino));
    }

    @GetMapping("/hora")
    public ResponseEntity<List<RutaResponseDTO>> searchByHora(@RequestParam String hora)
    {
        return ResponseEntity.ok(rutaService.searchByHora(hora));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RutaResponseDTO>> searchByDestino(@RequestParam Map<String, String> params)
    {
        return ResponseEntity.ok(rutaService.searchBy(params.get("destino"), params.get("origen"), params.get("hora"), params.get("fecha")));
    }
}
