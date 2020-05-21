import BorderLayout from "/game/layout/borderlayout.js";
import SplitLayout from "/game/layout/splitlayout.js";
import Label, {LabelAlignment} from "/game/component/label.js";
import MarginLayout from "/game/layout/marginlayout.js";
import Button from "/game/component/button.js";
import GridLayout from "/game/layout/gridlayout.js";
import TextField from "/game/component/textfield.js";

export default class HomeView {

    constructor(localeManager) {
        this.localeManager = localeManager;

        this.__build_layout__();
    }

    __build_layout__() {
        this.layout = new BorderLayout();
        this.layout.size = { width: document.body.clientWidth, height: document.body.clientHeight };

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

            this.navBarLayout = new SplitLayout(true, 0.50, 0.50);
            this.headerPanel.setComponent(1, this.navBarLayout);

            this.createLobbyButtonLayout = new MarginLayout({ top:0.15, left:0.10, bottom:0.15, right:0.10 });
            this.navBarLayout.setComponent(0, this.createLobbyButtonLayout)

            this.createLobbyButton = new Button(this.localeManager.locale.CREATE_LOBBY);
            this.createLobbyButtonLayout.setComponent(0, this.createLobbyButton);

            this.logoutButtonLayout = new MarginLayout({ top:0.15, left:0.10, bottom:0.15, right:0.10 });
            this.navBarLayout.setComponent(1, this.logoutButtonLayout);

            this.logoutButton = new Button(this.localeManager.locale.LOGOUT);
            this.logoutButtonLayout.setComponent(0, this.logoutButton);
        }

        this.centerPanel = new SplitLayout(true, 0.20, 0.80);
        this.layout.setComponent(1, this.centerPanel);

        {
            this.userInfoPanel = new GridLayout({ rows: 10, cols: 1 });
            this.userInfoPanel.backgroundColor = "rgba(35, 35, 35, 0.65)";
            this.centerPanel.setComponent(0, this.userInfoPanel);

            this.welcomeLabel = new Label(this.localeManager.locale.WELCOME, LabelAlignment.CENTER);
            this.welcomeLabel.fillStyle = "white";
            this.welcomeLabel.fontSize = 18;
            this.userInfoPanel.setComponent({row: 2, col: 0}, this.welcomeLabel);

            this.usernameLabel = new Label(this.localeManager.locale.DEFAULT_USERNAME, LabelAlignment.CENTER);
            this.usernameLabel.fillStyle = "white";
            this.usernameLabel.fontSize = 18;
            this.userInfoPanel.setComponent({row: 3, col: 0}, this.usernameLabel);

            this.winsLabel = new Label("NaN " + this.localeManager.locale.WINS, LabelAlignment.CENTER);
            this.winsLabel.fillStyle = "white";
            this.winsLabel.fontSize = 18;
            this.userInfoPanel.setComponent({row: 6, col: 0}, this.winsLabel);

            this.lossLabel = new Label("NaN " + this.localeManager.locale.LOSS, LabelAlignment.CENTER);
            this.lossLabel.fillStyle = "white";
            this.lossLabel.fontSize = 18;
            this.userInfoPanel.setComponent({row: 7, col: 0}, this.lossLabel);
        }

        {
            this.deskPanel = new SplitLayout(false, 0.15, 0.85);
            this.deskPanel.backgroundColor = "rgba(64, 64, 64, 0.85)";
            this.centerPanel.setComponent(1, this.deskPanel);

            this.searchPanel = new MarginLayout({ top: 0.25, bottom: 0.25, left: 0.25, right: 0.25 });
            this.deskPanel.setComponent(0, this.searchPanel);

            this.splitSearchFormPanel = new SplitLayout(true, 0.75, 0.25);
            this.searchPanel.setComponent(0, this.splitSearchFormPanel);

            this.searchTextField = new TextField();
            this.splitSearchFormPanel.setComponent(0, this.searchTextField);

            this.joinButton = new Button(this.localeManager.locale.JOIN);
            this.splitSearchFormPanel.setComponent(1, this.joinButton);

            this.activeLobbyList = new MarginLayout({ top: 0.10, bottom: 0.10, left: 0.15, right: 0.15 });
            this.deskPanel.setComponent(1, this.activeLobbyList);

            // TODO: List active public lobbies here
            this.soonLobbyList = new Label("Soon Lobby List Here", LabelAlignment.CENTER);
            this.soonLobbyList.fontSize = 18;
            this.soonLobbyList.fillStyle = "white";
            this.activeLobbyList.setComponent(0, this.soonLobbyList);
        }

        this.footerLayout = new MarginLayout({top:0.0, left:0.0, bottom:0.0, right:0.0});
        this.footerLayout.backgroundColor = "rgba(194, 81, 81, 0.85)";
        this.layout.setComponent(2, this.footerLayout);

        this.footerPanel = new Label(this.localeManager.locale.SERVER_STATUS + ": " + this.localeManager.locale.DISCONNECTED,
            LabelAlignment.CENTER);
        this.footerPanel.fillStyle = "rgb(0, 64, 64)";
        this.footerPanel.fontSize = 12;
        this.footerLayout.setComponent(0, this.footerPanel);
    }

    setUsernameLabelText(text) {
        this.usernameLabel.text = text;
    }

    setWinsLabelText(text) {
        this.winsLabel.text = text;
    }

    setLossLabelText(text) {
        this.lossLabel.text = text;
    }

    addCreateLobbyButtonConsumer(consumer) {
        this.createLobbyButton.onButtonClicked(consumer);
    }

    addLogoutButtonConsumer(consumer) {
        this.logoutButton.onButtonClicked(consumer);
    }

    addJoinButtonConsumer(consumer) {
        this.joinButton.onButtonClicked(consumer);
    }

    setServerStatus(status) {
        if (status) {
            this.footerLayout.backgroundColor = "rgba(81, 196, 146, 0.85)";
            this.footerPanel.text = this.localeManager.locale.SERVER_STATUS + ": " + this.localeManager.locale.CONNECTED;
        } else {
            this.footerLayout.backgroundColor = "rgba(194, 81, 81, 0.85)";
            this.footerPanel.text = this.localeManager.locale.SERVER_STATUS + ": " + this.localeManager.locale.DISCONNECTED;
        }
    }
}