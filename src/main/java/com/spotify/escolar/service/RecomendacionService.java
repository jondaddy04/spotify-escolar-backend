package com.spotify.escolar.service;

import com.spotify.escolar.dto.response.CancionResponse;
import com.spotify.escolar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendacionService {

    @Autowired private HistorialReproduccionRepository historialRepo;
    @Autowired private FavoritoRepository favoritoRepo;
    @Autowired private CancionRepository cancionRepo;
    @Autowired private GeneroRepository generoRepo;
    @Autowired private CancionService cancionService;

    /**
     * Algoritmo de recomendación híbrido por scoring ponderado:
     * - Historial de reproducción: 1 punto por reproducción
     * - Favoritos: 2 puntos (mayor peso porque es intención explícita)
     * Devuelve hasta 10 canciones de géneros con mayor score
     * excluyendo las más escuchadas recientemente.
     */
    public List<CancionResponse> obtenerRecomendaciones(Long usuarioId) {
        Map<Long, Double> generoScore = new HashMap<>();

        // Score por historial (1 punto × veces reproducida)
        List<Object[]> historialScores = historialRepo.findGeneroScoreByUsuario(usuarioId);
        for (Object[] row : historialScores) {
            Long generoId = (Long) row[0];
            Double score = ((Number) row[1]).doubleValue();
            generoScore.merge(generoId, score, Double::sum);
        }

        // Score por favoritos (2 puntos cada uno)
        List<Object[]> favoritoScores = favoritoRepo.findGeneroScoreByUsuario(usuarioId);
        for (Object[] row : favoritoScores) {
            Long generoId = (Long) row[0];
            Double score = ((Number) row[1]).doubleValue();
            generoScore.merge(generoId, score, Double::sum);
        }

        // Si el usuario no tiene historial ni favoritos → devolver canciones aleatorias
        if (generoScore.isEmpty()) {
            return cancionRepo.findAll().stream()
                    .limit(10)
                    .map(c -> cancionService.toResponse(c, usuarioId))
                    .collect(Collectors.toList());
        }

        // Ordenar géneros por score descendente y tomar los top 5
        List<Long> generosMasEscuchados = generoScore.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Canciones ya muy escuchadas (para excluirlas o darles menor prioridad)
        List<Long> cancionesEscuchadas = historialRepo
                .findCancionIdsMasEscuchadasByUsuario(usuarioId)
                .stream().limit(10).collect(Collectors.toList());

        // Obtener canciones de esos géneros excluyendo las muy escuchadas
        List<CancionResponse> recomendaciones;

        if (cancionesEscuchadas.isEmpty()) {
            recomendaciones = cancionRepo
                    .findRecomendacionesSinExcluir(generosMasEscuchados)
                    .stream()
                    .limit(10)
                    .map(c -> cancionService.toResponse(c, usuarioId))
                    .collect(Collectors.toList());
        } else {
            recomendaciones = cancionRepo
                    .findRecomendaciones(generosMasEscuchados, cancionesEscuchadas)
                    .stream()
                    .limit(10)
                    .map(c -> cancionService.toResponse(c, usuarioId))
                    .collect(Collectors.toList());

            // Si hay pocas recomendaciones nuevas, completar con las populares
            if (recomendaciones.size() < 5) {
                List<CancionResponse> extra = cancionRepo
                        .findRecomendacionesSinExcluir(generosMasEscuchados)
                        .stream()
                        .limit(10)
                        .map(c -> cancionService.toResponse(c, usuarioId))
                        .collect(Collectors.toList());
                recomendaciones.addAll(extra);
                recomendaciones = recomendaciones.stream()
                        .distinct()
                        .limit(10)
                        .collect(Collectors.toList());
            }
        }

        return recomendaciones;
    }
}
