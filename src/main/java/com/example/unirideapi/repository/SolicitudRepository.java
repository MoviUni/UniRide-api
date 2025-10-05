package com.example.unirideapi.repository;

import com.example.unirideapi.model.SolicitudViaje;
import com.example.unirideapi.model.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudViaje, Integer> {

    //Mod4: obtener las solicitudes por el id de la ruta.
    @Query("SELECT s FROM SolicitudViaje s WHERE s.ruta.idRuta = :idRuta ORDER BY s.fecha DESC, s.hora DESC")
    List<SolicitudViaje> findByRutaId(@Param("idRuta") Integer idRuta);

    @Modifying
    @Transactional
    @Query("UPDATE SolicitudViaje s SET s.estado = :estado, s.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE s.idSolicitudViaje = :idSolicitud")
    int actualizarEstadoSolicitud(@Param("idSolicitud") Integer idSolicitud,
                                  @Param("estado") Estado estado);
}

