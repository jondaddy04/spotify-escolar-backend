package com.spotify.escolar.service;

import com.spotify.escolar.dto.request.PerfilRequest;
import com.spotify.escolar.dto.response.*;
import com.spotify.escolar.entity.Usuario;
import com.spotify.escolar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private PlaylistRepository playlistRepo;
    @Autowired private FavoritoRepository favoritoRepo;
    @Autowired private HistorialReproduccionRepository historialRepo;
    @Autowired private CancionRepository cancionRepo;
    @Autowired private GeneroRepository generoRepo;
    @Autowired private RecomendacionService recomendacionService;
    @Autowired private GeneroService generoService;
    @Autowired private CancionService cancionService;

    public AuthResponse obtenerPerfil(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new AuthResponse(
                null,
                usuario.getCorreo(),
                usuario.getNombre(),
                usuario.getRol().getNombre(),
                usuario.getId(),
                usuario.getAvatarUrl()
        );
    }

    public AuthResponse editarPerfil(Long usuarioId, PerfilRequest req) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(req.getNombre());
        if (req.getAvatarUrl() != null) {
            usuario.setAvatarUrl(req.getAvatarUrl());
        }
        usuarioRepo.save(usuario);
        return new AuthResponse(
                null,
                usuario.getCorreo(),
                usuario.getNombre(),
                usuario.getRol().getNombre(),
                usuario.getId(),
                usuario.getAvatarUrl()
        );
    }

    public DashboardResponse obtenerDashboard(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DashboardResponse dash = new DashboardResponse();
        dash.setNombreUsuario(usuario.getNombre());
        dash.setAvatarUrl(usuario.getAvatarUrl());
        dash.setTotalPlaylists(playlistRepo.findByUsuarioId(usuarioId).size());
        dash.setTotalFavoritos(favoritoRepo.findByUsuarioId(usuarioId).size());
        dash.setTotalReproducidas(historialRepo.findByUsuarioIdOrderByReproducidoEnDesc(usuarioId).size());
        dash.setRecomendaciones(recomendacionService.obtenerRecomendaciones(usuarioId));
        dash.setGeneros(generoService.listarActivos());

        // Últimas canciones escuchadas
        List<CancionResponse> recientes = historialRepo
                .findByUsuarioIdOrderByReproducidoEnDesc(usuarioId)
                .stream()
                .limit(5)
                .map(h -> cancionService.toResponse(h.getCancion(), usuarioId))
                .collect(Collectors.toList());
        dash.setRecienEscuchadas(recientes);

        return dash;
    }

    public DashboardResponse obtenerDashboardAdmin() {
        DashboardResponse dash = new DashboardResponse();
        dash.setTotalCanciones(cancionRepo.count());
        dash.setTotalGeneros(generoRepo.count());
        dash.setTotalUsuarios((long) usuarioRepo.findByRolNombre("USUARIO").size());
        return dash;
    }

    public List<AuthResponse> listarUsuarios() {
        return usuarioRepo.findByRolNombre("USUARIO").stream()
                .map(u -> new AuthResponse(
                        null, u.getCorreo(), u.getNombre(),
                        u.getRol().getNombre(), u.getId(), u.getAvatarUrl()))
                .collect(Collectors.toList());
    }
}
