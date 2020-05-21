package org.claimapp.server.api;

import org.claimapp.server.dto.MessageDTO;
import org.claimapp.server.dto.PairDTO;
import org.claimapp.server.dto.SingletonDTO;
import org.claimapp.server.dto.TurnEndDTO;
import org.claimapp.server.entity.GameState;
import org.claimapp.server.entity.Lobby;
import org.claimapp.server.entity.User;
import org.claimapp.server.service.GameManager;
import org.claimapp.server.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;
    private final GameManager gameManager;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public LobbyController(LobbyService lobbyService,
                           GameManager gameManager,
                           SimpMessagingTemplate simpMessagingTemplate) {
        this.lobbyService = lobbyService;
        this.gameManager = gameManager;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/id/{lobbyId}")
    public Lobby getLobbyById(@PathVariable("lobbyId") UUID lobbyId) {
        return lobbyService.getLobbyById(lobbyId);
    }

    @GetMapping("/code/{accessCode}")
    public Lobby getLobbyByAccessCode(@PathVariable("accessCode") String accessCode) {
        return lobbyService.getLobbyByAccessCode(accessCode);
    }

    @PostMapping("/create")
    public Lobby createLobby(@RequestBody User host) {
        return lobbyService.create(host);
    }

    @PostMapping("/flip")
    public Lobby flipVisibility(@RequestBody UUID lobbyId) {
        return lobbyService.flipVisibility(lobbyId);
    }

    @PostMapping("/leave")
    public SingletonDTO<Boolean> leaveLobby(@RequestBody PairDTO<UUID, Long> lobbyUserPair) {
        lobbyService.userLeaveLobby(lobbyUserPair.getFirst(), lobbyUserPair.getSecond());

        Lobby lobby = lobbyService.getLobbyById(lobbyUserPair.getFirst());

        MessageDTO<Lobby> message = new MessageDTO<>("update", lobbyUserPair.getFirst(), lobby);

        simpMessagingTemplate.convertAndSend("/topic/lobby", message);

        return new SingletonDTO<>(lobby != null);
    }

    @PostMapping("/start")
    public void startMatch(@RequestBody SingletonDTO<UUID> lobbyIdDTO) {
        Lobby updatedLobby = lobbyService.startMatch(lobbyIdDTO.getContent());

        if (updatedLobby != null) {
            List<User> players = updatedLobby.getPlayers();
            GameState gameState = gameManager.create(lobbyIdDTO.getContent(), players);

            PairDTO<Lobby, GameState> pairLobbyGameState = new PairDTO<>(updatedLobby, gameState);

            MessageDTO<PairDTO<Lobby, GameState>> message = new MessageDTO<>("start",
                    lobbyIdDTO.getContent(),
                    pairLobbyGameState);

            simpMessagingTemplate.convertAndSend("/topic/lobby", message);
        }
    }
}
