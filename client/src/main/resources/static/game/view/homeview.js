import SplitLayout from "/game/layout/splitlayout.js";
import Label, {LabelAlignment} from "/game/component/label.js";
import MarginLayout from "/game/layout/marginlayout.js";
import Button from "/game/component/button.js";
import GridLayout from "/game/layout/gridlayout.js";
import TextField from "/game/component/textfield.js";
import InteractiveImage from "/game/component/interactiveimage.js";
import ResourceManager from "/game/misc/resourcemanager.js";

export default class HomeView {

    constructor(localeManager) {
        this.localeManager = localeManager;

        this.__build_layout__();
    }

    __build_layout__() {
        this.layout = new SplitLayout(false, 0.10, 0.90);
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

            this.profilePictureHoverLabel = new Label(this.localeManager.locale.PROFILE_HOVER);
            this.profilePictureHoverLabel.fillStile = "rgba(255, 255, 255, 1)";
            this.profilePictureHoverLabel.fontSize = 12;
            this.profilePictureHoverLabel.visible = false;
            this.userInfoPanel.setComponent({row: 1, col: 0}, this.profilePictureHoverLabel);

            this.profilePicture = new InteractiveImage(ResourceManager.getAsset("user-unknown"));
            this.userInfoPanel.setComponent({row: 2, col: 0}, this.profilePicture);

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

            this.setLobbyList([]);
        }
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

    setProfilePictureOnImageHover(consumer) {
        this.profilePicture.onImageHovered(consumer);
    }

    setProfilePictureOnImageClicked(consumer) {
        this.profilePicture.onImageClicked(consumer);
    }

    setProfilePictureAsset(assetId) {
        this.profilePicture.setImage(ResourceManager.getAsset(assetId));
    }

    setProfilePictureHoverLabelVisible(visible) {
        this.profilePictureHoverLabel.visible = visible;
    }

    setLobbyList(lobbyList) {
        let currentRows = Math.min(lobbyList.length, 8);

        if (currentRows === 0) {
            this.noLobbiesLabel = new Label("Could not find any public lobby", LabelAlignment.CENTER);
            this.noLobbiesLabel.fontSize = 18;
            this.noLobbiesLabel.fillStyle = "white";
            this.activeLobbyList.setComponent(0, this.noLobbiesLabel);

            return [];
        } else {
            let gridLayout = new GridLayout({rows: 9, cols: 3});
            this.activeLobbyList.setComponent(0, gridLayout);

            let hostnameLabel = new Label(this.localeManager.locale.HOSTED_BY.toUpperCase());
            hostnameLabel.fontSize = 22;
            gridLayout.setComponent({row: 0, col: 0}, hostnameLabel);

            let availableSlotsLabel = new Label(this.localeManager.locale.AVAILABLE_SLOTS.toUpperCase());
            availableSlotsLabel.fontSize = 22;
            gridLayout.setComponent({row: 0, col: 1}, availableSlotsLabel);

            let joinButtons = [];
            for (let i = 0; i < currentRows; ++i) {
                let hostname = lobbyList[i].hostname;
                let occupiedSlots = lobbyList[i].occupiedSlots;
                let totalSlots = lobbyList[i].totalSlots;

                gridLayout.setComponent({row: i + 1, col: 0}, new Label('@' + hostname));

                gridLayout.setComponent({row: i + 1, col: 1}, new Label(occupiedSlots + "/" + totalSlots));

                let joinButtonMargins = new MarginLayout({top: 0, bottom: 0, left: 0.45, right: 0.15});
                gridLayout.setComponent({row: i + 1, col: 2}, joinButtonMargins);

                let joinButton = new Button("Join");
                joinButtonMargins.setComponent(0, joinButton);

                joinButtons.push(joinButton);
            }

            return joinButtons;
        }
    }
}