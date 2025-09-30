package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.UsuarioRequestDTO;
import com.example.unirideapi.dto.response.UsuarioResponseDTO;
import com.example.unirideapi.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {
    private final ModelMapper modelMapper;

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    public Usuario toEntity(UsuarioRequestDTO dto) {return modelMapper.map(dto, Usuario.class);}
}
