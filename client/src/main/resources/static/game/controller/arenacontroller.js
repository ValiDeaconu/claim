import {Views} from "/game/view/viewmanager.js";
import AvatarManager from "/game/misc/avatarmanager.js";
import ConnectionHandler from "/game/server/connectionhandler.js";
import Request, {RequestMethod} from "/game/server/requesthandler.js";

export default class ArenaController {
    constructor(arenaView, viewManager, serverAddress) {
        this.ui = arenaView;
        this.viewManager = viewManager;
        this.serverAddress = serverAddress;

        this.lobby = null;
        this.gameState = null;

        this.droppedCards = [];
    }

    update() {
        if (this.lobby === undefined || this.lobby === null) {
            this.viewManager.setViewTo(Views.HOME);
            this.viewManager.updateUrl();
        }

        if (!this.lobby.running) {
            let controller = this.viewManager.setViewTo(Views.LOBBY);
            controller.lobby = this.lobby;
            this.viewManager.updateUrl();
        }

        this.ui.clearConsumers();
        this.userSelectedCards = [ false, false, false, false, false ];

        let currentPlayerIndexInLobby = 0;
        for (let i = 0; i < this.lobby.players.length; ++i) {
            if (this.lobby.players[i].id === this.viewManager.currentUser.id) {
                currentPlayerIndexInLobby = i;
            }

            this.lobby.players[i].image = AvatarManager.getRandomAvatar(Math.floor(Math.random() * 2))
        }

        let playerToCardMap = [];
        playerToCardMap[currentPlayerIndexInLobby] = 0;

        let cardIndex = 1;
        let playerIndex = 0;
        for (; playerIndex < this.lobby.players.length; ++playerIndex) {
            if (playerIndex === currentPlayerIndexInLobby) {
                continue;
            }

            this.ui.playerCard[cardIndex].setPlayer(this.lobby.players[playerIndex]);
            playerToCardMap[playerIndex] = cardIndex;
            cardIndex++;
        }

        for (; playerIndex < 5; ++playerIndex) {
            this.ui.playerCard[cardIndex].unsetPlayer();
            cardIndex++;
        }

        let trumpCardId = this.gameState.trump.rank + this.gameState.trump.suit;
        this.ui.setTrumpCard(trumpCardId);

        for (let i = 0; i < this.gameState.userHands.length; ++i) {
            if (i === currentPlayerIndexInLobby)
                continue;

            this.ui.setCardsInHand(playerToCardMap[i], this.gameState.userHands[i].cards.length);
        }

        let currentUserCardList = this.gameState.userHands[currentPlayerIndexInLobby].cards;
        let currentUserCardIndex = 0;
        for (; currentUserCardIndex < currentUserCardList.length; ++currentUserCardIndex) {
            let cardAssetId = currentUserCardList[currentUserCardIndex].rank + currentUserCardList[currentUserCardIndex].suit;
            this.ui.setCardVisible(currentUserCardIndex, true);
            this.ui.setCardInHand(currentUserCardIndex, cardAssetId);
        }

        for (; currentUserCardIndex < 5; ++currentUserCardIndex) {
            this.ui.setCardVisible(currentUserCardIndex, false);
        }

        let lastDroppedCard = this.gameState.thrownDeck.cards[this.gameState.thrownDeck.cards.length - 1];
        let lastDroppedCardAssetId = lastDroppedCard.rank + lastDroppedCard.suit;
        this.ui.setCurrentDroppedCard(lastDroppedCardAssetId);

        this.ui.setUserLastDroppedCardVisible(false);

        // set player turn
        this.ui.setTurn(playerToCardMap[this.gameState.turn]);

        // set round number
        this.ui.setRound(this.gameState.currentRound, 10);

        let isCurrentPlayerTurn = (this.gameState.turn === currentPlayerIndexInLobby);

        if (!this.handler || !this.handler.connected) {
            this.handler = new ConnectionHandler(this.serverAddress + "/game");
            this.handler.connect("/topic/lobby");
            this.handler.addOnReceiveCallback((jsonObject) => {
                let command = jsonObject.command;
                let lobbyId = jsonObject.lobbyId;

                if (this.lobby.id === lobbyId) {
                    if (command === "turn-update") {
                        this.gameState = jsonObject.arg;
                        this.droppedCards = [];
                        this.update();
                    } else if (command === 'update-user-left') {
                        this.lobby = jsonObject.arg;
                        this.gameState = this.lobby.gameState;
                        this.droppedCards = [];
                        this.update();
                        this.ui.setMessage("A player left the game", 3000);
                    } else if (command === "claim") {
                        this.lobby = jsonObject.arg.first;
                        let rankings = jsonObject.arg.second;

                        let winners = rankings.winners;
                        let losers = rankings.losers;

                        let winner = winners[0];

                        this.ui.putGameOverPanel();

                        this.ui.setClaimScore(winner.score);

                        if (winner.calledClaim) {
                            this.ui.setClaimCaller(winner.userDTO.username);
                            this.ui.setCallerWins(true);
                        } else {
                            let userWhoCalledClaimAndLost;
                            for (let i = 0; i < losers.length; ++i) {
                                if (losers[i].calledClaim) {
                                    userWhoCalledClaimAndLost = losers[i].userDTO;
                                    break;
                                }
                            }
                            this.ui.setClaimCaller(userWhoCalledClaimAndLost.username);
                            this.ui.setCallerWins(false, winner.userDTO.username);
                        }

                        let winningHand;
                        for (let i = 0; i < this.gameState.userHands.length; ++i) {
                            let currentIterationHand = this.gameState.userHands[i];
                            if (currentIterationHand.user.id === winner.userDTO.id) {
                                winningHand = currentIterationHand.cards;
                                break;
                            }
                        }

                        let cardIndex = 0;
                        for (; cardIndex < winningHand.length; ++cardIndex) {
                            let cardAssetId = winningHand[cardIndex].rank + winningHand[cardIndex].suit;
                            this.ui.setWinningCardVisible(cardIndex, false);
                            this.ui.setWinningCard(cardIndex, cardAssetId);
                        }

                        for (; cardIndex < 5; ++cardIndex) {
                            this.ui.setWinningCardVisible(cardIndex, false);
                        }

                        if (winners.length > 1) {
                            let otherWinners = [];
                            for (let i = 1; i < winners.length; ++i) {
                                otherWinners.push(winners[i].userDTO.username);
                            }

                            this.ui.setOtherWinners(otherWinners)
                        }
                    }
                }

            });

            this.request = new Request(this.serverAddress);
        }


        if (isCurrentPlayerTurn) {
            this.userSelectedCards = [];
            for (let i = 0; i < 5; ++i) {
                if (this.ui.cardInHand[i].visible) {
                    this.ui.setOnCardSelectConsumer(i, () => {
                        this.userSelectedCards[i] = true;
                    }, () => {
                        this.userSelectedCards[i] = false;
                    });
                }
            }

            let endTurnRequestFunc = (drawMethod) => {
                if (this.droppedCards.length === 0) {
                    this.ui.setMessage("You can draw a card after you dropped");
                    return;
                }

                let gameStateUpdate = {
                    droppedCards: this.droppedCards,
                    drawMethod: drawMethod
                }

                this.request.send(RequestMethod.POST,
                    "/match/" + this.lobby.id + "/turn-end",
                    JSON.stringify(gameStateUpdate));
            };

            this.ui.setOnDeckClickConsumer(() => { endTurnRequestFunc('deck'); });
            this.ui.setOnDroppedLastCardClickConsumer(() => { endTurnRequestFunc('drop'); });

            this.ui.setDropButtonConsumer(() => {
                if (this.droppedCards.length !== 0) {
                    this.ui.setMessage("You can drop only once, maybe you should draw a card");
                    return;
                }

                let cards = this.gameState.userHands[currentPlayerIndexInLobby].cards;

                for (let i = 0; i < this.userSelectedCards.length; ++i)
                    if (this.userSelectedCards[i])
                        this.droppedCards.push(cards[i]);

                if (this.droppedCards.length === 0) {
                    this.ui.setMessage("You have to drop at least 1 card");
                    return;
                }

                let allSameRank = true;
                for (let i = 1; i < this.droppedCards.length; ++i) {
                    if (!this.droppedCards[i])
                        throw 'droppedCards[' + i + '] is undefined';

                    if (this.droppedCards[i].rank !== this.droppedCards[i - 1].rank) {
                        allSameRank = false;
                    }
                }

                if (!allSameRank) {
                    this.droppedCards = [];
                    this.ui.setMessage("Only cards of the same rank can be thrown in pairs");
                    return;
                }

                let userLastDroppedCard = this.droppedCards[this.droppedCards.length - 1];
                let userLastDroppedCardId = userLastDroppedCard.rank + userLastDroppedCard.suit;

                this.ui.setUserLastDroppedCardVisible(true);
                this.ui.setUserLastDroppedCard(userLastDroppedCardId);

                for (let i = 0; i < this.userSelectedCards.length; ++i) {
                    if (this.userSelectedCards[i]) {
                        this.ui.setCardVisible(i, false);
                        this.userSelectedCards[i] = false;
                        this.ui.deselectCard(i);
                    }
                }
            });

            this.ui.setClaimButtonConsumer(() => {
                if (this.droppedCards.length !== 0) {
                    this.ui.setMessage("You can call claim only before you drop cards");
                    return;
                }

                if (this.gameState.currentRound <= 1) {
                    this.ui.setMessage("You must wait at least 1 round to call claim");
                    return;
                }

                this.request.send(RequestMethod.POST, "/match/" + this.lobby.id + "/claim");
            });
        }

        this.ui.setLeaveGameButtonConsumer(() => {
            if (this.lobby.running) {
                this.ui.setMessage("You cannot leave from an on-going match");
                return;
            }

            this.handler.disconnect();

            let controller = this.viewManager.setViewTo(Views.LOBBY);
            controller.lobby = this.lobby;
            controller.update();
            this.viewManager.updateUrl();
        });

    }

    downloadGameStateAndUpdate() {
        if (this.lobby === undefined || this.lobby === null) {
            this.viewManager.setViewTo(Views.HOME);
            this.viewManager.updateUrl();
        }

        if (!this.lobby.running) {
            let controller = this.viewManager.setViewTo(Views.LOBBY);
            controller.lobby = this.lobby;
            this.viewManager.updateUrl();
        }

        let request = new Request(this.serverAddress, (gameState) => {
            if (gameState) {
                this.gameState = gameState;

                let gameUsers = [];
                gameState.userHands.forEach(userHand => {
                   gameUsers.push(userHand.user);
                });

                let userInGame = false;
                for (let i = 0; i < gameUsers.length; ++i) {
                    if (gameUsers[i].id === this.viewManager.currentUser.id) {
                        userInGame = true;
                        break;
                    }
                }

                if (userInGame) {
                    this.update();
                    return;
                }
            }

            this.viewManager.setViewTo(Views.HOME);
            this.viewManager.updateUrl();
        });

        request.send(RequestMethod.GET, "/match/" + this.lobby.id + "/gamestate");
    }
}