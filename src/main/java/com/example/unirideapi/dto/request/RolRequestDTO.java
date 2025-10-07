package com.example.unirideapi.dto.request;

import com.example.unirideapi.model.enums.ERol;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RolRequestDTO(

        @NotNull(message = "El tipo de rol es obligatorio")
        ERol name
) {}