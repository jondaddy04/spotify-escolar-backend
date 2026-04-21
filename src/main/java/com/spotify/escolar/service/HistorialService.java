package com.spotify.escolar.service;

import com.spotify.escolar.entity.*;
import com.spotify.escolar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistorialService {

    @Autowired private HistorialReproduccionRepository historialRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private CancionRepository cancionRepo;

    @Transactional
    public void registrarReproduccion(Long usuarioId, Long cancionId) {
        var existente = historialRepo.findByUsuarioIdAndCancionId(usuarioId, cancionId);

        if (existente.isPresent()) {
            HistorialReproduccion h = existente.get();
            h.setVeces(h.getVeces() + 1);
            h.setReproducidoEn(java.time.LocalDateTime.now());
            historialRepo.save(h);
        } else {
            Usuario usuario = usuarioRepo.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Cancion cancion = cancionRepo.findById(cancionId)
                    .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

            HistorialReproduccion h = new HistorialReproduccion();
            h.setUsuario(usuario);
            h.setCancion(cancion);
            h.setVeces(1);
            h.setReproducidoEn(java.time.LocalDateTime.now());
            historialRepo.save(h);
        }
    }
}
