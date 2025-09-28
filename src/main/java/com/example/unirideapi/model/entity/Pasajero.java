package com.example.unirideapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Data
@Entity
@Table(name = "Pasajero")
public class Pasajero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPasajero;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "descripcionPasajero", columnDefinition = "TEXT")
    private String descripcionPasajero;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //FK
            ;
    @OneToOne
    @JoinColumn(name = "usuario_idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    //@OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Calificacion> calificaciones;

    //NO BORRAR, descomentar cuando esté implementado junto a Citas

}
