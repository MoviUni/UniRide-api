package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.model.SolicitudViaje;
import com.example.unirideapi.service.RutaService;
import com.example.unirideapi.service.SolicitudViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudViajeController {
    private final SolicitudViajeService solicitudViajeService;

    @PostMapping
    public ResponseEntity<SolicitudViajeResponseDTO> create(@Valid @RequestBody SolicitudViajeRequestDTO solicitudViajeRequestDTO) {
        return ResponseEntity.ok(solicitudViajeService.create(solicitudViajeRequestDTO));
    }
}
