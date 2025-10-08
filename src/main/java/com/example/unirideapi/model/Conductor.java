package com.example.unirideapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Data
@Entity
@Table(name = "Conductor")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Conductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConductor;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String dni;

    private Integer edad;

    @Column(name = "descripcion_conductor", columnDefinition = "TEXT")

    private String descripcionConductor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Relación con Vehiculo
    @OneToOne(mappedBy = "conductor", fetch = FetchType.LAZY)
    private Vehiculo vehiculo;

    // (FK en conductor)
    @OneToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
