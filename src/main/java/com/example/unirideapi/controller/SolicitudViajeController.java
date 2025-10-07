package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.SolicitudEstadoRequestDTO;
import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.service.SolicitudViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.service.impl.SolicitudViajeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    private final SolicitudViajeImpl solicitudViajeService;

    @GetMapping("/ruta/{idRuta}")
    public ResponseEntity<List<SolicitudViajeResponseDTO>> getSolicitudesByRuta(@PathVariable Integer idRuta) {
        return ResponseEntity.ok(solicitudViajeService.findSolicitudesByRutaId(idRuta));
    }

    @PatchMapping("/{idSolicitud}/estado")
    public ResponseEntity<SolicitudViajeResponseDTO> updateEstadoSolicitud(
            @PathVariable Integer idSolicitud,
            @RequestBody SolicitudEstadoRequestDTO request
    ) {
        var updated = solicitudViajeService.updateEstadoSolicitud(idSolicitud, request.estado());
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<SolicitudViajeResponseDTO>> searchByUsuario(@RequestParam Integer id) {
        return ResponseEntity.ok(solicitudViajeService.searchByUsuario(id));
    }

}

