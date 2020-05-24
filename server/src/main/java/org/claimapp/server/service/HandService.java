package org.claimapp.server.service;

import org.claimapp.server.model.Card;
import org.claimapp.server.model.Hand;

import java.util.List;

public interface HandService {

    int getScore(Hand hand, Card trump);

    String convertToString(List<Hand> hand);

    List<Hand> convertFromString(String handString);
}
