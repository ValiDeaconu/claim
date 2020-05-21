package org.claimapp.server.service.impl;

import org.claimapp.server.dto.*;
import org.claimapp.server.entity.*;
import org.claimapp.server.entity.misc.CardRank;
import org.claimapp.server.mapper.UserMapper;
import org.claimapp.server.repository.GameStateRepository;
import org.claimapp.server.service.DeckService;
import org.claimapp.server.service.GameManager;
import org.claimapp.server.service.HandService;
import org.claimapp.server.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameManagerImpl implements GameManager {

    private final GameStateRepository gameStateRepository;

    private final DeckService deckService;
    private final HandService handService;
    private final LobbyService lobbyService;

    private final UserMapper userMapper;

    @Autowired
    public GameManagerImpl(DeckService deckService,
                           HandService handService,
                           LobbyService lobbyService,
                           UserMapper userMapper,
                           GameStateRepository gameStateRepository) {
        this.deckService = deckService;
        this.handService = handService;
        this.lobbyService = lobbyService;

        this.userMapper = userMapper;

        this.gameStateRepository = gameStateRepository;
    }

    @Override
    public GameState create(UUID lobbyId, List<User> users) {
        // create a new deck and shuffle it
        Deck gameDeck = deckService.getShuffledFreshDeck();

        // extract trump from the game deck
        Card trump = deckService.drawCards(1, gameDeck).get(0);

        // while trump is an Ace, put it back to the end, and redraw
        while (trump.getRank().equals(CardRank.R_A)) {
            deckService.addCards(gameDeck, Collections.singletonList(trump));
            trump = deckService.drawCards(1, gameDeck).get(0);
        }

        // extract player hand cards
        List<Hand> hands = new ArrayList<>();
        for (User user : users) {
            List<Card> userCards = deckService.drawCards(5, gameDeck);
            hands.add(new Hand(user, userCards));
        }

        // extract first dropped card
        List<Card> thrownDeckFirstCardAsList = deckService.drawCards(1, gameDeck);

        // create a new, empty deck for thrown cards
        Deck thrownDeck = new Deck();
        thrownDeck.setCards(thrownDeckFirstCardAsList);

        // set turn to first player in list
        int turn = 0;

        // finally, create game state
        GameState gameState = new GameState();

        gameState.setRemainingDeck(gameDeck);
        gameState.setTrump(trump);
        gameState.setUserHands(hands);
        gameState.setThrownDeck(thrownDeck);
        gameState.setTurn(turn);
        gameState.setCurrentRound(1);

        gameStateRepository.save(lobbyId, gameState);

        return gameState;
    }

    @Override
    public GameState getGameState(UUID lobbyId) {
        return gameStateRepository.find(lobbyId).orElse(null);
    }

    @Override
    public GameState addMoveToCurrentGameState(UUID lobbyId, TurnEndDTO turnEndDTO) {
        if (!turnEndDTO.getDrawMethod().equals("deck") && !turnEndDTO.getDrawMethod().equals("drop"))
            return null;

        Optional<GameState> gameStateOptional = gameStateRepository.find(lobbyId);

        if (gameStateOptional.isPresent()) {
            GameState gameState = gameStateOptional.get();

            // get current player by turn index
            int currentTurn = gameState.getTurn();

            // get current player's hand
            Hand userHand = gameState.getUserHands().get(currentTurn);

            // remove all dropped cards
            userHand.getCards().removeAll(turnEndDTO.getDroppedCards());

            // if remaining deck it's empty, add thrown deck to it and reshuffle
            if (gameState.getRemainingDeck().getCards().size() == 0) {
                // get all cards from the thrown deck, without last one (the one current turn user can pick to draw)
                List<Card> thrownDeckCardsWithoutLastOne = deckService.drawCards(
                    gameState.getRemainingDeck().getCards().size() - 1,
                    gameState.getRemainingDeck());

                // add all thrown cards to the remaining deck
                gameState.getRemainingDeck().getCards().addAll(thrownDeckCardsWithoutLastOne);

                // reshuffle remaining deck
                deckService.shuffle(gameState.getRemainingDeck());
            }

            // get drawn card
            Card drawnCard;
            if (turnEndDTO.getDrawMethod().equals("deck")) {
                drawnCard = deckService.drawCards(1, gameState.getRemainingDeck()).get(0);
            } else {
                drawnCard = deckService.drawLastCard(gameState.getThrownDeck());
            }

            // add the drawn card to user hand
            userHand.getCards().add(drawnCard);

            // add dropped cards to the thrown deck
            gameState.getThrownDeck().getCards().addAll(turnEndDTO.getDroppedCards());

            // update user hand
            gameState.getUserHands().set(currentTurn, userHand);

            // compute next turn (increase round when it's necessary)
            int modulo = gameState.getUserHands().size();
            if (gameState.getTurn() + 1 >= modulo) {
                gameState.setTurn(0);
                gameState.setCurrentRound(gameState.getCurrentRound() + 1);
            } else {
                gameState.setTurn(gameState.getTurn() + 1);
            }

            gameStateRepository.update(lobbyId, gameState);

            return gameState;
        }

        return null;
    }

    @Override
    public RankingDTO getRankingOfGameState(UUID lobbyId) {
        Optional<GameState> gameStateOptional = gameStateRepository.find(lobbyId);

        if (gameStateOptional.isPresent()) {
            // set lobby as not running anymore
            lobbyService.endMatch(lobbyId);

            // compute other
            GameState gameState = gameStateOptional.get();

            List<Hand> userHands = gameState.getUserHands();

            List<Integer> userScores = new ArrayList<>();
            Map<Long, Integer> userIdToScore = new HashMap<>();
            for (Hand hand : userHands) {
                int score = handService.getScore(hand, gameState.getTrump());
                userScores.add(score);
                userIdToScore.put(hand.getUser().getId(), score);
            }

            int userWhoCalledClaimScore = userScores.get(gameState.getTurn());

            List<Integer> usersWhoBeatUserWhoCalledClaim = new ArrayList<>();
            for (int i = 0; i < userScores.size(); ++i) {
                if (i == gameState.getTurn())
                    continue;

                int currentIterationUserScore = userScores.get(i);
                if (currentIterationUserScore <= userWhoCalledClaimScore) {
                    usersWhoBeatUserWhoCalledClaim.add(i);
                }
            }

            if (usersWhoBeatUserWhoCalledClaim.size() == 0) {
                User winner = gameState.getUserHands().get(gameState.getTurn()).getUser();

                UserDTO winnerDTO = userMapper.toDTO(winner);

                UserScoreClaimDTO userScoreClaimDTO = new UserScoreClaimDTO();
                userScoreClaimDTO.setUserDTO(winnerDTO);
                userScoreClaimDTO.setCalledClaim(true);
                userScoreClaimDTO.setScore(userWhoCalledClaimScore);

                RankingDTO rankingDTO = new RankingDTO();
                rankingDTO.setWinners(Collections.singletonList(userScoreClaimDTO));

                List<UserScoreClaimDTO> losers = userHands
                        .stream()
                        .map(Hand::getUser)
                        .filter(u -> !u.getId().equals(winner.getId()))
                        .map(userMapper::toDTO)
                        .map(userDTO -> new UserScoreClaimDTO(
                                userDTO,
                                userIdToScore.get(userDTO.getId()),
                                false)
                        )
                        .collect(Collectors.toList());
                rankingDTO.setLosers(losers);

                return rankingDTO;
            }

            usersWhoBeatUserWhoCalledClaim.sort((o1, o2) -> {
                int lUserScore = userScores.get(o1);
                int rUserScore = userScores.get(o2);

                return Integer.compare(lUserScore, rUserScore);
            });

            boolean[] checkedPlayers = new boolean[userHands.size()];

            List<UserScoreClaimDTO> winners = new ArrayList<>();
            for (int userIndex : usersWhoBeatUserWhoCalledClaim) {
                checkedPlayers[userIndex] = true;

                User user = gameState.getUserHands().get(userIndex).getUser();
                UserDTO userDTO = userMapper.toDTO(user);

                UserScoreClaimDTO userScoreClaimDTO = new UserScoreClaimDTO();
                userScoreClaimDTO.setUserDTO(userDTO);
                userScoreClaimDTO.setScore(userScores.get(userIndex));
                userScoreClaimDTO.setCalledClaim(false);

                winners.add(userScoreClaimDTO);
            }

            List<UserScoreClaimDTO> losers = new ArrayList<>();
            for (int i = 0; i < userHands.size(); ++i) {
                if (checkedPlayers[i])
                    continue;

                User user = gameState.getUserHands().get(i).getUser();
                UserDTO userDTO = userMapper.toDTO(user);

                UserScoreClaimDTO userScoreClaimDTO = new UserScoreClaimDTO();
                userScoreClaimDTO.setUserDTO(userDTO);
                userScoreClaimDTO.setScore(userScores.get(i));
                userScoreClaimDTO.setCalledClaim(i == gameState.getTurn());

                losers.add(userScoreClaimDTO);
            }

            return new RankingDTO(winners, losers);
        }

        return null;
    }
}
