package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.RutaEstadoRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {
    private final RutaService rutaService;

    @PatchMapping("/{idRuta}/estado")
    public ResponseEntity<RutaResponseDTO> updateEstadoRuta(
            @PathVariable Integer idRuta,
            @RequestBody RutaEstadoRequestDTO request
    ) {
        var updated = rutaService.updateEstadoRuta(idRuta, request.estado());
        return ResponseEntity.ok(updated);
    }
}
