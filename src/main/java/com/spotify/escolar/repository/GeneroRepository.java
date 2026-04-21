package com.spotify.escolar.repository;

import com.spotify.escolar.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    List<Genero> findByActivoTrue();
    boolean existsByNombre(String nombre);
}
