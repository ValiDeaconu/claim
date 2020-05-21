package org.claimapp.server.repository.impl;

import org.claimapp.server.entity.Lobby;
import org.claimapp.server.entity.User;
import org.claimapp.server.repository.LobbyRepository;
import org.claimapp.server.service.AccessCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LobbyRepositoryImpl implements LobbyRepository {

    private final Map<UUID, Lobby> lobbyDb;

    private final AccessCodeGenerator accessCodeGenerator;

    @Autowired
    public LobbyRepositoryImpl(AccessCodeGenerator accessCodeGenerator) {
        this.lobbyDb = new HashMap<>();
        this.accessCodeGenerator = accessCodeGenerator;
    }

    @Override
    public Optional<Lobby> findById(UUID id) {
        if (lobbyDb.containsKey(id)) {
            return Optional.of(lobbyDb.get(id));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Lobby> findByHost(User host) {
        return lobbyDb
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getPlayers().get(0).getId().equals(host.getId()))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    @Override
    public Optional<Lobby> findByAccessCode(String accessCode) {
        return lobbyDb
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getAccessCode().equals(accessCode))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    @Override
    public Optional<Lobby> create(User host) {
        Lobby lobby = new Lobby();

        lobby.setId(UUID.randomUUID());

        List<User> userList = new ArrayList<>();
        userList.add(host);
        lobby.setPlayers(userList);

        lobby.setAccessCode(accessCodeGenerator.next());
        lobby.setVisible(true);

        lobbyDb.put(lobby.getId(), lobby);

        return Optional.of(lobby);
    }

    @Override
    public Optional<Lobby> update(Lobby lobby) {
        if (lobbyDb.containsKey(lobby.getId())) {
            lobbyDb.replace(lobby.getId(), lobby);
            return Optional.of(lobby);
        }

        return Optional.empty();
    }

    @Override
    public boolean remove(UUID id) {
        if (lobbyDb.containsKey(id)) {
            lobbyDb.remove(id);
            return true;
        }

        return false;
    }
}
