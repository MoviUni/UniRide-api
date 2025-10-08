package com.example.unirideapi.repository;

import com.example.unirideapi.model.Rol;
import com.example.unirideapi.model.enums.ERol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    //Buscar un rol por su nombre(usando el enum)
    Optional<Rol> findByName(ERol name);
}
