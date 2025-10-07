package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.SolicitudEstadoRequestDTO;
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
}

