package com.example.unirideapi.unit;

import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.repository.RutaRepository;

import com.example.unirideapi.service.impl.RutaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class RutaServiceUnitTest {
    @Mock
    private RutaRepository rutaRepository;
    @Mock
    private RutaMapper rutaMapper;
    @InjectMocks
    private RutaServiceImpl rutaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Total de viajes
    @Test
    @DisplayName("CP01 - Conductor con viajes registrados")
    void obtenerTotalViajes_conViajes_returnsCorrectCount() {
        // GIVEN
        given(rutaRepository.countRutaByConductor_IdConductor(1)).willReturn(5);

        // WHEN
        int total = rutaService.obtenerTotalViajes(1);

        // THEN
        assertThat(total).isEqualTo(5);
        verify(rutaRepository).countRutaByConductor_IdConductor(1);
    }

    @Test
    @DisplayName("CP02 - Conductor sin viajes registrados")
    void obtenerTotalViajes_sinViajes_returnsZero() {
        // GIVEN
        given(rutaRepository.countRutaByConductor_IdConductor(1)).willReturn(0);

        // WHEN
        int total = rutaService.obtenerTotalViajes(1);

        // THEN
        assertThat(total).isZero();
    }

    // Frecuencia de viajes por pasajero
    @Test
    @DisplayName("CP03 - Mostrar frecuencia de viajes con datos")
    void obtenerFrecuencia_conViajes_returnsFrecuenciaPorDia() {
        // GIVEN
        List<Object[]> resultados = List.of(
                new Object[]{1, 3}, // lunes:3
                new Object[]{3, 2}  // miércoles:2
        );
        given(rutaRepository.contarViajesPorDiaSemana(1)).willReturn(resultados);

        // WHEN
        Map<String, Integer> frecuencia = rutaService.obtenerFrecuenciaViajesPorPasajero(1);

        // THEN
        assertThat(frecuencia).containsEntry("lunes", 3)
                .containsEntry("miércoles", 2)
                .containsEntry("martes", 0);
    }

    @Test
    @DisplayName("CP04 - Usuario sin viajes registrados lanza excepción")
    void obtenerFrecuencia_sinViajes_throwsException() {
        // GIVEN
        given(rutaRepository.contarViajesPorDiaSemana(1)).willReturn(Collections.emptyList());

        // WHEN
        Throwable thrown = catchThrowable(() -> rutaService.obtenerFrecuenciaViajesPorPasajero(1));

        // THEN
        assertThat(thrown).isInstanceOf(BusinessRuleException.class)
                .hasMessage("No tiene viajes registrados aún");
    }

    // Mostrar rutas más frecuentes
    @Test
    @DisplayName("CP05 - Mostrar rutas más frecuentes con datos")
    void obtenerRutasMasFrecuentes_conDatos_returnsLista() {
        // GIVEN
        List<Object[]> resultados = List.<Object[]>of(
                new Object[]{
                        "Campus Sur",
                        "Campus Norte",
                        LocalDate.of(2025, 10, 22),
                        LocalTime.of(8, 30),
                        15L,
                        5L
                }
        );
        RutaFrecuenteResponseDTO dto = new RutaFrecuenteResponseDTO(
                "Campus Sur", "Campus Norte", LocalDate.of(2025, 10, 22),
                LocalTime.of(8, 30), 15L, 5L
        );
        given(rutaRepository.findRutasMasFrecuentes(1)).willReturn(resultados);
        given(rutaMapper.toRutaFrecuenteDTO(resultados.get(0))).willReturn(dto);

        // WHEN
        List<RutaFrecuenteResponseDTO> lista = rutaService.obtenerRutasMasFrecuentes(1);

        // THEN
        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).origen()).isEqualTo("Campus Sur");
        assertThat(lista.get(0).destino()).isEqualTo("Campus Norte");
        assertThat(lista.get(0).fechaSalida()).isEqualTo(LocalDate.of(2025, 10, 22));
        assertThat(lista.get(0).horaSalida()).isEqualTo(LocalTime.of(8, 30));
        assertThat(lista.get(0).tarifa()).isEqualTo(15L);
        assertThat(lista.get(0).frecuencia()).isEqualTo(5L);
    }

    @Test
    @DisplayName("CP06 - Usuario sin rutas frecuentes lanza excepción")
    void obtenerRutasMasFrecuentes_sinDatos_throwsException() {
        // GIVEN
        given(rutaRepository.findRutasMasFrecuentes(1)).willReturn(Collections.emptyList());

        // WHEN
        Throwable thrown = catchThrowable(() -> rutaService.obtenerRutasMasFrecuentes(1));

        // THEN
        assertThat(thrown).isInstanceOf(BusinessRuleException.class)
                .hasMessage("No tiene viajes registrados aún");
    }

    // Exportar PDF
    @Test
    @DisplayName("CP07 - Exportar historial PDF exitosamente")
    void exportarHistorialPdf_conDatos_returnsPdfBytes() {
        // GIVEN
        List<Object[]> historial = List.<Object[]>of(
                new Object[]{"Campus Sur", "Campus Norte", "2025-10-20", "08:30", "15"}
        );
        given(rutaRepository.exportarPDF(1)).willReturn(historial);

        // WHEN
        byte[] pdfBytes = rutaService.exportarHistorialPdf(1);

        // THEN
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("CP08 - Exportar historial sin viajes lanza excepción")
    void exportarHistorialPdf_sinDatos_throwsException() {
        // GIVEN
        given(rutaRepository.exportarPDF(1)).willReturn(Collections.emptyList());

        // WHEN
        Throwable thrown = catchThrowable(() -> rutaService.exportarHistorialPdf(1));

        // THEN
        assertThat(thrown).isInstanceOf(BusinessRuleException.class)
                .hasMessage("No tienes viajes para exportar");
    }

    @Test
    @DisplayName("CP09 - Exportar historial lanza excepción por error interno")
    void exportarHistorialPdf_errorInterno_throwsBusinessRuleException() {
        // GIVEN
        given(rutaRepository.exportarPDF(1)).willThrow(new RuntimeException("Error en DB"));

        // WHEN
        Throwable thrown = catchThrowable(() -> rutaService.exportarHistorialPdf(1));

        // THEN
        assertThat(thrown).isInstanceOf(BusinessRuleException.class)
                .hasMessage("Hubo un error al exportar tu reporte");
    }

}
