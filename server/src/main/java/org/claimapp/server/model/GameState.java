package org.claimapp.server.model;

import java.util.List;

public class GameState {
    private Long id;

    private Deck remainingDeck;

    private Card trump;

    private Deck thrownDeck;

    private List<Hand> userHands;

    private int turn;

    private int currentRound;

    public GameState() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Deck getRemainingDeck() {
        return remainingDeck;
    }

    public void setRemainingDeck(Deck remainingDeck) {
        this.remainingDeck = remainingDeck;
    }

    public Card getTrump() {
        return trump;
    }

    public void setTrump(Card trump) {
        this.trump = trump;
    }

    public Deck getThrownDeck() {
        return thrownDeck;
    }

    public void setThrownDeck(Deck thrownDeck) {
        this.thrownDeck = thrownDeck;
    }

    public List<Hand> getUserHands() {
        return userHands;
    }

    public void setUserHands(List<Hand> userHands) {
        this.userHands = userHands;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
