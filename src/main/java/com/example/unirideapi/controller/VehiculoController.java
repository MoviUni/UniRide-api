package com.example.unirideapi.controller;
import com.example.unirideapi.dto.request.VehiculoColorRequestDTO;
import com.example.unirideapi.dto.request.VehiculoRequestDTO;
import com.example.unirideapi.dto.response.VehiculoResponseDTO;
import com.example.unirideapi.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Vehículos", description = "Endpoints para gestionar vehículos de conductores.")
@RestController
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CONDUCTOR','ADMIN')")
public class VehiculoController {
    private final VehiculoService vehiculoService;

    @Operation(
            summary = "Registrar un vehículo para un conductor",
            description = "Permite registrar un nuevo vehículo asociado a un conductor específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehículo registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/conductor/{idConductor}")
    public ResponseEntity<VehiculoResponseDTO> registrarVehiculo(
            @PathVariable Integer idConductor,
            @RequestBody VehiculoRequestDTO request
    ) {
        var response = vehiculoService.registrarVehiculo(idConductor, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar el color de un vehículo",
            description = "Permite actualizar el color de un vehículo existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Color del vehículo actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o color no permitido"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{idVehiculo}/color")
    public ResponseEntity<VehiculoResponseDTO> actualizarColorVehiculo(
            @PathVariable Integer idVehiculo,
            @RequestBody VehiculoColorRequestDTO request
    ) {
        var updated = vehiculoService.actualizarColorVehiculo(idVehiculo, request.color());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Obtener el vehículo de un conductor",
            description = "Devuelve la información del vehículo registrado por un conductor específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado para el conductor"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/conductor/{idConductor}")
    public ResponseEntity<VehiculoResponseDTO> getVehiculoByConductor(@PathVariable Integer idConductor) {
        var response = vehiculoService.findVehiculoByConductorId(idConductor);
        return ResponseEntity.ok(response);
    }
}

