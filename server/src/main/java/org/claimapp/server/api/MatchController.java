package org.claimapp.server.api;

import org.claimapp.server.dto.MessageDTO;
import org.claimapp.server.dto.TurnEndDTO;
import org.claimapp.server.dto.UserScoreClaimDTO;
import org.claimapp.server.entity.GameState;
import org.claimapp.server.entity.User;
import org.claimapp.server.service.GameManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final GameManager gameManager;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MatchController(GameManager gameManager,
                           SimpMessagingTemplate simpMessagingTemplate) {
        this.gameManager = gameManager;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/{lobbyId}/turn-end")
    public void playerEndedTurn(@PathVariable("lobbyId") UUID lobbyId,
                                @RequestBody TurnEndDTO turnEndDTO) {
        GameState gameState = gameManager.addMoveToCurrentGameState(lobbyId, turnEndDTO);

        MessageDTO<GameState> message = new MessageDTO<>("turn-update", lobbyId, gameState);
        simpMessagingTemplate.convertAndSend("/topic/lobby", message);
    }

    @PostMapping("/{lobbyId}/claim")
    public void playerCalledClaim(@PathVariable("lobbyId") UUID lobbyId) {
        List<UserScoreClaimDTO> winners = gameManager.getRankingOfGameState(lobbyId);

        MessageDTO<List<UserScoreClaimDTO>> message = new MessageDTO<>("claim", lobbyId, winners);
        simpMessagingTemplate.convertAndSend("/topic/lobby", message);
    }

    @GetMapping("/{lobbyId}/gamestate")
    public GameState getGameState(@PathVariable("lobbyId") UUID lobbyId) {
        GameState gameState = gameManager.getGameState(lobbyId);
        return gameState;
    }
}
