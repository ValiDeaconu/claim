package org.claimapp.server.repository;

import org.claimapp.server.model.GameState;

public interface GameStateRepository {
    GameState save(GameState gameState);

    void deleteById(Long id);
}
