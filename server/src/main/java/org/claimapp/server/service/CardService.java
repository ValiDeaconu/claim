package org.claimapp.server.service;

import org.claimapp.server.model.Card;

public interface CardService {
    String convertToString(Card card);

    Card convertFromString(String cardStr);
}
