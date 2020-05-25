package org.claimapp.server.service;

import org.claimapp.common.dto.RankingDTO;
import org.claimapp.common.dto.TurnEndDTO;
import org.claimapp.server.model.Lobby;
import org.claimapp.server.entity.User;

import java.util.List;

public interface LobbyService {

    Lobby getLobbyById(Long id);
    Lobby getLobbyByAccessCode(String accessCode);

    Lobby create(User host);
    Lobby save(Lobby lobby);

    void remove(Long id);

    Lobby flipVisibility(Long lobbyId);

    boolean userLeaveLobby(Long lobbyId, Long userId);

    Lobby userJoinLobby(Long lobbyId, User user);

    Lobby startMatch(Long lobbyId);
    Lobby addMoveToCurrentGameState(Long lobbyId, TurnEndDTO turnEndDTO);
    RankingDTO getRankings(Long lobbyId);
    Lobby endMatch(Long lobbyId);

    List<Lobby> getAllPublicLobbies();
}
