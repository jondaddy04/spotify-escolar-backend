package com.spotify.escolar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_canciones",
       uniqueConstraints = @UniqueConstraint(columnNames = {"playlist_id", "cancion_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistCancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cancion_id", nullable = false)
    private Cancion cancion;

    @Column(nullable = false)
    private Integer orden = 0;

    @Column(name = "agregado_en", nullable = false, updatable = false)
    private LocalDateTime agregadoEn = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        agregadoEn = LocalDateTime.now();
    }
}
