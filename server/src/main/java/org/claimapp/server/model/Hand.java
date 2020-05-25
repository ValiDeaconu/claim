package org.claimapp.server.model;

import org.claimapp.server.entity.User;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hand hand = (Hand) o;
        return Objects.equals(user, hand.user) &&
                Objects.equals(cards, hand.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, cards);
    }
}
