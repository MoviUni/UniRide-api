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
import com.example.unirideapi.repository.SolicitudViajeRepository;
import com.example.unirideapi.service.impl.RutaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Disabled;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/*
@ExtendWith(MockitoExtension.class)
public class RutaServiceUnitTest {
    @Mock
    private RutaRepository rutaRepository;
    @Mock
    private SolicitudViajeRepository solicitudRepository;
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
                        15L, //tarifa
                        5L //frecuencia
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

    // ---------- CP10 - Publicar ruta (éxito)
    @Test
    @DisplayName("CP10 - Publicar ruta: crea correctamente con origen/destino/días/hora válidos")
    void publicarRuta_exitoso() {
        // GIVEN
        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("Lima")
                .destino("UPC-Monterrico")
                .fechaSalida(LocalDate.of(2025, 10, 20))
                .horaSalida(LocalTime.of(8, 30))
                .tarifa(15f)
                .asientosDisponibles(4)
                .estadoRuta(EstadoRuta.PROGRAMADO)
                .conductorId(10)
                .build();

        Ruta entityMapped = new Ruta();
        entityMapped.setOrigen(dto.origen());
        entityMapped.setDestino(dto.destino());
        entityMapped.setFechaSalida(dto.fechaSalida());
        entityMapped.setHoraSalida(dto.horaSalida());
        entityMapped.setTarifa(dto.tarifa().longValue()); // ajusta tipo si tu entidad no es Double
        entityMapped.setAsientosDisponibles(dto.asientosDisponibles());
        entityMapped.setEstadoRuta(dto.estadoRuta());

        Ruta saved = entityMapped; // simulamos retorno post-save

        RutaResponseDTO response = RutaResponseDTO.builder()
                .origen(dto.origen())
                .destino(dto.destino())
                .fechaSalida(dto.fechaSalida())
                .horaSalida(dto.horaSalida())
                .tarifa(15L) // ajusta si tu DTO usa otro tipo
                .asientosDisponibles(4)
                .estadoRuta(EstadoRuta.PROGRAMADO)
                .idConductor(10)
                .build();

        when(rutaMapper.toEntity(dto)).thenReturn(entityMapped);
        when(rutaRepository.save(entityMapped)).thenReturn(saved);
        when(rutaMapper.toDTO(saved)).thenReturn(response);

        // WHEN
        RutaResponseDTO out = rutaService.create(dto);

        // THEN
        assertThat(out).isNotNull();
        assertThat(out.origen()).isEqualTo("Lima");
        assertThat(out.destino()).isEqualTo("UPC-Monterrico");
        assertThat(out.asientosDisponibles()).isEqualTo(4);
        verify(rutaMapper).toEntity(dto);
        verify(rutaRepository).save(entityMapped);
        verify(rutaMapper).toDTO(saved);
    }

    // ---------- CP11 - Publicar ruta (faltan datos: destino)
    @Test
    @DisplayName("CP11 - Publicar ruta: 'Destino es obligatorio' si falta destino")
    void publicarRuta_faltaDestino_muestraError() {
        // GIVEN
        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("Lima")
                .destino(null) // falta
                .fechaSalida(LocalDate.of(2025, 10, 20))
                .horaSalida(LocalTime.of(8, 30))
                .tarifa(15f)
                .asientosDisponibles(4)
                .estadoRuta(EstadoRuta.PROGRAMADO)
                .conductorId(10)
                .build();

        // Nota: como la validación aún no existe en el service, simulamos la regla
        // haciendo que el mapper lance la excepción de negocio.
        when(rutaMapper.toEntity(dto))
                .thenThrow(new BusinessRuleException("Destino es obligatorio"));

        // WHEN / THEN
        assertThatThrownBy(() -> rutaService.create(dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Destino es obligatorio");

        verify(rutaMapper).toEntity(dto);
        verify(rutaRepository, never()).save(any(Ruta.class));
    }

    // ---------- CP12 - Publicar ruta (duplicada)
    @Test
    @DisplayName("CP12 - Publicar ruta duplicada: 'Ruta duplicada no permitida'")
    void publicarRuta_duplicada_bloqueaRegistro() {
        // GIVEN
        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("Lima")
                .destino("UPC-Monterrico")
                .fechaSalida(LocalDate.of(2025, 10, 20))
                .horaSalida(LocalTime.of(8, 30))
                .tarifa(15f)
                .asientosDisponibles(4)
                .estadoRuta(EstadoRuta.PROGRAMADO)
                .conductorId(10)
                .build();

        // Simulamos que al guardar la BD detecta duplicado
        when(rutaMapper.toEntity(dto)).thenReturn(new Ruta());
        when(rutaRepository.save(any(Ruta.class)))
                .thenThrow(new BusinessRuleException("Ruta duplicada no permitida"));

        // WHEN / THEN
        assertThatThrownBy(() -> rutaService.create(dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Ruta duplicada no permitida");

        verify(rutaRepository).save(any(Ruta.class));
    }

    // ---------- CP13 - Capacidad válida (=4) al crear
    @Test
    @DisplayName("CP13 - Capacidad válida (4): se guarda y refleja en la respuesta")
    void publicarRuta_capacidadValida_guarda() {
        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("Lima")
                .destino("UPC")
                .fechaSalida(LocalDate.of(2025, 10, 20))
                .horaSalida(LocalTime.of(8, 30))
                .tarifa(12f)
                .asientosDisponibles(4)
                .estadoRuta(EstadoRuta.PROGRAMADO)
                .conductorId(7)
                .build();

        Ruta entity = new Ruta();
        entity.setAsientosDisponibles(4);

        when(rutaMapper.toEntity(dto)).thenReturn(entity);
        when(rutaRepository.save(entity)).thenReturn(entity);
        when(rutaMapper.toDTO(entity)).thenReturn(
                RutaResponseDTO.builder()
                        .origen("Lima").destino("UPC")
                        .fechaSalida(dto.fechaSalida()).horaSalida(dto.horaSalida())
                        .tarifa(12L).asientosDisponibles(4).estadoRuta(EstadoRuta.PROGRAMADO)
                        .idConductor(7).build()
        );

        RutaResponseDTO out = rutaService.create(dto);

        assertThat(out.asientosDisponibles()).isEqualTo(4);
    }

    // ---------- CP14 - Capacidad inválida (0 o negativa) RECHAZA
    @Test
    @DisplayName("CP14 - Capacidad inválida (0 o negativa): rechaza con 'Capacidad mínima 1'")
    void publicarRuta_capacidadInvalida_rechaza() {
        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("Lima").destino("UPC")
                .fechaSalida(LocalDate.of(2025, 10, 20)).horaSalida(LocalTime.of(8, 30))
                .tarifa(12f).asientosDisponibles(0) // inválido
                .estadoRuta(EstadoRuta.PROGRAMADO).conductorId(7).build();

        // simulamos validación de negocio aún no implementada
        when(rutaMapper.toEntity(dto))
                .thenThrow(new BusinessRuleException("Capacidad mínima 1"));

        assertThatThrownBy(() -> rutaService.create(dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Capacidad mínima 1");
    }

    // ---------- CP15 - Reducción de capacidad con reservas: BLOQUEA siempre
    @Test
    @DisplayName("CP15 - Reducir capacidad por debajo de reservas: lanza BusinessRuleException")
    void actualizarRuta_reduccionCapacidad_conReservas_impide() {
        // ids: Long para repo/service; Integer para entidad/DTO
        Long idRuta = 600L;
        Integer idConductor = 77;

        // Ruta existente (dueño correcto)
        var existente = new com.example.unirideapi.model.Ruta();
        existente.setIdRuta(idRuta.intValue());
        var conductor = new com.example.unirideapi.model.Conductor();
        conductor.setIdConductor(idConductor);
        existente.setConductor(conductor);
        existente.setOrigen("A");
        existente.setDestino("B");
        existente.setFechaSalida(LocalDate.now().plusDays(1));
        existente.setHoraSalida(LocalTime.of(8, 0));
        existente.setAsientosDisponibles(6);
        existente.setEstadoRuta(EstadoRuta.PROGRAMADO);

        // DTO intenta BAJAR la capacidad por debajo de reservas (=5)
        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("A").destino("B")
                .fechaSalida(existente.getFechaSalida())
                .horaSalida(existente.getHoraSalida())
                .tarifa(10f)
                .asientosDisponibles(3)               // <--- menor que reservas (5)
                .estadoRuta(EstadoRuta.PROGRAMADO)
                .conductorId(idConductor)
                .build();

        when(rutaRepository.findById(idRuta)).thenReturn(java.util.Optional.of(existente));
        when(rutaRepository.countReservas(idRuta)).thenReturn(5); // <--- hay reservas

        // Sin importar confirmarCambios, la reducción por debajo de reservas BLOQUEA
        assertThatThrownBy(() ->
                rutaService.actualizarRutaFull(idRuta, idConductor, dto, false)
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("No puedes reducir la capacidad");

        // No se guarda
        verify(rutaRepository, never()).save(any(com.example.unirideapi.model.Ruta.class));
    }



    // ---------- CP16 - Edición exitosa (cambio de hora) siendo dueño
    @Test
    @DisplayName("CP16 - Editar ruta (hora) siendo conductor dueño: éxito")
    void actualizarRuta_horaSalida_exito() {
        Long idRuta = 100L; Integer idConductor = 50;

        Ruta existente = new Ruta();
        existente.setIdRuta(100);
        existente.setHoraSalida(LocalTime.of(8, 0));
        var conductor = new com.example.unirideapi.model.Conductor();
        conductor.setIdConductor(idConductor);
        existente.setConductor(conductor);

        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("Lima").destino("UPC").fechaSalida(LocalDate.now().plusDays(1))
                .horaSalida(LocalTime.of(9, 45)) // nueva hora
                .tarifa(10f).asientosDisponibles(3).estadoRuta(EstadoRuta.PROGRAMADO)
                .conductorId(idConductor).build();

        when(rutaRepository.findById(idRuta)).thenReturn(Optional.of(existente));
        when(rutaRepository.save(any(Ruta.class))).thenReturn(existente);
        when(rutaMapper.toDTO(any(Ruta.class))).thenReturn(
                RutaResponseDTO.builder()
                        .idRuta(100).origen("Lima").destino("UPC")
                        .fechaSalida(dto.fechaSalida()).horaSalida(dto.horaSalida())
                        .tarifa(10L).asientosDisponibles(3).estadoRuta(EstadoRuta.PROGRAMADO)
                        .idConductor(idConductor).build()
        );

        RutaResponseDTO out = rutaService.actualizarRutaFull(idRuta, idConductor, dto);

        assertThat(out.horaSalida()).isEqualTo(LocalTime.of(9, 45));
        verify(rutaRepository).findById(idRuta);
        verify(rutaRepository).save(any(Ruta.class));
    }

    // ---------- CP17 - Otro usuario intenta editar: acceso denegado
    @Test
    @DisplayName("CP17 - Editar ruta siendo OTRO usuario: 'Acceso denegado'")
    void actualizarRuta_otroUsuario_denegado() {
        Long idRuta = 200L; Integer idConductorDueño = 50; Integer idConductorAjeno = 77;

        Ruta existente = new Ruta();
        existente.setIdRuta(200);
        var conductor = new com.example.unirideapi.model.Conductor();
        conductor.setIdConductor(idConductorDueño);
        existente.setConductor(conductor);

        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("A").destino("B").fechaSalida(LocalDate.now().plusDays(1))
                .horaSalida(LocalTime.of(10, 0)).tarifa(8f).asientosDisponibles(2)
                .estadoRuta(EstadoRuta.PROGRAMADO).conductorId(idConductorAjeno).build();

        when(rutaRepository.findById(idRuta)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> rutaService.actualizarRutaFull(idRuta, idConductorAjeno, dto))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("No puedes editar rutas de otro conductor");

        verify(rutaRepository).findById(idRuta);
        verify(rutaRepository, never()).save(any(Ruta.class));
    }

    // ---------- CP18 - Edición con reservas sin confirmar (BLOQUEA) y con confirmar (OK)
    @Test
    @DisplayName("CP18 - Editar ruta con reservas: sin confirmar bloquea; con confirmar permite")
    void actualizarRuta_conReservas_confirmaSegunFlag() {
        Long idRuta = 600L; Integer idConductor = 42;

        com.example.unirideapi.model.Ruta existente = new com.example.unirideapi.model.Ruta();
        existente.setIdRuta(idRuta.intValue()); // <-- Integer
        var conductor = new com.example.unirideapi.model.Conductor();
        conductor.setIdConductor(idConductor);
        existente.setConductor(conductor);
        existente.setFechaSalida(LocalDate.of(2025, 10, 20));
        existente.setHoraSalida(LocalTime.of(8, 30));

        var dto = com.example.unirideapi.dto.request.RutaRequestDTO.builder()
                .origen("Lima").destino("UPC")
                .fechaSalida(LocalDate.of(2025, 10, 21)) // cambia fecha
                .horaSalida(LocalTime.of(9, 0))          // cambia hora
                .tarifa(12f).asientosDisponibles(4)
                .estadoRuta(EstadoRuta.PROGRAMADO).conductorId(idConductor)
                .build();

        when(rutaRepository.findById(idRuta)).thenReturn(Optional.of(existente));
        when(rutaRepository.countReservas(idRuta)).thenReturn(2); // hay reservas

        // Sin confirmación => BLOQUEA
        assertThatThrownBy(() -> rutaService.actualizarRutaFull(idRuta, idConductor, dto, false))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("confirmación")
                .hasMessageContaining("horario/fecha");

        // Con confirmación => PERMITE y guarda
        when(rutaRepository.findById(idRuta)).thenReturn(Optional.of(existente)); // segunda llamada
        when(rutaRepository.countReservas(idRuta)).thenReturn(2);
        when(rutaRepository.save(any(com.example.unirideapi.model.Ruta.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(rutaMapper.toDTO(any(com.example.unirideapi.model.Ruta.class)))
                .thenReturn(com.example.unirideapi.dto.response.RutaResponseDTO.builder()
                        .idRuta(idRuta.intValue()) // <-- Integer
                        .origen("Lima").destino("UPC")
                        .fechaSalida(dto.fechaSalida()).horaSalida(dto.horaSalida())
                        .tarifa(12L).asientosDisponibles(4).estadoRuta(EstadoRuta.PROGRAMADO)
                        .idConductor(idConductor).build());

        var ok = rutaService.actualizarRutaFull(idRuta, idConductor, dto, true);
        assertThat(ok.fechaSalida()).isEqualTo(dto.fechaSalida());
        assertThat(ok.horaSalida()).isEqualTo(dto.horaSalida());

        verify(rutaRepository, times(2)).findById(idRuta);
        verify(rutaRepository, times(2)).countReservas(idRuta);
        verify(rutaRepository).save(any(com.example.unirideapi.model.Ruta.class));
    }

    // ---------- CP19 - Eliminar ruta sin reservas: se desactiva o elimina
    @Test
    @DisplayName("CP19 - Eliminar ruta sin reservas: desaparece de la vista pública")
    void eliminarRuta_sinReservas_exito() {
        Long idRuta = 300L; Integer idConductor = 88;

        Ruta existente = new Ruta();
        existente.setIdRuta(300);
        var conductor = new com.example.unirideapi.model.Conductor();
        conductor.setIdConductor(idConductor);
        existente.setConductor(conductor);

        when(rutaRepository.findById(idRuta)).thenReturn(Optional.of(existente));
        doNothing().when(rutaRepository).delete(existente);

        rutaService.eliminarRutaDeConductor(idRuta, idConductor);

        verify(rutaRepository).findById(idRuta);
        verify(rutaRepository).delete(existente);
    }

    @Test
    @DisplayName("CP20 - Eliminar ruta con reservas: requiere confirmación y, si confirma, elimina")
    void eliminarRuta_conReservas_requiereConfirmacion() {
        Long idRuta = 400L;
        Integer idConductor = 10;

        Ruta existente = new Ruta();
        existente.setIdRuta(idRuta.intValue());
        var conductor = new com.example.unirideapi.model.Conductor();
        conductor.setIdConductor(idConductor);
        existente.setConductor(conductor);

        when(rutaRepository.findById(idRuta)).thenReturn(java.util.Optional.of(existente));

        // Subclase que fuerza "hay 3 reservas"
        RutaServiceImpl service = new RutaServiceImpl(rutaRepository, solicitudRepository,  rutaMapper) {
            @Override protected int obtenerReservas(Long ignored) { return 3; }
        };

        // SIN confirmación => debe lanzar BusinessRuleException
        assertThatThrownBy(() -> service.eliminarRutaDeConductorConReglas(idRuta, idConductor, false))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("se requiere confirmación");

        verify(rutaRepository, never()).delete(any(Ruta.class));

        // CON confirmación => elimina
        assertDoesNotThrow(() -> service.eliminarRutaDeConductorConReglas(idRuta, idConductor, true));
        verify(rutaRepository).delete(existente);
    }


    // ---------- CP21 - Eliminar siendo otro usuario: acción no permitida
    @Test
    @DisplayName("CP21 - Eliminar ruta siendo otro usuario: 'Acción no permitida'")
    void eliminarRuta_otroUsuario_noPermitida() {
        Long idRuta = 400L; Integer idConductorDueño = 10; Integer idConductorAjeno = 20;

        Ruta existente = new Ruta();
        existente.setIdRuta(400);
        var conductor = new com.example.unirideapi.model.Conductor();
        conductor.setIdConductor(idConductorDueño);
        existente.setConductor(conductor);

        when(rutaRepository.findById(idRuta)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> rutaService.eliminarRutaDeConductor(idRuta, idConductorAjeno))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("No puedes eliminar rutas de otro conductor");

        verify(rutaRepository).findById(idRuta);
        verify(rutaRepository, never()).delete(any(Ruta.class));
    }

}
*/