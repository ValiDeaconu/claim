package org.claimapp.server.mapper.impl;

import org.claimapp.server.dto.UserDTO;
import org.claimapp.server.entity.User;
import org.claimapp.server.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());

        return user;
    }
}
