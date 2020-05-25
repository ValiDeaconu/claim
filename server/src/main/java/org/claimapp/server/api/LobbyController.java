package org.claimapp.server.api;

import org.claimapp.common.dto.MessageDTO;
import org.claimapp.common.dto.PairDTO;
import org.claimapp.common.dto.SingletonDTO;
import org.claimapp.server.model.Lobby;
import org.claimapp.server.entity.User;
import org.claimapp.server.model.GameState;
import org.claimapp.server.service.GameStateService;
import org.claimapp.server.service.LobbyService;
import org.claimapp.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;
    private final UserService userService;

    private final GameStateService gameStateService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public LobbyController(LobbyService lobbyService,
                           UserService userService,
                           GameStateService gameStateService,
                           SimpMessagingTemplate simpMessagingTemplate) {
        this.lobbyService = lobbyService;
        this.userService = userService;

        this.gameStateService = gameStateService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/id/{lobbyId}")
    public Lobby getLobbyById(@PathVariable("lobbyId") Long lobbyId) {
        return lobbyService.getLobbyById(lobbyId);
    }

    @GetMapping("/code/{accessCode}")
    public Lobby getLobbyByAccessCode(@PathVariable("accessCode") String accessCode) {
        return lobbyService.getLobbyByAccessCode(accessCode);
    }

    @PostMapping("/create")
    public Lobby createLobby(@RequestBody SingletonDTO<Long> hostIdDTO) {
        User host = userService.findUserById(hostIdDTO.getContent());

        Lobby lobby = lobbyService.create(host);

        updateLobbyList();

        return lobby;
    }

    @PostMapping("/flip")
    public Lobby flipVisibility(@RequestBody Long lobbyId) {
        Lobby lobby = lobbyService.flipVisibility(lobbyId);

        updateLobbyList();

        return lobby;
    }

    @PostMapping("/leave")
    public SingletonDTO<Boolean> leaveLobby(@RequestBody PairDTO<Long, Long> lobbyUserPair) {
        lobbyService.userLeaveLobby(lobbyUserPair.getFirst(), lobbyUserPair.getSecond());

        Lobby lobby = lobbyService.getLobbyById(lobbyUserPair.getFirst());

        // inform lobby user leaves
        {
            MessageDTO<Lobby> message = new MessageDTO<>("update-user-left", lobbyUserPair.getFirst(), lobby);
            simpMessagingTemplate.convertAndSend("/topic/lobby", message);
        }

        updateLobbyList();

        return new SingletonDTO<>(lobby != null);
    }

    @PostMapping("/start")
    public void startMatch(@RequestBody SingletonDTO<Long> lobbyIdDTO) {
        Lobby updatedLobby = lobbyService.startMatch(lobbyIdDTO.getContent());

        if (updatedLobby != null) {
            PairDTO<Lobby, GameState> pairLobbyGameState = new PairDTO<>(updatedLobby, updatedLobby.getGameState());

            MessageDTO<PairDTO<Lobby, GameState>> message = new MessageDTO<>("start",
                    lobbyIdDTO.getContent(),
                    pairLobbyGameState);

            simpMessagingTemplate.convertAndSend("/topic/lobby", message);
        }
    }

    @GetMapping("/all")
    public List<Lobby> getAllPublicLobbies() {
        return lobbyService.getAllPublicLobbies();
    }

    private void updateLobbyList() {
        List<Lobby> publicLobbies = lobbyService.getAllPublicLobbies();
        MessageDTO<List<Lobby>> message = new MessageDTO<>("update", null, publicLobbies);
        simpMessagingTemplate.convertAndSend("/topic/lobby/list", message);
    }
}
