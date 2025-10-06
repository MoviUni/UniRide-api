package com.example.unirideapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

@Setter
@Data
@Entity
@Table(name = "Conductor")
public class Conductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConductor;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "disponibilidad")
    private String disponibilidad;

    @Column(name = "descripcion_conductor", columnDefinition = "TEXT")
    private String descripcionConductor;

    @OneToOne
    @JoinColumn(name = "usuario_idVehiculo", referencedColumnName = "idVehiculo")
    private Vehiculo vehiculo;

    @OneToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario", nullable = false, unique = true)
    private Usuario usuario;
}
