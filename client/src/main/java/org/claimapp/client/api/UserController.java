package org.claimapp.client.api;

import org.claimapp.client.dto.IdDTO;
import org.claimapp.client.entity.User;
import org.claimapp.client.misc.ContextHolderConstants;
import org.claimapp.client.service.ContextHolder;
import org.claimapp.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("/game")
public class UserController {

    private ContextHolder contextHolder;
    private UserService userService;

    @Autowired
    public UserController(ContextHolder contextHolder,
                          UserService userService) {
        this.contextHolder = contextHolder;
        this.userService = userService;
    }

    @GetMapping("/home")
    public ModelAndView getUserPage(@CookieValue(value = ContextHolderConstants.CURRENT_USER_COOKIE, defaultValue = ContextHolderConstants.CURRENT_USER_COOKIE_DEFAULT) String currentUserCookie,
                                    ModelAndView mav) {
        IdDTO currentUserIdDTO = contextHolder.getCurrentUserFromCookie(currentUserCookie);

        if (currentUserIdDTO == null) {
            mav.setViewName("redirect:/");
            return mav;
        }

        User currentUser = userService.getUser(currentUserIdDTO);

        if (currentUser == null) {
            // remove cookie if no user is found with current cookie
            contextHolder.createCookieWithCurrentUser(null);
            mav.setViewName("redirect:/");
            return mav;
        }

        mav.addObject("currentUser", currentUser);
        mav.setViewName("/game/ui");

        return mav;
    }

    @GetMapping("/lobby/{lobbyId}")
    public ModelAndView getLobbyPage(@PathVariable("lobbyId") UUID lobbyId,
                                     @CookieValue(value = ContextHolderConstants.CURRENT_USER_COOKIE, defaultValue = ContextHolderConstants.CURRENT_USER_COOKIE_DEFAULT) String currentUserCookie,
                                     ModelAndView mav) {
        IdDTO currentUserIdDTO = contextHolder.getCurrentUserFromCookie(currentUserCookie);

        if (currentUserIdDTO == null) {
            mav.setViewName("redirect:/");
            return mav;
        }

        User currentUser = userService.getUser(currentUserIdDTO);

        if (currentUser == null) {
            // remove cookie if no user is found with current cookie
            contextHolder.createCookieWithCurrentUser(null);
            mav.setViewName("redirect:/");
            return mav;
        }

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
