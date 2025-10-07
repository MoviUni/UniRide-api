package com.example.unirideapi.repository;
import com.example.unirideapi.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    boolean existsByPlaca(String placa);
    boolean existsByConductorIdConductor(Integer idConductor);

    @Modifying
    @Transactional
    @Query("UPDATE Vehiculo v SET v.color = :color WHERE v.idVehiculo = :idVehiculo")
    int updateColorVehiculo(@Param("idVehiculo") Integer idVehiculo, @Param("color") String color);


    @Query("SELECT v FROM Vehiculo v WHERE v.conductor.idConductor = :idConductor")
    Optional<Vehiculo> findByConductorId(@Param("idConductor") Integer idConductor);
}