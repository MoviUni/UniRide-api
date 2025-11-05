package com.example.unirideapi.service.impl;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.exception.ResourceNotFoundException;
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.mapper.RutaMapper;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
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
    private  final RutaMapper rutaMapper;

    @Override
    public RutaResponseDTO create(RutaRequestDTO rutaRequestDTO) {
        Ruta ruta = rutaMapper.toEntity(rutaRequestDTO);
        return rutaMapper.toDTO( rutaRepository.save(ruta));
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
        return rutaRepository.searchBy(destino, origen, hora, fecha).stream()
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
            // re-lanzamos si ya es del tipo esperado
            throw e;
        } catch (Exception e) {
            throw new BusinessRuleException("Hubo un error al exportar tu reporte");
        }
    }



    @Transactional
    @Override
    public RutaResponseDTO updateEstadoRuta(Integer idRuta, EstadoRuta nuevoEstado) {
        var ruta = rutaRepository.findById((long)idRuta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));

        //Regla 1: Solo rutas PROGRAMADAS pueden cambiar a CONFIRMADO o CANCELADO
        if (ruta.getEstadoRuta() != EstadoRuta.PROGRAMADO) {
            throw new BusinessRuleException("Solo se pueden confirmar o cancelar rutas en estado PROGRAMADO");
        }

        //Regla 2: No puede confirmarse ni cancelarse si falta menos de 1 hora
        LocalDateTime fechaHoraSalida = LocalDateTime.of(ruta.getFechaSalida(), ruta.getHoraSalida());
        LocalDateTime ahora = LocalDateTime.now();

        if (Duration.between(ahora, fechaHoraSalida).toMinutes() < 60) {
            throw new BusinessRuleException("No se puede confirmar o cancelar el viaje con menos de 1 hora de anticipación");
        }
        // Actualizar estado
        ruta.setEstadoRuta(nuevoEstado);
        rutaRepository.save(ruta);

        return rutaMapper.toDTO(ruta);
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
                        .tarifa(Long.parseLong(row[4].toString())) // ERROR : Required Long, Provided Double
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
        ruta.setTarifa(dto.tarifa() == null ? null : dto.tarifa().longValue());
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

}
