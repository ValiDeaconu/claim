package org.claimapp.server.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.claimapp.server.model.Card;
import org.claimapp.server.model.Deck;
import org.claimapp.server.model.misc.CardRank;
import org.claimapp.server.model.misc.CardSuit;
import org.claimapp.server.service.DeckService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class DeckServiceImpl implements DeckService {
    @Override
    public Deck getFreshDeck() {
        List<Card> cards = new LinkedList<>();
        for (String suit : CardSuit.SuitsAsList) {
            for (String rank : CardRank.RanksAsList) {
                cards.add(new Card(suit, rank));
            }
        }

        Deck deck = new Deck();
        deck.setCards(cards);

        return deck;
    }

    @Override
    public Deck shuffle(Deck deck) {
        Collections.shuffle(deck.getCards());
        return deck;
    }

    @Override
    public Deck getShuffledFreshDeck() {
        Deck deck = this.getFreshDeck();
        return this.shuffle(deck);
    }

    @Override
    public List<Card> drawCards(int count, Deck deck) {
        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < count; ++i) {
            cards.add(deck.getCards().get(0));
            deck.getCards().remove(0);
        }

        return cards;
    }

    @Override
    public Card drawLastCard(Deck deck) {
        Card lastCard = deck.getCards().get(deck.getCards().size() - 1);
        deck.getCards().remove(deck.getCards().size() - 1);

        return lastCard;
    }

    @Override
    public Deck addCards(Deck deck, List<Card> cards) {
        deck.getCards().addAll(cards);
        return deck;
    }

    @Override
    public String convertToString(Deck deck) {
        Gson gsonBuilder = new GsonBuilder().create();

        return gsonBuilder.toJson(deck);
    }

    @Override
    public Deck convertFromString(String deckStr) {
        Gson gson = new Gson();

        return gson.fromJson(deckStr, Deck.class);
    }
}
