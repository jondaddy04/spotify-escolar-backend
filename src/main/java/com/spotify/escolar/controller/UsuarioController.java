package com.spotify.escolar.controller;

import com.spotify.escolar.dto.request.PerfilRequest;
import com.spotify.escolar.dto.response.*;
import com.spotify.escolar.security.JwtUtil;
import com.spotify.escolar.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private RecomendacionService recomendacionService;
    @Autowired private CloudinaryService cloudinaryService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/api/usuario/perfil")
    public ResponseEntity<AuthResponse> perfil(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(uid(auth)));
    }

    @PutMapping("/api/usuario/perfil")
    public ResponseEntity<AuthResponse> editarPerfil(
            @Valid @RequestBody PerfilRequest req,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(usuarioService.editarPerfil(uid(auth), req));
    }

    @PostMapping("/api/usuario/perfil/avatar")
    public ResponseEntity<MensajeResponse> subirAvatar(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestHeader("Authorization") String auth) {
        Long userId = uid(auth);
        String url = cloudinaryService.subirImagen(archivo, "avatares");
        PerfilRequest req = new PerfilRequest();
        req.setNombre(usuarioService.obtenerPerfil(userId).getNombre());
        req.setAvatarUrl(url);
        usuarioService.editarPerfil(userId, req);
        return ResponseEntity.ok(new MensajeResponse(url, true));
    }

    @GetMapping("/api/usuario/dashboard")
    public ResponseEntity<DashboardResponse> dashboard(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(usuarioService.obtenerDashboard(uid(auth)));
    }

    @GetMapping("/api/usuario/recomendaciones")
    public ResponseEntity<List<CancionResponse>> recomendaciones(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(recomendacionService.obtenerRecomendaciones(uid(auth)));
    }

    @GetMapping("/api/admin/usuarios")
    public ResponseEntity<List<AuthResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/api/admin/dashboard")
    public ResponseEntity<DashboardResponse> dashboardAdmin() {
        return ResponseEntity.ok(usuarioService.obtenerDashboardAdmin());
    }

    private Long uid(String auth) {
        return jwtUtil.getUsuarioIdDesdeToken(auth.substring(7));
    }
}
