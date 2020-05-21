import Game from '/game/game.js'
import InputHandler from "/game/inputhandler/inputhandler.js";

import {Locale_EN} from "/game/locale/locale_en.js";

import ViewManager, {Views} from "/game/view/viewmanager.js";

import ResourceManager from "/game/misc/resourcemanager.js";
import AvatarManager from "/game/misc/avatarmanager.js";

import Request, {RequestMethod} from "/game/server/requesthandler.js";

let game = null;

window.addEventListener('load', () => {
    let currentUser = {
        id: parseInt($("#current-user-id").val()),
        username: $("#current-user-username").val(),
        wins: parseInt($("#current-user-wins").val()),
        loss: parseInt($("#current-user-loss").val()),
        image: AvatarManager.getRandomAvatar(Math.floor(Math.random() * 2))
    };

    let currentLobby = {
        id: $("#current-lobby-id").val()
    };

    let canvas = $("#gameScreen")[0];

    // prepare assets
    ResourceManager.loadAssets();

    // server address
    //const serverAddress = 'http://claim.ddns.net:8091';
    const serverAddress = 'http://localhost:8091';

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


