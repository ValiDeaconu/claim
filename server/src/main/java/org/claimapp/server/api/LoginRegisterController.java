package org.claimapp.server.api;

import org.claimapp.common.dto.LoginUserDTO;
import org.claimapp.common.dto.RegisterUserDTO;
import org.claimapp.common.dto.UserDTO;

import org.claimapp.server.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginRegisterController {

    private final UserService userService;

    @Autowired
    public LoginRegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody LoginUserDTO loginUserDTO) {
        return userService.getUser(loginUserDTO);
    }

    @GetMapping("/login/{id}")
    public UserDTO getUserById(@PathVariable("id") Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.registerUser(registerUserDTO);
    }
}
