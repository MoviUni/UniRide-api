package com.example.unirideapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pasajero pasajero;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Conductor conductor;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRol", referencedColumnName = "idRol",
            foreignKey = @ForeignKey(name = "Rol_idRol"))
    private Rol rol;


}