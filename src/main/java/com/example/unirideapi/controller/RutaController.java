package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.RutaEstadoRequestDTO;
import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.service.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<RutaResponseDTO>> searchDisponible()
    {
        return ResponseEntity.ok(rutaService.searchByDisponible());
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
    public ResponseEntity<List<RutaResponseDTO>> searchByDestino(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(rutaService.searchBy(params.get("destino"), params.get("origen"), params.get("hora"), params.get("fecha")));
    }
    @GetMapping("/conductor/{conductorId}/total")
    public ResponseEntity<?>obtenerTotalViajes(@PathVariable Integer conductorId) {
        int totalRutas = rutaService.obtenerTotalViajes(conductorId);
        return ResponseEntity.ok(totalRutas);
        //return ResponseEntity.ok(Map.of("totalViajes", totalViajes));
    }

    @GetMapping("/conductor/{conductorId}/frecuencia")
    public ResponseEntity<Map<String, Integer>> obtenerFrecuenciaViajes(@PathVariable Integer conductorId) {
        Map<String, Integer> frecuencia = rutaService.obtenerFrecuenciaViajesPorPasajero(conductorId);
        return ResponseEntity.ok(frecuencia);
    }

//    @GetMapping("/conductor/{conductorId}/RutaFrecuente")
//    public ResponseEntity<List<RutaFrecuenteResponseDTO>> obtenerRutasMasFrecuentes(
//            @PathVariable Integer conductorId) {
//
//        List<RutaFrecuenteResponseDTO> rutas = rutaService.obtenerRutasMasFrecuentes(conductorId);
//        return ResponseEntity.ok(rutas);
//    }

    @GetMapping("/conductor/{conductorId}/RutaFrecuente")
    public ResponseEntity<List<RutaFrecuenteResponseDTO>> obtenerRutasMasFrecuentes(
            @PathVariable Integer conductorId) {
        return ResponseEntity.ok(rutaService.obtenerRutasMasFrecuentes(conductorId));
    }


    @GetMapping("/conductor/{conductorId}/historial/pdf")
    public ResponseEntity<byte[]> exportarHistorialPdf(@PathVariable Integer conductorId) {
        byte[] pdfBytes = rutaService.exportarHistorialPdf(conductorId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=historial_conductor_" + conductorId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    @PatchMapping("/{idRuta}/estado")
    public ResponseEntity<RutaResponseDTO> updateEstadoRuta(
            @PathVariable Integer idRuta,
            @RequestBody RutaEstadoRequestDTO request
    ) {
        var updated = rutaService.updateEstadoRuta(idRuta, request.estado());
        return ResponseEntity.ok(updated);
    }
}
