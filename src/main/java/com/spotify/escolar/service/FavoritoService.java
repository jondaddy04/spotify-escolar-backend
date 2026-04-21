package com.spotify.escolar.service;

import com.spotify.escolar.dto.response.CancionResponse;
import com.spotify.escolar.dto.response.MensajeResponse;
import com.spotify.escolar.entity.*;
import com.spotify.escolar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritoService {

    @Autowired private FavoritoRepository favoritoRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private CancionRepository cancionRepo;
    @Autowired private CancionService cancionService;

    public List<CancionResponse> listarFavoritos(Long usuarioId) {
        return favoritoRepo.findByUsuarioId(usuarioId).stream()
                .map(f -> cancionService.toResponse(f.getCancion(), usuarioId))
                .collect(Collectors.toList());
    }

    @Transactional
    public MensajeResponse toggleFavorito(Long usuarioId, Long cancionId) {
        var existente = favoritoRepo.findByUsuarioIdAndCancionId(usuarioId, cancionId);

        if (existente.isPresent()) {
            favoritoRepo.deleteByUsuarioIdAndCancionId(usuarioId, cancionId);
            return MensajeResponse.ok("Eliminado de favoritos");
        } else {
            Usuario usuario = usuarioRepo.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Cancion cancion = cancionRepo.findById(cancionId)
                    .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

            Favorito favorito = new Favorito();
            favorito.setUsuario(usuario);
            favorito.setCancion(cancion);
            favoritoRepo.save(favorito);
            return MensajeResponse.ok("Agregado a favoritos");
        }
    }
}
