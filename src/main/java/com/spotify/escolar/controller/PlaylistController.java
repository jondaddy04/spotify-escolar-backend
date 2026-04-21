package com.spotify.escolar.controller;

import com.spotify.escolar.dto.request.PlaylistRequest;
import com.spotify.escolar.dto.response.MensajeResponse;
import com.spotify.escolar.dto.response.PlaylistResponse;
import com.spotify.escolar.security.JwtUtil;
import com.spotify.escolar.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired private PlaylistService playlistService;
    @Autowired private JwtUtil jwtUtil;

    // GET /api/playlists
    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> listar(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(playlistService.listarPorUsuario(uid(auth)));
    }

    // GET /api/playlists/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> obtener(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(playlistService.obtenerPorId(id, uid(auth)));
    }

    // POST /api/playlists
    @PostMapping
    public ResponseEntity<PlaylistResponse> crear(
            @Valid @RequestBody PlaylistRequest req,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(playlistService.crear(req, uid(auth)));
    }

    // PUT /api/playlists/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistResponse> editar(
            @PathVariable Long id,
            @Valid @RequestBody PlaylistRequest req,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(playlistService.editar(id, req, uid(auth)));
    }

    // DELETE /api/playlists/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> eliminar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {
        playlistService.eliminar(id, uid(auth));
        return ResponseEntity.ok(MensajeResponse.ok("Playlist eliminada"));
    }

    // POST /api/playlists/{id}/canciones/{cancionId}
    @PostMapping("/{id}/canciones/{cancionId}")
    public ResponseEntity<PlaylistResponse> agregarCancion(
            @PathVariable Long id,
            @PathVariable Long cancionId,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(playlistService.agregarCancion(id, cancionId, uid(auth)));
    }

    // DELETE /api/playlists/{id}/canciones/{cancionId}
    @DeleteMapping("/{id}/canciones/{cancionId}")
    public ResponseEntity<PlaylistResponse> quitarCancion(
            @PathVariable Long id,
            @PathVariable Long cancionId,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(playlistService.quitarCancion(id, cancionId, uid(auth)));
    }

    private Long uid(String auth) {
        return jwtUtil.getUsuarioIdDesdeToken(auth.substring(7));
    }
}
