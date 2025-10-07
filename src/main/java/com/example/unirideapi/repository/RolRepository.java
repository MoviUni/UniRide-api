package com.example.unirideapi.repository;

import com.example.unirideapi.model.Rol;
import com.example.unirideapi.model.enums.ERol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    //Buscar un rol por su nombre (enum Erol)
    Optional<Rol> findByName(ERol name);

    //Verificar si existe un rol por su nombre
    boolean existsByName(ERol name);
}
