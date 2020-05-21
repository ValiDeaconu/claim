package org.claimapp.client.service;

import org.claimapp.client.dto.IdDTO;

import javax.servlet.http.Cookie;

public interface ContextHolder {

    Cookie createCookieWithCurrentUser(IdDTO idDTO);
    IdDTO getCurrentUserFromCookie(String cookieValue);

}
