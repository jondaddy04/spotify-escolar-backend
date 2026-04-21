package com.spotify.escolar.dto.response;

import lombok.Data;

@Data
public class GeneroResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private Boolean activo;
    private Long totalCanciones;
}
