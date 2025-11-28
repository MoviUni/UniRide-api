package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.enums.EstadoRuta;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class RutaMapper {

    private final ModelMapper modelMapper;

    public RutaResponseDTO toDTO(Ruta entity) {

        Integer capacidadVehiculo = null;
        if (entity.getConductor() != null &&
                entity.getConductor().getVehiculo() != null) {

            capacidadVehiculo =
                    entity.getConductor().getVehiculo().getCapacidad();
        }

        return new RutaResponseDTO(
                entity.getIdRuta(),
                entity.getOrigen(),
                entity.getDestino(),
                entity.getFechaSalida(),
                entity.getHoraSalida(),
                entity.getTarifa(),                // BigDecimal
                entity.getAsientosDisponibles(),
                entity.getEstadoRuta(),
                entity.getConductor() != null
                        ? entity.getConductor().getIdConductor()
                        : null,
                capacidadVehiculo
        );
    }

    public Ruta toEntity(RutaRequestDTO dto) {
        Ruta ruta = new Ruta();

        ruta.setOrigen(dto.origen());
        ruta.setDestino(dto.destino());
        ruta.setFechaSalida(dto.fechaSalida());   // LocalDate
        ruta.setHoraSalida(dto.horaSalida());     // LocalTime

        // tarifa en DTO y en entidad: BigDecimal (nullable)
        if (dto.tarifa() != null) {
            ruta.setTarifa(dto.tarifa());         // ya es BigDecimal
        } else {
            ruta.setTarifa(null);                 // permite null en BD
        }

        ruta.setAsientosDisponibles(dto.asientosDisponibles());

        if (dto.estadoRuta() != null) {
            ruta.setEstadoRuta(dto.estadoRuta());
        } else {
            ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        }

        // El conductor se setea en el service usando conductorId
        return ruta;

    }

    public RutaFrecuenteResponseDTO toRutaFrecuenteDTO(Object[] obj) {

        BigDecimal tarifa = null;
        if (obj[3] != null) {
            if (obj[3] instanceof BigDecimal bd) {
                tarifa = bd;
            } else if (obj[3] instanceof Number n) {
                tarifa = BigDecimal.valueOf(n.doubleValue());
            }
        }

        return RutaFrecuenteResponseDTO.builder()
                .origen((String) obj[0])
                .destino((String) obj[1])
                .horaSalida(((java.sql.Time) obj[2]).toLocalTime())
                .tarifa(tarifa)   // ahora también BigDecimal en el DTO
                .frecuencia(((Number) obj[4]).longValue())
                .build();
    }
}
