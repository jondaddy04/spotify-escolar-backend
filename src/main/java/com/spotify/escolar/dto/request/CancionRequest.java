package com.spotify.escolar.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CancionRequest {
    @NotNull(message = "El género es requerido")
    private Long generoId;

    @NotBlank(message = "El título es requerido")
    @Size(max = 200)
    private String titulo;

    @NotBlank(message = "El autor es requerido")
    @Size(max = 150)
    private String autor;

    @NotBlank(message = "El artista es requerido")
    @Size(max = 150)
    private String artista;

    @Size(max = 150)
    private String album;

    @NotNull(message = "La duración es requerida")
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private Integer duracionSeg;

    private String fechaLanzamiento; // formato: yyyy-MM-dd

    private String imagenUrl;

    @NotBlank(message = "La URL del audio es requerida")
    private String audioUrl;
}
