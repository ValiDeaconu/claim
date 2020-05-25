package org.claimapp.client.service.impl;

import org.claimapp.client.config.ServerConfig;
import org.claimapp.client.service.UserGateway;

import org.claimapp.common.dto.IdDTO;
import org.claimapp.common.dto.LoginUserDTO;
import org.claimapp.common.dto.RegisterUserDTO;
import org.claimapp.common.dto.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserGatewayImpl implements UserGateway {
    private final ServerConfig serverConfig;

    @Autowired
    public UserGatewayImpl(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public UserDTO getUser(LoginUserDTO loginUserDTO) {
        String url = serverConfig.getLocalUrl() + "/login";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> request = new HttpEntity<>(loginUserDTO);

        return restTemplate.postForObject(url, request, UserDTO.class);
    }

    @Override
    public UserDTO getUser(IdDTO currentUserIdDTO) {
        String url = serverConfig.getLocalUrl() + "/login/" + currentUserIdDTO.getId();

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, UserDTO.class);
    }

    @Override
    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {
        String url = serverConfig.getLocalUrl() + "/register";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> request = new HttpEntity<>(registerUserDTO);

        return restTemplate.postForObject(url, request, UserDTO.class);
    }
}
