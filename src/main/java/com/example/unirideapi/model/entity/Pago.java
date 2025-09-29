package com.example.unirideapi.model.entity;

import com.example.unirideapi.enums.Estado;
import com.example.unirideapi.enums.MedioPago;
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
    private Integer idPago;

    @Column(name = "monto", nullable = false)
    private Float monto;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "comision", nullable = false)
    private Float comision;

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
