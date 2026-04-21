package com.spotify.escolar.repository;

import com.spotify.escolar.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUsuarioId(Long usuarioId);
    Optional<Playlist> findByIdAndUsuarioId(Long id, Long usuarioId);
}
