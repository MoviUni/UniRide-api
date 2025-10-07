package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.ERol;
import lombok.Builder;

@Builder
public record RolResponseDTO (
    Integer idRol,
    ERol name
) {}
