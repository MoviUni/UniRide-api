package com.example.unirideapi.repository;

import com.example.unirideapi.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    // JPQL: Buscar conductores cuyo nombre contenga un texto (case insensitive)
    @Query("SELECT c FROM Conductor c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Conductor> findByNameContainingIgnoreCase(@Param("name") String name);

    // Verifica si un usuario ya tiene un perfil de autor
    @Query("SELECT COUNT(c) > 0 FROM Conductor c WHERE c.usuario.id = :userId")
    boolean existsByUserId(@Param("userId") Long userId);
}
