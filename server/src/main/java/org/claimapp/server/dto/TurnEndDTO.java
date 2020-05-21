package org.claimapp.server.dto;

import org.claimapp.server.entity.Card;

import java.util.List;

public class TurnEndDTO {
    private List<Card> droppedCards;
    private String drawMethod;

    public TurnEndDTO() {
    }

    public TurnEndDTO(List<Card> droppedCards, String drawMethod) {
        this.droppedCards = droppedCards;
        this.drawMethod = drawMethod;
    }

    public List<Card> getDroppedCards() {
        return droppedCards;
    }

    public void setDroppedCards(List<Card> droppedCards) {
        this.droppedCards = droppedCards;
    }

    public String getDrawMethod() {
        return drawMethod;
    }

    public void setDrawMethod(String drawMethod) {
        this.drawMethod = drawMethod;
    }
}
