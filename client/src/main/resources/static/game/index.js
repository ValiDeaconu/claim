import Game from '/game/game.js'
import InputHandler from "/game/inputhandler/inputhandler.js";

import {Locale_EN} from "/game/locale/locale_en.js";

import ViewManager, {Views} from "/game/view/viewmanager.js";

import ResourceManager from "/game/misc/resourcemanager.js";

import Request, {RequestMethod} from "/game/server/requesthandler.js";

let game = null;

window.addEventListener('load', () => {
    let currentUser = {
        id: parseInt($("#current-user-id").val()),
        username: $("#current-user-username").val(),
        wins: parseInt($("#current-user-wins").val()),
        loss: parseInt($("#current-user-loss").val()),
        profileAssetIndex: parseInt($("#current-user-profile-asset-index").val()),
    };

    let currentLobby = {
        id: $("#current-lobby-id").val()
    };

    let canvas = $("#gameScreen")[0];

    // prepare assets
    ResourceManager.loadAssets();

    // server address
    let serverAddress = $("#server-address").val();

    let viewManager = new ViewManager(Locale_EN, currentUser, serverAddress);

    if (currentLobby.id && currentLobby.id !== "") {
        let lobbyRequest = new Request(serverAddress, (lobby) => {
            if (lobby.running) {
                let controller = viewManager.setViewTo(Views.ARENA);
                controller.lobby = lobby;
                controller.downloadGameStateAndUpdate();
                viewManager.updateUrl();
            } else {
                let userInLobby = false;
                for (let i = 0; i < lobby.players.length; ++i) {
                    if (lobby.players[i].id === currentUser.id) {
                        userInLobby = true;
                        break;
                    }
                }

                if (userInLobby) {
                    let controller = viewManager.setViewTo(Views.LOBBY);
                    controller.lobby = lobby;
                    controller.update();
                    viewManager.updateUrl();
                } else {
                    viewManager.setViewTo(Views.HOME);
                    viewManager.updateUrl();
                }
            }
        }, () => {
            viewManager.setViewTo(Views.HOME);
            viewManager.updateUrl();
        });

        lobbyRequest.send(RequestMethod.GET, "/lobby/id/" + currentLobby.id);
    } else {
        viewManager.setViewTo(Views.HOME);
    }

    game = new Game(canvas, viewManager, new InputHandler(), currentUser);

    game.resize(document.body.clientWidth, document.body.clientHeight);

    game.start();
})

window.addEventListener('resize', () => {
    if (game !== null) {
        game.resize(document.body.clientWidth, document.body.clientHeight);
    }
});

window.addEventListener("beforeunload", function (e) {
    leave();

    (e || window.event).returnValue = null;
    return null;
});

function leave() {
    if (game !== null && game.viewManager !== null) {
        if (game.viewManager.currentView === Views.HOME)
            return;

        let controller = game.viewManager.controller;
        let lobby = controller.lobby;

        let request = new Request(game.viewManager.serverAddress);

        let lobbyUserPair = {
            first: lobby.id,
            second: game.viewManager.currentUser.id
        };

        request.send(RequestMethod.POST, "/lobby/leave", JSON.stringify(lobbyUserPair));
        controller.handler.disconnect();
    }
}
