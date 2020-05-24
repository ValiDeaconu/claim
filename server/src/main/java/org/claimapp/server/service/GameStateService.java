package org.claimapp.server.service;

import org.claimapp.common.dto.RankingDTO;
import org.claimapp.common.dto.TurnEndDTO;
import org.claimapp.server.entity.User;
import org.claimapp.server.model.GameState;

import java.util.List;

public interface GameStateService {
    GameState create(Long lobbyId, List<User> users);

    GameState getGameStateByLobbyId(Long lobbyId);

    GameState addMoveToCurrentGameState(Long lobbyId, TurnEndDTO turnEndDTO);

    RankingDTO getRankingOfGameState(Long lobbyId);
}
