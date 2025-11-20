package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.RutaEstadoRequestDTO;
import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaCardResponseDTO;
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
@PreAuthorize("hasAnyRole('CONDUCTOR', 'ADMIN', 'PASAJERO')")
@RequiredArgsConstructor
public class RutaController {
    private final RutaService rutaService;

    @Operation(
            summary = "Registrar una nueva ruta",
            description = "Crea una nueva ruta de viaje proporcionando los datos necesarios en el cuerpo de la solicitud."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de la ruta inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<RutaResponseDTO> create(@Valid @RequestBody RutaRequestDTO rutaRequestDTO) {
        return ResponseEntity.ok(rutaService.create(rutaRequestDTO));
    }

    @Operation(
            summary = "Listar rutas disponibles",
            description = "Devuelve una lista de todas las rutas que actualmente se encuentran disponibles para los pasajeros."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutas disponibles obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<RutaResponseDTO>> searchDisponible()
    {
        return ResponseEntity.ok(rutaService.searchByDisponible());
    }

    @Operation(
            summary = "Buscar ruta por ID",
            description = "Obtiene los detalles de una ruta específica según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta encontrada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{rutaId}")
    public ResponseEntity<RutaResponseDTO> searchById(@PathVariable Long rutaId)
    {
        return ResponseEntity.ok(rutaService.searchById(rutaId));
    }

    @Operation(
            summary = "Buscar rutas por origen",
            description = "Devuelve una lista de rutas que parten del origen especificado en el parámetro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutas encontradas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "El parámetro 'origen' es inválido o está vacío"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron rutas con ese origen"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/origen")
    public ResponseEntity<List<RutaResponseDTO>> searchByOrigen(@RequestParam String origen)
    {
        return ResponseEntity.ok(rutaService.searchByOrigen(origen));
    }

    @Operation(
            summary = "Buscar rutas por destino",
            description = "Devuelve una lista de rutas que tienen como destino el valor especificado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutas encontradas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "El parámetro 'destino' es inválido o está vacío"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron rutas con ese destino"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/destino")
    public ResponseEntity<List<RutaResponseDTO>> searchByDestino(@RequestParam String destino)
    {
        return ResponseEntity.ok(rutaService.searchByDestino(destino));
    }

    @GetMapping("/info")
    public ResponseEntity<List<RutaCardResponseDTO>> searchInfo()
    {
        return ResponseEntity.ok(rutaService.searchInfo());
    }

    @Operation(
            summary = "Buscar rutas por hora de salida",
            description = "Devuelve una lista de rutas que coinciden con la hora de salida especificada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutas encontradas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "El parámetro 'hora' es inválido o está vacío"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron rutas con esa hora de salida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/hora")
    public ResponseEntity<List<RutaResponseDTO>> searchByHora(@RequestParam String hora)
    {
        return ResponseEntity.ok(rutaService.searchByHora(hora));
    }

    @Operation(
            summary = "Buscar rutas combinando criterios",
            description = "Permite buscar rutas filtrando por origen, destino, hora y fecha. " +
                    "Todos los parámetros son opcionales y se envían como query params."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutas filtradas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron rutas con los criterios especificados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(
            summary = "Actualizar el estado de una ruta",
            description = "Permite modificar el estado de una ruta existente (por ejemplo: ACTIVA, FINALIZADA, CANCELADA)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de la ruta actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Estado inválido o ruta inexistente"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{idRuta}/estado")
    public ResponseEntity<RutaResponseDTO> updateEstadoRuta(
            @PathVariable Integer idRuta,
            @RequestBody RutaEstadoRequestDTO request
    ) {
        var updated = rutaService.updateEstadoRuta(idRuta, request.estado());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Obtener historial de rutas",
            description = "Devuelve el historial de viajes de un usuario según su rol (PASAJERO o CONDUCTOR)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Rol o ID inválido"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontró historial para el usuario indicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/historial/{rol}/{idUsuario}")
    public ResponseEntity<List<RutaResponseDTO>> obtenerHistorial(
            @PathVariable String rol,
            @PathVariable Integer idUsuario) {
        return ResponseEntity.ok(rutaService.obtenerHistorialViajes(idUsuario, rol));
    }

    // ⬇️ ADD: NUEVO — listar mis rutas
    @Operation(
            summary = "Listar rutas creadas por un conductor",
            description = "Devuelve todas las rutas publicadas por el conductor especificado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutas del conductor obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID del conductor inválido"),
            @ApiResponse(responseCode = "404", description = "El conductor no tiene rutas registradas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(
            summary = "Listar rutas del conductor filtradas por estado",
            description = "Permite listar las rutas de un conductor filtrando por estado (por ejemplo: ACTIVA, CANCELADA, FINALIZADA)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutas filtradas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Estado o ID del conductor inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontraron rutas con ese estado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/mias/{idConductor}/estado/{estado}")
    public ResponseEntity<List<RutaResponseDTO>> misRutasPorEstado(
            @PathVariable Integer idConductor,
            @PathVariable EstadoRuta estado) {
        return ResponseEntity.ok(rutaService.listarRutasDelConductorPorEstado(idConductor, estado));
    }

    // PUT — ahora con confirmación opcional (?confirmar=true)
    @Operation(
            summary = "Actualizar los datos de una ruta",
            description = "Permite modificar los datos completos de una ruta asociada a un conductor. " +
                    "Se puede usar el parámetro 'confirmar=true' para confirmar la actualización antes de aplicarla definitivamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o parámetros incorrectos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Ruta o conductor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{idRuta}/conductor/{idConductor}")
    public ResponseEntity<RutaResponseDTO> actualizarRuta(
            @PathVariable Long idRuta,
            @PathVariable Integer idConductor,
            @RequestParam(name = "confirmar", defaultValue = "false") boolean confirmar,
            @RequestBody @Valid RutaRequestDTO dto) {

        return ResponseEntity.ok(
                rutaService.actualizarRutaFull(idRuta, idConductor, dto, confirmar)
        );
    }

    // DELETE — ahora con confirmación opcional (?confirmar=true)
    @Operation(
            summary = "Eliminar una ruta",
            description = "Permite eliminar una ruta asociada a un conductor. Se puede usar el parámetro 'confirmar=true' para confirmar la eliminación definitiva."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ruta eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos o eliminación no confirmada"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Ruta o conductor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{idRuta}/conductor/{idConductor}")
    public ResponseEntity<Void> eliminarRuta(
            @PathVariable Long idRuta,
            @PathVariable Integer idConductor,
            @RequestParam(name = "confirmar", defaultValue = "false") boolean confirmar) {

        rutaService.eliminarRutaDeConductor(idRuta, idConductor, confirmar);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Publicar una nueva ruta",
            description = "Permite a un conductor crear y publicar una nueva ruta de viaje con la información correspondiente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta publicada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RutaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de la ruta inválidos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/publicar")
    public ResponseEntity<RutaResponseDTO> publicar(@RequestBody @Valid RutaRequestDTO dto) {
        return ResponseEntity.ok(rutaService.publicarRutaComoConductor(dto));
    }

}
