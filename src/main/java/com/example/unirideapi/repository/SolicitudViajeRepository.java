package com.example.unirideapi.repository;

import com.example.unirideapi.model.SolicitudViaje;
import com.example.unirideapi.model.enums.EstadoSolicitud;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudViajeRepository extends JpaRepository<SolicitudViaje, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE SolicitudViaje s SET s.estadoSolicitud = :estado, s.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE s.idSolicitudViaje = :idSolicitud")
    int actualizarEstadoSolicitud(@Param("idSolicitud") Integer idSolicitud,
                                  @Param("estado") EstadoSolicitud estado);

    @Query("SELECT b FROM SolicitudViaje b WHERE b.pasajero.idPasajero = :idUsuario")
    List<SolicitudViaje> searchByUsuario(@Param("idUsuario") Integer idUsuario);

}

