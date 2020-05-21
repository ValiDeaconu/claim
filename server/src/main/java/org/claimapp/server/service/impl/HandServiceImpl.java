package org.claimapp.server.service.impl;

import org.claimapp.server.entity.Card;
import org.claimapp.server.entity.Hand;
import org.claimapp.server.entity.misc.CardRank;
import org.claimapp.server.service.HandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandServiceImpl implements HandService {

    @Override
    public int getScore(Hand hand, Card trump) {
        List<Card> cards = hand.getCards();

        int score = 0;
        for (Card card : cards) {
            if (card.getRank().equals(trump.getRank()))
                continue;

            score += CardRank.getScore(card.getRank());
        }

        return score;
    }
}
