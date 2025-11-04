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

    //Mod4: obtener las solicitudes por el id de la ruta.
    @Query("SELECT s FROM SolicitudViaje s WHERE s.ruta.idRuta = :idRuta ORDER BY s.fecha ASC, s.hora ASC")
    List<SolicitudViaje> findByRutaId(@Param("idRuta") Integer idRuta);

    @Query("SELECT b FROM SolicitudViaje b WHERE b.pasajero.idPasajero = :idUsuario")
    List<SolicitudViaje> searchByUsuario(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT b FROM SolicitudViaje b WHERE b.pasajero.idPasajero = :idPasajero AND b.ruta.idRuta = :idRuta")
    Boolean existDuplicate(@Param("idRuta") Integer idRuta, @Param("idPasajero") Integer idPasajero);

}

