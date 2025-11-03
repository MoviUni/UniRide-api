package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.PasajeroResponseDTO;
import com.example.unirideapi.service.PasajeroService;
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

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PASAJERO', 'ADMIN')")
@RequestMapping("/pasajero")
public class PasajeroController {
    private final PasajeroService pasajeroService;


    @GetMapping
    public ResponseEntity<List<PasajeroResponseDTO>> listAll() {
        List<PasajeroResponseDTO> pasajeros = pasajeroService.getAll();
        return new ResponseEntity<>(pasajeros, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<PasajeroResponseDTO>> paginate(@PageableDefault(size = 8, sort = "numDni") Pageable pageable) {
        Page<PasajeroResponseDTO> page = pasajeroService.paginate(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PasajeroResponseDTO> create(@Valid @RequestBody PasajeroRequestDTO pasajeroRequestDTO) {
        PasajeroResponseDTO createdPasajero = pasajeroService.create(pasajeroRequestDTO);
        return new ResponseEntity<>(createdPasajero, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody PasajeroRequestDTO pasajeroRequestDTO) {
        PasajeroResponseDTO updatedPasajero = pasajeroService.update(id, pasajeroRequestDTO);
        return new ResponseEntity<>(updatedPasajero, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pasajeroService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> getPerfil(@PathVariable Integer id) {
        PasajeroResponseDTO dto = pasajeroService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
