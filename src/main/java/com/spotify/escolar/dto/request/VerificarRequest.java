package com.spotify.escolar.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerificarRequest {
    @NotBlank(message = "El correo es requerido")
    private String correo;

    @NotBlank(message = "El código es requerido")
    private String codigo;
}
