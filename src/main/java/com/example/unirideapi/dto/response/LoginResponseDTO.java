package com.example.unirideapi.dto.response;

import com.example.unirideapi.model.enums.ERol;
import lombok.Builder;

@Builder
public record LoginResponseDTO(
        Long userId,
        String email,
        ERol role, // Enum tipado
        String token
) {}