package com.spotify.escolar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String correo;
    private String nombre;
    private String rol;
    private Long usuarioId;
    private String avatarUrl;
}
