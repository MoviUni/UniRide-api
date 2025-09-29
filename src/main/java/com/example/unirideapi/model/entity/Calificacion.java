package com.example.unirideapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Data
@Entity
@Table(name = "Calificacion")
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCalificacion;

    @Column(name = "puntaje", nullable = false)
    private Integer puntaje;

    @Column(name = "comentario", nullable = false)
    private String comentario;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //FK
    @ManyToOne
    @JoinColumn(name = "idConductor", referencedColumnName = "idConductor",
            foreignKey = @ForeignKey(name = "Conductor_idConductor"))
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "idPasajero", referencedColumnName = "idPasajero",
            foreignKey = @ForeignKey(name = "Pasajero_idPasajero"))
    private Pasajero pasajero;
}
