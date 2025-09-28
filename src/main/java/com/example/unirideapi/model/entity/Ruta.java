package com.example.unirideapi.model.entity;

import com.example.unirideapi.model.enums.Estado;
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

    @Column(name = "origen", nullable = false)
    private String origen;

    @Column(name = "destino", nullable = false)
    private String destino;

    @Column(name = "fechaSalida", nullable = false)
    private LocalDate fechaSalida;

    @Column(name = "horaSalida", nullable = false)
    private LocalTime horaSalida;

    @Column(name = "tarifa", nullable = false)
    private Long tarifa;

    @Column(name = "asientosDisponibles", nullable = false)
    private Integer asientosDisponibles;

    @Column(name = "descripcionVehiculo", columnDefinition = "TEXT")
    private String descripcionVehiculo;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    //FK
    @ManyToOne
    @JoinColumn(name = "idConductor", referencedColumnName = "idConductor",
            foreignKey = @ForeignKey(name = "Conductor_idConductor"))
    private Conductor conductor;

}
