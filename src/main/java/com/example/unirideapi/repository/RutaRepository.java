package com.example.unirideapi.repository;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.enums.EstadoRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.unirideapi.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Integer> {
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
}



