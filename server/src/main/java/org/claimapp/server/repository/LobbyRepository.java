package org.claimapp.server.repository;

import org.claimapp.server.model.Lobby;

import java.util.List;
import java.util.Optional;

public interface LobbyRepository {
    List<Lobby> findAll();
    Optional<Lobby> findById(Long id);
    Optional<Lobby> findByAccessCode(String accessCode);
    Lobby save(Lobby lobby);
    void deleteById(Long id);
}
