package org.claimapp.server.repository.impl;

import org.claimapp.server.entity.GameState;
import org.claimapp.server.entity.Lobby;
import org.claimapp.server.repository.GameStateRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GameStateRepositoryImpl implements GameStateRepository {

    private final Map<UUID, GameState> gameStateDb;

    public GameStateRepositoryImpl() {
        this.gameStateDb = new HashMap<>();
    }

    @Override
    public Optional<GameState> find(UUID lobbyId) {
        return this.gameStateDb.entrySet()
                .stream()
                .filter(e -> e.getKey().equals(lobbyId))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    @Override
    public Optional<GameState> save(UUID lobbyId, GameState gameState) {
        this.gameStateDb.put(lobbyId, gameState);
        return Optional.of(gameState);
    }

    @Override
    public Optional<GameState> update(UUID lobbyId, GameState gameState) {
        this.gameStateDb.replace(lobbyId, gameState);
        return Optional.of(gameState);
    }

    @Override
    public boolean delete(UUID lobbyId) {
        this.gameStateDb.remove(lobbyId);
        return true;
    }
}
