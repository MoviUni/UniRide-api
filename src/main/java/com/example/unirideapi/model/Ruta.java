package com.example.unirideapi.model;

import com.example.unirideapi.model.enums.EstadoRuta;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "Ruta")
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRuta;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDate fechaSalida;

    @Column(nullable = false)
    private LocalTime horaSalida;

    @Column(nullable = false)
    private Long tarifa;

    @Column(nullable = false)
    private Integer asientosDisponibles;

    @Enumerated(EnumType.STRING)
    private EstadoRuta estadoRuta;

    //FK
    @ManyToOne
    @JoinColumn(name = "idConductor", referencedColumnName = "idConductor",
            foreignKey = @ForeignKey(name = "Conductor_idConductor"))
    private Conductor conductor;

}