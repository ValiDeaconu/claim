import Request, {RequestMethod} from "/game/server/requesthandler.js";
import {Views} from "/game/view/viewmanager.js";
import ConnectionHandler from "/game/server/connectionhandler.js";

export default class HomeController {

    constructor(homeView, viewManager,serverAddress) {
        this.ui = homeView;

        this.viewManager = viewManager;

        this.serverAddress = serverAddress;

        this.update();
    }

    update() {
        this.ui.setUsernameLabelText(this.viewManager.currentUser.username);
        this.ui.setWinsLabelText(this.viewManager.currentUser.wins + " wins");
        this.ui.setLossLabelText(this.viewManager.currentUser.loss + " loss");

        this.ui.addCreateLobbyButtonConsumer(() => {
            let createLobbyRequest = new Request(this.serverAddress, (lobby) => {
                let lobbyController = this.viewManager.setViewTo(Views.LOBBY);
                lobbyController.lobby = lobby;
                lobbyController.update();
                this.viewManager.updateUrl();
            });

            let sendUser = { id: this.viewManager.currentUser.id, username: this.viewManager.currentUser.username };
            createLobbyRequest.send(RequestMethod.POST, "/lobby/create", JSON.stringify(sendUser));
        });

        this.ui.addLogoutButtonConsumer(() => {
            window.location.href = '/user/logout';
        });

        this.ui.addJoinButtonConsumer(() => {
            let accessCode = this.ui.searchTextField.label.text;

            let checkLobbyExistsRequest = new Request(this.serverAddress, (lobby) => {
                if (lobby) {

                    let handler = new ConnectionHandler(this.serverAddress + "/game");
                    handler.connect("/topic/lobby", () => {
                        let lobbyJoinPair = { first: lobby.id, second: this.viewManager.currentUser };
                        handler.send("/app/lobby/join", JSON.stringify(lobbyJoinPair));
                    });

                    handler.addOnReceiveCallback((jsonObject) => {
                        let command = jsonObject.command;
                        let lobbyId = jsonObject.lobbyId;

                        if (command === 'update') {
                            let lobbyController = this.viewManager.setViewTo(Views.LOBBY);
                            lobbyController.lobby = jsonObject.arg;
                            lobbyController.update();
                            handler.disconnect();
                            this.viewManager.updateUrl();
                        }
                    });
                } else {
                    alert("Requested lobby does not exists");
                }
            });

            checkLobbyExistsRequest.send(RequestMethod.GET, "/lobby/code/" + accessCode);
        });

        let request = new Request(this.serverAddress, () => {
            this.ui.setServerStatus(true);
        }, () => {
            this.ui.setServerStatus(false);
        });

        let statusChecker = function() {
            request.send(RequestMethod.GET, "/status");
            setTimeout(statusChecker, 10000);
        };
        statusChecker();
    }
}