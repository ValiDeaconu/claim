import LocaleManager from "/game/locale/localemanager.js";

import HomeView from "/game/view/homeview.js";
import LobbyView from "/game/view/lobbyview.js";
import ArenaView from "/game/view/arenaview.js";

import HomeController from "/game/controller/homecontroller.js";
import LobbyController from "/game/controller/lobbycontroller.js";
import ArenaController from "/game/controller/arenacontroller.js";

export const Views = {
  HOME: 0,
  LOBBY: 1,
  ARENA: 2
};

export default class ViewManager {
    constructor(locale, currentUser, serverAddress) {
        this.currentView = Views.HOME;
        this.currentUser = currentUser;
        this.serverAddress = serverAddress;

        this.localeManager = new LocaleManager(locale);

        this.setViewTo(Views.HOME);

        this.currentSize = this.view.layout.size;
    }

    setViewTo(viewKey = Views.HOME) {
        switch (viewKey) {
            case Views.HOME:
                this.view = new HomeView(this.localeManager);
                this.controller = new HomeController(this.view, this, this.serverAddress);
                this.controller.updateCurrentUser();
                break;
            case Views.LOBBY:
                this.view = new LobbyView(this.localeManager);
                this.controller = new LobbyController(this.view, this, this.serverAddress);
                break;
            case Views.ARENA:
                this.view = new ArenaView(this.localeManager);
                this.controller = new ArenaController(this.view, this, this.serverAddress);
                break;
        }

        this.currentView = viewKey;

        this.view.layout.resize(this.currentSize);

        return this.controller;
    }

    updateUrl() {
        switch (this.currentView) {
            case Views.HOME:
                history.pushState(null, '', '/game/home');
                break;
            case Views.LOBBY:
            case Views.ARENA:
                history.pushState(null, '', '/game/lobby/' + this.controller.lobby.id);
                break;
        }
    }

    getView() {
        return this.view.layout;
    }

    resize(size) {
        this.currentSize = size;

        this.view.layout.resize(size);
    }
}