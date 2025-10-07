package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.PagoRequestDTO;
import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.PagoResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.repository.PagoRepository;
import com.example.unirideapi.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService pagoService;
    @PostMapping
    public ResponseEntity<PagoResponseDTO> create(@Valid @RequestBody PagoRequestDTO pagoRequestDTO) {
        return ResponseEntity.ok(pagoService.create(pagoRequestDTO));
    }
}
