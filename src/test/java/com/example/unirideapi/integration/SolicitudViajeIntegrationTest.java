package com.example.unirideapi.integration;

import com.example.unirideapi.dto.request.SolicitudViajeRequestDTO;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import com.example.unirideapi.repository.SolicitudViajeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("SolicitudViaje - Pruebas de Integración")
public class SolicitudViajeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SolicitudViajeRepository solicitudViajeRepository;

    //@BeforeEach
    //void setUp(){
    //    solicitudViajeRepository.deleteAll();
    //}

    @Test
    @DisplayName("Debe crear una solicitud de viaje exitosamente con datos válidos")
    void createSolicitudViaje_ValidData_ReturnsCreated() throws Exception {
        // Given
        SolicitudViajeRequestDTO request = new SolicitudViajeRequestDTO(
                EstadoSolicitud.PENDIENTE,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),
                2,
                1,
                LocalDate.parse("2025-09-23")

        );

        // When & Then
        mockMvc.perform(post("/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSolicitudViaje", notNullValue()))
                .andExpect(jsonPath("$.estadoSolicitud", is("PENDIENTE")))
                .andExpect(jsonPath("$.fecha", is("2025-09-23")))
                .andExpect(jsonPath("$.hora", is("08:30:17")))
                .andExpect(jsonPath("$.rutaId", is(2)))
                .andExpect(jsonPath("$.pasajeroId", is(1)))
                .andExpect(jsonPath("$.updatedAt", is("2025-09-23T00:00:00")));
    }

    @Test
    @DisplayName("No debe crear solicitud de manera exitosa debido a datos duplicados")
    void createSolicitudViaje_DuplicateSolicitud_ReturnsConflict() throws Exception {
        // Given
        SolicitudViajeRequestDTO request = new SolicitudViajeRequestDTO(
                EstadoSolicitud.PENDIENTE,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),
                2,
                1,
                LocalDate.parse("2025-09-23")

        );

        // When & Then
        mockMvc.perform(post("/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("An unexpected error occurred: Un usuario no puede enviar más de una solicitud a una misma ruta")));

    }
}