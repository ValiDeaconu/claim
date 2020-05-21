package org.claimapp.client.api;

import org.claimapp.client.dto.IdDTO;
import org.claimapp.client.dto.RegisterUserDTO;
import org.claimapp.client.entity.User;
import org.claimapp.client.misc.ContextHolderConstants;
import org.claimapp.client.service.ContextHolder;
import org.claimapp.client.service.UserService;
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

    private ContextHolder contextHolder;
    private UserService userService;

    @Autowired
    public RegisterController(ContextHolder contextHolder, UserService userService) {
        this.contextHolder = contextHolder;
        this.userService = userService;
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
        User registeredUser = userService.registerUser(registerUserDTO, bindingResult);

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
