package com.example.unirideapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "pasajero", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dni"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pasajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasajero")
    private Long idPasajero;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @NotBlank
    @Size(max = 20)
    @Column(name = "dni", nullable = false, length = 20, unique = true)
    private String dni;

    @Min(0)
    @Column(name = "edad")
    private Integer edad;

    @Column(name = "descripcion_pasajero", columnDefinition = "TEXT")
    private String descripcionPasajero;

    // FK hacia Usuario: un Pasajero está ligado a un Usuario (cuenta).
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id_usuario", nullable = false, unique = true)
    private Usuario usuario;
}
