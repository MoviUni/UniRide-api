package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.SolicitudEstadoRequestDTO;
import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.service.SolicitudViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudViajeController {
    private final SolicitudViajeService solicitudViajeService;

    @PostMapping
    public ResponseEntity<SolicitudViajeResponseDTO> create(@Valid @RequestBody SolicitudViajeRequestDTO solicitudViajeRequestDTO) {
        return ResponseEntity.ok(solicitudViajeService.create(solicitudViajeRequestDTO));
    }
    @PatchMapping("/{idSolicitud}/estado")
    public ResponseEntity<SolicitudViajeResponseDTO> actualizarEstadoSolicitud(
            @PathVariable Integer idSolicitud,
            @RequestBody SolicitudEstadoRequestDTO request
    ) {
        var updated = solicitudViajeService.updateEstadoSolicitud(idSolicitud, request.estado());
        return ResponseEntity.ok(updated);
    }
}
