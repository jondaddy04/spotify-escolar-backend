package com.spotify.escolar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeResponse {
    private String mensaje;
    private boolean exito;

    public static MensajeResponse ok(String mensaje) {
        return new MensajeResponse(mensaje, true);
    }

    public static MensajeResponse error(String mensaje) {
        return new MensajeResponse(mensaje, false);
    }
}
