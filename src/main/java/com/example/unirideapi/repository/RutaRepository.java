package com.example.unirideapi.repository;

import com.example.unirideapi.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    @Query("SELECT b FROM Ruta b WHERE LOWER(b.origen) = LOWER(:origen)")
    List<Ruta> searchByOrigen (@Param("origen") String origen);
    @Query("SELECT b FROM Ruta b WHERE LOWER(b.destino) = LOWER(:destino)")
    List<Ruta> searchByDestino (@Param("destino") String destino);

    @Query("SELECT b FROM Ruta b WHERE LOWER(b.destino) = LOWER(:destino) AND LOWER(b.origen) = LOWER(:origen)" +
            "AND b.horaSalida = :hora AND b.fechaSalida = :fecha")
    List<Ruta> searchBy(@Param("destino") String destino, @Param("origen") String origen, @Param("hora") String hora, @Param("fecha") String fecha);

    @Query("SELECT b FROM Ruta b WHERE b.horaSalida <= :horaSalida")
    List<Ruta> searchByHora (@Param("horaSalida") LocalTime horaSalida);

    @Query("SELECT b FROM Ruta b WHERE DATE(b.fechaSalida) = (DATE(:fechaHoy) + :filtroDias)")
    List<Ruta> searchByDia (@Param("fechaSalida") LocalDate fechaSalida, LocalDate fechaHoy, Long filtroDias);
}
