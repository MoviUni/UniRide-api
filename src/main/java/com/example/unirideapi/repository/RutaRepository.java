package com.example.unirideapi.repository;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.enums.EstadoRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    @Query("SELECT b FROM Ruta b WHERE b.estadoRuta != 'EN_PROGRESO'")
    List<Ruta> searchByDisponible();
    @Query("SELECT b FROM Ruta b WHERE LOWER(b.origen) = LOWER(:origen)")
    List<Ruta> searchByOrigen (@Param("origen") String origen);
    @Query("SELECT b FROM Ruta b WHERE LOWER(b.destino) = LOWER(:destino)")
    List<Ruta> searchByDestino (@Param("destino") String destino);

    @Query("SELECT b FROM Ruta b WHERE LOWER(b.destino) = LOWER(:destino) AND LOWER(b.origen) = LOWER(:origen)" +
            "AND b.horaSalida = :hora AND b.fechaSalida = :fecha")
    List<Ruta> searchBy(@Param("destino") String destino, @Param("origen") String origen, @Param("hora") LocalTime hora, @Param("fecha") LocalDate fecha);
    @Query("SELECT b FROM Ruta b WHERE b.horaSalida <= :horaSalida")
    List<Ruta> searchByHora (@Param("horaSalida") LocalTime horaSalida);

    @Query("SELECT b FROM Ruta b WHERE DATE(b.fechaSalida) = (DATE(:fechaHoy) + :filtroDias)")
    List<Ruta> searchByDia (@Param("fechaSalida") LocalDate fechaSalida, LocalDate fechaHoy, Long filtroDias);

    int countRutaByConductor_IdConductor(Integer id);

    @Query(value = "SELECT EXTRACT(DOW FROM r.fecha_salida) as dia_num, COUNT(*) " +
            "FROM ruta r " +
            "WHERE r.id_conductor = :conductorId " +
            "GROUP BY dia_num",
            nativeQuery = true)
    List<Object[]> contarViajesPorDiaSemana(@Param("conductorId") Integer conductorId);

    @Query(value = "SELECT origen, destino, hora_salida, tarifa, COUNT(*) AS frecuencia " +
    "FROM ruta " +
    "WHERE id_conductor = :conductorId " +
    "GROUP BY origen, destino, hora_salida, tarifa " +
    "ORDER BY frecuencia DESC", nativeQuery = true)
    List<Object[]> findRutasMasFrecuentes(@Param("conductorId") Integer conductorId);

    List<Ruta> findRutaByConductor_IdConductor(Integer idConductor);

    @Query(value = "SELECT r.origen, r.destino, r.fecha_salida, r.hora_salida, r.tarifa " +
            "FROM ruta r " +
            "WHERE r.id_conductor = :conductorId " +
            "ORDER BY r.fecha_salida DESC, r.hora_salida DESC",
            nativeQuery = true)
    List<Object[]> exportarPDF(@Param("conductorId") Integer conductorId);
  
   @Modifying
    @Transactional
    @Query("UPDATE Ruta r SET r.estadoRuta = :estado WHERE r.idRuta = :idRuta")
    int updateEstadoRuta(@Param("idRuta") Integer idRuta, @Param("estado") EstadoRuta estado);



    // Historial de viajes realizados por un conductor
    @Query(value = "SELECT r.origen, r.destino, r.fecha_salida, r.hora_salida, r.tarifa " +
            "FROM ruta r " +
            "WHERE r.id_conductor = :usuarioId " +
            "ORDER BY r.fecha_salida DESC, r.hora_salida DESC",
            nativeQuery = true)
    List<Object[]> findHistorialByConductor(@Param("usuarioId") Integer usuarioId);

    // Historial de viajes en los que el pasajero participó
    @Query(value = "SELECT r.origen, r.destino, r.fecha_salida, r.hora_salida, r.tarifa " +
            "FROM ruta r " +
            "JOIN pasajero p ON p.id_ruta = r.id_ruta " +
            "WHERE p.id_usuario = :usuarioId " +
            "ORDER BY r.fecha_salida DESC, r.hora_salida DESC",
            nativeQuery = true)
    List<Object[]> findHistorialByPasajero(@Param("usuarioId") Integer usuarioId);

    List<Ruta> findByConductor_IdConductor(Integer idConductor);

    List<Ruta> findByConductor_IdConductorAndEstadoRuta(Integer idConductor, EstadoRuta estadoRuta);


}



