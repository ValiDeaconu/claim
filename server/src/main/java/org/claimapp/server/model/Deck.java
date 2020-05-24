package org.claimapp.server.model;

import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
