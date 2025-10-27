package com.example.unirideapi.controller;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.unit.ConductorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conductor")
public class ConductorController {
    private final ConductorService conductorService;


    @GetMapping
    public ResponseEntity<List<ConductorResponseDTO>> listAll() {
        List<ConductorResponseDTO> conductores = conductorService.getAll();
        return new ResponseEntity<>(conductores, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ConductorResponseDTO>> paginate(@PageableDefault(size = 8, sort = "numDni") Pageable pageable) {
        Page<ConductorResponseDTO> page = conductorService.paginate(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ConductorResponseDTO> create(@Valid @RequestBody ConductorRequestDTO conductorRequestDTO) {
        ConductorResponseDTO createdConductor = conductorService.create(conductorRequestDTO);
        return new ResponseEntity<>(createdConductor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConductorResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody ConductorRequestDTO conductorRequestDTO) {
        ConductorResponseDTO updatedConductor = conductorService.update(id, conductorRequestDTO);
        return new ResponseEntity<>(updatedConductor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        conductorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConductorResponseDTO> getPerfil(@PathVariable Integer id) {
        ConductorResponseDTO dto = conductorService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
