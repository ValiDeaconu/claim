package org.claimapp.server.api;

import org.claimapp.common.dto.SingletonDTO;
import org.claimapp.common.dto.UserDTO;
import org.claimapp.common.dto.UserProfileUpdateDTO;
import org.claimapp.server.entity.User;
import org.claimapp.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/{userId}")
    public UserDTO get(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping("{userId}/update/profile")
    public UserDTO save(@PathVariable("userId") Long userId,
                        @RequestBody SingletonDTO<Integer> profileAssetIndexDTO) {
        return userService.updateProfile(userId, profileAssetIndexDTO);
    }

    @PostMapping("/mass/update/increase/loss")
    public void increaseLoss(@RequestBody SingletonDTO<List<Long>> userIdsDTO) {
        userService.increaseLoss(userIdsDTO.getContent());
    }

    @PostMapping("/mass/update/increase/wins")
    public void increaseWins(@RequestBody SingletonDTO<List<Long>> userIdsDTO) {
        userService.increaseWins(userIdsDTO.getContent());
    }
}
