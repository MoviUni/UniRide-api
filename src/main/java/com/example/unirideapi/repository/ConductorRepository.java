package com.example.unirideapi.repository;

import com.example.unirideapi.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConductorRepository extends JpaRepository<Conductor, Integer> {

    List<Conductor> findByDni(String dni);
    List<Conductor> findByIdConductor(Integer idConductor);

    boolean existsByNombreAndApellido(String nombre, String apellido);

    // Método para verificar si existe un conductor con el mismo nombre y apellido, excepto el usuario actual
    boolean existsByNombreAndApellidoAndUsuario_idUsuarioNot(String nombre, String apellido, Integer idUsuario);
    boolean existsByDniAndIdConductorNot(String dni, Integer idConductor);
    boolean existsByUsuario_IdUsuarioAndIdConductorNot(Integer idUsuario, Integer idConductor);
    boolean existsByVehiculo_IdVehiculoAndIdConductorNot(Integer idVehiculo, Integer idConductor);
}

