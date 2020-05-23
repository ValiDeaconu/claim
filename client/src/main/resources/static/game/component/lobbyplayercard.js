import Component from "/game/component/component.js";
import AvatarManager from "/game/misc/avatarmanager.js";
import SplitLayout from "/game/layout/splitlayout.js";
import Label, {LabelAlignment}  from "/game/component/label.js";
import Image from "/game/component/image.js";
import {roundRect} from "/game/misc/roundrect.js";
import GridLayout from "/game/layout/gridlayout.js";
import ResourceManager from "/game/misc/resourcemanager.js";
import {ProfileAssetPack} from "/game/misc/assets.js";

export default class LobbyPlayerCard extends Component {
    constructor(localeManager) {
        super();

        this.localeManager = localeManager;

        this.backgroundColor = 'rgba(255, 255, 255, 0)';
        this.strokeStyle = 'rgba(255, 255, 255, 1)';

        this.__buildLayout__();
    }

    __buildLayout__() {
        this.wrapperLayout = new GridLayout({rows:1, cols:3});

        this.playerImage = new Image(AvatarManager.getRandomAvatar(Math.floor(Math.random() * 2)));
        this.wrapperLayout.setComponent({row:0, col:0}, this.playerImage);

        this.middleSideLayout = new SplitLayout(false);
        this.wrapperLayout.setComponent({row:0, col:1}, this.middleSideLayout);

        this.nameLabel = new Label(this.localeManager.locale.DEFAULT_USERNAME, LabelAlignment.CENTER);
        this.middleSideLayout.setComponent(0, this.nameLabel);

        this.winRateLabel = new Label(this.localeManager.locale.WIN_RATE + ": 0%", LabelAlignment.CENTER);
        this.middleSideLayout.setComponent(1, this.winRateLabel);

        this.statusLabel = new Label(this.localeManager.locale.NOT_READY, LabelAlignment.CENTER);
        this.wrapperLayout.setComponent({row:0, col:2}, this.statusLabel);
    }

    setBoundingBox(position, size) {
        super.setBoundingBox(position, size);

        if (this.hasPreferredSize) {
            let optimalSize = {
                width: Math.min(this.size.width, this.preferredSize.width),
                height: Math.min(this.size.height, this.preferredSize.height)
            };


            this.wrapperLayout.setBoundingBox(this.__preferredPosition__, optimalSize);
            this.wrapperLayout.resize(optimalSize);
        } else {
            this.wrapperLayout.setBoundingBox(this.position, this.size);
            this.wrapperLayout.resize(this.size);
        }
    }

    setPreferredSize(preferredSize) {
        super.setPreferredSize(preferredSize);

        this.wrapperLayout.setBoundingBox(this.__preferredPosition__, preferredSize);
        this.wrapperLayout.resize(preferredSize);
    }

    update(deltaTime, inputHandler) {
        this.wrapperLayout.update(deltaTime, inputHandler);
    }

    draw(context) {
        context.fillStyle = this.backgroundColor;
        context.strokeStyle = this.strokeStyle;

        if (this.hasPreferredSize) {
            let optimalSize = {
                width: Math.min(this.size.width, this.preferredSize.width),
                height: Math.min(this.size.height, this.preferredSize.height)
            };

            roundRect(context, this.__preferredPosition__.x, this.__preferredPosition__.y,
                optimalSize.width, optimalSize.height, {
                    tl: 50,
                    br: 25
                }, true);
        } else {
            roundRect(context, this.position.x, this.position.y,
                this.size.width, this.size.height, {
                    tl: 50,
                    br: 25
                }, true);
        }

        this.wrapperLayout.draw(context);
    }

    setPlayer(user) {
        this.nameLabel.text = user.username;

        this.playerImage.setImage(ResourceManager.getAsset(ProfileAssetPack[user.profileAssetIndex]));

        let winRate = (user.wins + user.loss > 0) ? Math.round((user.wins / (user.wins + user.loss)) * 10000) / 100 : 0;
        this.winRateLabel.text = this.localeManager.locale.WIN_RATE + ": " + winRate + "%";
    }

    unsetPlayer() {
        this.nameLabel.text = this.localeManager.locale.DEFAULT_USERNAME;

        this.playerImage.setImage(AvatarManager.getUnknownAvatar());

        this.winRateLabel.text = this.localeManager.locale.WIN_RATE + ": 0%";
    }

    setReadyStatus(status) {
        if (status) {
            this.statusLabel.text = this.localeManager.locale.READY;
            this.backgroundColor = "rgba(50, 131, 168, 0.80)";
        } else {
            this.statusLabel.text = this.localeManager.locale.NOT_READY;
            this.backgroundColor = "rgba(168, 80, 50, 0.80)";
        }
    }
}