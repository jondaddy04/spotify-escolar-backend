package com.spotify.escolar.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class DashboardResponse {
    // Para usuario
    private String nombreUsuario;
    private String avatarUrl;
    private int totalPlaylists;
    private int totalFavoritos;
    private int totalReproducidas;
    private List<CancionResponse> recomendaciones;
    private List<CancionResponse> recienEscuchadas;
    private List<GeneroResponse> generos;

    // Para admin
    private Long totalCanciones;
    private Long totalGeneros;
    private Long totalUsuarios;
}
