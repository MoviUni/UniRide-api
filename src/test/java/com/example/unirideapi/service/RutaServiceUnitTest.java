package com.example.unirideapi.service;

import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.model.*;
import com.example.unirideapi.model.enums.ERol;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.service.impl.RutaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class RutaServiceUnitTest {
    @Mock
    private RutaRepository rutaRepository;
    @Mock
    private RutaMapper rutaMapper;
    @InjectMocks
    private RutaServiceImpl rutaService;

    private Conductor mockConductor;
    private Vehiculo mockVehiculo;
    private Usuario mockUsuario;
    private Rol mockRol;

    @BeforeEach
    void setUp() {
        // mockRuta = createMockRuta(null, LocalDate.parse("2025-09-23"), LocalTime.parse("08:30:17"),"Surquillo", "UPC Monterrico", Long.parseLong("12"), 4, EstadoRuta.PROGRAMADO, 1);
        mockVehiculo = createMockVehiculo(1, "Audi", "ABC-123", "Azul", "nuevo",
                true, 4, "Es auto nuevo");

        mockRol = createMockRol(ERol.CONDUCTOR,2);
        mockUsuario = createMockUsuario(1, mockRol,"admin@uniride.test", "driver123");

        Conductor mockConductor = createMockConductor(1,
                "Carlos", "Soto",
                "5 años de experiencia conduciendo autos de servicio.",
                LocalDateTime.parse("2025-09-30T09:00:00"),
                30,
                "44444444",
                mockUsuario,
                mockVehiculo);
    }

    private Ruta createMockRuta(Integer id, LocalDate fechaSalida, LocalTime horaSalida,
                                String origen, String destino, Long tarifa, Integer asientosDisponibles,
                                EstadoRuta  estadoRuta, Conductor conductor) {

        Ruta ruta = new Ruta();
        ruta.setIdRuta(id);
        ruta.setEstadoRuta(estadoRuta);
        ruta.setFechaSalida(fechaSalida);
        ruta.setHoraSalida(horaSalida);
        ruta.setTarifa(tarifa);
        ruta.setAsientosDisponibles(asientosDisponibles);
        ruta.setOrigen(origen);
        ruta.setDestino(destino);
        ruta.setConductor(conductor);

        return ruta;
    }

    private Conductor createMockConductor(Integer id, String nombre, String apellido, String descripcion,
                                          LocalDateTime createdAt, Integer edad, String dni,
                                          Usuario usuario, Vehiculo vehiculo) {

        Conductor conductor = new Conductor();
        conductor.setIdConductor(id);
        conductor.setNombre(nombre);
        conductor.setApellido(apellido);
        conductor.setDescripcionConductor(descripcion);
        conductor.setEdad(edad);
        conductor.setDni(dni);
        conductor.setUsuario(usuario);
        conductor.setVehiculo(vehiculo);
        conductor.setCreatedAt(createdAt);

        return conductor;
    }

    private Usuario createMockUsuario(Integer id, Rol rol, String email, String password){

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setRol(rol);
        usuario.setEmail(email);
        usuario.setPassword(password);

        return usuario;
    }

    private Rol createMockRol(ERol name, Integer id){
        Rol rol = new Rol();
        rol.setIdRol(id);
        rol.setName(name);
        return rol;
    }

    private Vehiculo createMockVehiculo(Integer id, String marca, String placa, String color, String modelo,
                                        Boolean soat, Integer capacidad, String descripcion){
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setIdVehiculo(id);
        vehiculo.setMarca(marca);
        vehiculo.setPlaca(placa);
        vehiculo.setColor(color);
        vehiculo.setModelo(modelo);
        vehiculo.setSoat(soat);
        vehiculo.setCapacidad(capacidad);
        vehiculo.setDescripcionVehiculo(descripcion);
        return vehiculo;
    }
    @Test
    @DisplayName("Debe mostrar todas las rutas disponibles dado un filtro válido")
    void getRuta_ValidFilter_Success() {
        // Arrange

        Ruta ruta = createMockRuta(4,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),"Surquillo",
                "UPC Monterrico",
                Long.parseLong("12"),
                4,
                EstadoRuta.PROGRAMADO,
                mockConductor);

        when(rutaRepository.searchBy("UPC Monterrico", "Surquillo",
                LocalTime.parse("08:30:17"),LocalDate.parse("2025-09-23"))
        ).thenReturn(Arrays.asList(ruta));

        // Act
        List<RutaResponseDTO> responses = rutaService.searchBy("UPC Monterrico", "Surquillo",
                "08:30:17","2025-09-23");

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Debe mostrar un arreglo vacío dado un filtro no válido")
    void getRuta_InvalidFilter_Success() {
        // Arrange
        Ruta ruta = createMockRuta(4,
                LocalDate.parse("2025-09-23"),
                LocalTime.parse("08:30:17"),"Surquillo",
                "UPC San Isidro",
                Long.parseLong("12"),
                4,
                EstadoRuta.PROGRAMADO,
                mockConductor);

        List<Ruta> empty = new ArrayList<>();
        when(rutaRepository.searchBy("UPC San Isidro", "Surquillo",
                LocalTime.parse("08:30:17"),LocalDate.parse("2025-09-23"))
        ).thenReturn(empty);

        // Act
        List<RutaResponseDTO> responses = rutaService.searchBy("UPC San Isidro", "Surquillo",
                "08:30:17","2025-09-23");

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(0);

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
    // ------US: 15 -------------
    @Test
    @DisplayName("CP01: Confirmar viaje - debe confirmar viaje antes de la hora de salida")
    void updateEstadoRuta_RutaProgramada_ConfirmarViajeExito() {
        // Dado
        Ruta ruta = new Ruta();
        ruta.setIdRuta(1);
        ruta.setOrigen("Lima");
        ruta.setDestino("UPC-Monterrico");
        ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        ruta.setFechaSalida(LocalDate.now().plusDays(1));
        ruta.setHoraSalida(LocalTime.now().plusHours(4));
        ruta.setAsientosDisponibles(3);

        RutaResponseDTO expectedResponse = RutaResponseDTO.builder()
                .idRuta(1)
                .origen("Lima")
                .destino("UPC-Monterrico")
                .fechaSalida(ruta.getFechaSalida())
                .horaSalida(ruta.getHoraSalida())
                .tarifa(100L)
                .asientosDisponibles(3)
                .estadoRuta(EstadoRuta.CONFIRMADO)
                .idConductor(10)
                .build();

        when(rutaRepository.findById(1L)).thenReturn(Optional.of(ruta));
        when(rutaRepository.save(any(Ruta.class))).thenReturn(ruta);
        when(rutaMapper.toDTO(ruta)).thenReturn(expectedResponse);

        // Cuando
        RutaResponseDTO response = rutaService.updateEstadoRuta(1, EstadoRuta.CONFIRMADO);

        // Entonces
        assertThat(response).isNotNull();
        assertThat(response.estadoRuta()).isEqualTo(EstadoRuta.CONFIRMADO);

        verify(rutaRepository).findById(1L);
        verify(rutaRepository).save(ruta);
    }

    @Test
    @DisplayName("CP02: Cancelar Viaje - debe cancelar viaje antes de la hora de salida")
    void updateEstadoRuta_RutaProgramada_CancelarViajeExito() {
        // Dado
        Ruta ruta = new Ruta();
        ruta.setIdRuta(2);
        ruta.setOrigen("Chosica");
        ruta.setDestino("UPC-Monterrico");
        ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        ruta.setFechaSalida(LocalDate.now().plusDays(1));
        ruta.setHoraSalida(LocalTime.now().plusHours(2));
        ruta.setAsientosDisponibles(4);

        RutaResponseDTO expectedResponse = RutaResponseDTO.builder()
                .idRuta(2)
                .origen("Chosica")
                .destino("UPC-Monterrico")
                .fechaSalida(ruta.getFechaSalida())
                .horaSalida(ruta.getHoraSalida())
                .tarifa(80L)
                .asientosDisponibles(4)
                .estadoRuta(EstadoRuta.CANCELADO)
                .idConductor(15)
                .build();

        when(rutaRepository.findById(2L)).thenReturn(Optional.of(ruta));
        when(rutaRepository.save(any(Ruta.class))).thenReturn(ruta);
        when(rutaMapper.toDTO(ruta)).thenReturn(expectedResponse);

        // Cuando
        RutaResponseDTO response = rutaService.updateEstadoRuta(2, EstadoRuta.CANCELADO);

        // Entonces
        assertThat(response).isNotNull();
        assertThat(response.estadoRuta()).isEqualTo(EstadoRuta.CANCELADO);

        verify(rutaRepository).findById(2L);
        verify(rutaRepository).save(ruta);
    }
    @Test
    @DisplayName("CP03: Estado inválido - no debe permitir cambio de estado si la ruta ya está confirmada o cancelada")
    void updateEstadoRuta_RutaConfirmada_ThrowsException() {
        // Dado
        Ruta ruta = new Ruta();
        ruta.setIdRuta(3);
        ruta.setEstadoRuta(EstadoRuta.CONFIRMADO); // o CANCELADO
        ruta.setFechaSalida(LocalDate.now().plusDays(1));
        ruta.setHoraSalida(LocalTime.now().plusHours(5));

        when(rutaRepository.findById(3L)).thenReturn(Optional.of(ruta));

        // Cuando / Entonces
        assertThatThrownBy(() ->
                rutaService.updateEstadoRuta(3, EstadoRuta.CANCELADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Solo se pueden confirmar o cancelar rutas en estado PROGRAMADO");

        verify(rutaRepository).findById(3L);
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP04: Fuera de plazo - no debe permitir confirmar o cancelar si falta menos de 1 hora para la salida")
    void updateEstadoRuta_MenosDeUnaHora_ThrowsException() {
        // Dado
        Ruta ruta = new Ruta();
        ruta.setIdRuta(4);
        ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        ruta.setFechaSalida(LocalDate.now());
        ruta.setHoraSalida(LocalTime.now().plusMinutes(30));

        when(rutaRepository.findById(4L)).thenReturn(Optional.of(ruta));

        // Cuando / Entonces
        assertThatThrownBy(() ->
                rutaService.updateEstadoRuta(4, EstadoRuta.CONFIRMADO)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("No se puede confirmar o cancelar el viaje con menos de 1 hora de anticipación");

        verify(rutaRepository).findById(4L);
        verify(rutaRepository, never()).save(any());
    }
    @Test
    @DisplayName("CP05: Ruta no encontrada - debe lanzar excepción si la ruta no existe")
    void updateEstadoRuta_RutaNoEncontrada_ThrowsException() {
        // Dado
        when(rutaRepository.findById(99L)).thenReturn(Optional.empty());

        // Cuando / Entonces
        assertThatThrownBy(() ->
                rutaService.updateEstadoRuta(99, EstadoRuta.CONFIRMADO)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ruta no encontrada");

        verify(rutaRepository).findById(99L);
        verify(rutaRepository, never()).save(any());
    }

}
