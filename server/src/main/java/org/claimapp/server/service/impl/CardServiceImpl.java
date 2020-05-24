package org.claimapp.server.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.claimapp.server.model.Card;
import org.claimapp.server.model.Hand;
import org.claimapp.server.service.CardService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Override
    public String convertToString(Card card) {
        Gson gsonBuilder = new GsonBuilder().create();

        return gsonBuilder.toJson(card);
    }

    @Override
    public Card convertFromString(String cardStr) {
        Gson gson = new Gson();
        return gson.fromJson(cardStr, Card.class);
    }
}
