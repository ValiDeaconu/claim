package org.claimapp.server.service.impl;

import org.claimapp.common.dto.RankingDTO;
import org.claimapp.common.dto.TurnEndDTO;
import org.claimapp.server.model.GameState;
import org.claimapp.server.model.Lobby;
import org.claimapp.server.entity.User;
import org.claimapp.server.repository.LobbyRepository;
import org.claimapp.server.service.AccessCodeGenerator;
import org.claimapp.server.service.GameStateService;
import org.claimapp.server.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LobbyServiceImpl implements LobbyService {

    private final LobbyRepository lobbyRepository;
    private final AccessCodeGenerator accessCodeGenerator;
    private final GameStateService gameStateService;

    @Autowired
    public LobbyServiceImpl(LobbyRepository lobbyRepository,
                            GameStateService gameStateService,
                            AccessCodeGenerator accessCodeGenerator) {
        this.lobbyRepository = lobbyRepository;
        this.gameStateService = gameStateService;
        this.accessCodeGenerator = accessCodeGenerator;
    }


    @Override
    public Lobby getLobbyById(Long id) {
        return lobbyRepository.findById(id).orElse(null);
    }

    @Override
    public Lobby getLobbyByAccessCode(String accessCode) {
        return lobbyRepository.findByAccessCode(accessCode).orElse(null);
    }

    @Override
    public Lobby create(User host) {
        Lobby lobby = new Lobby();

        List<User> userList = new LinkedList<>();
        userList.add(host);
        lobby.setPlayers(userList);
        lobby.setRunning(false);
        lobby.setVisible(false);
        lobby.setAccessCode(accessCodeGenerator.next());

        return lobbyRepository.save(lobby);
    }

    @Override
    public Lobby save(Lobby lobby) {
        return lobbyRepository.save(lobby);
    }

    @Override
    public void remove(Long id) {
        lobbyRepository.deleteById(id);
    }

    @Override
    public Lobby flipVisibility(Long lobbyId) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);

        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            lobby.setVisible(!lobby.isVisible());
            return lobbyRepository.save(lobby);
        }

        return null;
    }

    @Override
    public boolean userLeaveLobby(Long lobbyId, Long userId) {
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
                    if (lobby.getGameState() != null)
                        gameStateService.delete(lobby.getGameState().getId());

                    lobbyRepository.deleteById(lobbyId);
                } else {
                    lobby.setPlayers(userList);

                    if (lobby.getGameState() != null) {
                        GameState gameState = gameStateService
                                .handleUserDisconnected(lobby.getGameState().getId(), userId);
                        lobby.setGameState(gameState);
                    }

                    lobbyRepository.save(lobby);
                }

                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public Lobby userJoinLobby(Long lobbyId, User user) {
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

                lobby = lobbyRepository.save(lobby);
            }

            return lobby;
        }

        return null;
    }

    @Override
    public Lobby startMatch(Long lobbyId) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);

        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            lobby.setRunning(true);

            GameState gameState = gameStateService.create(lobbyId, lobby.getPlayers());
            lobby.setGameState(gameState);

            return lobbyRepository.save(lobby);
        }

        return null;
    }

    @Override
    public Lobby addMoveToCurrentGameState(Long lobbyId, TurnEndDTO turnEndDTO) {
        Lobby lobby = getLobbyById(lobbyId);

        if (lobby == null)
            return null;

        GameState currentGameState = lobby.getGameState();
        GameState nextGameState = gameStateService.addMoveToCurrentGameState(currentGameState.getId(), turnEndDTO);

        lobby.setGameState(nextGameState);

        return lobby;
    }

    @Override
    public RankingDTO getRankings(Long lobbyId) {
        Lobby lobby = getLobbyById(lobbyId);

        if (lobby == null)
            return null;

        GameState gameState = lobby.getGameState();
        return gameStateService.getRankingOfGameState(gameState.getId());
    }

    @Override
    public Lobby endMatch(Long lobbyId) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);

        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            lobby.setRunning(false);

            return lobbyRepository.save(lobby);
        }

        return null;
    }

    @Override
    public List<Lobby> getAllPublicLobbies() {
        return lobbyRepository.findAll()
                .stream()
                .filter(l -> l.isVisible() && !l.isRunning())
                .collect(Collectors.toList());
    }


}
