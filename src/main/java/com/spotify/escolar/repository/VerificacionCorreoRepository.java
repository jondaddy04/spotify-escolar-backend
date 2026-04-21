package com.spotify.escolar.repository;

import com.spotify.escolar.entity.VerificacionCorreo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificacionCorreoRepository extends JpaRepository<VerificacionCorreo, Long> {
    Optional<VerificacionCorreo> findTopByUsuarioIdAndUsadoFalseOrderByIdDesc(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}
