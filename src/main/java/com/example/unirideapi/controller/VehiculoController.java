package com.example.unirideapi.controller;
import com.example.unirideapi.dto.request.RutaEstadoRequestDTO;
import com.example.unirideapi.dto.request.VehiculoColorRequestDTO;
import com.example.unirideapi.dto.request.VehiculoRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.dto.response.VehiculoResponseDTO;
import com.example.unirideapi.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {
    private final VehiculoService vehiculoService;

    @PostMapping("/conductor/{idConductor}")
    public ResponseEntity<VehiculoResponseDTO> registrarVehiculo(
            @PathVariable Integer idConductor,
            @RequestBody VehiculoRequestDTO request
    ) {
        var response = vehiculoService.registrarVehiculo(idConductor, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{idVehiculo}/color")
    public ResponseEntity<VehiculoResponseDTO> actualizarColorVehiculo(
            @PathVariable Integer idVehiculo,
            @RequestBody VehiculoColorRequestDTO request
    ) {
        var updated = vehiculoService.actualizarColorVehiculo(idVehiculo, request.color());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/conductor/{idConductor}")
    public ResponseEntity<VehiculoResponseDTO> getVehiculoByConductor(@PathVariable Integer idConductor) {
        var response = vehiculoService.findVehiculoByConductorId(idConductor);
        return ResponseEntity.ok(response);
    }
}

