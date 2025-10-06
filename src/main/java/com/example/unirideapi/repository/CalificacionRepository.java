package com.example.unirideapi.repository;

import com.example.unirideapi.model.Calificacion;
import com.example.unirideapi.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    List<Calificacion> findCalificacionByConductor_IdConductor(Integer idConductor);
    List<Calificacion> findCalificacionByPasajero_IdPasajero(Integer idPasajero);
}
