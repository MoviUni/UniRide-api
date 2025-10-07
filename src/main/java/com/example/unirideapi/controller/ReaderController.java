package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.PasajeroResponseDTO;
import com.example.unirideapi.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pasajeros")
@RequiredArgsConstructor
public class ReaderController {
    private final PasajeroService pasajeroService;

    @PostMapping
    public ResponseEntity<PasajeroResponseDTO> create(@Valid @RequestBody PasajeroRequestDTO dto) {
        return ResponseEntity.ok(pasajeroService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<PasajeroResponseDTO>> findAll() {
        return ResponseEntity.ok(pasajeroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pasajeroService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        pasajeroService.delete(id);
        return ResponseEntity.ok("Lector eliminado correctamente");
    }
}
