package org.claimapp.server.repository.impl;

import org.claimapp.server.model.Lobby;
import org.claimapp.server.repository.LobbyRepository;
import org.claimapp.server.repository.misc.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class LobbyRepositoryImpl implements LobbyRepository {

    private final IdGenerator<Long> idGenerator;

    private final Map<Long, Lobby> lobbyDb;


    public LobbyRepositoryImpl(IdGenerator<Long> idGenerator) {
        this.idGenerator = idGenerator;

        this.lobbyDb = new HashMap<>();
    }

    @Override
    public List<Lobby> findAll() {
        return new ArrayList<>(lobbyDb.values());
    }

    @Override
    public Optional<Lobby> findById(Long id) {
        if (lobbyDb.containsKey(id))
            return Optional.of(lobbyDb.get(id));

        return Optional.empty();
    }

    @Override
    public Optional<Lobby> findByAccessCode(String accessCode) {
        return lobbyDb
                .values()
                .stream()
                .filter(lobby -> lobby.getAccessCode().equals(accessCode))
                .findFirst();
    }

    @Override
    public Lobby save(Lobby lobby) {
        if (lobby.getId() == null || !lobbyDb.containsKey(lobby.getId())) {
            lobby.setId(idGenerator.next());
            lobbyDb.put(lobby.getId(), lobby);
        } else {
            lobbyDb.replace(lobby.getId(), lobby);
        }

        return lobby;
    }

    @Override
    public void deleteById(Long id) {
        lobbyDb.remove(id);
    }
}
