package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaCardResponseDTO;
import com.example.unirideapi.dto.response.SolicitudCardResponseDTO;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.SolicitudViaje;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import com.example.unirideapi.repository.ConductorRepository;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.repository.SolicitudViajeRepository;
import com.example.unirideapi.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import java.time.LocalTime;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;

import com.example.unirideapi.exception.BusinessRuleException;
import com.example.unirideapi.model.enums.EstadoRuta;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RutaServiceImpl implements RutaService {
    private final RutaRepository rutaRepository;
    private final SolicitudViajeRepository solicitudViajeRepository;
    private  final RutaMapper rutaMapper;
    private final ConductorRepository conductorRepository;
    private final SolicitudViajeRepository solicitudRepository;
    @Override
    public RutaResponseDTO create(RutaRequestDTO dto) {
        Ruta ruta = rutaMapper.toEntity(dto);

        // Buscar el conductor por id y asignarlo
        Conductor conductor = conductorRepository.findById(dto.conductorId())
                .orElseThrow(() -> new BusinessRuleException("Conductor no encontrado"));

        ruta.setConductor(conductor);

        Ruta guardada = rutaRepository.save(ruta);
        return rutaMapper.toDTO(guardada);
    }

    @Override
    public List<RutaResponseDTO> searchByDisponible(){
        return rutaRepository.searchByDisponible().stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RutaResponseDTO searchById(Long id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ruta no existe"));
        return rutaMapper.toDTO(ruta);
    }
    @Override
    public List<RutaResponseDTO> findAll(){
        return rutaRepository.findAll().stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<RutaResponseDTO> searchByOrigen(String origen) {
        return rutaRepository.searchByOrigen(origen).stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RutaResponseDTO> searchByDestino(String destino) {
        return rutaRepository.searchByDestino(destino).stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<RutaResponseDTO> searchByHora(String hora) {
        return rutaRepository.searchByHora(LocalTime.parse(hora)).stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<RutaResponseDTO> searchBy(String destino, String origen, String hora, String fecha) {
        return rutaRepository.searchBy(destino, origen, LocalTime.parse(hora), LocalDate.parse(fecha)).stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int obtenerTotalViajes(Integer idConductor) {
        return rutaRepository.countRutaByConductor_IdConductor(idConductor);
    }

    private static final String[] DIAS = {
            "lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"
    };

    @Override
    public Map<String, Integer> obtenerFrecuenciaViajesPorPasajero(Integer idConductor) {
        List<Object[]> resultados = rutaRepository.contarViajesPorDiaSemana(idConductor);

        if (resultados.isEmpty()) {
            throw new BusinessRuleException("No tiene viajes registrados aún");
        }

        Map<String, Integer> frecuencia = new LinkedHashMap<>();

        // Inicializacion en 0
        for (String dia : DIAS) {
            frecuencia.put(dia, 0);
        }

        for (Object[] fila : resultados) {
            int diaNum = ((Number) fila[0]).intValue();
            int cantidad = ((Number) fila[1]).intValue();
            frecuencia.put(DIAS[diaNum - 1], cantidad);
        }
        return frecuencia;
    }

    @Override
    public List<RutaFrecuenteResponseDTO> obtenerRutasMasFrecuentes(Integer conductorId) {
        List<Object[]> resultados = rutaRepository.findRutasMasFrecuentes(conductorId);

        if (resultados.isEmpty()) {
            throw new BusinessRuleException("No tiene viajes registrados aún");
        }

        return resultados.stream()
                .map(rutaMapper::toRutaFrecuenteDTO)
                .collect(Collectors.toList());
    }


    @Override
    public byte[] exportarHistorialPdf(Integer conductorId) {
        try {
            List<Object[]> historial = rutaRepository.exportarPDF(conductorId);

            if (historial.isEmpty()) {
                throw new BusinessRuleException("No tienes viajes para exportar");
            }

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 PDDocument document = new PDDocument()) {

                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDType1Font fontTitle = PDType1Font.HELVETICA_BOLD;
                PDType1Font fontText = PDType1Font.HELVETICA;

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    float margin = 50;
                    float yStart = page.getMediaBox().getHeight() - margin;

                    // Título
                    contentStream.beginText();
                    contentStream.setFont(fontTitle, 18);
                    contentStream.newLineAtOffset(page.getMediaBox().getWidth() / 2 - 100, yStart);
                    contentStream.showText("Historial de Viajes");
                    contentStream.endText();

                    // Subtítulo
                    yStart -= 40;
                    contentStream.beginText();
                    contentStream.setFont(fontText, 12);
                    contentStream.newLineAtOffset(margin, yStart);
                    contentStream.showText("Conductor ID: " + conductorId);
                    contentStream.endText();

                    // Encabezados
                    yStart -= 30;
                    float tableY = yStart;
                    float rowHeight = 20;
                    float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                    float[] colWidths = {100, 100, 100, 100, 80};
                    String[] headers = {"Origen", "Destino", "Fecha", "Hora", "Tarifa"};

                    float nextX = margin;

                    contentStream.setFont(fontTitle, 10);
                    for (int i = 0; i < headers.length; i++) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(nextX + 2, tableY);
                        contentStream.showText(headers[i]);
                        contentStream.endText();
                        nextX += colWidths[i];
                    }

                    // Filas
                    contentStream.setFont(fontText, 10);
                    tableY -= rowHeight;

                    for (Object[] row : historial) {
                        nextX = margin;
                        String[] values = {
                                row[0].toString(),
                                row[1].toString(),
                                row[2].toString(),
                                row[3].toString(),
                                row[4].toString()
                        };

                        for (int i = 0; i < values.length; i++) {
                            contentStream.beginText();
                            contentStream.newLineAtOffset(nextX + 2, tableY);
                            contentStream.showText(values[i]);
                            contentStream.endText();
                            nextX += colWidths[i];
                        }
                        tableY -= rowHeight;
                    }
                }

                document.save(baos);
                return baos.toByteArray();
            }

        } catch (BusinessRuleException e) {
            // relanzamos si ya es del tipo esperado
            throw e;
        } catch (Exception e) {
            throw new BusinessRuleException("Hubo un error al exportar tu reporte");
        }
    }
    private long contarSolicitudesAceptadas(Ruta ruta) {
        List<SolicitudViaje> solicitudes =
                solicitudRepository.findByRutaId(ruta.getIdRuta());

        return solicitudes.stream()
                .filter(s -> s.getEstadoSolicitud() == EstadoSolicitud.ACEPTADO)
                .count();
    }

    @Transactional
    @Override
    public RutaResponseDTO updateEstadoRuta(Integer idRuta, EstadoRuta nuevoEstado) {
        Ruta ruta = rutaRepository.findById(idRuta.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        EstadoRuta estadoActual = ruta.getEstadoRuta();

        switch (nuevoEstado) {
            case CONFIRMADO -> confirmarRuta(ruta, estadoActual);
            case EN_PROGRESO -> iniciarViaje(ruta, estadoActual);
            case FINALIZADO -> finalizarViaje(ruta, estadoActual);
            case CANCELADO -> cancelarRuta(ruta, estadoActual);
            default -> throw new BusinessRuleException("Estado de ruta no soportado: " + nuevoEstado);
        }

        rutaRepository.save(ruta);
        return rutaMapper.toDTO(ruta);
    }

    /** CONFIRMAR: desde PROGRAMADO, con al menos 1 solicitud aceptada y faltando > 1h */
    private void confirmarRuta(Ruta ruta, EstadoRuta estadoActual) {
        if (estadoActual != EstadoRuta.PROGRAMADO) {
            throw new BusinessRuleException("Solo se pueden confirmar rutas en estado PROGRAMADO");
        }

        // Regla de tiempo: debe faltar más de 1 hora
        LocalDateTime fechaHoraSalida = LocalDateTime.of(
                ruta.getFechaSalida(),
                ruta.getHoraSalida()
        );
        LocalDateTime limite = fechaHoraSalida.minusHours(1);
        LocalDateTime ahora = LocalDateTime.now();

        if (ahora.isAfter(fechaHoraSalida)) {
            ruta.setEstadoRuta(EstadoRuta.CANCELADO);
            rutaRepository.save(ruta);
            throw new BusinessRuleException(
                    "El viaje ya inició o ha finalizado; se canceló automáticamente y no se puede confirmar."
            );
        }

        if (!ahora.isBefore(limite)) { // ahora >= límite
            ruta.setEstadoRuta(EstadoRuta.CANCELADO);
            rutaRepository.save(ruta);
            throw new BusinessRuleException(
                    "Se venció el plazo para confirmar el viaje (1 hora antes de la salida). La ruta ha sido cancelada automáticamente."
            );
        }

        // Debe haber al menos 1 solicitud aceptada
        long aceptadas = contarSolicitudesAceptadas(ruta);
        if (aceptadas < 1) {
            throw new BusinessRuleException("No se puede confirmar una ruta sin pasajeros aceptados.");
        }

        ruta.setEstadoRuta(EstadoRuta.CONFIRMADO);
    }

    /** INICIAR: desde CONFIRMADO, con ≥1 aceptado, entre horaSalida y horaSalida + 10min */
    private void iniciarViaje(Ruta ruta, EstadoRuta estadoActual) {
        if (estadoActual != EstadoRuta.CONFIRMADO) {
            throw new BusinessRuleException("Solo se puede iniciar un viaje confirmado.");
        }

        long aceptadas = contarSolicitudesAceptadas(ruta);
        if (aceptadas < 1) {
            throw new BusinessRuleException("No se puede iniciar un viaje sin pasajeros aceptados.");
        }

        LocalDateTime fechaHoraSalida = LocalDateTime.of(
                ruta.getFechaSalida(),
                ruta.getHoraSalida()
        );
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteInicio = fechaHoraSalida.plusMinutes(10);

        if (ahora.isBefore(fechaHoraSalida)) {
            throw new BusinessRuleException(
                    "Aún no puedes iniciar el viaje. Solo se puede iniciar desde la hora de salida programada."
            );
        }

        if (ahora.isAfter(limiteInicio)) {
            ruta.setEstadoRuta(EstadoRuta.CANCELADO);
            rutaRepository.save(ruta);
            throw new BusinessRuleException(
                    "Se venció el plazo de 10 minutos para iniciar el viaje. La ruta ha sido cancelada."
            );
        }

        ruta.setEstadoRuta(EstadoRuta.EN_PROGRESO);
    }

    /** FINALIZAR: solo EN_PROGRESO. Se recomienda hacerlo antes de 3h después de la salida */
    private void finalizarViaje(Ruta ruta, EstadoRuta estadoActual) {
        if (estadoActual != EstadoRuta.EN_PROGRESO) {
            throw new BusinessRuleException("Solo se puede finalizar un viaje en progreso.");
        }

        LocalDateTime fechaHoraSalida = LocalDateTime.of(
                ruta.getFechaSalida(),
                ruta.getHoraSalida()
        );
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteFinalizacion = fechaHoraSalida.plusHours(3);

        // Si ya pasó el límite, igual lo dejamos finalizar, pero podrías loguear o registrar métrica
        if (ahora.isAfter(limiteFinalizacion)) {
            // Aquí podrías guardar un flag de “fuera de tiempo” si quisieras
        }

        ruta.setEstadoRuta(EstadoRuta.FINALIZADO);
    }

    // Helper común para cancelar las solicitudes cuando la ruta se cancela
    private void cancelarSolicitudesPorCancelacion(Ruta ruta) {
        List<SolicitudViaje> solicitudes = solicitudRepository.findByRutaId(ruta.getIdRuta());

        solicitudes.stream()
                .filter(s -> s.getEstadoSolicitud() == EstadoSolicitud.PENDIENTE
                        || s.getEstadoSolicitud() == EstadoSolicitud.ACEPTADO)
                .forEach(s -> s.setEstadoSolicitud(EstadoSolicitud.CANCELADO_CONDUCTOR));

        solicitudRepository.saveAll(solicitudes);
    }

    /** CANCELAR: SOLO desde PROGRAMADO (acción del conductor) */
    private void cancelarRuta(Ruta ruta, EstadoRuta estadoActual) {

        // 1) Solo se puede cancelar si está PROGRAMADO
        if (estadoActual != EstadoRuta.PROGRAMADO) {
            throw new BusinessRuleException(
                    "Solo se pueden cancelar rutas en estado PROGRAMADO."
            );
        }

        // 2) Regla de tiempo: debe faltar más de 1 hora
        LocalDateTime fechaHoraSalida = LocalDateTime.of(
                ruta.getFechaSalida(),
                ruta.getHoraSalida()
        );
        LocalDateTime limite = fechaHoraSalida.minusHours(1);
        LocalDateTime ahora = LocalDateTime.now();

        if (ahora.isAfter(fechaHoraSalida)) {
            // si ya pasó la hora, la marcamos cancelada y no dejamos operar
            ruta.setEstadoRuta(EstadoRuta.CANCELADO);
            cancelarSolicitudesPorCancelacion(ruta);
            rutaRepository.save(ruta);
            throw new BusinessRuleException(
                    "El viaje ya inició o ha finalizado; se canceló automáticamente y no se puede cambiar su estado."
            );
        }

        if (!ahora.isBefore(limite)) { // ahora >= límite (menos de 1h)
            ruta.setEstadoRuta(EstadoRuta.CANCELADO);
            cancelarSolicitudesPorCancelacion(ruta);
            rutaRepository.save(ruta);
            throw new BusinessRuleException(
                    "Se venció el plazo para cancelar el viaje (1 hora antes de la salida). " +
                            "La ruta ha sido cancelada automáticamente."
            );
        }

        // 3) Cancelar normalmente cuando sí se cumple la regla
        ruta.setEstadoRuta(EstadoRuta.CANCELADO);
        cancelarSolicitudesPorCancelacion(ruta);
        rutaRepository.save(ruta);
    }



    @Override // ERROR : Method does not override method from its superclas
    public List<RutaResponseDTO> obtenerHistorialViajes(Integer idUsuario, String rol) {
        List<Object[]> resultados;

        if ("CONDUCTOR".equalsIgnoreCase(rol)) {
            resultados = rutaRepository.findHistorialByConductor(idUsuario);
        } else if ("PASAJERO".equalsIgnoreCase(rol)) {
            resultados = rutaRepository.findHistorialByPasajero(idUsuario);
        } else {
            throw new IllegalArgumentException("Rol no válido: " + rol);
        }

        // Convertimos a DTO
        return resultados.stream()
                .map(row -> RutaResponseDTO.builder()
                        .origen(row[0].toString())
                        .destino(row[1].toString())
                        .fechaSalida(LocalDate.parse(row[2].toString()))
                        .horaSalida(LocalTime.parse(row[3].toString()))
                        .tarifa((BigDecimal) row[4])
                        .build())
                .collect(Collectors.toList());
    }

    // ⬇️ ADD: NUEVO — listar todas mis rutas (conductor)
    @Override
    public List<RutaResponseDTO> listarRutasDelConductor(Integer idConductor) {
        return rutaRepository.findByConductor_IdConductor(idConductor)
                .stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }
    private void cancelarSiExpirada(Ruta ruta) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime salida = LocalDateTime.of(
                ruta.getFechaSalida(),
                ruta.getHoraSalida()
        );

        boolean seCancelo = false;

        // PROGRAMADO: se cancela si ya pasó el límite de confirmación (1h antes)
        if (ruta.getEstadoRuta() == EstadoRuta.PROGRAMADO) {
            LocalDateTime limiteConfirmacion = salida.minusHours(1);
            if (!ahora.isBefore(limiteConfirmacion)) { // ahora >= límite
                ruta.setEstadoRuta(EstadoRuta.CANCELADO);
                seCancelo = true;
            }
        }

        // CONFIRMADO: si nunca se inició y ya pasaron los 10 min de tolerancia, se cancela
        if (ruta.getEstadoRuta() == EstadoRuta.CONFIRMADO) {
            LocalDateTime limiteInicio = salida.plusMinutes(10);
            if (ahora.isAfter(limiteInicio)) {
                ruta.setEstadoRuta(EstadoRuta.CANCELADO);
                seCancelo = true;
            }
        }

        if (seCancelo) {
            cancelarSolicitudesPorCancelacion(ruta);
            rutaRepository.save(ruta);
        }
    }



    /**
     * Auto-finaliza una ruta EN_PROGRESO si ya pasaron más de 3 horas
     * desde la hora de salida.
     */
    private void finalizarSiSuperoLimite(Ruta ruta) {
        if (ruta.getEstadoRuta() != EstadoRuta.EN_PROGRESO) {
            return;
        }

        LocalDateTime fechaHoraSalida = LocalDateTime.of(
                ruta.getFechaSalida(),
                ruta.getHoraSalida()
        );

        LocalDateTime limiteFin = fechaHoraSalida.plusHours(3);
        LocalDateTime ahora = LocalDateTime.now();

        if (ahora.isAfter(limiteFin)) {
            ruta.setEstadoRuta(EstadoRuta.FINALIZADO);
            rutaRepository.save(ruta);
        }
    }

    @Transactional
    @Override
    public List<RutaResponseDTO> listarRutasActivasDelConductor(Integer idConductor) {
        LocalDateTime ahora = LocalDateTime.now();

        // 1) Traemos TODAS las rutas del conductor
        List<Ruta> rutas = rutaRepository.findByConductor_IdConductor(idConductor);

        // 2) Aplicamos reglas automáticas:
        //    - cancelar PROGRAMADO/CONFIRMADO cuando venza su ventana
        //    - finalizar EN_PROGRESO después de 3 horas
        rutas.forEach(ruta -> {
            cancelarSiExpirada(ruta);
            finalizarSiSuperoLimite(ruta);
        });

        // 3) Devolver solo las "activas" para el conductor:
        //    PROGRAMADO, CONFIRMADO o EN_PROGRESO
        return rutas.stream()
                .filter(r -> r.getEstadoRuta() == EstadoRuta.PROGRAMADO
                        || r.getEstadoRuta() == EstadoRuta.CONFIRMADO
                        || r.getEstadoRuta() == EstadoRuta.EN_PROGRESO)
                .filter(r -> {
                    LocalDateTime salida = LocalDateTime.of(
                            r.getFechaSalida(),
                            r.getHoraSalida()
                    );

                    // EN_PROGRESO: siempre se considera activo aquí.
                    if (r.getEstadoRuta() == EstadoRuta.EN_PROGRESO) {
                        return true;
                    }

                    // CONFIRMADO: lo consideramos "activo" desde antes de la salida
                    // y hasta 10 minutos después para poder iniciar el viaje.
                    if (r.getEstadoRuta() == EstadoRuta.CONFIRMADO) {
                        LocalDateTime limiteInferior = salida.minusHours(30); // o lo que quieras
                        LocalDateTime limiteSuperior = salida.plusMinutes(10); // ventana de tolerancia
                        return !ahora.isBefore(limiteInferior) && !ahora.isAfter(limiteSuperior);
                        // en la práctica, con no estar después de +10 min ya basta
                        // return !ahora.isAfter(limiteSuperior);
                    }

                    return !salida.isBefore(ahora); // salida >= ahora
                })
                // ordenados por fecha+hora
                .sorted(Comparator.comparing(
                        r -> LocalDateTime.of(r.getFechaSalida(), r.getHoraSalida())
                ))
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }




    // ⬇️ ADD: NUEVO — listar mis rutas por estado
    @Override
    public List<RutaResponseDTO> listarRutasDelConductorPorEstado(Integer idConductor, EstadoRuta estado) {
        return rutaRepository.findByConductor_IdConductorAndEstadoRuta(idConductor, estado)
                .stream()
                .map(rutaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ⬇️ ADD: NUEVO — actualizar (PUT) una ruta propia usando tu RutaRequestDTO
    @Transactional
    @Override
    public RutaResponseDTO actualizarRutaFull(Long idRuta, Integer idConductor, RutaRequestDTO dto) {
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        // validar propiedad
        if (ruta.getConductor() == null
                || ruta.getConductor().getIdConductor() == null
                || !ruta.getConductor().getIdConductor().equals(idConductor)) {
            throw new AccessDeniedException("No puedes editar rutas de otro conductor");
        }

        // Reemplazo completo (PUT) con tu DTO actual
        ruta.setOrigen(dto.origen());
        ruta.setDestino(dto.destino());
        ruta.setFechaSalida(dto.fechaSalida());
        ruta.setHoraSalida(dto.horaSalida());
        // ⚠️ Si tu entidad usa otro tipo para tarifa (BigDecimal/Long), ajusta aquí:
        ruta.setTarifa(dto.tarifa());
        ruta.setAsientosDisponibles(dto.asientosDisponibles());
        ruta.setEstadoRuta(dto.estadoRuta());

        return rutaMapper.toDTO(rutaRepository.save(ruta));
    }

    // ⬇️ ADD: NUEVO — eliminar una ruta propia (sin tocar repo con métodos nuevos)
    @Transactional
    @Override
    public void eliminarRutaDeConductor(Long idRuta, Integer idConductor) {
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        if (ruta.getConductor() == null
                || ruta.getConductor().getIdConductor() == null
                || !ruta.getConductor().getIdConductor().equals(idConductor)) {
            throw new AccessDeniedException("No puedes eliminar rutas de otro conductor");
        }

        rutaRepository.delete(ruta);
    }

    // =====================
// HELPERS PRIVADOS
// =====================
    private boolean esNuloOVacio(String s) {
        return s == null || s.isBlank();
    }

    /** Chequeo de duplicado usando tu propio searchBy(...) */
    private boolean existeRutaDuplicada(Integer idConductor,
                                        String origen,
                                        String destino,
                                        LocalDate fecha,
                                        LocalTime hora) {
        return rutaRepository
                .searchBy(destino, origen, hora, fecha)
                .stream()
                .anyMatch(r -> r.getConductor() != null
                        && r.getConductor().getIdConductor() != null
                        && r.getConductor().getIdConductor().equals(idConductor));
    }

    /** Placeholder: mientras no tengas countReservas(idRuta) en el repo, devuelve 0 */
    protected int obtenerReservas(Long idRuta) {
        // TODO: si luego agregas rutaRepository.countReservas(idRuta), retorna ese valor real.
        return 0;
    }

    // ======================================================
// PUBLICAR RUTA con validaciones (NO reemplaza tu create())
// ======================================================
    public RutaResponseDTO publicarRutaComoConductor(RutaRequestDTO dto) {
        if (esNuloOVacio(dto.destino())) {
            throw new BusinessRuleException("Destino es obligatorio");
        }
        if (dto.asientosDisponibles() == null || dto.asientosDisponibles() < 1) {
            throw new BusinessRuleException("Capacidad mínima 1");
        }
        if (dto.conductorId() != null &&
                existeRutaDuplicada(dto.conductorId(), dto.origen(), dto.destino(),
                        dto.fechaSalida(), dto.horaSalida())) {
            throw new BusinessRuleException("Ruta duplicada no permitida");
        }
        // Reutiliza tu create() que ya funciona
        return create(dto);
    }

    // =====================================================================
// ACTUALIZAR con reglas y confirmación (nombre distinto del tuyo)
// =====================================================================
    @Transactional
    public RutaResponseDTO actualizarRutaFullConReglas(Long idRuta,
                                                       Integer idConductor,
                                                       RutaRequestDTO dto,
                                                       boolean confirmarCambios) {
        var ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        // Propiedad
        if (ruta.getConductor() == null
                || ruta.getConductor().getIdConductor() == null
                || !ruta.getConductor().getIdConductor().equals(idConductor)) {
            throw new AccessDeniedException("No puedes editar rutas de otro conductor");
        }

        // Capacidad
        if (dto.asientosDisponibles() == null || dto.asientosDisponibles() < 1) {
            throw new BusinessRuleException("Capacidad mínima 1");
        }

        int reservas = rutaRepository.countReservas(idRuta);
        if (reservas > 0 && dto.asientosDisponibles() < reservas) {
            throw new BusinessRuleException("No puedes reducir la capacidad por debajo de pasajeros reservados");
        }

        boolean cambiaFecha = dto.fechaSalida() != null && !dto.fechaSalida().equals(ruta.getFechaSalida());
        boolean cambiaHora  = dto.horaSalida()  != null && !dto.horaSalida().equals(ruta.getHoraSalida());
        if (reservas > 0 && (cambiaFecha || cambiaHora) && !confirmarCambios) {
            throw new BusinessRuleException("La ruta tiene pasajeros: se requiere confirmación para cambiar horario/fecha");
        }

        // Actualización total (respetando tu tipo Long en tarifa)
        ruta.setOrigen(dto.origen());
        ruta.setDestino(dto.destino());
        ruta.setFechaSalida(dto.fechaSalida());
        ruta.setHoraSalida(dto.horaSalida());
        ruta.setTarifa(dto.tarifa());
        ruta.setAsientosDisponibles(dto.asientosDisponibles());
        ruta.setEstadoRuta(dto.estadoRuta());

        return rutaMapper.toDTO(rutaRepository.save(ruta));
    }

    // =====================================================================
// ELIMINAR con confirmación (nombre distinto del tuyo)
// =====================================================================
    @Transactional
    public void eliminarRutaDeConductorConReglas(Long idRuta, Integer idConductor, boolean confirmar) {
        var ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        if (ruta.getConductor() == null
                || ruta.getConductor().getIdConductor() == null
                || !ruta.getConductor().getIdConductor().equals(idConductor)) {
            throw new AccessDeniedException("No puedes eliminar rutas de otro conductor");
        }

        int reservas = obtenerReservas(idRuta);
        if (reservas > 0 && !confirmar) {
            throw new BusinessRuleException("La ruta tiene pasajeros: se requiere confirmación para eliminar");
        }

        rutaRepository.delete(ruta);
    }

    // ======================================================
// CONSULTAR todas mis rutas (alias para controlador)
// ======================================================
    public List<RutaResponseDTO> consultarRutasPublicadasComoConductor(Integer idConductor) {
        return listarRutasDelConductor(idConductor);
    }

    @Override
    public RutaResponseDTO actualizarRutaFull(Long idRuta,
                                              Integer idConductor,
                                              RutaRequestDTO dto,
                                              boolean confirmarCambios) {
        return actualizarRutaFullConReglas(idRuta, idConductor, dto, confirmarCambios);
    }

    @Override
    public void eliminarRutaDeConductor(Long idRuta, Integer idConductor, boolean confirmar) {
        eliminarRutaDeConductorConReglas(idRuta, idConductor, confirmar);
    }

    @Override
    public List<RutaCardResponseDTO> searchInfo(Integer pasajeroId){


        List<RutaCardResponseDTO> newRutas =  new ArrayList<>();

        List<RutaCardResponseDTO> allRutas = rutaRepository.getInfo().stream()
                .map(row -> RutaCardResponseDTO.builder()
                        .idRuta((Integer)row[0])
                        .origen(row[1].toString())
                        .destino(row[2].toString())
                        .fechaSalida(LocalDate.parse(row[3].toString()))
                        .horaSalida(LocalTime.parse(row[4].toString()))
                        .tarifa(((Number) row[5]).longValue())
                        .asientosDisponibles((Integer) row[6])
                        .nombreConductor(row[7].toString())
                        .apellidoConductor(row[8].toString())
                        .vehiculoColor(row[9].toString())
                        .vehiculoPlaca(row[10].toString())
                        .vehiculoModelo(row[11].toString())
                        .build())
                .collect(Collectors.toList());

        List<SolicitudCardResponseDTO>  allSolicitudes = solicitudViajeRepository.getInfo(pasajeroId).stream()
                .map(row -> SolicitudCardResponseDTO.builder()
                        .idSolicitudViaje((Integer)row[0])
                        .estadoSolicitud(EstadoSolicitud.valueOf(row[1].toString()))
                        .origen(row[2].toString())
                        .destino(row[3].toString())
                        .fechaSalida(LocalDate.parse(row[4].toString()))
                        .horaSalida(LocalTime.parse(row[5].toString()))
                        .tarifa(((Number) row[6]).longValue())
                        .asientosDisponibles((Integer) row[7])
                        .nombreConductor(row[8].toString())
                        .apellidoConductor(row[9].toString())
                        .idRuta((Integer)row[10])
                        .vehiculoColor(row[11].toString())
                        .vehiculoPlaca(row[12].toString())
                        .vehiculoModelo(row[13].toString())
                        .build())
                .collect(Collectors.toList());

        if(allSolicitudes.isEmpty()) return allRutas;

        for (int i = 0; i < allRutas.size(); i++) {
            for (int j = 0; j < allSolicitudes.size(); j++) {
                if(Objects.equals(allSolicitudes.get(j).idRuta(), allRutas.get(i).idRuta())){
                    break;
                }
                if(j == allSolicitudes.size() - 1) newRutas.add(allRutas.get(i));

            }
        }

        return newRutas;
    }


}
