package org.claimapp.server.mapper;

import org.claimapp.common.dto.RegisterUserDTO;
import org.claimapp.common.dto.UserDTO;
import org.claimapp.server.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface UserMapper {

    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    User toEntityFromRegisterUserDTO(RegisterUserDTO registerUserDTO);

}
