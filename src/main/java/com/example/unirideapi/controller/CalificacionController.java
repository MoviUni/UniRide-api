package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.CalificacionRequestDTO;
import com.example.unirideapi.dto.response.CalificacionResponseDTO;
import com.example.unirideapi.service.CalificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Crear una calificación",
            description = "Crea una nueva calificación para un pasajero o conductor."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Calificación creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalificacionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> createCalificacion(@RequestBody CalificacionRequestDTO calificacionRequestDTO) {

        CalificacionResponseDTO created = calificacionService.createCalificacion(calificacionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Obtener calificación por ID",
            description = "Devuelve los datos de una calificación específica por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calificación obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalificacionResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Calificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> getCalificacionById(@PathVariable Integer id) {
        CalificacionResponseDTO calificacion = calificacionService.findCalificacionById(id);
        return ResponseEntity.ok(calificacion);
    }

    @Operation(
            summary = "Obtener calificaciones por pasajero",
            description = "Devuelve todas las calificaciones asociadas a un pasajero específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalificacionResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado o sin calificaciones"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/pasajero/{idPasajero}")
    public ResponseEntity<List<CalificacionResponseDTO>> getCalificacionesByPasajero(@PathVariable Integer idPasajero) {
        List<CalificacionResponseDTO> calificaciones = calificacionService.findCalificacionByPasajeroId(idPasajero);
        return ResponseEntity.ok(calificaciones);
    }

    @Operation(
            summary = "Obtener calificaciones por conductor",
            description = "Devuelve todas las calificaciones asociadas a un conductor específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalificacionResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado o sin calificaciones"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/conductor/{idConductor}")
    public ResponseEntity<List<CalificacionResponseDTO>> getCalificacionesByConductor(@PathVariable Integer idConductor) {
        List<CalificacionResponseDTO> calificaciones = calificacionService.findCalificacionByConductorId(idConductor);
        return ResponseEntity.ok(calificaciones);
    }

    @Operation(
            summary = "Actualizar una calificación",
            description = "Actualiza los datos de una calificación existente por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calificación actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalificacionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Calificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> updateCalificacion(@PathVariable Integer id, @RequestBody CalificacionRequestDTO calificacionRequestDTO) {
        CalificacionResponseDTO updated = calificacionService.updateCalificacion(id, calificacionRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar una calificación",
            description = "Elimina una calificación específica por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Calificación eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Calificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalificacion(@PathVariable Integer id) {
        calificacionService.deleteCalificacion(id);
        return ResponseEntity.noContent().build();
    }


}
