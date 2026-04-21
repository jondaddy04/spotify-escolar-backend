package com.spotify.escolar.service;

import com.spotify.escolar.dto.request.CancionRequest;
import com.spotify.escolar.dto.response.CancionResponse;
import com.spotify.escolar.entity.Cancion;
import com.spotify.escolar.entity.Genero;
import com.spotify.escolar.repository.CancionRepository;
import com.spotify.escolar.repository.FavoritoRepository;
import com.spotify.escolar.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CancionService {

    @Autowired private CancionRepository cancionRepo;
    @Autowired private GeneroRepository generoRepo;
    @Autowired private FavoritoRepository favoritoRepo;

    public List<CancionResponse> listarTodas(Long usuarioId) {
        return cancionRepo.findAll().stream()
                .map(c -> toResponse(c, usuarioId))
                .collect(Collectors.toList());
    }

    public List<CancionResponse> listarPorGenero(Long generoId, Long usuarioId) {
        return cancionRepo.findByGeneroId(generoId).stream()
                .map(c -> toResponse(c, usuarioId))
                .collect(Collectors.toList());
    }

    public CancionResponse obtenerAleatoriaPorGenero(Long generoId, Long usuarioId) {
        List<Cancion> canciones = cancionRepo.findByGeneroIdRandom(generoId);
        if (canciones.isEmpty()) {
            throw new RuntimeException("No hay canciones para este género");
        }
        return toResponse(canciones.get(0), usuarioId);
    }

    public CancionResponse obtenerPorId(Long id, Long usuarioId) {
        Cancion cancion = cancionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));
        return toResponse(cancion, usuarioId);
    }

    public List<CancionResponse> buscar(String query, Long usuarioId) {
        return cancionRepo.buscar(query).stream()
                .map(c -> toResponse(c, usuarioId))
                .collect(Collectors.toList());
    }

    public CancionResponse crear(CancionRequest req) {
        Genero genero = generoRepo.findById(req.getGeneroId())
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));
        Cancion cancion = new Cancion();
        mapearRequest(cancion, req, genero);
        cancionRepo.save(cancion);
        return toResponse(cancion, null);
    }

    public CancionResponse editar(Long id, CancionRequest req) {
        Cancion cancion = cancionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));
        Genero genero = generoRepo.findById(req.getGeneroId())
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));
        mapearRequest(cancion, req, genero);
        cancionRepo.save(cancion);
        return toResponse(cancion, null);
    }

    public void eliminar(Long id) {
        if (!cancionRepo.existsById(id)) {
            throw new RuntimeException("Canción no encontrada");
        }
        cancionRepo.deleteById(id);
    }

    private void mapearRequest(Cancion cancion, CancionRequest req, Genero genero) {
        cancion.setGenero(genero);
        cancion.setTitulo(req.getTitulo());
        cancion.setAutor(req.getAutor());
        cancion.setArtista(req.getArtista());
        cancion.setAlbum(req.getAlbum());
        cancion.setDuracionSeg(req.getDuracionSeg());
        cancion.setImagenUrl(req.getImagenUrl());
        cancion.setAudioUrl(req.getAudioUrl());
        if (req.getFechaLanzamiento() != null && !req.getFechaLanzamiento().isBlank()) {
            cancion.setFechaLanzamiento(LocalDate.parse(req.getFechaLanzamiento()));
        }
    }

    public CancionResponse toResponse(Cancion c, Long usuarioId) {
        CancionResponse r = new CancionResponse();
        r.setId(c.getId());
        r.setGeneroId(c.getGenero().getId());
        r.setGeneroNombre(c.getGenero().getNombre());
        r.setTitulo(c.getTitulo());
        r.setAutor(c.getAutor());
        r.setArtista(c.getArtista());
        r.setAlbum(c.getAlbum());
        r.setDuracionSeg(c.getDuracionSeg());
        r.setDuracionFormato(formatearDuracion(c.getDuracionSeg()));
        r.setFechaLanzamiento(c.getFechaLanzamiento());
        r.setCreatedAt(c.getCreatedAt());

        // Devolver rutas tal como están en la BD (relativas)
        // La app móvil construye la URL completa con su propio BASE_URL
        r.setImagenUrl(c.getImagenUrl());
        r.setAudioUrl(c.getAudioUrl());

        if (usuarioId != null) {
            r.setEsFavorita(favoritoRepo.existsByUsuarioIdAndCancionId(usuarioId, c.getId()));
        }

        return r;
    }

    private String formatearDuracion(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%d:%02d", min, seg);
    }
}