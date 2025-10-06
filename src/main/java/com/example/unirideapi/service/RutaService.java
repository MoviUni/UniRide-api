package com.example.unirideapi.service;

import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;

import java.util.List;
import java.util.Map;

public interface RutaService {
    //Total de viajes realizados
    int obtenerTotalViajes(Integer idConductor);

    //Frecuencia de viajes por día de la semana
    Map<String, Integer> obtenerFrecuenciaViajesPorPasajero(Integer pasajeroId);

    //Consultar rutas más utilizadas
    List<RutaFrecuenteResponseDTO> obtenerRutasMasFrecuentes(Integer conductorId);

    //Exportar historial de viajes PDF
    byte[] exportarHistorialPdf(Integer conductorId);


}
