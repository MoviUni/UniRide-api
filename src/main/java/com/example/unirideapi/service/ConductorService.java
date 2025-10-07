package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.response.ConductorResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.repository.ConductorRepository;
//import com.example.unirideapi.repository.BookRepository;
import com.example.unirideapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConductorService {
    private final ConductorRepository conductorRepository;
    private final UsuarioRepository usuarioRepository;
    //private final BookRepository bookRepository;

    @Transactional
    public ConductorResponseDTO create(ConductorRequestDTO dto) {
        var user = usuarioRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Regla: un usuario no puede tener más de un perfil de autor
        if (conductorRepository.existsByUserId(dto.userId())) {
            throw new BusinessRuleException("El usuario ya tiene un perfil de conductor");
        }

        var conductor = Conductor.builder()
                .nombre(dto.nombre().trim())
                .apellido(dto.apellido())
                .edad(dto.edad())
                .descripcionConductor(dto.descripciononductor())
                .disponibilidad(dto.disponibilidad())
                .usuario(user)
                .build();

        return toResponse(conductorRepository.save(conductor));
    }

    @Transactional(readOnly = true)
    public List<ConductorResponseDTO> findAll() {
        return conductorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConductorResponseDTO findById(Long id) {
        return conductorRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado"));
    }

    @Transactional
    public void delete(Long id) {
        var author = conductorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado"));

//        // Regla: no se puede eliminar si el autor tiene libros asociados
//        if (!bookRepository.findByAuthorId(id).isEmpty()) {
//            throw new BusinessRuleException("No se puede eliminar el autor porque tiene libros registrados");
//        }

        conductorRepository.delete(author);
    }

    private ConductorResponseDTO toResponse(Conductor conductor) {
        return ConductorResponseDTO.builder()
                .idConductor(conductor.getIdConductor())
                .nombre(conductor.getNombre())
                .apellido(conductor.getApellido())
                .descripcionConductor(conductor.getDescripcionConductor())
                .disponibilidad(conductor.getDisponibilidad())
                .userId(conductor.getUsuario().getIdUsuario())
                .build();
    }
}
