package com.example.unirideapi.model;

import com.example.unirideapi.model.enums.EstadoPago;
import com.example.unirideapi.model.enums.MedioPago;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "Pago")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private EstadoPago estadoPago;

    //FK
    @ManyToOne
    @JoinColumn(name = "idSolicitudViaje", referencedColumnName = "idSolicitudViaje",
            foreignKey = @ForeignKey(name = "SolicitudViaje_idSolicitudViaje"))
    private SolicitudViaje solicitudViaje;

}
