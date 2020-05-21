package org.claimapp.server.entity;

import java.util.List;

public class Hand {

    private User user;
    private List<Card> cards;

    public Hand() {
    }

    public Hand(User user, List<Card> cards) {
        this.user = user;
        this.cards = cards;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
