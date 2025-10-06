package com.example.unirideapi.controller;
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
}
