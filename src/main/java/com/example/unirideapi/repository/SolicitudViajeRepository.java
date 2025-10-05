package com.example.unirideapi.repository;

import com.example.unirideapi.model.SolicitudViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudViajeRepository extends JpaRepository<SolicitudViaje, Long> {
}
