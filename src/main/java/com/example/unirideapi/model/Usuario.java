package com.example.unirideapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(
            name="role_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_user_role")
    )
    private Rol role;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Conductor conductor;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Pasajero pasajero;
}