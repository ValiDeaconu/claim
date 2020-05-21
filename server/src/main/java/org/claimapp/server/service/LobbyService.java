package org.claimapp.server.service;

import org.claimapp.server.entity.Lobby;
import org.claimapp.server.entity.User;

import java.util.UUID;

public interface LobbyService {

    Lobby getLobbyById(UUID id);

    Lobby getLobbyByAccessCode(String accessCode);

    Lobby create(User host);

    boolean remove(UUID id);

    Lobby flipVisibility(UUID lobbyId);

    boolean userLeaveLobby(UUID lobbyId, Long userId);

    Lobby userJoinLobby(UUID lobbyId, User user);

    Lobby startMatch(UUID lobbyId);
}
