package org.claimapp.server.api;

import org.claimapp.server.dto.SingletonDTO;
import org.claimapp.server.entity.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
// @CrossOrigin(origins = "*")
public class UserController {

    @MessageMapping("/user")
    @SendTo("/topic/user")
    public SingletonDTO<String> getUser(User user) {
        return new SingletonDTO<>("Hi " + user.getUsername());
    }

}
