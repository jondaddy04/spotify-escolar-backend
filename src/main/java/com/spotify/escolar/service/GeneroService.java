package com.spotify.escolar.service;

import com.spotify.escolar.dto.response.GeneroResponse;
import com.spotify.escolar.entity.Genero;
import com.spotify.escolar.repository.CancionRepository;
import com.spotify.escolar.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneroService {

    @Autowired private GeneroRepository generoRepo;
    @Autowired private CancionRepository cancionRepo;

    public List<GeneroResponse> listarActivos() {
        return generoRepo.findByActivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<GeneroResponse> listarTodos() {
        return generoRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GeneroResponse obtenerPorId(Long id) {
        Genero genero = generoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));
        return toResponse(genero);
    }

    public GeneroResponse crear(String nombre, String descripcion, String imagenUrl) {
        if (generoRepo.existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un género con ese nombre");
        }
        Genero genero = new Genero();
        genero.setNombre(nombre);
        genero.setDescripcion(descripcion);
        genero.setImagenUrl(imagenUrl);
        generoRepo.save(genero);
        return toResponse(genero);
    }

    public GeneroResponse editar(Long id, String nombre, String descripcion, String imagenUrl) {
        Genero genero = generoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));
        genero.setNombre(nombre);
        genero.setDescripcion(descripcion);
        if (imagenUrl != null) genero.setImagenUrl(imagenUrl);
        generoRepo.save(genero);
        return toResponse(genero);
    }

    public void toggleActivo(Long id) {
        Genero genero = generoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));
        genero.setActivo(!genero.getActivo());
        generoRepo.save(genero);
    }

    private GeneroResponse toResponse(Genero g) {
        GeneroResponse r = new GeneroResponse();
        r.setId(g.getId());
        r.setNombre(g.getNombre());
        r.setDescripcion(g.getDescripcion());
        r.setActivo(g.getActivo());
        r.setImagenUrl(g.getImagenUrl());
        r.setTotalCanciones((long) cancionRepo.findByGeneroId(g.getId()).size());
        return r;
    }
}