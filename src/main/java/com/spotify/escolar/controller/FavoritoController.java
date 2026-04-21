package com.spotify.escolar.controller;

import com.spotify.escolar.dto.response.CancionResponse;
import com.spotify.escolar.dto.response.MensajeResponse;
import com.spotify.escolar.security.JwtUtil;
import com.spotify.escolar.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired private FavoritoService favoritoService;
    @Autowired private JwtUtil jwtUtil;

    // GET /api/favoritos
    @GetMapping
    public ResponseEntity<List<CancionResponse>> listar(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(favoritoService.listarFavoritos(uid(auth)));
    }

    // POST /api/favoritos/{cancionId}  → toggle (agrega o quita)
    @PostMapping("/{cancionId}")
    public ResponseEntity<MensajeResponse> toggle(
            @PathVariable Long cancionId,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(favoritoService.toggleFavorito(uid(auth), cancionId));
    }

    private Long uid(String auth) {
        return jwtUtil.getUsuarioIdDesdeToken(auth.substring(7));
    }
}
