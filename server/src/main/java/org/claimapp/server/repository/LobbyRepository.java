package org.claimapp.server.repository;

import org.claimapp.server.entity.Lobby;
import org.claimapp.server.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface LobbyRepository {
    Optional<Lobby> findById(UUID id);
    Optional<Lobby> findByHost(User host);
    Optional<Lobby> findByAccessCode(String accessCode);

    Optional<Lobby> create(User host);

    Optional<Lobby> update(Lobby lobby);

    boolean remove(UUID id);
}
