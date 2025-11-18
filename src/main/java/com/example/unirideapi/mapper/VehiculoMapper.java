package com.example.unirideapi.mapper;

import com.example.unirideapi.dto.request.VehiculoRequestDTO;
import com.example.unirideapi.dto.response.VehiculoResponseDTO;
import com.example.unirideapi.model.Vehiculo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehiculoMapper {

    private final ModelMapper modelMapper;

    public Vehiculo toEntity(VehiculoRequestDTO dto) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(dto.placa());
        vehiculo.setSoat(dto.soat());
        vehiculo.setModelo(dto.modelo());
        vehiculo.setMarca(dto.marca());
        vehiculo.setColor(dto.color());
        vehiculo.setCapacidad(dto.capacidad());
        vehiculo.setDescripcionVehiculo(dto.descripcionVehiculo());
        return vehiculo;
    }


    public VehiculoResponseDTO toDTO(Vehiculo vehiculo) {
        return new VehiculoResponseDTO(
                vehiculo.getPlaca(),
                vehiculo.getSoat(),
                vehiculo.getModelo(),
                vehiculo.getMarca(),
                vehiculo.getColor(),
                vehiculo.getCapacidad(),
                vehiculo.getDescripcionVehiculo(),
                vehiculo.getConductor().getIdConductor(),
                vehiculo.getIdVehiculo()
        );
    }
}

