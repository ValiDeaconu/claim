package org.claimapp.server.api;

import org.claimapp.server.dto.MessageDTO;
import org.claimapp.server.dto.PairDTO;
import org.claimapp.server.entity.Lobby;
import org.claimapp.server.entity.User;
import org.claimapp.server.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
// @CrossOrigin(origins = "*")
public class LobbyMessageController {

    private final LobbyService lobbyService;

    @Autowired
    public LobbyMessageController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @MessageMapping("/lobby/join")
    @SendTo("/topic/lobby")
    public MessageDTO<Lobby> userJoinedOnLobby(PairDTO<UUID, User> lobbyJoinPair) {
        Lobby lobby = lobbyService.userJoinLobby(lobbyJoinPair.getFirst(), lobbyJoinPair.getSecond());

        if (lobby != null) {
            return new MessageDTO<>("update", lobbyJoinPair.getFirst(), lobby);
        }

        return new MessageDTO<>("err_no_room_to_join", lobbyJoinPair.getFirst(), null);
    }
}
