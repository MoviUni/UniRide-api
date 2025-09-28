package com.example.unirideapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "descripcionVehiculo", columnDefinition = "TEXT")
    private String descripcionVehiculo;

}
