package org.claimapp.server.service;

import org.claimapp.server.entity.Card;
import org.claimapp.server.entity.Deck;

import java.util.List;

public interface DeckService {
    Deck getFreshDeck();

    Deck shuffle(Deck deck);

    Deck getShuffledFreshDeck();

    List<Card> drawCards(int count, Deck deck);

    Card drawLastCard(Deck deck);

    Deck addCards(Deck deck, List<Card> cards);
}
