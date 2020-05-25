package org.claimapp.client.api;

import org.claimapp.client.validator.UserValidator;
import org.claimapp.common.dto.IdDTO;
import org.claimapp.common.dto.RegisterUserDTO;

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
public class RegisterController {

    private final ContextHolder contextHolder;
    private final UserGateway userGateway;
    private final UserValidator userValidator;

    @Autowired
    public RegisterController(ContextHolder contextHolder,
                              UserGateway userGateway,
                              UserValidator userValidator) {
        this.contextHolder = contextHolder;
        this.userGateway = userGateway;
        this.userValidator = userValidator;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(@CookieValue(value = ContextHolderConstants.CURRENT_USER_COOKIE, defaultValue = ContextHolderConstants.CURRENT_USER_COOKIE_DEFAULT) String currentUserCookie,
                                        ModelMap modelMap,
                                        ModelAndView mav) {
        IdDTO currentUserIdDTO = contextHolder.getCurrentUserFromCookie(currentUserCookie);

        if (currentUserIdDTO != null) {
            mav.setViewName("redirect:/game/home");
            return mav;
        }

        modelMap.addAttribute("user", new RegisterUserDTO());
        mav.setViewName("welcome/register");
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView getRegisterSubmittedPage(@Valid @ModelAttribute("user") RegisterUserDTO registerUserDTO,
                                                 HttpServletResponse response,
                                                 BindingResult bindingResult,
                                                 ModelAndView mav) {
        userValidator.validateRegisterUser(registerUserDTO, bindingResult);

        UserDTO registeredUser = null;
        try {
            registeredUser = userGateway.registerUser(registerUserDTO);
            if (registeredUser == null) {
                bindingResult.reject("UsernameAlreadyTaken");
            }
        } catch (Exception ignored) {
            bindingResult.reject("CouldNotConnectToServer");
        }

        if (bindingResult.hasErrors()) {
            mav.setViewName("welcome/register");
            return mav;
        }

        Cookie cookieWithCurrentUser = contextHolder.createCookieWithCurrentUser(new IdDTO(registeredUser.getId()));
        response.addCookie(cookieWithCurrentUser);

        mav.setViewName("redirect:/game/home");
        return mav;
    }
}
