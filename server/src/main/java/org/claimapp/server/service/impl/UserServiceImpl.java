package org.claimapp.server.service.impl;

import org.claimapp.common.dto.LoginUserDTO;
import org.claimapp.common.dto.RegisterUserDTO;
import org.claimapp.common.dto.SingletonDTO;
import org.claimapp.common.dto.UserDTO;
import org.claimapp.server.entity.User;
import org.claimapp.server.mapper.UserMapper;
import org.claimapp.server.repository.UserRepository;
import org.claimapp.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUser(LoginUserDTO loginUserDTO) {
        List<User> all = userRepository.findAll();
        Optional<User> userOptional = all.stream()
                .filter(user ->
                        user.getUsername().equals(loginUserDTO.getUsername()) &&
                                user.getPassword().equals(loginUserDTO.getPassword())
                )
                .findFirst();

        // Can be null only if user with the inserted credentials is not found
        if (userOptional.isEmpty())
            return null;

        return userMapper.toDTO(userOptional.get());
    }

    @Override
    public UserDTO getUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty())
            return null;

        return userMapper.toDTO(userOptional.get());
    }


    @Override
    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {
        Optional<User> usernameFoundOptional = userRepository.findByUsername(registerUserDTO.getUsername());

        if (usernameFoundOptional.isPresent())
            return null; // null only if username already exists

        User toRegisterUser = userMapper.toEntityFromRegisterUserDTO(registerUserDTO);

        User registeredUser = userRepository.save(toRegisterUser);

        return userMapper.toDTO(registeredUser);
    }

    @Override
    public UserDTO updateProfile(Long userId, SingletonDTO<Integer> profileAssetIndexDTO) {
        Optional<User> userByIdOptional = userRepository.findById(userId);

        if (userByIdOptional.isEmpty())
            return null;

        User userById = userByIdOptional.get();
        userById.setProfileAssetIndex(profileAssetIndexDTO.getContent());
        User savedUser = userRepository.save(userById);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public void increaseLoss(Long userId) {
        Optional<User> userByIdOptional = userRepository.findById(userId);

        if (userByIdOptional.isPresent()) {
            User user = userByIdOptional.get();
            user.setLoss(user.getLoss() + 1);
            userRepository.save(user);
        }
    }

    @Override
    public void increaseLoss(List<Long> userIds) {
        userIds.forEach(this::increaseLoss);
    }

    @Override
    public void increaseWins(Long userId) {
        Optional<User> userByIdOptional = userRepository.findById(userId);

        if (userByIdOptional.isPresent()) {
            User user = userByIdOptional.get();
            user.setWins(user.getWins() + 1);
            userRepository.save(user);
        }
    }

    @Override
    public void increaseWins(List<Long> userIds) {
        userIds.forEach(this::increaseWins);
    }
}
