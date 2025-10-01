package com.example.unirideapi.repository;

import com.example.unirideapi.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    @Query("SELECT b FROM Ruta b WHERE LOWER(b.origen) = LOWER(:origen)")
    Optional<Ruta> searchByOrigen (@Param("origen") String origen);
    @Query("SELECT b FROM Ruta b WHERE LOWER(b.destino) = LOWER(:destino)")
    Optional<Ruta> searchByDestino (@Param("destino") String destino);

    @Query("SELECT b FROM Ruta b WHERE b.horaSalida < :horaSalida")
    Optional<Ruta> searchByHora (@Param("horaSalida") LocalTime horaSalida);

    @Query("SELECT b FROM Ruta b WHERE DATE(b.fechaSalida) = (DATE(:fechaHoy) + :filtroDias)")
    Optional<Ruta> searchByDia (@Param("fechaSalida") LocalDate fechaSalida, LocalDate fechaHoy, Long filtroDias);
}
