package org.claimapp.server.api;

import org.claimapp.common.dto.MessageDTO;
import org.claimapp.common.dto.PairDTO;
import org.claimapp.server.model.Lobby;
import org.claimapp.server.entity.User;
import org.claimapp.server.service.LobbyService;
import org.claimapp.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyMessageController {

    private final LobbyService lobbyService;
    private final UserService userService;

    @Autowired
    public LobbyMessageController(LobbyService lobbyService,
                                  UserService userService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @MessageMapping("/lobby/join")
    @SendTo("/topic/lobby")
    public MessageDTO<Lobby> userJoinedOnLobby(PairDTO<Long, Long> lobbyUserPair) {
        User user = userService.findUserById(lobbyUserPair.getSecond());
        Lobby lobby = lobbyService.userJoinLobby(lobbyUserPair.getFirst(), user);

        if (lobby != null) {
            return new MessageDTO<>("update", lobbyUserPair.getFirst(), lobby);
        }

        return new MessageDTO<>("err_no_room_to_join", lobbyUserPair.getFirst(), null);
    }
}
