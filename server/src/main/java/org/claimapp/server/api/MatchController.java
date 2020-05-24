package org.claimapp.server.api;

import org.claimapp.common.dto.MessageDTO;
import org.claimapp.common.dto.RankingDTO;
import org.claimapp.common.dto.TurnEndDTO;

import org.claimapp.server.model.GameState;
import org.claimapp.server.service.GameStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final GameStateService gameStateService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MatchController(GameStateService gameStateService,
                           SimpMessagingTemplate simpMessagingTemplate) {
        this.gameStateService = gameStateService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/{lobbyId}/turn-end")
    public void playerEndedTurn(@PathVariable("lobbyId") Long lobbyId,
                                @RequestBody TurnEndDTO turnEndDTO) {
        GameState archivedGameState = gameStateService.addMoveToCurrentGameState(lobbyId, turnEndDTO);

        MessageDTO<GameState> message = new MessageDTO<>("turn-update", lobbyId, archivedGameState);
        simpMessagingTemplate.convertAndSend("/topic/lobby", message);
    }

    @PostMapping("/{lobbyId}/claim")
    public void playerCalledClaim(@PathVariable("lobbyId") Long lobbyId) {
        RankingDTO rankingDTO = gameStateService.getRankingOfGameState(lobbyId);

        MessageDTO<RankingDTO> message = new MessageDTO<>("claim", lobbyId, rankingDTO);
        simpMessagingTemplate.convertAndSend("/topic/lobby", message);
    }

    @GetMapping("/{lobbyId}/gamestate")
    public GameState getGameState(@PathVariable("lobbyId") Long lobbyId) {
        return gameStateService.getGameStateByLobbyId(lobbyId);
    }
}
