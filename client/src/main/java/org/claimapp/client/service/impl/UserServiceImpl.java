package org.claimapp.client.service.impl;

import org.claimapp.client.dto.IdDTO;
import org.claimapp.client.dto.LoginUserDTO;
import org.claimapp.client.dto.RegisterUserDTO;
import org.claimapp.client.entity.User;
import org.claimapp.client.mapper.UserMapper;
import org.claimapp.client.repository.UserRepository;
import org.claimapp.client.service.UserService;
import org.claimapp.client.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserValidator userValidator;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserValidator userValidator,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
    }

    @Override
    public User getUser(LoginUserDTO loginUserDTO, BindingResult bindingResult) {
        userValidator.validateLoginUser(loginUserDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return null;
        }

        List<User> all = userRepository.findAll();
        Optional<User> userOptional = all.stream()
                .filter(user ->
                            user.getUsername().equals(loginUserDTO.getUsername()) &&
                            user.getPassword().equals(loginUserDTO.getPassword())
                )
                .findFirst();

        if (userOptional.isEmpty()) {
            bindingResult.reject("UsernameNotFound");
        }

        return userOptional.orElse(null);
    }

    @Override
    public User getUser(IdDTO idDTO) {
        return userRepository.findById(idDTO.getId()).orElse(null);
    }


    @Override
    public User registerUser(RegisterUserDTO registerUserDTO, BindingResult bindingResult) {
        userValidator.validateRegisterUser(registerUserDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return null;
        }

        Optional<User> usernameFoundOptional = userRepository.findByUsername(registerUserDTO.getUsername());

        if (usernameFoundOptional.isPresent()) {
            bindingResult.rejectValue("username", "UsernameAlreadyExists");
            return null;
        }

        User toRegisterUser = userMapper.toEntityFromRegisterUserDTO(registerUserDTO);

        return userRepository.save(toRegisterUser);
    }

}
