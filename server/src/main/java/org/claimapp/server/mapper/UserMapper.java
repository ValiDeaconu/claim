package org.claimapp.server.mapper;

import org.claimapp.server.dto.UserDTO;
import org.claimapp.server.entity.User;

public interface UserMapper {

    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);

}
