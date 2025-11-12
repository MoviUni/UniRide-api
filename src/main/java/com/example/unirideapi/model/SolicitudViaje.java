package com.example.unirideapi.model;

import com.example.unirideapi.model.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Data
@Entity
@Table(name = "SolicitudViaje")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudViaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSolicitudViaje;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estadoSolicitud;

    //FK
    @ManyToOne
    @JoinColumn(name = "idRuta", referencedColumnName = "idRuta",
            foreignKey = @ForeignKey(name = "Ruta_idRuta"))
    private Ruta ruta;

    @ManyToOne
    @JoinColumn(name = "idPasajero", referencedColumnName = "idPasajero",
            foreignKey = @ForeignKey(name = "Pasajero_idPasajero"))
    private Pasajero pasajero;
}
