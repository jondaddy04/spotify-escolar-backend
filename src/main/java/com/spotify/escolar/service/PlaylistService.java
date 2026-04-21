package com.spotify.escolar.service;

import com.spotify.escolar.dto.request.PlaylistRequest;
import com.spotify.escolar.dto.response.PlaylistResponse;
import com.spotify.escolar.entity.*;
import com.spotify.escolar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    @Autowired private PlaylistRepository playlistRepo;
    @Autowired private PlaylistCancionRepository playlistCancionRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private CancionRepository cancionRepo;
    @Autowired private CancionService cancionService;

    public List<PlaylistResponse> listarPorUsuario(Long usuarioId) {
        return playlistRepo.findByUsuarioId(usuarioId).stream()
                .map(p -> toResponse(p, usuarioId))
                .collect(Collectors.toList());
    }

    public PlaylistResponse obtenerPorId(Long id, Long usuarioId) {
        Playlist playlist = playlistRepo.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
        return toResponse(playlist, usuarioId);
    }

    public PlaylistResponse crear(PlaylistRequest req, Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Playlist playlist = new Playlist();
        playlist.setUsuario(usuario);
        playlist.setNombre(req.getNombre());
        playlist.setDescripcion(req.getDescripcion());
        playlistRepo.save(playlist);
        return toResponse(playlist, usuarioId);
    }

    public PlaylistResponse editar(Long id, PlaylistRequest req, Long usuarioId) {
        Playlist playlist = playlistRepo.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
        playlist.setNombre(req.getNombre());
        playlist.setDescripcion(req.getDescripcion());
        playlistRepo.save(playlist);
        return toResponse(playlist, usuarioId);
    }

    public void eliminar(Long id, Long usuarioId) {
        Playlist playlist = playlistRepo.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
        playlistRepo.delete(playlist);
    }

    @Transactional
    public PlaylistResponse agregarCancion(Long playlistId, Long cancionId, Long usuarioId) {
        Playlist playlist = playlistRepo.findByIdAndUsuarioId(playlistId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        if (playlistCancionRepo.existsByPlaylistIdAndCancionId(playlistId, cancionId)) {
            throw new RuntimeException("La canción ya está en esta playlist");
        }

        Cancion cancion = cancionRepo.findById(cancionId)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        PlaylistCancion pc = new PlaylistCancion();
        pc.setPlaylist(playlist);
        pc.setCancion(cancion);
        pc.setOrden(playlist.getCanciones().size());
        playlistCancionRepo.save(pc);

        return toResponse(playlistRepo.findById(playlistId).get(), usuarioId);
    }

    @Transactional
    public PlaylistResponse quitarCancion(Long playlistId, Long cancionId, Long usuarioId) {
        playlistRepo.findByIdAndUsuarioId(playlistId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
        playlistCancionRepo.deleteByPlaylistIdAndCancionId(playlistId, cancionId);
        return toResponse(playlistRepo.findById(playlistId).get(), usuarioId);
    }

    private PlaylistResponse toResponse(Playlist p, Long usuarioId) {
        PlaylistResponse r = new PlaylistResponse();
        r.setId(p.getId());
        r.setNombre(p.getNombre());
        r.setDescripcion(p.getDescripcion());
        r.setCreatedAt(p.getCreatedAt());
        r.setTotalCanciones(p.getCanciones().size());
        r.setCanciones(p.getCanciones().stream()
                .map(pc -> cancionService.toResponse(pc.getCancion(), usuarioId))
                .collect(Collectors.toList()));
        return r;
    }
}
