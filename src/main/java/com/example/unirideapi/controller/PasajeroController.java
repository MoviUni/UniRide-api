package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.PasajeroResponseDTO;
import com.example.unirideapi.service.PasajeroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Pasajero",
        description = "Endpoints para gestionar pasajeros" +
                "Permite listar, paginar, crear, actualizar, eliminar y obtener el perfil de un pasajero."
)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PASAJERO', 'ADMIN')")
@RequestMapping("/pasajero")
public class PasajeroController {
    private final PasajeroService pasajeroService;


    @Operation(
            summary = "Listar todos los pasajeros",
            description = "Devuelve una lista completa de los pasajeros registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PasajeroResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PasajeroResponseDTO>> listAll() {
        List<PasajeroResponseDTO> pasajeros = pasajeroService.getAll();
        return new ResponseEntity<>(pasajeros, HttpStatus.OK);
    }

    @Operation(
            summary = "Paginar pasajeros",
            description = "Obtiene una página de pasajeros con soporte para tamaño y ordenamiento configurables."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de pasajeros obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/page")
    public ResponseEntity<Page<PasajeroResponseDTO>> paginate(@PageableDefault(size = 8, sort = "numDni") Pageable pageable) {
        Page<PasajeroResponseDTO> page = pasajeroService.paginate(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Operation(
            summary = "Registrar un nuevo pasajero",
            description = "Permite registrar un nuevo pasajero proporcionando la información requerida en el cuerpo de la solicitud."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pasajero registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PasajeroResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos del pasajero inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PasajeroResponseDTO> create(@Valid @RequestBody PasajeroRequestDTO pasajeroRequestDTO) {
        PasajeroResponseDTO createdPasajero = pasajeroService.create(pasajeroRequestDTO);
        return new ResponseEntity<>(createdPasajero, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Actualizar un pasajero existente",
            description = "Actualiza la información de un pasajero según el ID proporcionado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pasajero actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PasajeroResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody PasajeroRequestDTO pasajeroRequestDTO) {
        PasajeroResponseDTO updatedPasajero = pasajeroService.update(id, pasajeroRequestDTO);
        return new ResponseEntity<>(updatedPasajero, HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar un pasajero",
            description = "Elimina un pasajero del sistema según el ID especificado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pasajero eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pasajeroService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Obtener perfil de pasajero",
            description = "Devuelve la información del perfil de un pasajero según el ID proporcionado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PasajeroResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> getPerfil(@PathVariable Integer id) {
        PasajeroResponseDTO dto = pasajeroService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
