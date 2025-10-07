package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.service.ConductorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/conductores")
@RequiredArgsConstructor
public class ConductorController {
    private final ConductorService conductorService;

    @PostMapping
    public ResponseEntity<ConductorResponseDTO> create(@Valid @RequestBody ConductorRequestDTO dto) {
        return ResponseEntity.ok(conductorService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ConductorResponseDTO>> findAll() {
        return ResponseEntity.ok(conductorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConductorResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(conductorService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        conductorService.delete(id);
        return ResponseEntity.ok("Conductor eliminado correctamente");
    }
}
