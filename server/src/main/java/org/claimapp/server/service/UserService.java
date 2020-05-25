package org.claimapp.server.service;

import org.claimapp.common.dto.*;
import org.claimapp.server.entity.User;

import java.util.List;

public interface UserService {
    UserDTO getUser(LoginUserDTO loginUserDTO);
    UserDTO getUser(Long userId);

    User findUserById(Long userId);

    UserDTO registerUser(RegisterUserDTO registerUserDTO);

    UserDTO updateProfile(Long userId, SingletonDTO<Integer> profileAssetIndexDTO);

    void increaseLoss(Long userId);
    void increaseLoss(List<Long> userIds);
    void increaseWins(Long userId);
    void increaseWins(List<Long> userIds);
}
