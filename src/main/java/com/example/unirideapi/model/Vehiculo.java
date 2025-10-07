package com.example.unirideapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Integer idVehiculo;

    @Column(name = "placa", nullable = false)
    private String placa;

    @Column(name = "soat", nullable = false)
    private boolean soat;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "descripcion_vehiculo", columnDefinition = "TEXT")
    private String descripcionVehiculo;
}
