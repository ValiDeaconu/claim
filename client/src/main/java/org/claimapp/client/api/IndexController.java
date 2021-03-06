package org.claimapp.client.api;

import org.claimapp.common.dto.IdDTO;

import org.claimapp.client.misc.ContextHolderConstants;
import org.claimapp.client.service.ContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {

    private final ContextHolder contextHolder;

    @Autowired
    public IndexController(ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }

    @GetMapping("/")
    public ModelAndView getWelcomePage(@CookieValue(value = ContextHolderConstants.CURRENT_USER_COOKIE, defaultValue = ContextHolderConstants.CURRENT_USER_COOKIE_DEFAULT) String currentUserCookie,
                                       ModelAndView mav) {
        IdDTO currentUserIdDTO = contextHolder.getCurrentUserFromCookie(currentUserCookie);

        if (currentUserIdDTO != null) {
            mav.setViewName("redirect:/game/home");
            return mav;
        }

        mav.setViewName("redirect:/login");
        return mav;
    }
}
