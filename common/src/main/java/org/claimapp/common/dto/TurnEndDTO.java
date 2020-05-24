package org.claimapp.common.dto;

import java.util.List;

public class TurnEndDTO {
    private List<CardDTO> droppedCards;
    private String drawMethod;

    public TurnEndDTO() {
    }

    public TurnEndDTO(List<CardDTO> droppedCards, String drawMethod) {
        this.droppedCards = droppedCards;
        this.drawMethod = drawMethod;
    }

    public List<CardDTO> getDroppedCards() {
        return droppedCards;
    }

    public void setDroppedCards(List<CardDTO> droppedCards) {
        this.droppedCards = droppedCards;
    }

    public String getDrawMethod() {
        return drawMethod;
    }

    public void setDrawMethod(String drawMethod) {
        this.drawMethod = drawMethod;
    }
}
