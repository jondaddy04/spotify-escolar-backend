package com.spotify.escolar.controller;

import com.spotify.escolar.dto.request.CancionRequest;
import com.spotify.escolar.dto.response.CancionResponse;
import com.spotify.escolar.dto.response.MensajeResponse;
import com.spotify.escolar.security.JwtUtil;
import com.spotify.escolar.service.CancionService;
import com.spotify.escolar.service.HistorialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CancionController {

    @Autowired private CancionService cancionService;
    @Autowired private HistorialService historialService;
    @Autowired private JwtUtil jwtUtil;

    // GET /api/canciones
    @GetMapping("/canciones")
    public ResponseEntity<List<CancionResponse>> listar(
            @RequestHeader(value = "Authorization", required = false) String auth) {
        Long uid = extraerUsuarioId(auth);
        return ResponseEntity.ok(cancionService.listarTodas(uid));
    }

    // GET /api/canciones/{id}
    @GetMapping("/canciones/{id}")
    public ResponseEntity<CancionResponse> obtener(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        Long uid = extraerUsuarioId(auth);
        return ResponseEntity.ok(cancionService.obtenerPorId(id, uid));
    }

    // GET /api/canciones/genero/{generoId}
    @GetMapping("/canciones/genero/{generoId}")
    public ResponseEntity<List<CancionResponse>> porGenero(
            @PathVariable Long generoId,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        Long uid = extraerUsuarioId(auth);
        return ResponseEntity.ok(cancionService.listarPorGenero(generoId, uid));
    }

    // GET /api/canciones/genero/{generoId}/aleatoria
    @GetMapping("/canciones/genero/{generoId}/aleatoria")
    public ResponseEntity<CancionResponse> aleatoriaPorGenero(
            @PathVariable Long generoId,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        Long uid = extraerUsuarioId(auth);
        return ResponseEntity.ok(cancionService.obtenerAleatoriaPorGenero(generoId, uid));
    }

    // GET /api/canciones/buscar?q=texto
    @GetMapping("/canciones/buscar")
    public ResponseEntity<List<CancionResponse>> buscar(
            @RequestParam String q,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        Long uid = extraerUsuarioId(auth);
        return ResponseEntity.ok(cancionService.buscar(q, uid));
    }

    // POST /api/canciones/{id}/reproducir  → registra en historial
    @PostMapping("/canciones/{id}/reproducir")
    public ResponseEntity<MensajeResponse> reproducir(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {
        Long uid = extraerUsuarioId(auth);
        if (uid != null) historialService.registrarReproduccion(uid, id);
        return ResponseEntity.ok(MensajeResponse.ok("Reproducción registrada"));
    }

    // ---- ADMIN ----

    // POST /api/admin/canciones
    @PostMapping("/admin/canciones")
    public ResponseEntity<CancionResponse> crear(@Valid @RequestBody CancionRequest req) {
        return ResponseEntity.ok(cancionService.crear(req));
    }

    // PUT /api/admin/canciones/{id}
    @PutMapping("/admin/canciones/{id}")
    public ResponseEntity<CancionResponse> editar(
            @PathVariable Long id,
            @Valid @RequestBody CancionRequest req) {
        return ResponseEntity.ok(cancionService.editar(id, req));
    }

    // DELETE /api/admin/canciones/{id}
    @DeleteMapping("/admin/canciones/{id}")
    public ResponseEntity<MensajeResponse> eliminar(@PathVariable Long id) {
        cancionService.eliminar(id);
        return ResponseEntity.ok(MensajeResponse.ok("Canción eliminada"));
    }

    private Long extraerUsuarioId(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (jwtUtil.validarToken(token)) {
                return jwtUtil.getUsuarioIdDesdeToken(token);
            }
        }
        return null;
    }
}
