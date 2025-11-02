package com.example.unirideapi.unit;

import com.example.unirideapi.dto.request.VehiculoRequestDTO;
import com.example.unirideapi.dto.response.VehiculoResponseDTO;

public interface VehiculoService {
    VehiculoResponseDTO registrarVehiculo(Integer idConductor, VehiculoRequestDTO request);
    VehiculoResponseDTO actualizarColorVehiculo(Integer idVehiculo, String nuevoColor);
    VehiculoResponseDTO findVehiculoByConductorId(Integer idConductor);
}
