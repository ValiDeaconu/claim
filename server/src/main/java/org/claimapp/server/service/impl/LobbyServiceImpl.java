package org.claimapp.server.service.impl;

import org.claimapp.server.entity.Lobby;
import org.claimapp.server.entity.User;
import org.claimapp.server.repository.LobbyRepository;
import org.claimapp.server.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LobbyServiceImpl implements LobbyService {

    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyServiceImpl(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }


    @Override
    public Lobby getLobbyById(UUID id) {
        return lobbyRepository.findById(id).orElse(null);
    }

    @Override
    public Lobby getLobbyByAccessCode(String accessCode) {
        return lobbyRepository.findByAccessCode(accessCode).orElse(null);
    }

    @Override
    public Lobby create(User host) {
        Optional<Lobby> lobbyOptional = lobbyRepository.create(host);

        return lobbyOptional.orElse(null);
    }

    @Override
    public boolean remove(UUID id) {
        return lobbyRepository.remove(id);
    }

    @Override
    public Lobby flipVisibility(UUID lobbyId) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);

        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            lobby.setVisible(!lobby.isVisible());
            lobbyRepository.update(lobby);
            return lobby;
        }

        return null;
    }

    @Override
    public boolean userLeaveLobby(UUID lobbyId, Long userId) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);

        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            List<User> userList = lobby.getPlayers();

            Optional<User> anyOptional = userList.stream()
                    .filter(u -> u.getId().equals(userId))
                    .findAny();

            if (anyOptional.isPresent()) {
                User user = anyOptional.get();
                userList.remove(user);

                if (userList.isEmpty()) {
                    lobbyRepository.remove(lobbyId);
                } else {
                    lobby.setPlayers(userList);
                    lobbyRepository.update(lobby);
                }

                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public Lobby userJoinLobby(UUID lobbyId, User user) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);

        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();

            if (lobby.isRunning()) {
                return null;
            }

            List<User> userList = lobby.getPlayers();

            if (userList.size() >= 5) {
                return null;
            }

            Optional<User> anyOptional = userList.stream()
                    .filter(u -> u.getId().equals(user.getId()))
                    .findAny();

            if (anyOptional.isEmpty()) {
                userList.add(user);
                lobby.setPlayers(userList);

                lobbyRepository.update(lobby);

            }

            return lobby;
        }

        return null;
    }

    @Override
    public Lobby startMatch(UUID lobbyId) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);

        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            lobby.setRunning(true);

            lobbyRepository.update(lobby);

            return lobby;
        }

        return null;
    }

    @Override
    public Lobby endMatch(UUID lobbyId) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);

        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            lobby.setRunning(false);

            lobbyRepository.update(lobby);

            return lobby;
        }

        return null;
    }


}
