package com.spotify.escolar.repository;

import com.spotify.escolar.entity.PlaylistCancion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlaylistCancionRepository extends JpaRepository<PlaylistCancion, Long> {
    Optional<PlaylistCancion> findByPlaylistIdAndCancionId(Long playlistId, Long cancionId);
    boolean existsByPlaylistIdAndCancionId(Long playlistId, Long cancionId);
    void deleteByPlaylistIdAndCancionId(Long playlistId, Long cancionId);
}
