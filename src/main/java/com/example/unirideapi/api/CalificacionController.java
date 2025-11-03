package com.example.unirideapi.api;

import com.example.unirideapi.dto.request.CalificacionRequestDTO;
import com.example.unirideapi.dto.response.CalificacionResponseDTO;
import com.example.unirideapi.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calificaciones")
public class CalificacionController {
    private final CalificacionService calificacionService;

    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> createCalificacion(@RequestBody CalificacionRequestDTO calificacionRequestDTO) {

        CalificacionResponseDTO created = calificacionService.createCalificacion(calificacionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> getCalificacionById(@PathVariable Integer id) {
        CalificacionResponseDTO calificacion = calificacionService.findCalificacionById(id);
        return ResponseEntity.ok(calificacion);
    }

    @GetMapping("/pasajero/{idPasajero}")
    public ResponseEntity<List<CalificacionResponseDTO>> getCalificacionesByPasajero(@PathVariable Integer idPasajero) {
        List<CalificacionResponseDTO> calificaciones = calificacionService.findCalificacionByPasajeroId(idPasajero);
        return ResponseEntity.ok(calificaciones);
    }

    @GetMapping("/conductor/{idConductor}")
    public ResponseEntity<List<CalificacionResponseDTO>> getCalificacionesByConductor(@PathVariable Integer idConductor) {
        List<CalificacionResponseDTO> calificaciones = calificacionService.findCalificacionByConductorId(idConductor);
        return ResponseEntity.ok(calificaciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> updateCalificacion(@PathVariable Integer id, @RequestBody CalificacionRequestDTO calificacionRequestDTO) {
        CalificacionResponseDTO updated = calificacionService.updateCalificacion(id, calificacionRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalificacion(@PathVariable Integer id) {
        calificacionService.deleteCalificacion(id);
        return ResponseEntity.noContent().build();
    }


}
