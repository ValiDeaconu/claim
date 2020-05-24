package org.claimapp.client.service;

import org.claimapp.common.dto.IdDTO;

import javax.servlet.http.Cookie;

public interface ContextHolder {

    Cookie createCookieWithCurrentUser(IdDTO idDTO);
    IdDTO getCurrentUserFromCookie(String cookieValue);

}
