package org.claimapp.server.service;

import org.claimapp.server.entity.Card;
import org.claimapp.server.entity.Hand;

public interface HandService {

    int getScore(Hand hand, Card trump);

}
