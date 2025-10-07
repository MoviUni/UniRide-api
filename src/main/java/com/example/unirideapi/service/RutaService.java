package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;

import java.util.List;
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.model.enums.EstadoRuta;
import org.springframework.transaction.annotation.Transactional;

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

    RutaResponseDTO updateEstadoRuta(Integer idRuta, EstadoRuta nuevoEstado);


public interface RutaService {
    RutaResponseDTO create(RutaRequestDTO rutaRequestDTO);
    RutaResponseDTO searchById(Long id);
    List<RutaResponseDTO> searchByOrigen(String origen);
    List<RutaResponseDTO> searchByDestino(String destino);
    List<RutaResponseDTO> searchByHora(String hora);
    List<RutaResponseDTO> searchByDisponible();
    List<RutaResponseDTO> searchBy(String destino, String origen, String hora, String fecha);
    List<RutaResponseDTO> findAll();
}
