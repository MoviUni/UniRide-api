package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.PagoRequestDTO;
import com.example.unirideapi.dto.response.PagoResponseDTO;
import com.example.unirideapi.service.PagoService;
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
    @PostMapping
    public ResponseEntity<PagoResponseDTO> create(@Valid @RequestBody PagoRequestDTO pagoRequestDTO) {
        return ResponseEntity.ok(pagoService.create(pagoRequestDTO));
    }
}
