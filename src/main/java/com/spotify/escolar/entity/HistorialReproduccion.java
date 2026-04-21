package com.spotify.escolar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_reproduccion",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "cancion_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialReproduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cancion_id", nullable = false)
    private Cancion cancion;

    @Column(name = "reproducido_en", nullable = false)
    private LocalDateTime reproducidoEn = LocalDateTime.now();

    @Column(nullable = false)
    private Integer veces = 1;
}
