package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.SolicitudEstadoRequestDTO;
import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.dto.response.SolicitudCardResponseDTO;
import com.example.unirideapi.dto.response.SolicitudEstadoResponseDTO;
import com.example.unirideapi.dto.response.SolicitudViajeResponseDTO;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import com.example.unirideapi.service.SolicitudViajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Solicitudes de Viaje",
        description = "Endpoints para gestionar solicitudes de viaje." +
                "Permite crear solicitudes, actualizar estado y listar solicitudes por ruta o usuario."
)
@RestController
@RequestMapping("/solicitudes")
@PreAuthorize("hasAnyRole('CONDUCTOR', 'PASAJERO', 'ADMIN')")
@RequiredArgsConstructor
public class SolicitudViajeController {
    private final SolicitudViajeService solicitudViajeService;


    @Operation(
        summary = "Crear una solicitud de viaje",
            description = "Crea una nueva solicitud de viaje para un conductor"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitud de viaje creada correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudViajeResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<SolicitudViajeResponseDTO> create(@Valid @RequestBody SolicitudViajeRequestDTO solicitudViajeRequestDTO) {
        return ResponseEntity.ok(solicitudViajeService.create(solicitudViajeRequestDTO));
    }

    @Operation(
            summary = "Obtener solicitudes de viaje",
            description = "Devuelve los datos de las solicitudes relacionados a cierto id ruta"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitudes obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudViajeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/ruta/{idRuta}")
    public ResponseEntity<List<SolicitudViajeResponseDTO>> getSolicitudesByRuta(@PathVariable Integer idRuta) {
        return ResponseEntity.ok(solicitudViajeService.findSolicitudesByRutaId(idRuta));
    }

    @Operation(
            summary = "Actualizar el estado de una solicitud",
            description = "Actualiza el estado de una solicitud por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estado de la solicitud actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudViajeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{idSolicitud}/estado")
    public ResponseEntity<SolicitudViajeResponseDTO> updateEstadoSolicitud(
            @PathVariable Integer idSolicitud,
            @RequestBody SolicitudEstadoRequestDTO request
    ) {
        var updated = solicitudViajeService.updateEstadoSolicitud(idSolicitud, request.estado());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{idSolicitud}/cancelar")
    public ResponseEntity<SolicitudViajeResponseDTO> cancelarSolicitud(
            @PathVariable Integer idSolicitud
    ) {
        var updated = solicitudViajeService.cancelSolicitud(idSolicitud);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Obtener los estados de la solicitudes de viaje",
            description = "Obtener los estados de las solicitudes de viaje relacionadas a un id de pasajero"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitudes obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudEstadoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<SolicitudViajeResponseDTO>> searchByUsuario(@RequestParam Integer id) {
        return ResponseEntity.ok(solicitudViajeService.searchByUsuario(id));
    }

    @GetMapping("/info")
    public ResponseEntity<List<SolicitudCardResponseDTO>> searchInfo() {
        return ResponseEntity.ok(solicitudViajeService.searchInfo());
    }

}

