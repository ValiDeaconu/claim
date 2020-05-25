package org.claimapp.server.repository.impl;

import org.claimapp.server.model.GameState;
import org.claimapp.server.repository.GameStateRepository;
import org.claimapp.server.repository.misc.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
public class GameStateRepositoryImpl implements GameStateRepository {

    private final IdGenerator<Long> idGenerator;

    private final Map<Long, GameState> gameStateDb;

    @Autowired
    public GameStateRepositoryImpl(IdGenerator<Long> idGenerator) {
        this.idGenerator = idGenerator;

        gameStateDb = new HashMap<>();
    }

    @Override
    public Optional<GameState> findById(Long id) {
        if (gameStateDb.containsKey(id))
            return Optional.of(gameStateDb.get(id));
        return Optional.empty();
    }

    @Override
    public GameState save(GameState gameState) {
        if (gameState.getId() == null || !gameStateDb.containsKey(gameState.getId())) {
            gameState.setId(idGenerator.next());
            gameStateDb.put(gameState.getId(), gameState);
        } else {
            gameStateDb.replace(gameState.getId(), gameState);
        }

        return gameState;
    }

    @Override
    public void deleteById(Long id) {
        gameStateDb.remove(id);
    }
}
