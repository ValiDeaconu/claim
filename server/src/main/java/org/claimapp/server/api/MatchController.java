package org.claimapp.server.api;

import org.claimapp.common.dto.MessageDTO;
import org.claimapp.common.dto.PairDTO;
import org.claimapp.common.dto.RankingDTO;
import org.claimapp.common.dto.TurnEndDTO;

import org.claimapp.server.model.GameState;
import org.claimapp.server.model.Lobby;
import org.claimapp.server.service.GameStateService;
import org.claimapp.server.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final GameStateService gameStateService;
    private final LobbyService lobbyService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MatchController(GameStateService gameStateService,
                           LobbyService lobbyService,
                           SimpMessagingTemplate simpMessagingTemplate) {
        this.gameStateService = gameStateService;
        this.lobbyService = lobbyService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/{lobbyId}/turn-end")
    public void playerEndedTurn(@PathVariable("lobbyId") Long lobbyId,
                                @RequestBody TurnEndDTO turnEndDTO) {

        Lobby lobby = lobbyService.addMoveToCurrentGameState(lobbyId, turnEndDTO);

        if (lobby == null)
            return;

        MessageDTO<GameState> message = new MessageDTO<>("turn-update", lobbyId, lobby.getGameState());
        simpMessagingTemplate.convertAndSend("/topic/lobby", message);
    }

    @PostMapping("/{lobbyId}/claim")
    public void playerCalledClaim(@PathVariable("lobbyId") Long lobbyId) {
        Lobby lobby = lobbyService.endMatch(lobbyId);
        RankingDTO rankingDTO = lobbyService.getRankings(lobby.getId());

        MessageDTO<PairDTO<Lobby, RankingDTO>> message = new MessageDTO<>(
                "claim",
                lobbyId,
                new PairDTO<>(lobby, rankingDTO));
        simpMessagingTemplate.convertAndSend("/topic/lobby", message);
    }

    @GetMapping("/{lobbyId}/gamestate")
    public GameState getGameState(@PathVariable("lobbyId") Long lobbyId) {
        Lobby lobbyById = lobbyService.getLobbyById(lobbyId);

        if (lobbyById != null)
            return lobbyById.getGameState();

        return null;
    }
}
