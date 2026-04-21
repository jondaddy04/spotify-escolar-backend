package com.spotify.escolar.repository;

import com.spotify.escolar.entity.HistorialReproduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface HistorialReproduccionRepository extends JpaRepository<HistorialReproduccion, Long> {

    Optional<HistorialReproduccion> findByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    List<HistorialReproduccion> findByUsuarioIdOrderByReproducidoEnDesc(Long usuarioId);

    // IDs de canciones más escuchadas por el usuario
    @Query("SELECT h.cancion.id FROM HistorialReproduccion h " +
           "WHERE h.usuario.id = :usuarioId ORDER BY h.veces DESC")
    List<Long> findCancionIdsMasEscuchadasByUsuario(@Param("usuarioId") Long usuarioId);

    // Géneros más escuchados con su score (veces reproducidas)
    @Query("SELECT h.cancion.genero.id, SUM(h.veces) as score " +
           "FROM HistorialReproduccion h WHERE h.usuario.id = :usuarioId " +
           "GROUP BY h.cancion.genero.id ORDER BY score DESC")
    List<Object[]> findGeneroScoreByUsuario(@Param("usuarioId") Long usuarioId);
}
