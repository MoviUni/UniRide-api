package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.RutaEstadoRequestDTO;
import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.service.RutaService;
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
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.unirideapi.model.enums.EstadoRuta;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Rutas",
        description = "Endpoints para gestionar rutas." +
                "Permite crear rutas, buscar por varios filtros, actualizar estado, obtener historial y exportar PDFs."
)
@RestController
@RequestMapping("/rutas")
@PreAuthorize("hasAnyRole('CONDUCTOR', 'ADMIN')")
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

    @Operation(
            summary = "Obtener total de viajes de un conductor",
            description = "Devuelve el número total de viajes realizados por un conductor específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se obtuvo el total de viajes correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente para ver las estadísticas"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/conductor/{conductorId}/total")
    public ResponseEntity<?>obtenerTotalViajes(@PathVariable Integer conductorId) {
        int totalRutas = rutaService.obtenerTotalViajes(conductorId);
        return ResponseEntity.ok(totalRutas);
    }

    @Operation(
            summary = "Obtener frecuencia de viajes de un conductor",
            description = "Devuelve la frecuencia de viajes realizados por día de la semana."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frecuencia de viajes obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/conductor/{conductorId}/frecuencia")
    public ResponseEntity<Map<String, Integer>> obtenerFrecuenciaViajes(@PathVariable Integer conductorId) {
        Map<String, Integer> frecuencia = rutaService.obtenerFrecuenciaViajesPorPasajero(conductorId);
        return ResponseEntity.ok(frecuencia);
    }

    @Operation(
            summary = "Obtener rutas más frecuentes de un conductor",
            description = "Devuelve las rutas más utilizadas por un conductor ordenadas por frecuencia."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutas obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RutaFrecuenteResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron rutas frecuentes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/conductor/{conductorId}/RutaFrecuente")
    public ResponseEntity<List<RutaFrecuenteResponseDTO>> obtenerRutasMasFrecuentes(
            @PathVariable Integer conductorId) {
        return ResponseEntity.ok(rutaService.obtenerRutasMasFrecuentes(conductorId));
    }

    @Operation(
            summary = "Exportar historial de viajes de un conductor en PDF",
            description = "Genera y devuelve un archivo PDF con el historial de viajes de un conductor."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial exportado correctamente",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado, token inválido o no proporcionado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "No hay historial de viajes para este conductor"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al generar PDF")
    })
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

    @GetMapping("/historial/{rol}/{idUsuario}")
    public ResponseEntity<List<RutaResponseDTO>> obtenerHistorial(
            @PathVariable String rol,
            @PathVariable Integer idUsuario) {
        return ResponseEntity.ok(rutaService.obtenerHistorialViajes(idUsuario, rol));
    }

    // ⬇️ ADD: NUEVO — listar mis rutas
    @GetMapping("/mias/{idConductor}")
    public ResponseEntity<List<RutaResponseDTO>> misRutas(@PathVariable Integer idConductor) {
        return ResponseEntity.ok(rutaService.listarRutasDelConductor(idConductor));
    }

    // rutas activas (PROGRAMADO/CONFIRMADO y futuras)
    @GetMapping("/mias/activas/{idConductor}")
    public ResponseEntity<List<RutaResponseDTO>> misRutasActivas(@PathVariable Integer idConductor) {
        return ResponseEntity.ok(rutaService.listarRutasActivasDelConductor(idConductor));
    }

    // ⬇️ ADD: NUEVO — listar mis rutas por estado
    @GetMapping("/mias/{idConductor}/estado/{estado}")
    public ResponseEntity<List<RutaResponseDTO>> misRutasPorEstado(
            @PathVariable Integer idConductor,
            @PathVariable EstadoRuta estado) {
        return ResponseEntity.ok(rutaService.listarRutasDelConductorPorEstado(idConductor, estado));
    }

    // ⬇️ ADD: NUEVO — actualizar mi ruta (PUT, reemplazo completo, usando tu RutaRequestDTO)
    @PutMapping("/{idRuta}/conductor/{idConductor}")
    public ResponseEntity<RutaResponseDTO> actualizarRuta(
            @PathVariable Long idRuta,
            @PathVariable Integer idConductor,
            @RequestBody @Valid RutaRequestDTO dto) {
        return ResponseEntity.ok(rutaService.actualizarRutaFull(idRuta, idConductor, dto));
    }

    // ⬇️ ADD: NUEVO — eliminar mi ruta
    @DeleteMapping("/{idRuta}/conductor/{idConductor}")
    public ResponseEntity<Void> eliminarRuta(
            @PathVariable Long idRuta,
            @PathVariable Integer idConductor) {
        rutaService.eliminarRutaDeConductor(idRuta, idConductor);
        return ResponseEntity.noContent().build();
    }
}
