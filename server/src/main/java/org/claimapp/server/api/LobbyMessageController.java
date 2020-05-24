package org.claimapp.server.api;

import org.claimapp.common.dto.MessageDTO;
import org.claimapp.common.dto.PairDTO;
import org.claimapp.server.model.Lobby;
import org.claimapp.server.entity.User;
import org.claimapp.server.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyMessageController {

    private final LobbyService lobbyService;

    @Autowired
    public LobbyMessageController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @MessageMapping("/lobby/join")
    @SendTo("/topic/lobby")
    public MessageDTO<Lobby> userJoinedOnLobby(PairDTO<Long, User> lobbyJoinPair) {
        Lobby lobby = lobbyService.userJoinLobby(lobbyJoinPair.getFirst(), lobbyJoinPair.getSecond());

        if (lobby != null) {
            return new MessageDTO<>("update", lobbyJoinPair.getFirst(), lobby);
        }

        return new MessageDTO<>("err_no_room_to_join", lobbyJoinPair.getFirst(), null);
    }
}
