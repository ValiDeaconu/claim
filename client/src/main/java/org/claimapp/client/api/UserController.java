package org.claimapp.client.api;

import org.claimapp.client.config.ServerConfig;
import org.claimapp.client.misc.ContextHolderConstants;
import org.claimapp.client.service.ContextHolder;
import org.claimapp.client.service.UserGateway;

import org.claimapp.common.dto.IdDTO;

import org.claimapp.common.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/game")
public class UserController {

    private final ContextHolder contextHolder;
    private final ServerConfig serverConfig;
    private final UserGateway userGateway;

    @Autowired
    public UserController(ContextHolder contextHolder,
                          ServerConfig serverConfig,
                          UserGateway userGateway) {
        this.contextHolder = contextHolder;
        this.serverConfig = serverConfig;
        this.userGateway = userGateway;
    }

    @GetMapping("/home")
    public ModelAndView getUserPage(@CookieValue(value = ContextHolderConstants.CURRENT_USER_COOKIE, defaultValue = ContextHolderConstants.CURRENT_USER_COOKIE_DEFAULT) String currentUserCookie,
                                    ModelAndView mav) {
        IdDTO currentUserIdDTO = contextHolder.getCurrentUserFromCookie(currentUserCookie);

        if (currentUserIdDTO == null) {
            mav.setViewName("redirect:/");
            return mav;
        }

        UserDTO currentUser = userGateway.getUser(currentUserIdDTO);

        if (currentUser == null) {
            // remove cookie if no user is found with current cookie
            contextHolder.createCookieWithCurrentUser(null);
            mav.setViewName("redirect:/");
            return mav;
        }

        mav.addObject("serverAddress", serverConfig.getUrl());
        mav.addObject("currentUser", currentUser);
        mav.setViewName("/game/ui");

        return mav;
    }

    @GetMapping("/lobby/{lobbyId}")
    public ModelAndView getLobbyPage(@PathVariable("lobbyId") Long lobbyId,
                                     @CookieValue(value = ContextHolderConstants.CURRENT_USER_COOKIE, defaultValue = ContextHolderConstants.CURRENT_USER_COOKIE_DEFAULT) String currentUserCookie,
                                     ModelAndView mav) {
        IdDTO currentUserIdDTO = contextHolder.getCurrentUserFromCookie(currentUserCookie);

        if (currentUserIdDTO == null) {
            mav.setViewName("redirect:/");
            return mav;
        }

        UserDTO currentUser = userGateway.getUser(currentUserIdDTO);

        if (currentUser == null) {
            // remove cookie if no user is found with current cookie
            contextHolder.createCookieWithCurrentUser(null);
            mav.setViewName("redirect:/");
            return mav;
        }

        mav.addObject("serverAddress", serverConfig.getUrl());
        mav.addObject("currentLobbyId", lobbyId);
        mav.addObject("currentUser", currentUser);
        mav.setViewName("/game/ui");

        return mav;
    }

    @GetMapping("/logout")
    public ModelAndView userLogout(HttpServletResponse response,
                                   ModelAndView mav) {
        Cookie cookieRemoval = contextHolder.createCookieWithCurrentUser(null);
        response.addCookie(cookieRemoval);

        mav.setViewName("redirect:/");
        return mav;
    }
}
