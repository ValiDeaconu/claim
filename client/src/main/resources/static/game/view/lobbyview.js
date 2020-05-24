import SplitLayout from "/game/layout/splitlayout.js";
import Label, {LabelAlignment} from "/game/component/label.js";
import MarginLayout from "/game/layout/marginlayout.js";
import Button from "/game/component/button.js";
import GridLayout from "/game/layout/gridlayout.js";
import LobbyPlayerCard from "/game/component/lobbyplayercard.js";

export default class LobbyView {

    constructor(localeManager) {
        this.localeManager = localeManager;

        this.__build_layout__();
    }

    __build_layout__() {
        this.layout = new SplitLayout(false, 0.10, 0.90);
        this.layout.size = { width: document.body.clientWidth, height: document.body.clientHeight };

        this.playerCard = [];
        this.playerCardLayout = [];
        this.rowLayout = [];

        this.headerPanel = new SplitLayout(true, 0.75, 0.25);
        this.headerPanel.backgroundColor = "rgba(35, 35, 35, 0.65)";
        this.layout.setComponent(0, this.headerPanel);

        {
            this.logoLayout = new MarginLayout({left:0.015, top:0, right:0, bottom:0});
            this.headerPanel.setComponent(0, this.logoLayout);

            this.logoLabel = new Label(this.localeManager.locale.LOGO_LABEL, LabelAlignment.LEFT);
            this.logoLabel.fillStyle = "white";
            this.logoLabel.fontSize = 24;
            this.logoLayout.setComponent(0, this.logoLabel);

            this.navBarLayout = new MarginLayout({ top:0.15, left:0.55, bottom:0.15, right:0.05 });
            this.headerPanel.setComponent(1, this.navBarLayout);

            this.leaveLobbyButton = new Button(this.localeManager.locale.LEAVE_LOBBY);
            this.navBarLayout.setComponent(0, this.leaveLobbyButton);
        }

        this.centerPanel = new GridLayout({rows: 5, cols: 1});
        this.layout.setComponent(1, this.centerPanel);

        const playerCardLayoutMargin = { top:0.15, left:0.10, bottom:0.15, right:0.10 };
        const playerCardPreferredSize = { width: 250, height: 75 };

        // first row
        {
            this.rowLayout[0] = new GridLayout({rows: 1, cols: 5});
            this.centerPanel.setComponent({row: 0, col: 0}, this.rowLayout[0]);

            this.playerCardLayout[2] = new MarginLayout(playerCardLayoutMargin);
            this.rowLayout[0].setComponent({row: 0, col: 1}, this.playerCardLayout[2]);

            this.playerCard[2] = new LobbyPlayerCard(this.localeManager);
            this.playerCardLayout[2].setComponent(0, this.playerCard[2]);
            this.playerCard[2].unsetPlayer();
            this.playerCard[2].setReadyStatus(false);
            this.playerCard[2].setPreferredSize(playerCardPreferredSize);

            this.playerCardLayout[3] = new MarginLayout(playerCardLayoutMargin);
            this.rowLayout[0].setComponent({row: 0, col: 3}, this.playerCardLayout[3]);

            this.playerCard[3] = new LobbyPlayerCard(this.localeManager);
            this.playerCardLayout[3].setComponent(0, this.playerCard[3]);
            this.playerCard[3].unsetPlayer();
            this.playerCard[3].setReadyStatus(false);
            this.playerCard[3].setPreferredSize(playerCardPreferredSize);
        }

        // second row
        {
            this.rowLayout[1] = new GridLayout({rows: 1, cols: 3});
            this.centerPanel.setComponent({row: 2, col: 0}, this.rowLayout[1]);

            this.playerCardLayout[1] = new MarginLayout(playerCardLayoutMargin);
            this.rowLayout[1].setComponent({row: 0, col: 0}, this.playerCardLayout[1]);

            this.playerCard[1]  = new LobbyPlayerCard(this.localeManager);
            this.playerCardLayout[1].setComponent(0, this.playerCard[1]);
            this.playerCard[1].unsetPlayer();
            this.playerCard[1].setReadyStatus(false);
            this.playerCard[1].setPreferredSize(playerCardPreferredSize);

            this.middleInfoLayout = new SplitLayout(false);
            this.rowLayout[1].setComponent({row: 0, col: 1}, this.middleInfoLayout);

            this.privateCodeLabel = new Label(this.localeManager.locale.PRIVATE_CODE + ": " + this.localeManager.locale.DEFAULT_PRIVATE_CODE);
            this.middleInfoLayout.setComponent(0, this.privateCodeLabel);

            this.hostButtonsWrapperLayout = new MarginLayout({ top:0.15, left:0.10, bottom:0.15, right:0.10 });
            this.middleInfoLayout.setComponent(1, this.hostButtonsWrapperLayout);

            this.hostButtonsLayout = new SplitLayout(true);
            this.hostButtonsWrapperLayout.setComponent(0, this.hostButtonsLayout);

            this.changeVisibilityButton = new Button(this.localeManager.locale.MAKE_PRIVATE);
            this.hostButtonsLayout.setComponent(0, this.changeVisibilityButton);

            this.startMatchButton = new Button(this.localeManager.locale.START_MATCH);
            this.hostButtonsLayout.setComponent(1, this.startMatchButton);

            this.playerCardLayout[4] = new MarginLayout(playerCardLayoutMargin);
            this.rowLayout[1].setComponent({row: 0, col: 2}, this.playerCardLayout[4]);

            this.playerCard[4]  = new LobbyPlayerCard(this.localeManager);
            this.playerCardLayout[4].setComponent(0, this.playerCard[4]);
            this.playerCard[4].unsetPlayer();
            this.playerCard[4].setReadyStatus(false);
            this.playerCard[4].setPreferredSize(playerCardPreferredSize);
        }

        // third row
        {
            this.rowLayout[2] = new GridLayout({rows: 1, cols: 3});
            this.centerPanel.setComponent({row: 4, col: 0}, this.rowLayout[2]);

            this.playerCardLayout[0] = new MarginLayout(playerCardLayoutMargin);
            this.rowLayout[2].setComponent({row: 0, col: 1}, this.playerCardLayout[0]);

            this.playerCard[0]  = new LobbyPlayerCard(this.localeManager);
            this.playerCardLayout[0].setComponent(0, this.playerCard[0]);
            this.playerCard[0].unsetPlayer();
            this.playerCard[0].setReadyStatus(false);
            this.playerCard[0].setPreferredSize(playerCardPreferredSize);
        }
    }

    setStartMatchButtonVisibility(visibility) {
        this.startMatchButton.visible = visibility;
    }

    setChangeVisibilityButtonVisibility(visibility) {
        this.changeVisibilityButton.visible = visibility;
    }

    setPrivateCode(privateCode) {
        this.privateCodeLabel.text = this.localeManager.locale.PRIVATE_CODE + ": " + privateCode;
    }

    flipChangeVisibility(visible) {
        if (visible) {
            this.changeVisibilityButton.label.text = this.localeManager.locale.MAKE_PRIVATE;
        } else {
            this.changeVisibilityButton.label.text = this.localeManager.locale.MAKE_PUBLIC;
        }
    }

    addChangeVisibilityConsumer(consumer) {
        this.changeVisibilityButton.onButtonClicked(consumer);
    }

    addLeaveLobbyConsumer(consumer) {
        this.leaveLobbyButton.onButtonClicked(consumer);
    }

    addStartMatchConsumer(consumer) {
        this.startMatchButton.onButtonClicked(consumer);
    }
}