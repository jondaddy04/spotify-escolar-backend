package com.spotify.escolar.repository;

import com.spotify.escolar.entity.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CancionRepository extends JpaRepository<Cancion, Long> {

    List<Cancion> findByGeneroId(Long generoId);

    @Query("SELECT c FROM Cancion c WHERE c.genero.id = :generoId ORDER BY FUNCTION('RANDOM')")
    List<Cancion> findByGeneroIdRandom(@Param("generoId") Long generoId);

    @Query("SELECT c FROM Cancion c WHERE " +
           "LOWER(c.titulo) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(c.artista) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(c.album) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Cancion> buscar(@Param("q") String query);

    @Query("SELECT c FROM Cancion c WHERE c.genero.id IN :generoIds " +
           "AND c.id NOT IN :excluirIds ORDER BY FUNCTION('RANDOM')")
    List<Cancion> findRecomendaciones(
        @Param("generoIds") List<Long> generoIds,
        @Param("excluirIds") List<Long> excluirIds
    );

    @Query("SELECT c FROM Cancion c WHERE c.genero.id IN :generoIds ORDER BY FUNCTION('RANDOM')")
    List<Cancion> findRecomendacionesSinExcluir(@Param("generoIds") List<Long> generoIds);
}
