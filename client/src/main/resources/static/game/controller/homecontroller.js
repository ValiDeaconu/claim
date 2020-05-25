import Request, {RequestMethod} from "/game/server/requesthandler.js";
import {Views} from "/game/view/viewmanager.js";
import ConnectionHandler from "/game/server/connectionhandler.js";
import {ProfileAssetPack} from "/game/misc/assets.js";

export default class HomeController {

    constructor(homeView, viewManager, serverAddress) {
        this.ui = homeView;

        this.viewManager = viewManager;

        this.serverAddress = serverAddress;

        this.update();
    }

    updateCurrentUser() {
        let request = new Request(this.serverAddress, (currentUser) => {
            this.viewManager.currentUser = {
                id: currentUser.id,
                username: currentUser.username,
                wins: currentUser.wins,
                loss: currentUser.loss,
                profileAssetIndex: currentUser.profileAssetIndex
            };
        });

        request.send(RequestMethod.GET, "/user/get/" + this.viewManager.currentUser.id);
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

            let hostIdDTO = { content: this.viewManager.currentUser.id };
            createLobbyRequest.send(RequestMethod.POST, "/lobby/create", JSON.stringify(hostIdDTO));
        });

        this.ui.addLogoutButtonConsumer(() => {
            window.location.href = '/game/logout';
        });

        this.ui.addJoinButtonConsumer(() => {
            let accessCode = this.ui.searchTextField.label.text;

            let checkLobbyExistsRequest = new Request(this.serverAddress, (lobby) => {
                if (lobby) {
                    this.__broadcastJoinLobby__(lobby.id);
                } else {
                    alert("Requested lobby does not exists");
                }
            });

            checkLobbyExistsRequest.send(RequestMethod.GET, "/lobby/code/" + accessCode);
        });

        if (!this.viewManager.currentUser.profileAssetIndex) {
            this.viewManager.currentUser.profileAssetIndex = 0;
            this.ui.setProfilePictureAsset(ProfileAssetPack[0]);
        } else {
          this.ui.setProfilePictureAsset(ProfileAssetPack[this.viewManager.currentUser.profileAssetIndex]);
        }

        this.ui.setProfilePictureOnImageClicked(() => {
            this.viewManager.currentUser.profileAssetIndex = (this.viewManager.currentUser.profileAssetIndex + 1) % ProfileAssetPack.length;
            this.ui.setProfilePictureAsset(ProfileAssetPack[this.viewManager.currentUser.profileAssetIndex]);

            let profileAssetIndexDTO = {
                content: this.viewManager.currentUser.profileAssetIndex
            };

            (new Request(this.serverAddress))
                .send(
                    RequestMethod.POST,
                    "/user/" + this.viewManager.currentUser.id + "/update/profile",
                    JSON.stringify(profileAssetIndexDTO)
                );
        });

        let hoverDisappearTid = null;
        this.ui.setProfilePictureOnImageHover(() => {
            if (hoverDisappearTid === null) {
                this.ui.setProfilePictureHoverLabelVisible(true);
                hoverDisappearTid = setTimeout(() => {
                    this.ui.setProfilePictureHoverLabelVisible(false);
                    hoverDisappearTid = null;
                }, 1000);
            }
        });

        this.handler = new ConnectionHandler(this.serverAddress + "/game");
        this.handler.connect("/topic/lobby/list");
        this.handler.addOnReceiveCallback((jsonObject) => {
            let command = jsonObject.command;

            if (command === 'update') {
                this.__updateLobbyList__(jsonObject.arg);
            }
        });

        (new Request(this.serverAddress, (lobbyList) => {
            this.__updateLobbyList__(lobbyList);
        })).send(RequestMethod.GET, "/lobby/all");
    }

    __broadcastJoinLobby__(lobbyId) {
        let socketHandler = new ConnectionHandler(this.serverAddress + "/game");
        socketHandler.connect("/topic/lobby", () => {
            socketHandler.send("/app/lobby/join", JSON.stringify({
                first: lobbyId,
                second: this.viewManager.currentUser.id
            }));
        });

        socketHandler.addOnReceiveCallback((jsonObject) => {
            let command = jsonObject.command;

            if (command === 'update') {
                let lobbyController = this.viewManager.setViewTo(Views.LOBBY);
                lobbyController.lobby = jsonObject.arg;
                lobbyController.update();
                socketHandler.disconnect();
                this.viewManager.updateUrl();
            }
        });
    }

    __updateLobbyList__(lobbyList) {
        if (lobbyList.length === 0) {
            this.ui.setLobbyList([]);
        } else {
            let lobbyShowList = [];
            for (let i = 0; i < lobbyList.length; ++i) {
                lobbyShowList.push({
                    hostname: lobbyList[i].players[0].username,
                    occupiedSlots: lobbyList[i].players.length,
                    totalSlots: 5,
                    lobbyId: lobbyList[i].id
                });
            }

            let joinButtons = this.ui.setLobbyList(lobbyShowList);
            for (let i = 0; i < joinButtons.length; ++i) {
                joinButtons[i].onButtonClicked(() => {
                    this.__broadcastJoinLobby__(lobbyShowList[i].lobbyId);
                });
            }
        }
    }
}