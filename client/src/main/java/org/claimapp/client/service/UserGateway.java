package org.claimapp.client.service;

import org.claimapp.common.dto.IdDTO;
import org.claimapp.common.dto.LoginUserDTO;
import org.claimapp.common.dto.RegisterUserDTO;
import org.claimapp.common.dto.UserDTO;

public interface UserGateway {
    UserDTO getUser(LoginUserDTO loginUserDTO);
    UserDTO getUser(IdDTO currentUserIdDTO);

    UserDTO registerUser(RegisterUserDTO registerUserDTO);
}
