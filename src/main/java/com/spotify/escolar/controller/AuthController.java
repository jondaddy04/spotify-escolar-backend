package com.spotify.escolar.controller;

import com.spotify.escolar.dto.request.LoginRequest;
import com.spotify.escolar.dto.request.RegisterRequest;
import com.spotify.escolar.dto.request.VerificarRequest;
import com.spotify.escolar.dto.response.AuthResponse;
import com.spotify.escolar.dto.response.MensajeResponse;
import com.spotify.escolar.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

    // POST /api/auth/registro
    @PostMapping("/registro")
    public ResponseEntity<MensajeResponse> registrar(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.registrar(req));
    }

    // POST /api/auth/verificar
    @PostMapping("/verificar")
    public ResponseEntity<MensajeResponse> verificar(@Valid @RequestBody VerificarRequest req) {
        return ResponseEntity.ok(authService.verificarCuenta(req));
    }

    // POST /api/auth/reenviar-codigo?correo=xxx
    @PostMapping("/reenviar-codigo")
    public ResponseEntity<MensajeResponse> reenviarCodigo(@RequestParam String correo) {
        return ResponseEntity.ok(authService.reenviarCodigo(correo));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
