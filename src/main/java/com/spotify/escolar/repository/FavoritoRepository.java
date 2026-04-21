package com.spotify.escolar.repository;

import com.spotify.escolar.entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuarioId(Long usuarioId);

    Optional<Favorito> findByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    boolean existsByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    void deleteByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Géneros favoritos con score (cada favorito vale 2 puntos)
    @Query("SELECT f.cancion.genero.id, COUNT(f.id) * 2 as score " +
           "FROM Favorito f WHERE f.usuario.id = :usuarioId " +
           "GROUP BY f.cancion.genero.id ORDER BY score DESC")
    List<Object[]> findGeneroScoreByUsuario(@Param("usuarioId") Long usuarioId);
}
