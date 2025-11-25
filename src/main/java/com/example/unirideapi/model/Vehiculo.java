package com.example.unirideapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVehiculo;

    @NotBlank(message = "La placa es obligatoria")
    @Column(nullable = false, unique = true)
    private String placa;

    private String color;
    private String modelo;
    private String marca;
    @NotNull(message = "El campo SOAT no puede ser nulo")
    private Boolean soat;

    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Column(nullable = false)
    private Integer capacidad;

    // FK hacia Conductor
    @OneToOne
    @JoinColumn(name = "idConductor", referencedColumnName = "idConductor")
    private Conductor conductor;

}
