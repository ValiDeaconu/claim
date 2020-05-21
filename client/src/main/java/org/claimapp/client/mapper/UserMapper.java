package org.claimapp.client.mapper;

import org.claimapp.client.dto.RegisterUserDTO;
import org.claimapp.client.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface UserMapper {
    User toEntityFromRegisterUserDTO(RegisterUserDTO registerUserDTO);
}
