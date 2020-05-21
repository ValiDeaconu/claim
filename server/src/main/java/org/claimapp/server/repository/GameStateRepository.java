package org.claimapp.server.repository;

import org.claimapp.server.entity.GameState;

import java.util.Optional;
import java.util.UUID;

public interface GameStateRepository {
    Optional<GameState> find(UUID lobbyId);

    Optional<GameState> save(UUID lobbyId, GameState gameState);

    Optional<GameState> update(UUID lobbyId, GameState gameState);

    boolean delete(UUID lobbyId);
}
