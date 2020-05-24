package org.claimapp.server.service.impl;

import org.claimapp.common.dto.RankingDTO;
import org.claimapp.common.dto.TurnEndDTO;
import org.claimapp.common.dto.UserDTO;
import org.claimapp.common.dto.UserScoreClaimDTO;
import org.claimapp.server.entity.*;
import org.claimapp.server.model.*;
import org.claimapp.server.model.misc.CardRank;
import org.claimapp.server.mapper.CardMapper;
import org.claimapp.server.mapper.UserMapper;
import org.claimapp.server.repository.GameStateRepository;
import org.claimapp.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameStateServiceImpl implements GameStateService {

    private final GameStateRepository gameStateRepository;

    private final DeckService deckService;
    private final HandService handService;

    private final UserService userService;
    private final LobbyService lobbyService;

    private final UserMapper userMapper;
    private final CardMapper cardMapper;

    @Autowired
    public GameStateServiceImpl(DeckService deckService,
                                HandService handService,
                                UserService userService,
                                LobbyService lobbyService,
                                UserMapper userMapper,
                                CardMapper cardMapper,
                                GameStateRepository gameStateRepository) {
        this.deckService = deckService;
        this.handService = handService;

        this.userService = userService;
        this.lobbyService = lobbyService;

        this.userMapper = userMapper;
        this.cardMapper = cardMapper;

        this.gameStateRepository = gameStateRepository;
    }

    @Override
    public GameState create(Long lobbyId, List<User> users) {
        Lobby lobbyById = lobbyService.getLobbyById(lobbyId);
        if (lobbyById == null)
            return null;

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

        gameStateRepository.save(gameState);

        lobbyById.setGameState(gameState);
        lobbyService.save(lobbyById);

        return gameState;
    }

    @Override
    public GameState getGameStateByLobbyId(Long lobbyId) {
        Lobby lobbyById = lobbyService.getLobbyById(lobbyId);

        if (lobbyById == null)
            return null;

        return lobbyById.getGameState();
    }

    @Override
    public GameState addMoveToCurrentGameState(Long lobbyId, TurnEndDTO turnEndDTO) {
        if (!turnEndDTO.getDrawMethod().equals("deck") && !turnEndDTO.getDrawMethod().equals("drop"))
            return null;

        GameState gameState = getGameStateByLobbyId(lobbyId);

        if (gameState != null) {
            // get current player by turn index
            int currentTurn = gameState.getTurn();

            // get current player's hand
            Hand userHand = gameState.getUserHands().get(currentTurn);

            // remove all dropped cards
            turnEndDTO.getDroppedCards().forEach(cardDTO -> {
                Card card = cardMapper.toEntity(cardDTO);
                userHand.getCards().remove(card);
            });

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
            turnEndDTO.getDroppedCards().forEach(cardDTO -> {
                Card card = cardMapper.toEntity(cardDTO);
                gameState.getThrownDeck().getCards().add(card);
            });

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

            gameStateRepository.save(gameState);

            return gameState;
        }

        return null;
    }

    @Override
    public RankingDTO getRankingOfGameState(Long lobbyId) {
        GameState gameState = getGameStateByLobbyId(lobbyId);

        if (gameState != null) {
            // set lobby as not running anymore
            lobbyService.endMatch(lobbyId);

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

                // increase wins and loss
                userService.increaseWins(winnerDTO.getId());

                losers.stream()
                        .map(UserScoreClaimDTO::getUserDTO)
                        .map(UserDTO::getId)
                        .forEach(userService::increaseLoss);

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

            Lobby lobbyById = lobbyService.getLobbyById(lobbyId);
            lobbyById.setGameState(null);
            lobbyService.save(lobbyById);

            gameStateRepository.deleteById(gameState.getId());

            // increase wins and loss
            winners.stream()
                    .map(UserScoreClaimDTO::getUserDTO)
                    .map(UserDTO::getId)
                    .forEach(userService::increaseWins);

            losers.stream()
                    .map(UserScoreClaimDTO::getUserDTO)
                    .map(UserDTO::getId)
                    .forEach(userService::increaseLoss);

            return new RankingDTO(winners, losers);
        }

        return null;
    }
}
