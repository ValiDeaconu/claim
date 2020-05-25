import Request, {RequestMethod} from "/game/server/requesthandler.js";
import {Views} from "/game/view/viewmanager.js";
import ConnectionHandler from "/game/server/connectionhandler.js";

export default class LobbyController {
    constructor(lobbyView, viewManager, serverAddress) {
        this.ui = lobbyView;
        this.viewManager = viewManager;
        this.serverAddress = serverAddress;

        this.lobby = null;
    }

    update() {
        if (this.lobby === undefined || this.lobby === null) {
            this.viewManager.setViewTo(Views.HOME);
            this.viewManager.updateUrl();
        }

        let currentPlayerIndexInLobby = 0;
        for (let i = 0; i < this.lobby.players.length; ++i) {
            if (this.lobby.players[i].id === this.viewManager.currentUser.id) {
                currentPlayerIndexInLobby = i;
            }
        }

        this.ui.playerCard[0].setPlayer(this.lobby.players[currentPlayerIndexInLobby]);
        this.ui.playerCard[0].setReadyStatus(true);

        if (this.lobby.players[0].id === this.viewManager.currentUser.id) {
            this.ui.setStartMatchButtonVisibility(true);
            this.ui.setChangeVisibilityButtonVisibility(true);
        } else {
            this.ui.setStartMatchButtonVisibility(false);
            this.ui.setChangeVisibilityButtonVisibility(false);
        }

        if (currentPlayerIndexInLobby === 0) {
            this.ui.playerCard[0].strokeStyle = "rgba(250, 189, 67, 1)";
        }

        let cardIndex = 1;
        let playerIndex = 0;
        for (; playerIndex < this.lobby.players.length; ++playerIndex) {
            if (playerIndex === 0) {
                this.ui.playerCard[cardIndex].strokeStyle = "rgba(250, 189, 67, 1)";
            } else {
                this.ui.playerCard[cardIndex].strokeStyle = "rgba(255, 255, 255, 1)";
            }

            if (playerIndex === currentPlayerIndexInLobby)
                continue;

            this.ui.playerCard[cardIndex].setPlayer(this.lobby.players[playerIndex]);
            this.ui.playerCard[cardIndex].setReadyStatus(true);
            cardIndex++;
        }

        for (; playerIndex < 5; ++playerIndex) {
            this.ui.playerCard[cardIndex].strokeStyle = "rgba(255, 255, 255, 1)";
            this.ui.playerCard[cardIndex].unsetPlayer();
            this.ui.playerCard[cardIndex].setReadyStatus(false);
            cardIndex++;
        }

        this.ui.flipChangeVisibility(this.lobby.visible);

        this.ui.setPrivateCode(this.lobby.accessCode);

        this.ui.addChangeVisibilityConsumer(() => {
            let request = new Request(this.serverAddress, (newLobby) => {
                this.ui.flipChangeVisibility(newLobby.visible);
                this.lobby = newLobby;
            });

            request.send(RequestMethod.POST, "/lobby/flip", JSON.stringify(this.lobby.id));
        });

        this.ui.addLeaveLobbyConsumer(() => {
            let request = new Request(this.serverAddress, (newLobby) => {
                this.viewManager.setViewTo(Views.HOME);
                this.viewManager.updateUrl();
            });

            let lobbyUserPair = {
                first: this.lobby.id,
                second: this.viewManager.currentUser.id
            };

            request.send(RequestMethod.POST, "/lobby/leave", JSON.stringify(lobbyUserPair));
            this.handler.disconnect();
        });

        this.ui.addStartMatchConsumer(() => {
            let request = new Request(this.serverAddress);
            request.send(RequestMethod.POST, "/lobby/start", JSON.stringify({ content: this.lobby.id }));
        });

        if (!this.handler || !this.handler.connected) {
            this.handler = new ConnectionHandler(this.serverAddress + "/game");
            this.handler.connect("/topic/lobby");
            this.handler.addOnReceiveCallback((jsonObject) => {
                let command = jsonObject.command;
                let lobbyId = jsonObject.lobbyId;

                if (this.lobby.id !== lobbyId)
                    return;

                if (command === 'update' || command === 'update-user-left') {
                    this.lobby = jsonObject.arg;
                    this.update();
                } else if (command === 'start' && this.lobby.id === lobbyId) {
                    let lobbyGameStatePair = jsonObject.arg;
                    let gameLobby = lobbyGameStatePair.first;
                    let gameState = lobbyGameStatePair.second;

                    this.handler.disconnect();

                    let controller = this.viewManager.setViewTo(Views.ARENA);
                    controller.lobby = gameLobby;
                    controller.gameState = gameState;
                    controller.update();
                    this.viewManager.updateUrl();
                }
            });
        }

        if (this.lobby.running) {
            let controller = this.viewManager.setViewTo(Views.ARENA);
            controller.lobby = this.lobby;
            controller.update();
            this.viewManager.updateUrl();
        }
    }
}