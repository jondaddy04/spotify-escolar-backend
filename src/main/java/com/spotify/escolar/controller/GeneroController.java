package com.spotify.escolar.controller;

import com.spotify.escolar.dto.response.GeneroResponse;
import com.spotify.escolar.dto.response.MensajeResponse;
import com.spotify.escolar.service.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GeneroController {

    @Autowired private GeneroService generoService;

    // GET /api/generos  (público)
    @GetMapping("/generos")
    public ResponseEntity<List<GeneroResponse>> listar() {
        return ResponseEntity.ok(generoService.listarActivos());
    }

    // GET /api/generos/{id}
    @GetMapping("/generos/{id}")
    public ResponseEntity<GeneroResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(generoService.obtenerPorId(id));
    }

    // ---- ADMIN ----

    // GET /api/admin/generos
    @GetMapping("/admin/generos")
    public ResponseEntity<List<GeneroResponse>> listarAdmin() {
        return ResponseEntity.ok(generoService.listarTodos());
    }

    // POST /api/admin/generos
    @PostMapping("/admin/generos")
    public ResponseEntity<GeneroResponse> crear(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(generoService.crear(
                body.get("nombre"),
                body.get("descripcion"),
                body.get("imagenUrl")
        ));
    }

    // PUT /api/admin/generos/{id}
    @PutMapping("/admin/generos/{id}")
    public ResponseEntity<GeneroResponse> editar(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(generoService.editar(
                id,
                body.get("nombre"),
                body.get("descripcion"),
                body.get("imagenUrl")
        ));
    }

    // PATCH /api/admin/generos/{id}/toggle
    @PatchMapping("/admin/generos/{id}/toggle")
    public ResponseEntity<MensajeResponse> toggle(@PathVariable Long id) {
        generoService.toggleActivo(id);
        return ResponseEntity.ok(MensajeResponse.ok("Estado actualizado"));
    }
}
