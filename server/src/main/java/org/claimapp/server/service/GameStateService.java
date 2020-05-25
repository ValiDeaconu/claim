package org.claimapp.server.service;

import org.claimapp.common.dto.RankingDTO;
import org.claimapp.common.dto.TurnEndDTO;
import org.claimapp.server.entity.User;
import org.claimapp.server.model.GameState;

import java.util.List;

public interface GameStateService {
    GameState create(Long lobbyId, List<User> users);

    void delete(Long id);

    GameState getGameStateById(Long id);

    GameState addMoveToCurrentGameState(Long id, TurnEndDTO turnEndDTO);

    GameState handleUserDisconnected(Long id, Long userId);

    RankingDTO getRankingOfGameState(Long id);
}
