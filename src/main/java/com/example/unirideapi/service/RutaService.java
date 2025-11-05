package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.RutaRequestDTO;
import com.example.unirideapi.dto.response.RutaResponseDTO;
import com.example.unirideapi.dto.response.RutaFrecuenteResponseDTO;
import com.example.unirideapi.model.enums.EstadoRuta;

import java.util.List;
import java.util.Map;

public interface RutaService {

    // Total de viajes realizados
    int obtenerTotalViajes(Integer idConductor);

    // Frecuencia de viajes por día de la semana
    Map<String, Integer> obtenerFrecuenciaViajesPorPasajero(Integer idConductor);

    // Consultar rutas más utilizadas
    List<RutaFrecuenteResponseDTO> obtenerRutasMasFrecuentes(Integer conductorId);

    // Exportar historial de viajes PDF
    byte[] exportarHistorialPdf(Integer conductorId);

    // ====== ESTADO ======
    RutaResponseDTO updateEstadoRuta(Integer idRuta, EstadoRuta nuevoEstado);

    // ====== CREAR / BUSCAR ======
    RutaResponseDTO create(RutaRequestDTO rutaRequestDTO);
    RutaResponseDTO searchById(Long id);
    List<RutaResponseDTO> searchByOrigen(String origen);
    List<RutaResponseDTO> searchByDestino(String destino);
    List<RutaResponseDTO> searchByHora(String hora);
    List<RutaResponseDTO> searchBy(String destino, String origen, String hora, String fecha);
    List<RutaResponseDTO> searchByDisponible();
    List<RutaResponseDTO> findAll();
    List<RutaResponseDTO> obtenerHistorialViajes(Integer idUsuario, String rol);

    // ====== CONDUCTOR: listar ======
    List<RutaResponseDTO> listarRutasDelConductor(Integer idConductor);
    List<RutaResponseDTO> listarRutasDelConductorPorEstado(Integer idConductor, EstadoRuta estado);

    // ====== CONDUCTOR: actualizar / eliminar ======
    // Delegador sin confirmación (coincide con tu implementación)
    RutaResponseDTO actualizarRutaFull(Long idRuta, Integer idConductor, RutaRequestDTO dto);
    void eliminarRutaDeConductor(Long idRuta, Integer idConductor);

    // Versiones con confirmación (también están en tu implementación)
    RutaResponseDTO actualizarRutaFull(Long idRuta, Integer idConductor, RutaRequestDTO dto, boolean confirmarCambios);
    void eliminarRutaDeConductor(Long idRuta, Integer idConductor, boolean confirmar);

    // === NUEVAS FIRMAS (no reemplazan nada existente) ===
    RutaResponseDTO publicarRutaComoConductor(RutaRequestDTO dto);

    RutaResponseDTO actualizarRutaFullConReglas(Long idRuta,
                                                Integer idConductor,
                                                RutaRequestDTO dto,
                                                boolean confirmarCambios);

    void eliminarRutaDeConductorConReglas(Long idRuta,
                                          Integer idConductor,
                                          boolean confirmar);

    List<RutaResponseDTO> consultarRutasPublicadasComoConductor(Integer idConductor);

}
