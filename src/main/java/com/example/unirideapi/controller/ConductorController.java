package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.service.ConductorService;
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
        name = "Conductor",
        description = "Endpoints para gestionar conductores. " +
                "Permite listar, paginar, crear, actualizar, eliminar y ver perfiles de conductores."
)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CONDUCTOR', 'ADMIN')")
@RequestMapping("/conductor")
public class ConductorController {
    private final ConductorService conductorService;


    @Operation(
            summary = "Listar todos los conductores",
            description = "Devuelve una lista completa de todos los conductores registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conductores obtenidos correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConductorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ConductorResponseDTO>> listAll() {
        List<ConductorResponseDTO> conductores = conductorService.getAll();
        return new ResponseEntity<>(conductores, HttpStatus.OK);
    }

    @Operation(
            summary = "Paginar lista de conductores",
            description = "Obtiene una lista paginada de conductores, con tamaño y orden configurables mediante parámetros."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conductores paginados correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConductorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/page")
    public ResponseEntity<Page<ConductorResponseDTO>> paginate(@PageableDefault(size = 8, sort = "numDni") Pageable pageable) {
        Page<ConductorResponseDTO> page = conductorService.paginate(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Operation(
            summary = "Crear nuevo conductor",
            description = "Registra un nuevo conductor con la información proporcionada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conductor creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConductorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ConductorResponseDTO> create(@Valid @RequestBody ConductorRequestDTO conductorRequestDTO) {
        ConductorResponseDTO createdConductor = conductorService.create(conductorRequestDTO);
        return new ResponseEntity<>(createdConductor, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Actualizar datos de un conductor",
            description = "Actualiza la información de un conductor existente según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conductor actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConductorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConductorResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody ConductorRequestDTO conductorRequestDTO) {
        ConductorResponseDTO updatedConductor = conductorService.update(id, conductorRequestDTO);
        return new ResponseEntity<>(updatedConductor, HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar conductor",
            description = "Elimina un conductor del sistema según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conductor eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        conductorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Obtener perfil de conductor",
            description = "Devuelve los datos del perfil de un conductor específico según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConductorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: rol insuficiente"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConductorResponseDTO> getPerfil(@PathVariable Integer id) {
        ConductorResponseDTO dto = conductorService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
