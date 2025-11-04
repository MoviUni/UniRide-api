package com.example.unirideapi.repository;

import com.example.unirideapi.model.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasajeroRepository extends JpaRepository<Pasajero, Integer> {

    List<Pasajero> findByDni(String dni);
    List<Pasajero> findByIdPasajero(Integer idPasajero);
    boolean existsByDni(String dni);

    boolean existsByNombreAndApellido(String nombre, String apellido);

    // Método para verificar si existe un pasajero con el mismo nombre y apellido, excepto el usuario actual
    boolean existsByNombreAndApellidoAndUsuario_idUsuarioNot(String nombre, String apellido, Integer idUsuario);
    boolean existsByDniAndIdPasajeroNot(String dni, Integer idPasajero);
    boolean existsByUsuario_IdUsuarioAndIdPasajeroNot(Integer idUsuario, Integer idPasajero);
}

