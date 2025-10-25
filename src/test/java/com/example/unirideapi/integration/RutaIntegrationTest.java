package com.example.unirideapi.integration;


import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.repository.RutaRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Ruta - Pruebas de Integración")
public class RutaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RutaRepository rutaRepository;

    @Test
    @DisplayName("Debe mostrar todas las rutas disponibles dado un filtro válido")
    void getRuta_ValidFilter_ReturnsOk() throws Exception {
        // Given
        RutaRequestDTO request = new RutaRequestDTO(
                "Surquillo",
                "UPC Monterrico",
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),
                Float.parseFloat("12"),
                4,
                EstadoRuta.PROGRAMADO,
                1
        );

        // When & Then
        mockMvc.perform(post("/rutas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rutas/buscar?hora=08:30:17&origen=Surquillo&destino=UPC Monterrico&fecha=2025-09-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRuta", notNullValue()))
                .andExpect(jsonPath("$[0].horaSalida", is("08:30:17")))
                .andExpect(jsonPath("$[0].fechaSalida", is("2025-09-23")))
                .andExpect(jsonPath("$[0].origen", is("Surquillo")))
                .andExpect(jsonPath("$[0].destino", is("UPC Monterrico")))
                .andExpect(jsonPath("$[0].tarifa", is(12)))
                .andExpect(jsonPath("$[0].asientosDisponibles", is(4)))
                .andExpect(jsonPath("$[0].estadoRuta", is("PROGRAMADO")))
                .andExpect(jsonPath("$[0].idConductor", notNullValue()));
    }

    @Test
    @DisplayName("No debe mostrar ninguna ruta ya que el filtro aplicado no contiene rutas")
    void getRuta_InvalidFilter_ReturnsOk() throws Exception {
        // Given
        RutaRequestDTO request = new RutaRequestDTO(
                "Surquillo",
                "UPC Villa",
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),
                Float.parseFloat("12"),
                4,
                EstadoRuta.PROGRAMADO,
                1
        );

        // When & Then
        mockMvc.perform(post("/rutas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rutas/buscar?hora=08:30:17&origen=Surquillo&destino=UPC Villa&fecha=2025-09-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRuta", notNullValue()))
                .andExpect(jsonPath("$[0].horaSalida", is("08:30:17")))
                .andExpect(jsonPath("$[0].fechaSalida", is("2025-09-23")))
                .andExpect(jsonPath("$[0].origen", is("Surquillo")))
                .andExpect(jsonPath("$[0].destino", is("UPC Villa")))
                .andExpect(jsonPath("$[0].tarifa", is(12)))
                .andExpect(jsonPath("$[0].asientosDisponibles", is(4)))
                .andExpect(jsonPath("$[0].estadoRuta", is("PROGRAMADO")))
                .andExpect(jsonPath("$[0].idConductor", notNullValue()));
    }

}
