package com.spotify.escolar.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlaylistResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime createdAt;
    private int totalCanciones;
    private List<CancionResponse> canciones;
}
