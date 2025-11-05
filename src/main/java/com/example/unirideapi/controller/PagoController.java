package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.PagoRequestDTO;
import com.example.unirideapi.dto.response.PagoResponseDTO;
import com.example.unirideapi.service.PagoService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Pagos",
        description = "Endpoints para gestionar pagos dentro de la app. " +
                "Actualmente permite registrar un nuevo pago."
)
@RestController
@RequestMapping("/pagos")
@PreAuthorize("hasAnyRole('CONDUCTOR', 'PASAJERO', 'ADMIN')")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService pagoService;

    @Operation(
            summary = "Registrar un nuevo pago",
            description = "Permite registrar un pago realizado por un pasajero o asociado a un viaje específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos del pago inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PagoResponseDTO> create(@Valid @RequestBody PagoRequestDTO pagoRequestDTO) {
        return ResponseEntity.ok(pagoService.create(pagoRequestDTO));
    }
}
