package com.spotify.escolar.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CancionResponse {
    private Long id;
    private Long generoId;
    private String generoNombre;
    private String titulo;
    private String autor;
    private String artista;
    private String album;
    private Integer duracionSeg;
    private String duracionFormato; // "3:45"
    private LocalDate fechaLanzamiento;
    private String imagenUrl;
    private String audioUrl;
    private LocalDateTime createdAt;
    private boolean esFavorita;
}
