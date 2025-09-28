package com.example.unirideapi.model.entity;

import com.example.unirideapi.model.enums.Estado;
import com.example.unirideapi.model.enums.MedioPago;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "Pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRuta;

    @Column(name = "monto", nullable = false)
    private String monto;

    @Column(name = "destino", nullable = false)
    private String destino;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "tarifa", nullable = false)
    private Long tarifa;

    @Column(name = "asientosDisponibles", nullable = false)
    private Integer asientosDisponibles;

    @Column(name = "descripcionVehiculo", columnDefinition = "TEXT")
    private String descripcionVehiculo;

    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    //FK
    @ManyToOne
    @JoinColumn(name = "idSolicitudViaje", referencedColumnName = "idSolicitudViaje",
            foreignKey = @ForeignKey(name = "SolicitudViaje_idSolicitudViaje"))
    private SolicitudViaje solicitudViaje;

}
