package com.spotify.escolar.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PerfilRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100)
    private String nombre;

    private String avatarUrl;
}
