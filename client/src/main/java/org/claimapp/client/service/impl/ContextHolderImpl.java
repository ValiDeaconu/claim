package org.claimapp.client.service.impl;

import org.claimapp.client.dto.IdDTO;
import org.claimapp.client.misc.ContextHolderConstants;
import org.claimapp.client.service.ContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class ContextHolderImpl implements ContextHolder {
    @Override
    public Cookie createCookieWithCurrentUser(IdDTO idDTO) {
        if (idDTO == null) {
            Cookie cookie = new Cookie(ContextHolderConstants.CURRENT_USER_COOKIE,
                    ContextHolderConstants.CURRENT_USER_COOKIE_DEFAULT);

            cookie.setMaxAge(100);
            cookie.setPath("/");

            return cookie;
        }

        Cookie cookie = new Cookie(ContextHolderConstants.CURRENT_USER_COOKIE, idDTO.getId().toString());

        cookie.setMaxAge(ContextHolderConstants.CURRENT_USER_COOKIE_EXPIRE_TIME);
        cookie.setPath("/");

        return cookie;
    }

    @Override
    public IdDTO getCurrentUserFromCookie(String cookieValue) {
        IdDTO idDTO = new IdDTO();

        // if long cannot be parsed from the cookie, set idDTO to null
        try {
            idDTO.setId(Long.parseLong(cookieValue));
            return idDTO.getId().compareTo(0L) > 0 ? idDTO : null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
