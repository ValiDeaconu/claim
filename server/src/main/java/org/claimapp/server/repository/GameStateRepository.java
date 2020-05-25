package org.claimapp.server.repository;

import org.claimapp.server.model.GameState;

import java.util.Optional;

public interface GameStateRepository {
    Optional<GameState> findById(Long id);

    GameState save(GameState gameState);

    void deleteById(Long id);
}
