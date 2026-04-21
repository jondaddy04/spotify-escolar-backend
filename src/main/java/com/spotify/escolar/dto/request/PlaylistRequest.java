package com.spotify.escolar.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PlaylistRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 150)
    private String nombre;

    @Size(max = 300)
    private String descripcion;
}
