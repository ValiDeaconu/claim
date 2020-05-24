package org.claimapp.server.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.claimapp.server.model.Card;
import org.claimapp.server.model.Deck;
import org.claimapp.server.model.Hand;
import org.claimapp.server.model.misc.CardRank;
import org.claimapp.server.service.HandService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    @Override
    public String convertToString(List<Hand> hands) {
        Gson gsonBuilder = new GsonBuilder().create();

        return gsonBuilder.toJson(hands);
    }

    @Override
    public List<Hand> convertFromString(String handString) {
        Gson gson = new Gson();

        Hand[] hands = gson.fromJson(handString, Hand[].class);
        return Arrays.asList(hands);
    }
}
