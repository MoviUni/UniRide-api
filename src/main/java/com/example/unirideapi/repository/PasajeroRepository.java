package com.example.unirideapi.repository;

import com.example.unirideapi.model.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero,Long> {

    // JPQL: Buscar un pasajero por el id de usuario
    @Query("SELECT p FROM Pasajero p WHERE p.usuario.id = :userId")
    Optional<Pasajero> findByUserId(@Param("userId") Long userId);

    // JPQL: Buscar pasajeros cuyo nombre contenga un texto (case insensitive)
    @Query("SELECT p FROM Pasajero p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Pasajero> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT COUNT(p) > 0 FROM Pasajero p WHERE p.usuario.id = :userId")
    boolean existsByUserId(@Param("userId") Long userId);
}
