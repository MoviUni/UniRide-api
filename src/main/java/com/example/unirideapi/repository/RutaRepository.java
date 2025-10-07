package com.example.unirideapi.repository;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Ruta r SET r.estado = :estado WHERE r.idRuta = :idRuta")
    int updateEstadoRuta(@Param("idRuta") Integer idRuta, @Param("estado") Estado estado);
}