package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.SolicitudEstadoRequestDTO;
import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudEstadoResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.unit.SolicitudViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@PreAuthorize("hasAnyRole('CONDUCTOR', 'PASAJERO', 'ADMIN')")
@RequiredArgsConstructor
public class SolicitudViajeController {
    private final SolicitudViajeService solicitudViajeService;

    @PostMapping
    public ResponseEntity<SolicitudViajeResponseDTO> create(@Valid @RequestBody SolicitudViajeRequestDTO solicitudViajeRequestDTO) {
        return ResponseEntity.ok(solicitudViajeService.create(solicitudViajeRequestDTO));
    }

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
    public ResponseEntity<List<SolicitudEstadoResponseDTO>> searchByUsuario(@RequestParam Integer id) {
        return ResponseEntity.ok(solicitudViajeService.searchByUsuario(id));
    }

}

