package org.claimapp.client.service;

import org.claimapp.client.dto.IdDTO;
import org.claimapp.client.dto.LoginUserDTO;
import org.claimapp.client.dto.RegisterUserDTO;
import org.claimapp.client.entity.User;
import org.springframework.validation.BindingResult;

public interface UserService {

    User getUser(LoginUserDTO loginUserDTO, BindingResult bindingResult);
    User getUser(IdDTO idDTO);

    User registerUser(RegisterUserDTO registerUserDTO, BindingResult bindingResult);

}
