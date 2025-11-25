package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.enums.EstadoRuta;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RutaMapper {

    private final ModelMapper modelMapper;

    public RutaResponseDTO toDTO(Ruta entity) {

        // 👇 Calculamos la capacidad del vehículo (si existe)
        Integer capacidadVehiculo = null;
        if (entity.getConductor() != null &&
                entity.getConductor().getVehiculo() != null) {

            capacidadVehiculo =
                    entity.getConductor().getVehiculo().getCapacidad();
            // ajusta getCapacidadAsientos() al nombre real del getter en Vehiculo
        }

        return new RutaResponseDTO(
                entity.getIdRuta(),
                entity.getOrigen(),
                entity.getDestino(),
                entity.getFechaSalida(),
                entity.getHoraSalida(),
                entity.getTarifa(),
                entity.getAsientosDisponibles(),
                entity.getEstadoRuta(),
                entity.getConductor() != null ? entity.getConductor().getIdConductor() : null,
                capacidadVehiculo   // 👈 ahora sí existe
        );
    }

    public Ruta toEntity(RutaRequestDTO dto) {
        Ruta ruta = new Ruta();

        ruta.setOrigen(dto.origen());
        ruta.setDestino(dto.destino());
        ruta.setFechaSalida(dto.fechaSalida());   // LocalDate
        ruta.setHoraSalida(dto.horaSalida());     // LocalTime

        // tarifa en DTO es Float, en entidad es Long
        if (dto.tarifa() != null) {
            ruta.setTarifa(dto.tarifa().longValue());
        } else {
            // Opción A: permitir null si cambias la entidad
            // ruta.setTarifa(null);

            // Opción B (rápida): poner 0 por defecto para evitar NOT NULL
            ruta.setTarifa(0L);
        }

        ruta.setAsientosDisponibles(dto.asientosDisponibles());

        // estadoRuta: si viene null, lo ponemos PROGRAMADO por defecto
        if (dto.estadoRuta() != null) {
            ruta.setEstadoRuta(dto.estadoRuta());
        } else {
            ruta.setEstadoRuta(EstadoRuta.PROGRAMADO);
        }

        // OJO: el conductor lo seteas en el service usando conductorId
        return ruta;

    }


    public RutaFrecuenteResponseDTO toRutaFrecuenteDTO(Object[] obj) {
        return RutaFrecuenteResponseDTO.builder()
                .origen((String) obj[0])
                .destino((String) obj[1])
                .horaSalida(((java.sql.Time) obj[2]).toLocalTime())
                .tarifa(((Number) obj[3]).longValue())
                .frecuencia(((Number) obj[4]).longValue())
                .build();
    }
}
