package com.spotify.escolar.service;

import com.spotify.escolar.dto.request.LoginRequest;
import com.spotify.escolar.dto.request.RegisterRequest;
import com.spotify.escolar.dto.request.VerificarRequest;
import com.spotify.escolar.dto.response.AuthResponse;
import com.spotify.escolar.dto.response.MensajeResponse;
import com.spotify.escolar.entity.Rol;
import com.spotify.escolar.entity.Usuario;
import com.spotify.escolar.entity.VerificacionCorreo;
import com.spotify.escolar.repository.RolRepository;
import com.spotify.escolar.repository.UsuarioRepository;
import com.spotify.escolar.repository.VerificacionCorreoRepository;
import com.spotify.escolar.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private RolRepository rolRepo;
    @Autowired private VerificacionCorreoRepository verificacionRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private CorreoService correoService;

    @Transactional
    public MensajeResponse registrar(RegisterRequest req) {
        if (usuarioRepo.existsByCorreo(req.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Rol rolUsuario = rolRepo.findByNombre("USUARIO")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setRol(rolUsuario);
        usuario.setNombre(req.getNombre());
        usuario.setCorreo(req.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(req.getContrasena()));
        usuario.setVerificado(false);
        usuario.setActivo(true);
        usuarioRepo.save(usuario);

        // Generar y enviar código
        String codigo = generarCodigo();
        VerificacionCorreo verif = new VerificacionCorreo();
        verif.setUsuario(usuario);
        verif.setCodigo(codigo);
        verif.setExpiraEn(LocalDateTime.now().plusMinutes(15));
        verif.setUsado(false);
        verificacionRepo.save(verif);

        correoService.enviarCodigoVerificacion(usuario.getCorreo(), usuario.getNombre(), codigo);

        return MensajeResponse.ok("Registro exitoso. Revisa tu correo para verificar tu cuenta.");
    }

    @Transactional
    public MensajeResponse verificarCuenta(VerificarRequest req) {
        Usuario usuario = usuarioRepo.findByCorreo(req.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        VerificacionCorreo verif = verificacionRepo
                .findTopByUsuarioIdAndUsadoFalseOrderByIdDesc(usuario.getId())
                .orElseThrow(() -> new RuntimeException("No hay código pendiente para este usuario"));

        if (verif.getExpiraEn().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El código ha expirado. Solicita uno nuevo.");
        }

        if (!verif.getCodigo().equals(req.getCodigo())) {
            throw new RuntimeException("Código incorrecto");
        }

        verif.setUsado(true);
        verificacionRepo.save(verif);

        usuario.setVerificado(true);
        usuarioRepo.save(usuario);

        return MensajeResponse.ok("Cuenta verificada correctamente. Ya puedes iniciar sesión.");
    }

    public AuthResponse login(LoginRequest req) {
        Usuario usuario = usuarioRepo.findByCorreo(req.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Cuenta desactivada");
        }

        if (!usuario.getVerificado()) {
            throw new RuntimeException("Debes verificar tu cuenta antes de iniciar sesión");
        }

        if (!passwordEncoder.matches(req.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }

        String token = jwtUtil.generarToken(
                usuario.getCorreo(),
                usuario.getRol().getNombre(),
                usuario.getId()
        );

        return new AuthResponse(
                token,
                usuario.getCorreo(),
                usuario.getNombre(),
                usuario.getRol().getNombre(),
                usuario.getId(),
                usuario.getAvatarUrl()
        );
    }

    @Transactional
    public MensajeResponse reenviarCodigo(String correo) {
        Usuario usuario = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getVerificado()) {
            throw new RuntimeException("La cuenta ya está verificada");
        }

        verificacionRepo.deleteByUsuarioId(usuario.getId());

        String codigo = generarCodigo();
        VerificacionCorreo verif = new VerificacionCorreo();
        verif.setUsuario(usuario);
        verif.setCodigo(codigo);
        verif.setExpiraEn(LocalDateTime.now().plusMinutes(15));
        verif.setUsado(false);
        verificacionRepo.save(verif);

        correoService.enviarCodigoVerificacion(usuario.getCorreo(), usuario.getNombre(), codigo);

        return MensajeResponse.ok("Código reenviado. Revisa tu correo.");
    }

    private String generarCodigo() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
