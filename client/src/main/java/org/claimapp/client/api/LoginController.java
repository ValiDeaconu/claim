package org.claimapp.client.api;

import org.claimapp.client.validator.UserValidator;
import org.claimapp.common.dto.IdDTO;
import org.claimapp.common.dto.LoginUserDTO;

import org.claimapp.client.misc.ContextHolderConstants;
import org.claimapp.client.service.ContextHolder;
import org.claimapp.client.service.UserGateway;

import org.claimapp.common.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class LoginController {

    private final ContextHolder contextHolder;
    private final UserValidator userValidator;
    private final UserGateway userGateway;

    @Autowired
    public LoginController(ContextHolder contextHolder,
                           UserGateway userGateway,
                           UserValidator userValidator) {
        this.contextHolder = contextHolder;
        this.userGateway = userGateway;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@CookieValue(value = ContextHolderConstants.CURRENT_USER_COOKIE, defaultValue = ContextHolderConstants.CURRENT_USER_COOKIE_DEFAULT) String currentUserCookie,
                                     ModelMap modelMap,
                                     ModelAndView mav) {

        IdDTO currentUserIdDTO = contextHolder.getCurrentUserFromCookie(currentUserCookie);

        if (currentUserIdDTO != null) {
            mav.setViewName("redirect:/game/home");
            return mav;
        }

        modelMap.addAttribute("user", new LoginUserDTO());
        mav.setViewName("welcome/login");
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView getLoginSubmittedPage(@Valid @ModelAttribute("user") LoginUserDTO loginUserDTO,
                                              HttpServletResponse response,
                                              BindingResult bindingResult,
                                              ModelAndView mav) {
        userValidator.validateLoginUser(loginUserDTO, bindingResult);

        UserDTO userDTO = userGateway.getUser(loginUserDTO);
        if (userDTO == null) {
            bindingResult.reject("CredentialsNotFound");
        }

        if (bindingResult.hasErrors()) {
            mav.setViewName("welcome/login");
            return mav;
        }

        Cookie cookieWithCurrentUser = contextHolder.createCookieWithCurrentUser(new IdDTO(userDTO.getId()));
        response.addCookie(cookieWithCurrentUser);

        mav.setViewName("redirect:/game/home");
        return mav;
    }
}
