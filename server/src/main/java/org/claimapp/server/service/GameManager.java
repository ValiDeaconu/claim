package org.claimapp.server.service;

import org.claimapp.server.dto.TurnEndDTO;
import org.claimapp.server.dto.UserScoreClaimDTO;
import org.claimapp.server.entity.GameState;
import org.claimapp.server.entity.User;

import java.util.List;
import java.util.UUID;

public interface GameManager {
    GameState create(UUID lobbyId, List<User> users);

    GameState getGameState(UUID lobbyId);

    GameState addMoveToCurrentGameState(UUID lobbyId, TurnEndDTO turnEndDTO);

    List<UserScoreClaimDTO> getRankingOfGameState(UUID lobbyId);
}
